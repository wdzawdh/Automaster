package com.cw.automaster.platform

interface PropertiesManager {
    // 读取 Int 值
    fun getInt(key: String, defaultValue: Int = 0): Int

    // 写入 Int 值
    fun setInt(key: String, value: Int)

    // 读取 String 值
    fun getString(key: String, defaultValue: String = ""): String

    // 写入 String 值
    fun setString(key: String, value: String)

    // 读取 Boolean 值
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    // 写入 Boolean 值
    fun setBoolean(key: String, value: Boolean)
}