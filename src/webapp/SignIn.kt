package com.smile.webapp

import com.smile.MIN_PASSWORD_LENGTH
import com.smile.MIN_USER_ID_LENGTH
import com.smile.model.EPSession
import com.smile.redirect
import com.smile.repository.Repository
import com.smile.userNameValid
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.Parameters
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set

const val SIGNIN = "/signin"

@Location(SIGNIN)
data class SignIn(val userId: String = "", val error: String = "")

fun Route.signIn(db: Repository, hashFunction: (String) -> String) {

    post<SignIn>{
        val signInParameters = call.receive<Parameters>()
        val userId = signInParameters["userId"] ?: return@post call.redirect(it)
        val password = signInParameters["password"] ?: return@post call.redirect(it)

        val signInError = SignIn(userId)

        val signIn = when {
            userId.length < MIN_USER_ID_LENGTH -> null
            password.length < MIN_PASSWORD_LENGTH -> null
            !userNameValid(userId) -> null
            else -> db.user(userId, hashFunction(password))
        }
        if (signIn == null) {
            call.redirect(signInError.copy(error = "Invalid username or password"))
        } else {
            call.sessions.set(EPSession(signIn.userId))
            call.redirect(Phrases())
        }
    }

    get<SignIn> {
        val user = call.sessions.get<EPSession>()?.let { db.user(it.userId) }
        if (user != null) {
            call.redirect(Home())
        } else {
            call.respond(FreeMarkerContent("signin.ftl", null))
        }
    }
}
