package com.example.soccerquiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccerquiz.util.Constants.Companion.GOOGLE_URL
import com.example.soccerquiz.util.Constants.Companion.QUERY_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    application: Application,
private val dataStoreRepository: DataStoreRepository)
    :AndroidViewModel(application) {

    private var lastUrl = GOOGLE_URL

    var readLastUrl = dataStoreRepository.readLastUrl

    fun saveLastUrl(lastUrl: String) =
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveLastUrl(lastUrl)
        }
    fun applyQueries(): HashMap<String, String>{
        val queries: HashMap<String,String> = HashMap()

        viewModelScope.launch {
            readLastUrl.collect{value->
                lastUrl = value.firstUrl
            }
        }
        queries[QUERY_URL] = lastUrl
        return queries
    }

}