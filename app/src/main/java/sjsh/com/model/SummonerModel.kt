package sjsh.com.model

import java.io.Serializable

data class SummonerModel(
        var icon_url: String? = null,
        var soloRank_tier_url: String? = null,
        var soloRank_tier_text: String? = null,
        var freeRank_tier_url: String? = null,
        var freeRank_tier_text: String? = null,
        var nickName: String? = null,
        var level: String? = null
): Serializable








