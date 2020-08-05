package com.smile.webapp

import com.smile.repository.Repository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

const val SIGNIN = "/signin"

@Location(SIGNIN)
data class SignIn(val userId: String = "", val error: String = "")

fun Route.signIn(db: Repository, hashFunction: (String) -> String) {
    get<SignIn> {
        call.respond(FreeMarkerContent("signin.ftl", null))
    }
}
