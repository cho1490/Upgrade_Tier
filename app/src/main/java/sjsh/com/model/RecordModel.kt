package sjsh.com.model

import java.io.Serializable

data class RecordModel(
        var result: String? = null,
        var time: String? = null,
        var kill: String? = null,
        var death: String? = null,
        var assist: String? = null,
        var kdaPercent: String? = null,
        var gameMode: String? = null,
        var gameAgo: String? = null,
        var badge: String? = null,
        var championUrl: String? = null,
        var spellUrl: ArrayList<String>? = arrayListOf(),
        var runeUrl: ArrayList<String>? = arrayListOf(),
        var itemUrl: ArrayList<String>? = arrayListOf()
): Serializable