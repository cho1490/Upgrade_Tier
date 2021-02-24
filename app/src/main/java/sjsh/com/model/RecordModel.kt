package sjsh.com.model

import java.io.Serializable

data class RecordModel(
        var result: String? = null,
        var time: String? = null,
        var kill: String? = null,
        var death: String? = null,
        var assist: String? = null,
        var kda_percent: String? = null,
        var game_mode: String? = null,
        var game_ago: String? = null,
        var badge: String? = null,
        var champion_url: String? = null,
        var spell_url: ArrayList<String>? = arrayListOf(),
        var rune_url: ArrayList<String>? = arrayListOf(),
        var item_url: ArrayList<String>? = arrayListOf()
): Serializable