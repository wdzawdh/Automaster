package com.cw.automaster.tray

import com.cw.automaster.platform.MacWorkflowManager
import java.awt.AWTException
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import javax.imageio.ImageIO

object TrayManager {

    private val popupMenu by lazy { PopupMenu() }
    private var hasInit = false

    fun initTray() {
        if (hasInit || !SystemTray.isSupported()) {
            return
        }
        try {
            // 创建托盘图标
            val trayIcon = java.awt.TrayIcon(
                ImageIO.read(object {}.javaClass.getResource("/tray.png"))
            )
            SystemTray.getSystemTray().add(trayIcon.apply { popupMenu = TrayManager.popupMenu })
            removeAllItem()
            MacWorkflowManager.getAllWorkflows().forEach {
                addMenuItem(it.name) {
                    MacWorkflowManager.runWorkflow(it.path)
                }
            }
            hasInit = true
        } catch (e: AWTException) {
            e.printStackTrace()
        }
    }

    fun addMenuItem(item: String, onClick: () -> Unit) {
        popupMenu.add(
            MenuItem(item).apply {
                addActionListener {
                    onClick()
                }
            }
        )
    }

    fun removeAllItem() {
        popupMenu.removeAll()
    }
}