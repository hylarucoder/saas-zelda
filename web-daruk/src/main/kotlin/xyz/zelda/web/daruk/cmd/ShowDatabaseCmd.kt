package xyz.zelda.web.daruk.cmd

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.SQLException
import javax.sql.DataSource

@SpringBootApplication
open class ShowDatabaseCmd : CommandLineRunner {
    @Autowired
    private val dataSource: DataSource? = null

    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        showConnection()
        showData()
    }

    @Throws(SQLException::class)
    private fun showConnection() {
        val conn = dataSource!!.connection
        conn.close()
    }

    private fun showData() {
//        jdbcTemplate!!.queryForList("SELECT * FROM FOO")
//                .forEach(Consumer { row: Map<String?, Any?> -> info(row.toString()) })
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ShowDatabaseCmd::class.java, *args)
        }
    }
}