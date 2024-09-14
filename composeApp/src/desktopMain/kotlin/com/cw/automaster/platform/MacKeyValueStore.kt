package com.cw.automaster.platform

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

object MacKeyValueStore : KeyValueStore {
    private val configFile =
        File(System.getProperty("user.home"), ".automaster/setting.properties")

    private val properties = Properties()

    init {
        loadProperties()
    }

    // 加载 properties 文件
    private fun loadProperties() {
        if (configFile.exists()) {
            FileInputStream(configFile).use { inputStream ->
                properties.load(inputStream)
            }
        }
    }

    // 保存 properties 到文件
    private fun saveProperties() {
        FileOutputStream(configFile).use { outputStream ->
            properties.store(outputStream, "Settings")
        }
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return properties.getProperty(key)?.toIntOrNull() ?: defaultValue
    }

    override fun setInt(key: String, value: Int) {
        properties.setProperty(key, value.toString())
        saveProperties()
    }

    override fun getString(key: String, defaultValue: String): String {
        return properties.getProperty(key, defaultValue)
    }

    override fun setString(key: String, value: String) {
        properties.setProperty(key, value)
        saveProperties()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return properties.getProperty(key)?.toBoolean() ?: defaultValue
    }

    override fun setBoolean(key: String, value: Boolean) {
        properties.setProperty(key, value.toString())
        saveProperties()
    }
}