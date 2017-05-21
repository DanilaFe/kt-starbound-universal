package com.danilafe.ktstarbound.readers.impl

import com.danilafe.ktstarbound.readers.GenericReader

/**
 * A reader that supports reading from a simple Array.
 * As opposted to Platform-specific readers, this one is
 * meant to be used for data that has already been decoded by something else.
 */
public class ArrayReader(val array: ByteArray, var arrayIndex: Long) : GenericReader() {

    override fun index(): Long {
        return arrayIndex
    }

    override fun read(length: Int): ByteArray? {
        val bytes = ByteArray(length)
        for(i in 0..length - 1){
            bytes[i] = array[(arrayIndex + i).toInt()]
        }
        advance(length.toLong())
        return bytes
    }

    override fun advance(by: Long) {
        move(arrayIndex + by)
    }

    override fun move(to: Long) {
        arrayIndex = to
    }

}
