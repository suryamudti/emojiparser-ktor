package com.smile.model

import io.ktor.auth.Principal

data class User(val displayName: String) : Principal