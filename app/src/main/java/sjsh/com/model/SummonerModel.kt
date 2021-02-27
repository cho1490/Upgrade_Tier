package sjsh.com.model

import java.io.Serializable

data class SummonerModel(
        var iconUrl: String? = null,
        var soloRankTierUrl: String? = null,
        var soloRankTierText: String? = null,
        var freeRankTierUrl: String? = null,
        var freeRankTierText: String? = null,
        var nickName: String? = null,
        var level: String? = null
): Serializable








