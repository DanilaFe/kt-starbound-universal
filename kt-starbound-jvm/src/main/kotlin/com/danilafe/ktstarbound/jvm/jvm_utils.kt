@file:JvmName("JvmUtils")
package com.danilafe.ktstarbound.jvm

import com.danilafe.ktstarbound.extractor.GenericExtractor
import com.danilafe.ktstarbound.jvm.extractor.JvmExtractor
import com.danilafe.ktstarbound.jvm.readers.RandomLeafReader
import com.danilafe.ktstarbound.jvm.readers.RandomReader
import com.danilafe.ktstarbound.readers.GenericLeafReader
import com.danilafe.ktstarbound.readers.GenericReader
import com.danilafe.ktstarbound.readers.impl.ArrayReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.util.zip.Inflater

/**
 * Function for GenericLeafReader that uses JVM-specific data
 * to read an integer.
 */
fun readRawInt(array: ByteArray): Int {
    return ByteBuffer.wrap(array).int
}

/**
 * Decompresses the given data in a platform-dependent way (Inflater)
 */
fun decompress(array: ByteArray): ByteArray {
    val inflater = Inflater()
    val outputStream = ByteArrayOutputStream()
    val buffer = ByteArray(2048)
    inflater.setInput(array)
    while(!inflater.finished()){
        val readBytes = inflater.inflate(buffer)
        outputStream.write(buffer, 0, readBytes)
    }
    return outputStream.toByteArray()
}

/**
 * Creates an extractor from an ArrayReader, used for the World class.
 * Uses the platform-specific JvmExtractor
 */
fun createArrayExtractor(reader: ArrayReader): GenericExtractor<GenericReader> {
    return JvmExtractor(reader)
}

/**
 * Creates an extractor for a leaf reader from the file path and other required info.
 */
fun createLeafExtractor(file: String, headerSize: Long, blockSize: Long, prefixSize: Long): JvmExtractor<GenericLeafReader> {
    return JvmExtractor(RandomLeafReader(File(file), 0, headerSize, blockSize, prefixSize))
}

/**
 * Creates an extractor for a normal reader from the file path.
 */
fun createExtractor(file: String): JvmExtractor<GenericReader> {
    return JvmExtractor(RandomReader(File(file), 0))
}

/**
 * Creates a leaf extractor from the given file instance and other required info.
 */
fun createLeafExtractor(file: File, headerSize: Long, blockSize: Long, prefixSize: Long): JvmExtractor<GenericLeafReader> {
    return JvmExtractor(RandomLeafReader(file, 0, headerSize, blockSize, prefixSize))
}

/**
 * Creates a normal extractor from the given file.
 */
fun createExtractor(file: File): JvmExtractor<GenericReader> {
    return JvmExtractor(RandomReader(file, 0))
}