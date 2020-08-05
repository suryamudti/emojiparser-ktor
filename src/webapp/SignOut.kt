package com.smile.webapp

import com.smile.redirect
import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.routing.Route
import io.ktor.routing.get

const val SIGNOUT = "/signout"

@Location(SIGNOUT)
class SignOut

fun Route.signOut() {
    get<SignOut> {
        call.redirect(SignIn())
    }
}

