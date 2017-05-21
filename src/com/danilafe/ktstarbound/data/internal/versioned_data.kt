package com.danilafe.ktstarbound.data.internal

import com.danilafe.ktstarbound.extractor.GenericExtractor
import com.danilafe.ktstarbound.readers.GenericReader

/**
 * A versioned data object found inside Starbound files.
 */
public class VersionedData(extractor: GenericExtractor<GenericReader>) {

    /**
     * The name of the versioned data.
     */
    public val name = extractor.serializedReadString()!!

    init {
        extractor.reader.advance(1)
    }

    /**
     * The version of the versioned data.
     */
    public val version = extractor.readInt()
    /**
     * The data of the versioned data.
     */
    public val data = extractor.serializedReadDynamic()

}
