package com.danilafe.ktstarbound.extractor

import com.danilafe.ktstarbound.dynamic.*
import com.danilafe.ktstarbound.readers.GenericReader

/**
 * A class built to extract information from a reader,
 * in a platform independent way. Things like ByteBuffers are
 * too Java-specific to work here.
 */
public abstract class GenericExtractor<T : GenericReader>(public var reader: T) {

    /**
     * Reads a byte of data from the reader.
     */
    public abstract fun readByte(): Byte?

    /**
     * Reads a boolean from the reader.
     */
    public abstract fun readBoolean(): Boolean?

    /**
     * Reads a float from the reader.
     */
    public abstract fun readFloat(): Float?

    /**
     * Reads a double from the reader.
     */
    public abstract fun readDouble(): Double?

    /**
     * Reads a short from the reader.
     */
    public abstract fun readShort(): Short?

    /**
     * Reads an integer from the reader.
     */
    public abstract fun readInt(): Int?

    /**
     * Reads a long from the reader.
     */
    public abstract fun readLong(): Long?

    /**
     * Reads a string from the reader.
     */
    public abstract fun readString(length: Int): String?

    /**
     * Reads variable integer from the reader.
     */
    public abstract fun readVariableInt(): Long?

    /**
     * Reads bytes from the reader, the number
     * of which is specified by variable length integer.
     */
    public fun serializedReadBytes(): ByteArray? {
       val length = readVariableInt()?: return null
        return reader.read(length.toInt())
    }

    /**
     * Reads a string from the reader, the length
     * of which is defined by a variable length integer.
     */
    public fun serializedReadString(): String? {
        val bytes = serializedReadBytes()?: return null
        var string = String()
        for(byte in bytes){
            string += byte.toChar()
        }
        return string
    }

    /**
     * Reads a list Dynamic elements.
     */
    public fun serializedReadList(): List<Dynamic>? {
        val length = readVariableInt()?: return null
        val list = mutableListOf<Dynamic>()
        for(i in 0..length - 1) {
            val dynamic = serializedReadDynamic()?: return null
            list.add(dynamic)
        }
        return list
    }

    /**
     * Reads a Map of Dynamic elements.
     */
    public fun serializedReadMap(): Map<String, Dynamic>? {
        val length = readVariableInt()?: return null
        val map = mutableMapOf<String, Dynamic>()
        for(i in 0..length - 1){
            val key = serializedReadString()?: return null
            val value = serializedReadDynamic()?: return null
            map.put(key, value)
        }
        return map
    }

    /**
     * Reads a Dynamic element.
     */
    public fun serializedReadDynamic(): Dynamic? {
        val type = readByte()?: return null
        return when(type.toInt()){
            1 -> DynamicNil
            2 -> {
                val double = readDouble()?: return null
                return DynamicDouble(double)
            }
            3 -> {
                val boolean = readBoolean()?: return null
                return DynamicBoolean(boolean)
            }
            4 -> {
                val long = readVariableInt()?: return null
                return DynamicVariableInteger(long)
            }
            5 -> {
                val string = serializedReadString()?: return null
                return DynamicString(string)
            }
            6 -> {
                val list = serializedReadList()?: return null
                return DynamicList(list)
            }
            7 -> {
                val map = serializedReadMap()?: return null
                return DynamicMap(map)
            }
            else -> null
        }
    }

}
