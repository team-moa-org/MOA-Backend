package moa.moabackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.io.File

@EnableScheduling
@SpringBootApplication
class MoaBackendApplication

fun main(args: Array<String>) {
    loadDotEnv()
    runApplication<MoaBackendApplication>(*args)
}

fun loadDotEnv() {
    val envFile = File(".env")
    if (envFile.exists()) {
        envFile.readLines().forEach { line ->
            val trimmedLine = line.trim()
            if (trimmedLine.isNotEmpty() && !trimmedLine.startsWith("#") && trimmedLine.contains("=")) {
                val (key, value) = trimmedLine.split("=", limit = 2)
                System.setProperty(key.trim(), value.trim())
            }
        }
    }
}
