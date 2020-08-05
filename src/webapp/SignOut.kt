package com.smile.webapp

import com.smile.model.EPSession
import com.smile.redirect
import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.routing.Route
import io.ktor.sessions.clear
import io.ktor.sessions.sessions

const val SIGNOUT = "/signout"

@Location(SIGNOUT)
class SignOut

fun Route.signOut() {
    get<SignOut> {
        call.sessions.clear<EPSession>()
        call.redirect(SignIn())
    }
}

