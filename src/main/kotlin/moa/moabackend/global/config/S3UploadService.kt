package moa.moabackend.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
// import software.amazon.awssdk.core.sync.RequestBody
// import software.amazon.awssdk.services.s3.S3Client
// import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.*

@Service
class S3UploadService(
    // private val s3Client: S3Client,
    // @Value("${spring.cloud.aws.s3.bucket}")
    // private val bucket: String,
    // @Value("${spring.cloud.aws.s3.endpoint}")
    // private val s3Url: String
) {

    fun upload(file: MultipartFile): String {
        if (file.isEmpty) return ""

        val fileName = UUID.randomUUID().toString() + "-" + file.originalFilename
        /*
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .contentType(file.contentType)
            .build()

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.bytes))
        */

        // return "$s3Url/$fileName"
        return "https://dummy-s3-url.com/$fileName"
    }
}
