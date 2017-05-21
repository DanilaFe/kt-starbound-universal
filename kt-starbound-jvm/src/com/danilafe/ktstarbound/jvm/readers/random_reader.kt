package com.danilafe.ktstarbound.jvm.readers

import com.danilafe.ktstarbound.readers.GenericReader
import java.io.File
import java.io.RandomAccessFile

/**
 * A normal reader powered by the RandomAccessFile class.
 */
public class RandomReader(file: File, index: Long) : GenericReader() {

    /**
     * The random access file responsible for reading data and such.
     */
    private val fileReader = RandomAccessFile(file, "r")

    init {
        fileReader.seek(index)
    }

    override fun index(): Long {
        return fileReader.filePointer
    }

    override fun read(length: Int): ByteArray? {
        val buffer = ByteArray(length)
        fileReader.read(buffer)
        return buffer
    }

    override fun advance(by: Long) {
        fileReader.seek(fileReader.filePointer + by)
    }

    override fun move(to: Long) {
        fileReader.seek(to)
    }

}

