package com.danilafe.ktstarbound.jvm

import java.nio.ByteBuffer

fun readRawInt(array: ByteArray): Int {
    return ByteBuffer.wrap(array).int
}
