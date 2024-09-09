package com.cw.automaster.utils

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes

object FileUtils {
    /**
     * 拷贝文件夹
     */
    fun copyDirectory(source: Path, destination: Path) {
        // 确保目标目录存在
        if (Files.notExists(destination)) {
            Files.createDirectories(destination)
        }

        Files.walkFileTree(source, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                // 在目标路径创建子目录
                val targetPath = destination.resolve(source.relativize(dir))
                try {
                    Files.createDirectories(targetPath)
                } catch (e: IOException) {
                    e.printStackTrace()
                    return FileVisitResult.TERMINATE
                }
                return FileVisitResult.CONTINUE
            }

            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                // 复制文件
                val targetPath = destination.resolve(source.relativize(file))
                try {
                    Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING)
                } catch (e: IOException) {
                    e.printStackTrace()
                    return FileVisitResult.TERMINATE
                }
                return FileVisitResult.CONTINUE
            }
        })
    }
}