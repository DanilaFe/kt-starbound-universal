@file:JvmName("JvmExtensions")
package com.danilafe.ktstarbound.jvm

import com.danilafe.ktstarbound.data.BTreeDB5
import com.danilafe.ktstarbound.types.World
import java.io.File

public fun BTreeDB5.get(key: ByteArray, file: File): ByteArray? {
    val normalReader = createExtractor(file)
    val leafReader = createLeafExtractor(file, headerSize.toLong(), blockSize.toLong(), prefixSize.toLong())
    return get(key, normalReader, leafReader)
}
public fun BTreeDB5.get(key: ByteArray, file: String): ByteArray? {
    return get(key, File(file))
}
public fun World.get(layer: Byte, x: Short, y: Short, file: File): ByteArray? {
    val normalReader = createExtractor(file)
    val leafReader = createLeafExtractor(file, btree.headerSize.toLong(), btree.blockSize.toLong(), btree.prefixSize.toLong())
    return get(layer, x, y, normalReader, leafReader)
}
public fun World.get(layer: Byte, x: Short, y: Short, file: String): ByteArray? {
    return get(layer, x, y, File(file))
}
public fun World.getMetadata(file: File): World.Metadata? {
    val normalReader = createExtractor(file)
    val leafReader = createLeafExtractor(file, btree.headerSize.toLong(), btree.blockSize.toLong(), btree.prefixSize.toLong())
    return getMetadata(normalReader, leafReader)
}
public fun World.getMetadata(file: String): World.Metadata? {
    return getMetadata(File(file))
}