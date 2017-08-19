package com.danilafe.ktstarbound.data

import com.danilafe.ktstarbound.data.internal.VersionedData
import com.danilafe.ktstarbound.extractor.GenericExtractor
import com.danilafe.ktstarbound.readers.GenericReader

/**
 * A class capable of reading the Starbound SBVJ01 file.
 */
public class SBVJ01(extractor: GenericExtractor<GenericReader>) {

    /**
     * The string found at the beginning of an SBVJ01 file.
     */
    public val keyString = extractor.readString(6)!!
    /**
     * The actual versioned data stored in this file.
     */
    public val versionedData = VersionedData(extractor)

}
