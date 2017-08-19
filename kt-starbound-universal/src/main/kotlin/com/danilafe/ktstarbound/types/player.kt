package com.danilafe.ktstarbound.types

import com.danilafe.ktstarbound.data.SBVJ01
import com.danilafe.ktstarbound.dynamic.DynamicMap
import com.danilafe.ktstarbound.dynamic.DynamicString
import com.danilafe.ktstarbound.extractor.GenericExtractor
import com.danilafe.ktstarbound.readers.GenericReader

public class Player(extractor: GenericExtractor<GenericReader>) {

    public val sbvj = SBVJ01(extractor)
    public val name: String

    init {
        val map = ((sbvj.versionedData.data as? DynamicMap)!!).data
        val identityMap = ((map["identity"] as? DynamicMap)!!).data

        name = ((identityMap["name"] as? DynamicString)!!).data
    }

}
