package com.danilafe.ktstarbound.types

import com.danilafe.ktstarbound.data.BTreeDB5
import com.danilafe.ktstarbound.data.internal.VersionedData
import com.danilafe.ktstarbound.dynamic.*
import com.danilafe.ktstarbound.extractor.GenericExtractor
import com.danilafe.ktstarbound.readers.GenericLeafReader
import com.danilafe.ktstarbound.readers.GenericReader
import com.danilafe.ktstarbound.readers.impl.ArrayReader

/**
 * A wrapper around Starbound's .world file type.
 * Takes two extra functions that can't be implemented in pure Kotlin -
 * decompressFunction, which decompresses ZLIB-compressed data,
 * and createExtractorFunction, which creates a platform-specific extractor
 * from the pure Kotlin ArrayReader
 */
public class World(val decompressionFunction: (ByteArray) -> ByteArray,
                   val createExtractorFunction: (ArrayReader) -> GenericExtractor<GenericReader>,
                   startExtractor: GenericExtractor<GenericReader>) {

    /**
     * The Metadata wrapper class. This holds not only pre-extracted information, but also
     * a full VersionedData that contains all of the world metadata.
     */
    public data class Metadata(val width: Int, val height: Int,
                               val playerStart: Pair<Double, Double>,
                               val respawnInWorld: Boolean, val spawningEnabled: Boolean, val adjustPlayerStart: Boolean,
                               val dungeonMap: Map<Long, String>, val protectedDungeons: List<Long>,
                               val fullData: VersionedData)

    /**
     * The BTreeDB5 that backs the world file.
     */
    public val btree = BTreeDB5(startExtractor)

    /**
     * Converts the world parameters to a byte array key
     * for use in the BTreeDB
     */
    public fun dataToKey(layer: Byte, x: Short, y: Short): ByteArray {
        val newKey = ByteArray(5)
        newKey[0] = layer
        newKey[1] = (x.toInt() shr 8).toByte()
        newKey[2] = (x.toInt() and 0xff).toByte()
        newKey[3] = (y.toInt() shr 8).toByte()
        newKey[4] = (y.toInt() and 0xff).toByte()
        return newKey
    }

    /**
     * Gets the data at the given parameters.
     */
    public fun get(layer: Byte, x: Short, y: Short, extractor: GenericExtractor<GenericReader>, leafExtractor: GenericExtractor<GenericLeafReader>): ByteArray? {
        val key = dataToKey(layer, x, y)
        val data = btree.get(key, extractor, leafExtractor)?: return null
        return decompressionFunction(data)
    }

    /**
     * Extracts the world's metadata.
     */
    public fun getMetadata(extractor: GenericExtractor<GenericReader>, leafExtractor: GenericExtractor<GenericLeafReader>): Metadata? {
        val data = get(0, 0, 0, extractor, leafExtractor)?: return null
        val arrayExtractor = createExtractorFunction(ArrayReader(data, 0))
        val width = arrayExtractor.readInt()?: return null
        val height = arrayExtractor.readInt()?: return null
        val versionedData = VersionedData(arrayExtractor)
        val worldDataMap = (versionedData.data as? DynamicMap ?: return null).data

        val playerStartList = (worldDataMap["playerStart"] as? DynamicList ?: return null).data
        val playerStartPair = (playerStartList[0] as? DynamicDouble ?: return null).data to
                (playerStartList[1] as? DynamicDouble ?: return null).data

        val respawnInWorld = (worldDataMap["respawnInWorld"] as? DynamicBoolean ?: return null).data
        val spawningEnabled = (worldDataMap["spawningEnabled"] as? DynamicBoolean ?: return null).data
        val adjustPlayerStart = (worldDataMap["adjustPlayerStart"] as? DynamicBoolean ?: return null).data

        val dungeonIds = mutableMapOf<Long, String>()
        val dungeonIdMap = (worldDataMap["dungeonIdMap"] as? DynamicList ?: return null).data
        for(list in dungeonIdMap){
            val dungeon = (list as? DynamicList ?: return null).data
            val key = (dungeon[0] as? DynamicVariableInteger ?: return null).data
            val value = (dungeon[1] as? DynamicString ?: return null).data
            dungeonIds.put(key, value)
        }

        val protectedDungeons = mutableListOf<Long>()
        val protectedDungeonIds = (worldDataMap["protectedDungeonIds"] as? DynamicList ?: return null).data
        protectedDungeonIds.mapTo(protectedDungeons) { (it as? DynamicVariableInteger ?: return null).data }

        return Metadata(width, height,
                playerStartPair,
                respawnInWorld, spawningEnabled, adjustPlayerStart,
                dungeonIds, protectedDungeons,
                versionedData)
    }

}
