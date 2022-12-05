package discord.embed

import Environment.S3Credentials.accessKey
import Environment.S3Credentials.bucket
import Environment.S3Credentials.endpoint
import Environment.S3Credentials.secretKey
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import musicbrainz.data.ReleaseGroup
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.io.File
import java.net.URL

class MultiAlbumCoverEmbed(releaseGroups: List<ReleaseGroup>) {
    private val localFilename = run {
        val ids = releaseGroups.map { it.id }
        val combined = ids.reduce { acc, id -> "${acc}_$id" }
        "$combined.jpg"
    }
    private val localFile = File("/tmp/$localFilename")
    private val imageGenerator = ImageGenerator(releaseGroups)

    fun build(): MessageEmbed? {
        imageGenerator.generate(localFile)
        return if (localFile.isFile) {
            val pm = PersistenceManager(localFile)
            pm.upload()
            pm.makePubliclyReadable()
            pm.getUrl()
            EmbedBuilder().setImage(pm.getUrl().toExternalForm()).build()
        } else null
    }

    private class ImageGenerator(private val releaseGroups: List<ReleaseGroup>) {
        private fun imageMagickCommand(writeTo: File): List<String> {
            val imageUrls = releaseGroups.map { AlbumCoverEmbed(it).imageUrl }
            return listOf(
                listOf("convert"),
                imageUrls,
                listOf(
                    "-resize",
                    "${ALBUM_COVER_WIDTH_PX}x$ALBUM_COVER_WIDTH_PX",
                    "+append"
                ),
                listOf(writeTo.absolutePath)
            ).flatten()
        }

        fun generate(writeTo: File) {
            val command = imageMagickCommand(writeTo)

            println("About to run the following command:")
            println(command)

            val process = ProcessBuilder(command)
                .redirectError(File("/tmp/naima-imagemagick.log"))
                .start()

            process.waitFor()
            if (process.exitValue() == 0) {
                println("Successfully wrote a multi album cover image to $writeTo")
            } else {
                println("Failed to write a multi album cover image to $writeTo")
            }
        }
    }

    private class PersistenceManager(private val file: File) {
        private val objectKey = "multi-album-covers/${file.name}"
        private val s3Credentials = BasicAWSCredentials(accessKey, secretKey)

        private val s3Client = AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    endpoint,
                    ""
                )
            )
            .withCredentials(AWSStaticCredentialsProvider(s3Credentials))
            .build()

        fun upload() {
            println("Uploading ${file.absolutePath} to object storage")
            s3Client.putObject(bucket, objectKey, file)
        }

        fun makePubliclyReadable() {
            println("Making $objectKey publicly readable")
            s3Client.setObjectAcl(bucket, objectKey, CannedAccessControlList.PublicRead)
        }

        fun getUrl(): URL {
            println("Fetching url for $objectKey")
            return s3Client.getUrl(bucket, objectKey)
        }
    }

    companion object {
        private const val ALBUM_COVER_WIDTH_PX = 500
    }
}
