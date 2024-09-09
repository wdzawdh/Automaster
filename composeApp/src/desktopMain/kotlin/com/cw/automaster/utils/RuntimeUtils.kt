package com.cw.automaster.utils

import java.io.BufferedReader
import java.io.InputStreamReader

object RuntimeUtils {
    fun runCommand(command: String): String {
        return try {
            // 使用 Runtime.exec 执行命令
            val process = Runtime.getRuntime().exec(command)

            // 读取命令输出
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText()

            // 等待进程完成并返回输出
            process.waitFor()
            output
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}