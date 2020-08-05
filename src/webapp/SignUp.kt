package com.smile.webapp

import com.smile.MIN_PASSWORD_LENGTH
import com.smile.MIN_USER_ID_LENGTH
import com.smile.model.EPSession
import com.smile.model.User
import com.smile.redirect
import com.smile.repository.Repository
import com.smile.userNameValid
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.Parameters
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set

const val SIGNUP = "/signup"

@Location(SIGNUP)
data class SignUp(
        val userId: String = "",
        val displayName: String = "",
        val email: String = "",
        val error: String = "")

fun Route.signUp(db: Repository, hashFunction: (String) -> String) {

    post<SignUp> {
        val user = call.sessions.get<EPSession>()?.let { db.user(it.userId) }
        if (user != null) return@post call.redirect(Phrases())

        val signUpParameters = call.receive<Parameters>()
        val userId = signUpParameters["userId"] ?: return@post call.redirect(it)
        val password = signUpParameters["password"] ?: return@post call.redirect(it)
        val displayName = signUpParameters["displayName"] ?: return@post call.redirect(it)
        val email = signUpParameters["email"] ?: return@post call.redirect(it)

        val signUpError = SignUp(userId, displayName, email)

        when {
            password.length < MIN_PASSWORD_LENGTH ->
                call.redirect(signUpError.copy(error = "Password should be at least $MIN_PASSWORD_LENGTH characters lengt"))
            userId.length < MIN_USER_ID_LENGTH ->
                call.redirect(signUpError.copy(error = "user Id should be at least $MIN_USER_ID_LENGTH characters length"))
            !userNameValid(userId) ->
                call.redirect(signUpError.copy(error = "Username should consist of digits, letters, dots or underscores"))
            db.user(userId) != null ->
                call.redirect(signUpError.copy(error = "This user is already registered"))
            else -> {
                val hash = hashFunction(password)
                val newUser = User(userId, email, displayName, hash)
                try {
                    db.createUser(newUser)
                } catch (e: Throwable) {
                    when {
                        db.user(userId) != null ->
                            call.redirect(signUpError.copy(error = "This user is already registered"))
                        db.userByEmail(email) != null ->
                            call.redirect(signUpError.copy(error = "User with this email already registered"))
                        else -> {
                            application.log.error("Failed to register user", e)
                            call.redirect(signUpError.copy(error = "Failed to register"))
                        }
                    }
                }
                call.sessions.set(EPSession(newUser.userId))
                call.redirect(Phrases())
            }
        }
    }

    get<SignUp> {
        val user = call.sessions.get<EPSession>()?.let { db.user(it.userId) }
        if (user != null)
            call.redirect(Phrases())
        else
            call.respond(FreeMarkerContent("signup.ftl", mapOf("error" to it.error)))
    }
}