package discord.embed

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.UploadObjectArgs
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
        val imageFile = generateImage()
        imageFile?.let {
            uploadToObjectStorage()
        }
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
        val bucketName = "naima"
        val minioClient = MinioClient.builder()
            .endpoint("https://play.min.io")
            .credentials("Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG")
            .build()
        val found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
        }
        minioClient.uploadObject(
            UploadObjectArgs.builder()
                .bucket(bucketName)
                .`object`(localFilename)
                .filename(localPath)
                .build()
        )
    }

    companion object {
        private const val ALBUM_COVER_WIDTH_PX = 500
    }
}
