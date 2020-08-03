package com.smile.repository

import com.smile.model.EmojiPhrase
import java.util.concurrent.atomic.AtomicInteger

class InMemoryRepository : Repository {

    private val idCounter = AtomicInteger()
    private val phrases = ArrayList<EmojiPhrase>()

    override suspend fun add(phrase: EmojiPhrase): EmojiPhrase {
        TODO("Not yet implemented")
    }

    override suspend fun phrase(id: Int): EmojiPhrase? {
        TODO("Not yet implemented")
    }

    override suspend fun phrase(id: String): EmojiPhrase? {
        TODO("Not yet implemented")
    }

    override suspend fun phrases(): List<EmojiPhrase> {
        TODO("Not yet implemented")
    }

    override suspend fun remove(phrase: EmojiPhrase) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

}