package com.danilafe.ktstarbound.data.internal

import com.danilafe.ktstarbound.extractor.GenericExtractor
import com.danilafe.ktstarbound.readers.GenericReader


/**
 * Internal tile class.
 */
public class Tile(extractor: GenericExtractor<GenericReader>, length: Int = 30){

    /**
     * The material of this tile.
     */
    public val foregroundMaterial = extractor.readShort()!!
    /**
     * By how much this tile's hue is shifted
     */
    public val foregroundHueShift = extractor.readByte()!!
    /**
     * The color variant of this tile.
     */
    public val foregroundColorVariant = extractor.readByte()!!
    /**
     * The foreground "mod".
     */
    public val foregroundMod = extractor.readShort()!!
    /**
     * The hue shift of the foreground "mod'.
     */
    public val foregroundModHueShift = extractor.readByte()!!

    /**
     * The material in the background.
     */
    public val backgroundMaterial = extractor.readShort()!!
    /**
     * The hue shift of the tile's background.
     */
    public val backgroundHueShift = extractor.readByte()!!
    /**
     * The color variant of this tile's background.
     */
    public val backgroundColorVariant = extractor.readByte()!!
    /**
     * The background "mod".
     */
    public val backgroundMod = extractor.readShort()!!
    /**
     * The hue shift of the background mod
     */
    public val backgroundModHueShift = extractor.readByte()!!

    /**
     * The type of liquid in this tile.
     */
    public val liquid = extractor.readByte()!!
    /**
     * The amount of liquid in this tile.
     */
    public val liquidLevel = extractor.readFloat()!!
    /**
     * The pressure of liquid in this tile.
     */
    public val liquidPressure = extractor.readFloat()!!
    /**
     * Whether the liquid in this tile is infinite.
     */
    public val liquidInfinite = extractor.readBoolean()!!

    /**
     * The collision type (platform, block, etc)
     */
    public val collisionType = extractor.readByte()!!
    /**
     * The dungeon ID of this tile.
     */
    public val dungeonId = extractor.readShort()!!

    init {
        extractor.reader.advance(2)
    }

    /**
     * Whether this tile can't be broken.
     */
    public val indestructible = extractor.readBoolean()!!

    init {
        extractor.reader.advance(length.toLong() - 30)
    }

}