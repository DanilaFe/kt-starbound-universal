package com.danilafe.ktstarbound.jvm.readers

import com.danilafe.ktstarbound.jvm.readRawInt
import com.danilafe.ktstarbound.readers.GenericLeafReader
import java.io.File
import java.io.RandomAccessFile

public class RandomLeafReader(file: File, index: Long, headerSize: Long, blockSize: Long, prefixSize: Long)
    : GenericLeafReader(::readRawInt, headerSize, blockSize, prefixSize){

    private val fileReader = RandomAccessFile(file, "r")

    init {
        fileReader.seek(index)
    }

    override fun index(): Long {
        return fileReader.filePointer
    }

    override fun move(to: Long) {
        fileReader.seek(to)
    }

    override fun readRaw(length: Int): ByteArray? {
        val bytes = ByteArray(length)
        fileReader.read(bytes)
        return bytes
    }

    override fun advanceRaw(length: Long) {
        fileReader.seek(fileReader.filePointer + length)
    }

}

