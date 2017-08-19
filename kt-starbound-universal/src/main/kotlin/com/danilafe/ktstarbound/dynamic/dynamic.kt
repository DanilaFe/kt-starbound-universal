package com.danilafe.ktstarbound.dynamic

sealed class Dynamic
data class DynamicDouble(val data: Double) : Dynamic()
data class DynamicBoolean(val data: Boolean) : Dynamic()
data class DynamicVariableInteger(val data: Long) : Dynamic()
data class DynamicString(val data: String) : Dynamic()
data class DynamicList(val data: List<Dynamic>) : Dynamic()
data class DynamicMap(val data: Map<String, Dynamic>) : Dynamic()
object DynamicNil : Dynamic()
