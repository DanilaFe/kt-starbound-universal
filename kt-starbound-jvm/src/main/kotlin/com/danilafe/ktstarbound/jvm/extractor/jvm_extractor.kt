package com.danilafe.ktstarbound.jvm.extractor

import com.danilafe.ktstarbound.extractor.GenericExtractor
import com.danilafe.ktstarbound.readers.GenericReader
import java.nio.ByteBuffer

/**
 * An extractor that specifically uses JVM-specific classes and such
 * to extract data from its readers.
 */
public class JvmExtractor<T : GenericReader>(reader: T) : GenericExtractor<T>(reader){

    override fun readByte(): Byte? {
        var bytes = reader.read(1)?: return null
        return bytes[0]
    }

    override fun readBoolean(): Boolean? {
        val byte = readByte()?: return null
        return byte.toInt() != 0
    }

    override fun readFloat(): Float? {
        val bytes = reader.read(4)?: return null
        return ByteBuffer.wrap(bytes).float
    }

    override fun readDouble(): Double? {
        val bytes = reader.read(8)?: return null
        return ByteBuffer.wrap(bytes).double
    }

    override fun readShort(): Short? {
        val bytes = reader.read(2)?: return null
        return ByteBuffer.wrap(bytes).short
    }

    override fun readInt(): Int? {
        val bytes = reader.read(4)?: return null
        return ByteBuffer.wrap(bytes).int
    }

    override fun readLong(): Long? {
        val bytes = reader.read(8)?: return null
        return ByteBuffer.wrap(bytes).long
    }

    override fun readString(length: Int): String? {
        var bytes = reader.read(length)?: return null
        return String(bytes)
    }

    override fun readVariableInt(): Long? {
        var currentLong = 0L
        var byte: Byte
        do {
            currentLong = currentLong shl 7
            byte = readByte() ?: return null
            currentLong = currentLong or (byte.toLong() and 0b1111111)
        } while((byte.toInt() and 0b10000000) != 0)
        return currentLong
    }

}
