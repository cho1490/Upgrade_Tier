package sjsh.com.util

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class DownloadService {

    private val parentJob = Job()

    private val handler = CoroutineExceptionHandler { _, exception -> println("Caught original $exception") }

    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob + handler)

    fun startService(url: String, context: Context){
        coroutineScope.launch(Dispatchers.Main) {
            scrapPage(url)
        }
    }

    private suspend fun scrapPage(url: String) =
            withContext(Dispatchers.IO){
                try{
                    Log.d("csh", url)
                    Log.d("csh", "1")
                    val response = Jsoup.connect(url).get()
                    Log.d("csh", "2")
                    val element : Element = response.selectFirst("div.Information");
                }catch (e: Exception){
                    when(e) {
                        is HttpStatusException -> {
                            print("csh : " + e.statusCode)
                        }
                    }
                }
                return@withContext
            }

}