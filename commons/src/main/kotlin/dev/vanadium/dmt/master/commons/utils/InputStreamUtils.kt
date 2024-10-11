package dev.vanadium.dmt.master.commons.utils

import java.io.InputStream
import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
fun InputStream.sha256(): String {


    val buffer = ByteArray(1024)
    val digest = MessageDigest.getInstance("SHA-256")

    var bytesRead = this.read(buffer)
    while (bytesRead != -1) {
        digest.update(buffer, 0, bytesRead)
        bytesRead = this.read(buffer)
    }

    return digest.digest().toHexString()
}