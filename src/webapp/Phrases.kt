package com.smile.webapp

import com.smile.model.EmojiPhrase
import com.smile.model.User
import com.smile.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post

const val PHRASES = "/phrases"

fun Route.phrases(db: Repository) {
    authenticate("auth") {
        get(PHRASES) {
            val user = call.authentication.principal as User
            val phrases = db.phrases()
            call.respond(FreeMarkerContent("phrases.ftl", mapOf("phrases" to phrases,
                "displayName" to user.displayName)))
        }
        post(PHRASES) {
            val params = call.receiveParameters()
            val emoji = params["emoji"] ?: throw IllegalArgumentException("Missing parameter: emoji")
            val phrase = params["phrase"] ?: throw IllegalArgumentException("Missing parameter: phrase")
            db.add(EmojiPhrase(emoji, phrase))
            call.respondRedirect(PHRASES)
        }
    }
}