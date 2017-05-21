package com.danilafe.ktstarbound.data

import com.danilafe.ktstarbound.extractor.GenericExtractor
import com.danilafe.ktstarbound.readers.GenericReader

/**
 * A class that's capable of reading an SBAsset6 file.
 */
public class SBAsset6(extractor: GenericExtractor<GenericReader>) {

    /**
     * A data class that holds information about a single path entry.
     */
    public data class AssetData(val offset: Long, val length: Long)

    /**
     * The string at the beginning of an SBAsset6 file.
     */
    public val keyString = extractor.readString(8)!!
    /**
     * The offset of the index of this file.
     */
    public val offset = extractor.readLong()!!

    init {
        extractor.reader.move(offset)
    }

    /**
     * The string found at the beginning of an index.
     */
    public val indexString = extractor.readString(5)!!
    /**
     * The information about this file.
     */
    public val fileInfo = extractor.serializedReadMap()!!
    /**
     * The number of files in this asset file.
     */
    public val fileCount = extractor.readVariableInt()!!
    /**
     * The map of paths to their AssetData elements.
     */
    public val pathMap: Map<String, AssetData> = mutableMapOf()

    init {
        for(i in 0..fileCount - 1){
            val length = extractor.readByte()!!
            val path = extractor.readString(length.toInt())!!
            (pathMap as MutableMap).put(path, AssetData(extractor.readLong()!!, extractor.readLong()!!))
        }
    }

}