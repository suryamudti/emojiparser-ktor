package com.smile.webapp

import com.smile.repository.Repository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

const val SIGNUP = "/signup"

@Location(SIGNUP)
data class SignUp(
        val userId: String = "",
        val displayName: String = "",
        val email: String = "",
        val error: String = "")

fun Route.signUp(db: Repository, hashFunction: (String) -> String) {
    get<SignUp> {
        call.respond(FreeMarkerContent("signup.ftl", null))
    }
}