package org.lyy.hello

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProjectWebApplication

fun main(args: Array<String>) {
    runApplication<ProjectWebApplication>(*args)
}
