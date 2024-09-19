package com.cw.automaster.tray

import java.awt.AWTException
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import javax.imageio.ImageIO

object TrayManager {

    private val popupMenu by lazy { PopupMenu() }
    private var hasInit = false

    private fun initMenu() {
        if (hasInit || !SystemTray.isSupported()) {
            return
        }
        try {
            // 创建托盘图标
            val trayIcon = java.awt.TrayIcon(
                ImageIO.read(object {}.javaClass.getResource("/tray.png"))
            )
            SystemTray.getSystemTray().add(trayIcon.apply { popupMenu = TrayManager.popupMenu })
            hasInit = true
        } catch (e: AWTException) {
            e.printStackTrace()
        }
    }

    fun addMenuItem(item: String, onClick: () -> Unit) {
        initMenu()
        popupMenu.add(
            MenuItem(item).apply {
                addActionListener {
                    onClick()
                }
            }
        )
    }

    fun removeAllItem() {
        initMenu()
        popupMenu.removeAll()
    }
}