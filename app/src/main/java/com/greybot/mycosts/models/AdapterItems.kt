package com.greybot.mycosts.models

import com.greybot.mycosts.R
import com.greybot.mycosts.present.folder.preview.ButtonType

sealed class AdapterItems {
    data class FolderItem(
        val name: String,
        val path: String,
        val countInner: String,
        val total: String,
        val objectId: String? = null
    ) :
        AdapterItems() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FolderItem

            if (name != other.name) return false
            if (path != other.path) return false
            if (countInner != other.countInner) return false
            if (total != other.total) return false
            if (objectId != other.objectId) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + path.hashCode()
            result = 31 * result + countInner.hashCode()
            result = 31 * result + total.hashCode()
            result = 31 * result + (objectId?.hashCode() ?: 0)
            return result
        }
    }

    data class RowItem(
        val objectId: String,
        val name: String,
        val path: String,
        val measure: MeasureType,
        val count: Double,
        val price: Double,
        val isBought: Boolean = false,
        val highlight: Boolean = false,
    ) : AdapterItems()

    class ButtonAddItem(val type: ButtonType) : AdapterItems()
    class TotalItem(
        val name1: String = "Total: Order",
        val value1: String,
        val name2: String = "Total: Check",
        val value2: String
    ) : AdapterItems()

    data class SpaceItem(val heightRes: Int = R.dimen.height_margin_84) : AdapterItems()
}

enum class MeasureType(val rowValue: String) {
    KG("kg"), PCS("pcs"), None("");

    companion object {
        fun toType(measure: String?): MeasureType {
            return values().find { it.name == measure } ?: None
        }
    }

}
