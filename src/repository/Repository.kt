package com.smile.repository

import com.smile.model.EmojiPhrase

interface Repository {
    suspend fun add(emojiValue: String, phraseValue: String)
    suspend fun phrase(id: Int): EmojiPhrase?
    suspend fun phrase(id: String): EmojiPhrase?
    suspend fun phrases(): List<EmojiPhrase>
    suspend fun remove(id: Int): Boolean
    suspend fun remove(id: String): Boolean
    suspend fun clear()
}