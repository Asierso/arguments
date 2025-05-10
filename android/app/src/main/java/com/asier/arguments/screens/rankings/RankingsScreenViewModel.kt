package com.asier.arguments.screens.rankings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.api.rankings.RankingsService
import com.asier.arguments.api.users.UsersService
import com.asier.arguments.entities.rankings.Ranking
import com.asier.arguments.entities.user.User
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.utils.GsonUtils
import com.asier.arguments.utils.storage.LocalStorage
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RankingsScreenViewModel : ViewModel() {
    var storage by mutableStateOf<LocalStorage?>(null)

    //Ranking data
    var sortedRanking = mutableMapOf<String,Int>()
    var selfPosition by mutableIntStateOf(0)
    var xpEarn by mutableIntStateOf(0)
    var totalXp by mutableIntStateOf(0)

    fun loadRanking(scope: CoroutineScope){
        //val discussionId = storage!!.load("discussion")
        val discussionId = "681aa26719a2aa0186950d1b"
        scope.launch {
            withContext(Dispatchers.IO){
                val response = RankingsService.selectByDiscussion(localStorage = storage!!, discussionId = discussionId!!)
                when(StatusCodes.valueOf(response!!.status)){
                    StatusCodes.SUCCESSFULLY -> {
                        val ranking = GsonUtils.jsonToClass<Ranking>(response.result as LinkedTreeMap<*, *>)
                        withContext(Dispatchers.Main){
                            sortedRanking = ranking.ranking.toSortedMap(java.util.Comparator { t, t2 ->
                                return@Comparator ranking.ranking.getValue(t2).compareTo(ranking.ranking.getValue(t))
                            })

                            val username = storage!!.load("user")

                            //Get self position in ranking
                            for((key,value) in sortedRanking){
                                selfPosition++
                                if(key.equals(username))
                                    break
                            }

                            //Get earned xp
                            xpEarn = ranking.xpPoints[username] ?: 0
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun loadUserXp(scope: CoroutineScope){
        scope.launch {
            withContext(Dispatchers.IO){
                val username = storage!!.load("user") ?: return@withContext
                val response = UsersService.getByUsername(username)
                when(StatusCodes.valueOf(response!!.status)){
                    StatusCodes.SUCCESSFULLY -> {
                        totalXp = GsonUtils.jsonToClass<User>(response.result as LinkedTreeMap<*, *>).xp
                    }
                    else -> {}
                }
            }
        }
    }
}