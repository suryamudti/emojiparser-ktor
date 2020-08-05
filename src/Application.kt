package com.smile

import com.ryanharter.ktor.moshi.moshi
import com.smile.api.phrase
import com.smile.model.User
import com.smile.repository.DatabaseFactory
import com.smile.repository.EmojiPhrasesRepository
import com.smile.webapp.*
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.basic
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.freemarker.FreeMarker
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.locations.Locations
import io.ktor.locations.locations
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(DefaultHeaders)

    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }

    install(ContentNegotiation) {
        moshi()
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Locations)

    val hashFunction = { s: String -> hash(s)}

    DatabaseFactory.init()

    val db = EmojiPhrasesRepository()

    routing {
        static("/static") {
            resources("images")
        }
        home()
        about()
        phrases(db)
        signIn(db, hashFunction)
        signOut()
        signUp(db, hashFunction)

        // API
        phrase(db)
    }
}

const val API_VERSION = "/api/v1"

suspend fun ApplicationCall.redirect(location: Any) {
    respondRedirect(application.locations.href(location))
}

