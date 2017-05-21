package com.danilafe.ktstarbound.data

import com.danilafe.ktstarbound.compareByteArrays
import com.danilafe.ktstarbound.extractor.GenericExtractor
import com.danilafe.ktstarbound.readers.GenericLeafReader
import com.danilafe.ktstarbound.readers.GenericReader

/**
 * A class that's capable of reading a BTreeDB5 structure from Starbound.
 */
public class BTreeDB5(startExtractor: GenericExtractor<GenericReader>) {

    /**
     * The header size of the BTreeDB5
     */
    public val headerSize = 512
    /**
     * The size of the node prefix, like "II" or "FF"
     */
    public val prefixSize = 2

    /**
     * The string that the BTreeDB5 file starts with.
     */
    public val keyString = startExtractor.readString(8)!!
    /**
     * The size of blocks in the database.
     */
    public val blockSize = startExtractor.readInt()!!
    /**
     * The name of the database.
     */
    public val name = startExtractor.readString(16)!!.substringBefore(0.toChar())
    /**
     * The size of keys in the database.
     */
    public val keySize = startExtractor.readInt()!!
    /**
     * The size of key-value pairs in the database.
     */
    public val entrySize = keySize + 4
    /**
     * Whether to use the second root node instead of the first.
     */
    public val swap = startExtractor.readBoolean()!!

    /**
     * The block index of the first free node.
     */
    public val firstFreeIndex = startExtractor.readInt()!!
    init {
        startExtractor.reader.advance(4)
    }

    /**
     * The offset of the first free node.
     */
    public val firstFreeOffset = startExtractor.readInt()!!
    /**
     * The block index of the first root node.
     */
    public val firstRootIndex = startExtractor.readInt()!!
    /**
     * Whether the first root node is a leaf.
     */
    public val isFirstRootLeaf = startExtractor.readBoolean()!!

    /**
     * The block index of the second free node.
     */
    public val secondFreeIndex = startExtractor.readInt()!!
    init {
        startExtractor.reader.advance(4)
    }

    /**
     * The offset of the second free node.
     */
    public val secondFreeOffset = startExtractor.readInt()!!
    /**
     * The index of the second root node.
     */
    public val secondRootIndex = startExtractor.readInt()!!
    /**
     * Whether the second root node is a leaf.
     */
    public val isSecondLeaf = startExtractor.readBoolean()!!

    /**
     * Gets the data from the BTreeDB5 at the given key.
     */
    public fun  getData(key: ByteArray, normalExtractor: GenericExtractor<GenericReader>, leafExtractor: GenericExtractor<GenericLeafReader>): ByteArray? {
        /* Gets index of the root node to which the search should go first. */
        val firstKeyIndex = (if (swap) secondRootIndex else firstRootIndex) * blockSize + headerSize
        var nodeType: String

         /* Go the the index of the root node. */
        normalExtractor.reader.move(firstKeyIndex.toLong())
        do {
            /* Reads the type of the node. */
            nodeType = normalExtractor.readString(prefixSize)?: return null
            if(nodeType == "II"){
                /* Advances the reader to skip some unused data. */
                normalExtractor.reader.advance(1)

                /* Initializes data for binary search */
                var low = 0
                var high = normalExtractor.readInt()?: return null

                /* The block to which the jump */
                var block = normalExtractor.readInt()?: return null

                /* The spot where the keys begin. */
                val keyBase = normalExtractor.reader.index()
                while(low < high){
                    /* Finds the middle between low and high, and reads the key to compare */
                    val med = (low + high) / 2
                    normalExtractor.reader.move(keyBase + entrySize * med)

                    val compareKey = normalExtractor.reader.read(keySize)?: return null
                    val comparisonResult = compareByteArrays(key, compareKey)

                    /* Move the low or high to the middle,
                       to narrow the width of the search. */
                    if(comparisonResult > 0){
                        high = med
                    } else {
                        low = med + 1
                    }
                }

                /* If we don't want to jump to the first block, find the next block to jump to. */
                if(low > 0) {
                    normalExtractor.reader.move(keyBase + (low - 1) * entrySize + keySize)
                    block = normalExtractor.readInt()?: return null
                }
                normalExtractor.reader.move((headerSize + blockSize * block).toLong())
            } else if(nodeType == "LL"){
                /* Move the leaf extractor to the index to read using LeafExtractor */
                leafExtractor.reader.move(normalExtractor.reader.index())

                /* Iterates through all the keys to find the one we need. */
                val numKeys = leafExtractor.readInt()?: return null
                for(i in 0..numKeys - 1){
                    /* Reads the key and length of this entry */
                    val compareKey = leafExtractor.reader.read(keySize)?: return null
                    val dataLength = leafExtractor.readVariableInt()?: return null

                    if(compareByteArrays(key, compareKey) == 0){
                        /* If the key is right, read and return it. */
                        return leafExtractor.reader.read(dataLength.toInt())
                    } else {
                        /* Else, the data is linear, so skip the data that we don't care about. */
                        leafExtractor.reader.advance(dataLength)
                    }
                }
            }
        }  while(nodeType == "II")

        return null
    }

}
