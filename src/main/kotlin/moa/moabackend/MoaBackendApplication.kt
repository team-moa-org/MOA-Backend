package moa.moabackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoaBackendApplication

fun main(args: Array<String>) {
    runApplication<MoaBackendApplication>(*args)
}
