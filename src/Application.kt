package com.smile

import com.ryanharter.ktor.moshi.moshi
import com.smile.api.phrase
import com.smile.model.User
import com.smile.repository.InMemoryRepository
import com.smile.webapp.about
import com.smile.webapp.home
import com.smile.webapp.phrases
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
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

    install(Authentication) {
        basic(name = "auth") {
            realm = "Ktor Server"
            validate { credentials ->
                if (credentials.password == "${credentials.name}123") User(credentials.name) else null
            }
        }
    }

    val db = InMemoryRepository()

    routing {
        static("/static") {
            resources("images")
        }
        home()
        about()
        phrases(db)

        // API
        phrase(db)
    }
}

const val API_VERSION = "/api/v1"

