package discord.embed

import Environment.S3Credentials.accessKey
import Environment.S3Credentials.bucket
import Environment.S3Credentials.endpoint
import Environment.S3Credentials.secretKey
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.PutObjectRequest
import musicbrainz.data.ReleaseGroup
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.io.File

class MultiAlbumCoverEmbed(releaseGroups: List<ReleaseGroup>) {
    private val fallbackImageUrl = "https://coverartarchive.org/release-group/a5572828-f7f2-405d-8853-a2019b019e07/front"

    private val localFilename = run {
        val ids = releaseGroups.map { it.id }
        val combined = ids.reduce { acc, id -> "${acc}_$id" }
        "$combined.jpg"
    }

    private val localPath = "/tmp/$localFilename"

    private val imageMagickCommand = run {
        val imageUrls = releaseGroups.map { AlbumCoverEmbed(it).imageUrl }
        listOf(
            listOf("magick"),
            imageUrls,
            listOf(
                "-resize", "${ALBUM_COVER_WIDTH_PX}x$ALBUM_COVER_WIDTH_PX",
                "+append",
            ),
            listOf(localPath)
        ).flatten()
    }

    private val imageUrl: String by lazy {
        // val imageFile = generateImage()
        // imageFile?.let {
            uploadToObjectStorage()
        // }
        fallbackImageUrl
    }
    fun build(): MessageEmbed = EmbedBuilder()
        .setImage(imageUrl)
        .build()

    private fun generateImage(): File? {
        println("About to run the following command:")
        println(imageMagickCommand)

        val process = ProcessBuilder(imageMagickCommand)
            .redirectError(File("/tmp/naima-imagemagick.log"))
            .start()

        process.waitFor()
        if (process.exitValue() == 0) {
            println("Successfully wrote a multi album cover image to $localPath")
        } else {
            println("Failed to write a multi album cover image to $localPath")
        }

        return File(localPath).let { if (it.isFile) it else null }
    }

    private fun uploadToObjectStorage() {
        val credentials = BasicAWSCredentials(accessKey, secretKey)

        val client = AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(
                endpoint, ""
            ))
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build()

        val imageFile = generateImage()!!
        client.putObject(bucket, "multi-album-covers/${imageFile.name}", imageFile)
    }

    companion object {
        private const val ALBUM_COVER_WIDTH_PX = 500
    }
}
