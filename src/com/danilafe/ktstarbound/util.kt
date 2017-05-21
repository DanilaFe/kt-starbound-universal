package com.danilafe.ktstarbound

public fun compareByteArrays(a: ByteArray, b: ByteArray): Int {
    if(a.size != b.size) return a.size - b.size

    return (0..a.size - 1)
            .firstOrNull { a[it] != b[it] }
            ?.let { (a[it].toInt() and 0xFF) - (b[it].toInt() - 0xFF) }
            ?: 0
}

