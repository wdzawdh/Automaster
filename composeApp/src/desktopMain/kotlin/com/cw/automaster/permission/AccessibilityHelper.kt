package com.cw.automaster.permission

import java.nio.file.Paths

@Suppress("UnsafeDynamicallyLoadedCode")
object AccessibilityHelper {
    init {
        System.load(
            Paths.get(
                "src/desktopMain/libs/libaccessibilityhelper.dylib"
            ).toAbsolutePath().toString()
        )
    }

    @JvmStatic
    external fun hasAccessibilityPermission(): Boolean

    @JvmStatic
    external fun openAccessibilitySettings()
}