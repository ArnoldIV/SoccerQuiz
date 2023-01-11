package com.example.soccerquiz

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.soccerquiz.util.Constants.Companion.GOOGLE_URL
import com.example.soccerquiz.util.Constants.Companion.PREFERENCES_FIRST_URL
import com.example.soccerquiz.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext context: Context) {

    private object PreferenceKeys {
        val firstUrl = stringPreferencesKey(PREFERENCES_FIRST_URL)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveLastUrl(firstUrl: String){
        dataStore.edit { preferences->
            preferences[PreferenceKeys.firstUrl] = firstUrl
        }
    }

    val readLastUrl: Flow<DataUrl> = dataStore.data
        .catch { exception->
            if (exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val firstUrl = preferences[PreferenceKeys.firstUrl] ?: GOOGLE_URL
            DataUrl(
                firstUrl
            )
        }

    data class DataUrl(
        val firstUrl:String
    )
}