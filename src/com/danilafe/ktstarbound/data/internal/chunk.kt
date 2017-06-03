package com.danilafe.ktstarbound.data.internal

import com.danilafe.ktstarbound.extractor.GenericExtractor
import com.danilafe.ktstarbound.readers.GenericReader

/**
 * A chunk held in the BTreeDB5 storing a world.
 * A single chunk stores an array of 32x32 tiles.
 */
public class Chunk(extractor: GenericExtractor<GenericReader>){

    init {
        extractor.reader.advance(2)
    }

    /**
     * The length of the tile data.
     */
    public val tileDataLength = extractor.readByte()!!.toInt() - 2
    /**
     * The tile data itself.
     */
    public val tiles = Array(32, { Array(32, { Tile(extractor, tileDataLength) }) })

}
