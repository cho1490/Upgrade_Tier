package sjsh.com.util

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import sjsh.com.model.RecordModel
import sjsh.com.model.SummonerModel

object  DownloadService {

    private val parentJob = Job()

    private val handler = CoroutineExceptionHandler { _, exception -> println("Caught original $exception") }

    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob + handler)

    var summonerModel: SummonerModel = SummonerModel()
    var recordModel: ArrayList<RecordModel> = arrayListOf()


    fun startService(url: String, context: Context) {
        coroutineScope.launch(Dispatchers.Main) {
            val response = Jsoup.connect(url).get()
            getSummonerModel(response)
            getRecordModel(response)
        }
    }

    private suspend fun getSummonerModel(response: Document) =
            withContext(Dispatchers.IO) {
                try {
                    summonerModel.apply {
                        response.apply {
                            val profileInfo: Element = selectFirst("div.ProfileIcon")
                            iconUrl = "https:" + profileInfo.child(profileInfo.childrenSize() - 2).attr("src")
                            level = profileInfo.child(profileInfo.childrenSize() - 1).text()

                            val summonerName: Element = selectFirst("div.Information")
                            nickName = summonerName.child(summonerName.childrenSize() - 3).text();

                            val soloRankTier: Element = selectFirst("div.SummonerRatingMedium")
                            soloRankTierUrl = "https:" + soloRankTier.child(0).child(0).attr("src");
                            soloRankTierText = soloRankTier.child(1).child(1).text();

                            val freeRankTier: Element = selectFirst("div.sub-tier")
                            freeRankTierUrl = "https:" + freeRankTier.child(0).attr("src")
                            freeRankTierText = freeRankTier.child(1).child(1).text()
                        }
                    }
                } catch (e: Exception) {
                    when (e) {
                        is HttpStatusException -> {
                            Log.d("csh : ", e.statusCode.toString())
                        }
                    }
                }
                return@withContext
            }

    private suspend fun getRecordModel(response: Document) =
            withContext(Dispatchers.IO) {
                try {
                    recordModel.apply {
                        response.apply {
                            val gameInfo: Elements = select("div.GameStats")
                            val kda: Elements = select("div.KDA div.KDA")
                            val kdaPercents: Elements = select("div.CKRate")
                            val gameStatusUrl: Elements = select("div.GameSettingInfo")
                            val itemUrls: Elements = select("div.itemList")

                            for (index in 0..gameInfo.count()) {
                                val recordModelElement: RecordModel = RecordModel()

                                recordModelElement.apply {
                                    when {
                                        gameInfo[index].child(3).text() == "Victory" -> result = "승"
                                        gameInfo[index].child(3).text() == "Defeat" -> result = "패"
                                        gameInfo[index].child(3).text() == "Remake" -> result = "리"
                                    }
                                    time = (gameInfo[index].child(4).text().split(" ".toRegex()).toTypedArray()[0].replace("m", "분"))

                                    kill = (kda[index].child(0).text())
                                    death = (kda[index].child(1).text())
                                    assist = (kda[index].child(2).text())
                                    badge = if (kda[index].parent().childrenSize() > 2) (getBadge(kda[index].parent().child(kda[index].parent().childrenSize() - 1).child(0).text())) else ("")

                                    kdaPercent = ("킬관여 " + kdaPercents[index].text().split(" ".toRegex()).toTypedArray()[1])

                                    championUrl = ("https:" + gameStatusUrl[index].child(0).child(0).child(0).attr("src"))

                                    spellUrl?.add("https:" + gameStatusUrl[index].child(1).child(0).child(0).attr("src"))
                                    spellUrl?.add("https:" + gameStatusUrl[index].child(1).child(1).child(0).attr("src"))
                                    runeUrl?.add("https:" + gameStatusUrl[index].child(2).child(0).child(0).attr("src"))
                                    runeUrl?.add("https:" + gameStatusUrl[index].child(2).child(1).child(0).attr("src"))

                                    for (item in 0..6) {
                                        if (itemUrls[index].child(item).child(0).hasAttr("src"))
                                            itemUrl?.add(itemUrls[index].child(item).child(0).attr("src"))
                                        else
                                            itemUrl?.add("")
                                    }
                                }
                                Log.d("csh : " ,"추가")
                                recordModel.add(recordModelElement)
                                Log.d("csh : " ,"완료")
                            }
                        }
                    }
                } catch (e: Exception) {
                    when (e) {
                        is HttpStatusException -> {
                            Log.d("csh : ", e.statusCode.toString())
                        }
                    }
                }
                return@withContext
            }

    private fun getBadge(Badge: String): String? {
        var badgeName = "?"

        //System.out.println("csh : "  + Badge);
        badgeName = if (Badge == "Double Kill") "더블킬" else if (Badge == "Triple Kill") "트리플킬" else if (Badge == "Quadra Kill") "쿼드라킬" else if (Badge == "Penta Kill") "펜타킬" else Badge
        return badgeName
    }

}