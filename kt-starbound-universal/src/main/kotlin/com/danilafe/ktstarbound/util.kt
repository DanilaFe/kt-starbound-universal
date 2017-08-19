package com.danilafe.ktstarbound

/**
 * Compares two byte arrays, returning 0 if they are equal,
 * a value bigger than one if the first array is bigger,
 * and a value smaller than one if the second array is bigger.
 */
public fun compareByteArrays(a: ByteArray, b: ByteArray): Int {
    if(a.size != b.size) return a.size - b.size

    return (0..a.size - 1)
            .firstOrNull { a[it] != b[it] }
            ?.let { (a[it].toInt() and 0xff) - (b[it].toInt() and 0xff) }
            ?: 0
}

