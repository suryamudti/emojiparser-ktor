package com.smile.repository

import com.smile.model.EmojiPhrases
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(hikari())

        transaction {
            SchemaUtils.create(EmojiPhrases)

            EmojiPhrases.insert {
                it[emoji] ="e1"
                it[phrase] = "p1"
            }
            EmojiPhrases.insert {
                it[emoji] ="e2"
                it[phrase] = "p2"
            }
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:mem:test"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery (
        block: () -> T): T =
            withContext(Dispatchers.IO) {
                transaction { block() }
            }
}
