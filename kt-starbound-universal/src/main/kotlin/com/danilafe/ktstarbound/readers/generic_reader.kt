package com.danilafe.ktstarbound.readers

/**
 * A reader is an abstract class that reads
 * some data, from somewhere, at a certain index.
 *
 * Reading data advances the index, as does calling advance().
 * The reader should provide non-linear access to a file by implementing
 * move().
 */
public abstract class GenericReader {

    /**
     * Gets the current index in the data.
     */
    public abstract fun index(): Long

    /**
     * Reads data from the reader, advancing it by the amount
     * of data left.
     */
    public abstract fun read(length: Int): ByteArray?

    /**
     * Advances the reader without reading the data.
     */
    public abstract fun advance(by: Long)

    /**
     * Moves the reader's index to the given value.
     */
    public abstract fun move(to: Long)

}