package com.smile.api

import com.smile.API_VERSION
import com.smile.model.EmojiPhrase
import com.smile.model.Request
import com.smile.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post

const val PHRASE_ENDPOINT = "$API_VERSION/phrase"

fun Route.phrase(db: Repository) {

    authenticate("auth") {
        post(PHRASE_ENDPOINT) {
            val request = call.receive<Request>()
            val phrase = db.add(EmojiPhrase(request.emoji, request.phrase))
            call.respond(phrase)
        }
    }
}