package org.lyy.projectweb.hello

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController(private val jdbcTemplate: JdbcTemplate,
                      private val mongoTemplate: MongoTemplate) {
    @GetMapping("/hello")
    fun hello(): String {
        val pgStatus = try {
            val res = jdbcTemplate.queryForObject("SELECT 1", Int::class.java)
            if (res == 1) "✅ 连接成功" else "❌ 异常"
        } catch (e: Exception) {
            "❌ 连接失败: ${e.message}"
        }

        val mongoStatus = try {
            mongoTemplate.executeCommand("{ ping: 1 }")
            "✅ 连接成功"
        } catch (e: Exception) {
            "❌ 连接失败: ${e.message}"
        }

        return """
            <h1>Hello World! Kotlin + Spring Boot 运行正常</h1>
            <p>PostgreSQL 状态: $pgStatus</p>
            <p>MongoDB 状态: $mongoStatus</p>
        """.trimIndent()
    }
}