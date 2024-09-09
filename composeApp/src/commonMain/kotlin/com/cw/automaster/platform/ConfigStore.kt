package com.cw.automaster.platform

interface ConfigStore {
    fun getConfigJson(): String
    fun saveConfigJson(json: String)
}