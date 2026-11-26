package org.lyy.projectweb.hello

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RestController
class HelloController(private val jdbcTemplate: JdbcTemplate,
                      private val mongoTemplate: MongoTemplate) {

    private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS z")

    @GetMapping("/hello")
    fun hello(): String {
        val requestTime = ZonedDateTime.now()
        val startNanos = System.nanoTime()

        val (pgStatus, pgMs) = timed {
            try {
                val res = jdbcTemplate.queryForObject("SELECT 1", Int::class.java)
                if (res == 1) "✅ 连接成功" else "❌ 异常"
            } catch (e: Exception) {
                "❌ 连接失败: ${e.message}"
            }
        }

        val (mongoStatus, mongoMs) = timed {
            try {
                mongoTemplate.executeCommand("{ ping: 1 }")
                "✅ 连接成功"
            } catch (e: Exception) {
                "❌ 连接失败: ${e.message}"
            }
        }

        val totalMs = (System.nanoTime() - startNanos) / 1_000_000.0

        return """
            <h1>Hello World! Kotlin + Spring Boot 运行正常</h1>
            <p>请求时间: ${requestTime.format(timeFormatter)}</p>
            <p>PostgreSQL 状态: $pgStatus (${"%.2f".format(pgMs)} ms)</p>
            <p>MongoDB 状态: $mongoStatus (${"%.2f".format(mongoMs)} ms)</p>
            <p>请求总耗时: ${"%.2f".format(totalMs)} ms</p>
        """.trimIndent()
    }

    private inline fun <T> timed(block: () -> T): Pair<T, Double> {
        val start = System.nanoTime()
        val result = block()
        return result to (System.nanoTime() - start) / 1_000_000.0
    }
}
