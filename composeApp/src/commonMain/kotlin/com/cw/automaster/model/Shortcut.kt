package com.cw.automaster.model

import kotlinx.serialization.Serializable

@Serializable
data class Shortcut(
    val key: String,
    var modifiers: List<String>? = null,
) {
    override fun toString(): String {
        return if (!modifiers.isNullOrEmpty()) {
            val modifierText = modifiers?.joinToString("+") ?: ""
            "$modifierText+$key"
        } else {
            key
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o is Shortcut) {
            val m1 = modifiers
            val m2 = o.modifiers
            return if (!m1.isNullOrEmpty() && !m2.isNullOrEmpty()) {
                key == o.key && m1.toSet() == m2.toSet()
            } else {
                key == o.key
            }
        }
        return false
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + (modifiers?.hashCode() ?: 0)
        return result
    }
}
