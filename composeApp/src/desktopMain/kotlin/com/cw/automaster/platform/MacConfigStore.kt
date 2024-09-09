package com.cw.automaster.platform

import java.io.File

object MacConfigStore : ConfigStore {
    private val configFile =
        File(System.getProperty("user.home"), ".automaster/config.json")

    override fun getConfigJson(): String {
        if (configFile.exists()) {
            return configFile.readText()
        }
        return ""
    }

    override fun saveConfigJson(json: String) {
        if (!configFile.exists()) {
            configFile.createNewFile()
        }
        configFile.writeText(json)
    }
}