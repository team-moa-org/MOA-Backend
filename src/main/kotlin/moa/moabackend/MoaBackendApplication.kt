package moa.moabackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class MoaBackendApplication

fun main(args: Array<String>) {
    runApplication<MoaBackendApplication>(*args)
}
