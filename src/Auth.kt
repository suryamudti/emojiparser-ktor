package com.smile

import io.ktor.util.hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

val hashKey = hex(System.getenv("SECRET_KEY"))

val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

fun hash(pasword: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(pasword.toByteArray(Charsets.UTF_8)))
}

private val userIdPattern = "[a-zA-Z0-9_\\.]+".toRegex()

internal fun userNameValid(userId: String) = userId.matches(userIdPattern)