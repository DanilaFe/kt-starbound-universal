package com.danilafe.ktstarbound.readers

/**
 * A generic reader built to operate on Leaf nodes in a BTreeDB.
 */
public abstract class GenericLeafReader
    (protected val parseIntFunction: (ByteArray) -> Int, protected val headerSize: Long, protected val blockSize: Long, protected val prefixSze: Long) : GenericReader() {

    /**
     * The index of the block this reader is currently on
     */
    private var blockIndex = 0

    /**
     * Gets the end index of the given block.
     */
    public fun getBlockEndIndex(block: Int): Long {
        return (headerSize + blockSize * (block + 1))
    }

    /**
     * Gets the end index of the current block.
     */
    public fun getCurrentEndIndex(): Long {
        return getBlockEndIndex(blockIndex)
    }

    /**
     * Reads data from the reader without regards for the leaf boundaries.
     */
    public abstract fun readRaw(length: Int): ByteArray?

    /**
     * Advances the reader without regards for leaf boundaries.
     */
    public abstract fun advanceRaw(length: Long)

    override fun read(length: Int): ByteArray? {
        var remainingData = length
        val data = ByteArray(length)
        val index = 0

        while(remainingData > 0){
            val canRead = minOf(remainingData, (getCurrentEndIndex() - 4 - index()).toInt())
            val newData = readRaw(canRead)?: return null

            for(i in 0..canRead - 1){
                data[index + i] = newData[i]
            }

            remainingData -= canRead
            if(index() == getCurrentEndIndex() - 4){
                val integerBytes = readRaw(4)?: return null
                blockIndex = parseIntFunction(integerBytes)
                move(headerSize + blockSize * blockIndex + prefixSze)
            }
        }

        return data
    }

    override fun advance(by: Long) {
        var remainingData = by

        while(remainingData > 0){
            val canRead = minOf(remainingData, getCurrentEndIndex() - 4 - index())
            advanceRaw(canRead)

            remainingData -= canRead
            if(index() == getCurrentEndIndex() - 4){
                val integerBytes = readRaw(4)?: return
                blockIndex = parseIntFunction(integerBytes)
                move(headerSize + blockSize * blockIndex + prefixSze)
            }
        }
    }

}