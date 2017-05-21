@file:JvmName("JvmUtils")
package com.danilafe.ktstarbound.jvm

import com.danilafe.ktstarbound.jvm.extractor.JvmExtractor
import com.danilafe.ktstarbound.jvm.readers.RandomLeafReader
import com.danilafe.ktstarbound.jvm.readers.RandomReader
import com.danilafe.ktstarbound.readers.GenericLeafReader
import com.danilafe.ktstarbound.readers.GenericReader
import java.io.File
import java.nio.ByteBuffer

fun readRawInt(array: ByteArray): Int {
    return ByteBuffer.wrap(array).int
}

fun createLeafExtractor(file: String, headerSize: Long, blockSize: Long, prefixSize: Long): JvmExtractor<GenericLeafReader> {
    return JvmExtractor(RandomLeafReader(File(file), 0, headerSize, blockSize, prefixSize))
}

fun createExtractor(file: String): JvmExtractor<GenericReader> {
    return JvmExtractor(RandomReader(File(file), 0))
}

fun createLeafExtractor(file: File, headerSize: Long, blockSize: Long, prefixSize: Long): JvmExtractor<GenericLeafReader> {
    return JvmExtractor(RandomLeafReader(file, 0, headerSize, blockSize, prefixSize))
}

fun createExtractor(file: File): JvmExtractor<GenericReader> {
    return JvmExtractor(RandomReader(file, 0))
}