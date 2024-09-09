package com.cw.automaster.manager

import com.cw.automaster.getConfigStore
import com.cw.automaster.model.Config
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ConfigManager {
    private val configList: MutableList<Config> = mutableListOf()
    private val configStore = getConfigStore()

    fun init() {
        // 初始化时读取配置文件到内存
        loadConfigFromFile()
    }

    // 读取配置文件
    private fun loadConfigFromFile() {
        try {
            val jsonContent = configStore?.getConfigJson()
            if (!jsonContent.isNullOrEmpty()) {
                val configs = Json.decodeFromString<List<Config>>(jsonContent)
                configList.clear()
                configList.addAll(configs)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 获取所有配置
    fun getConfigs(): List<Config> {
        return configList
    }

    // 根据名称获取特定配置
    fun getConfigByPath(path: String): Config? {
        return configList.find { it.path == path }
    }

    // 添加或更新配置
    fun addOrUpdateConfig(config: Config) {
        val existingConfig = configList.indexOfFirst { it.path == config.path }
        if (existingConfig != -1) {
            // 如果配置已经存在，则更新
            configList[existingConfig] = config
        } else {
            // 否则添加新的配置
            configList.add(config)
        }
        saveConfigToFile() // 每次更新后写回文件
    }

    // 删除配置
    fun deleteConfig(path: String) {
        val config = configList.find { it.path == path }
        configList.remove(config)
        saveConfigToFile()
    }

    // 将配置保存到文件
    private fun saveConfigToFile() {
        try {
            val jsonContent = Json.encodeToString(configList)
            configStore?.saveConfigJson(jsonContent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}