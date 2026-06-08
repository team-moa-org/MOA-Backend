package moa.moabackend.global.config

import io.awspring.cloud.s3.S3Template
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.util.*

@Service
class S3UploadService(
    private val s3Template: S3Template,
    @Value("\${spring.cloud.aws.s3.bucket}")
    private val bucket: String
) {

    fun upload(file: MultipartFile): String {
        if (file.isEmpty) return ""

        val fileName = "moa/" + UUID.randomUUID().toString() + "-" + (file.originalFilename ?: "unnamed")

        return file.inputStream.use { inputStream ->
            s3Template.upload(bucket, fileName, inputStream)
            fileName
        }
    }

    fun generatePresignedUrl(storedKey: String?): String? {
        if (storedKey.isNullOrBlank()) return null
        
        val key = extractKey(storedKey)
        
        return try {
            s3Template.createSignedGetURL(bucket, key, Duration.ofHours(1)).toString()
        } catch (e: Exception) {
            if (storedKey.startsWith("http")) storedKey else "https://$bucket.s3.amazonaws.com/$storedKey"
        }
    }

    private fun extractKey(storedValue: String): String {
        return when {
            storedValue.contains(".amazonaws.com/") -> storedValue.substringAfter(".amazonaws.com/")
            storedValue.contains("://") -> {
                val path = storedValue.substringAfter("://").substringAfter("/")
                if (path.startsWith("$bucket/")) path.substringAfter("$bucket/") else path
            }
            storedValue.startsWith("/") -> storedValue.substring(1)
            else -> storedValue
        }.substringBefore("?")
    }
}
