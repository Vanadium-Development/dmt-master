package dev.vanadium.dmt.master.commons.utils

import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
fun ByteArray.sha256(): String {
    val md = MessageDigest.getInstance("SHA-256")
    val bytes = md.digest(this)
    return bytes.toHexString()
}

