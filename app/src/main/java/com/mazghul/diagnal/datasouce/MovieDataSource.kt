package com.mazghul.diagnal.datasouce

import android.content.Context
import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mazghul.diagnal.data.model.Content
import com.mazghul.diagnal.data.model.MovieResponse
import com.mazghul.diagnal.presentation.MovieGridViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class MovieDataSource(
    private val scope: CoroutineScope,
    private val context: Context,
    private val query: String = "",
    private val movieGridViewModel: MovieGridViewModel
) :
    PageKeyedDataSource<Int, Content>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Content>
    ) {
        scope.launch {
            try {
                val response = getWorkoutFromFile(context, 1)
                var resultMovies = response.page.contentItems.content
                if (query != "") {
                    resultMovies = resultMovies.filter { it1 ->
                        it1.name.toLowerCase(
                            Locale.ROOT
                        ).startsWith(query.toLowerCase(Locale.ROOT))
                    }
                }
                val contentCount = resultMovies.size
                movieGridViewModel.setContentDetails(response.page.title, contentCount)
                callback.onResult(resultMovies, null, 2)

            } catch (exception: Exception) {
                movieGridViewModel.setContentDetails("", 0)
                Log.e("ContentDataSource", "Failed to fetch data!")
            }

        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Content>) {
        scope.launch {
            try {
                val response = getWorkoutFromFile(context, params.key)
                var resultMovies = response.page.contentItems.content
                if (query != "") {
                    resultMovies = resultMovies.filter { it1 ->
                        it1.name.toLowerCase(
                            Locale.ROOT
                        ).startsWith(query.toLowerCase(Locale.ROOT))
                    }
                }
                callback.onResult(resultMovies, params.key + 1)

            } catch (exception: Exception) {
                Log.e("ContentDataSource", "Failed to fetch data!")
            }
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Content>) {

    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }

    private fun getWorkoutFromFile(context: Context, page: Int): MovieResponse {
        val jsonFileString =
            "CONTENTLISTINGPAGE-PAGE${page}.json".getJsonDataFromAsset(context)
        val gson = Gson()
        val workoutData = object : TypeToken<MovieResponse>() {}.type
        return gson.fromJson(jsonFileString, workoutData)
    }

    private fun String.getJsonDataFromAsset(context: Context): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(this).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

}