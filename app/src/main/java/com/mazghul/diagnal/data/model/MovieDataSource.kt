package com.mazghul.diagnal.data.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.paging.PageKeyedDataSource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class MovieDataSource(private val scope: CoroutineScope, private val context: Context, private val query:String = "") :
    PageKeyedDataSource<Int, Content>() {

    //private val apiService = ApiClient.getClient().create(ApiService::class.java)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Content>
    ) {
        scope.launch {
            try {
                val response = getWorkoutFromFile(context,1)
                //Toast.makeText(context, response.page.pageNum, Toast.LENGTH_SHORT).show()
                var resultMovies = response.page.contentItems.content
                if(query != "") {
                    resultMovies = resultMovies.filter { it1 ->
                        it1.name.toLowerCase(
                            Locale.ROOT
                        ).startsWith(query.toLowerCase(Locale.ROOT))
                    }
                }
                callback.onResult(resultMovies, null, 2)

            } catch (exception: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data!")
            }

        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Content>) {
        scope.launch {
            try {
                val response = getWorkoutFromFile(context, params.key)
                //apiService.fetchPosts(loadSize = params.requestedLoadSize, after = params.key)
                var resultMovies = response.page.contentItems.content
                if(query != "") {
                    resultMovies = resultMovies.filter { it1 ->
                        it1.name.toLowerCase(
                            Locale.ROOT
                        ).startsWith(query.toLowerCase(Locale.ROOT))
                    }
                }
                callback.onResult(resultMovies, params.key + 1)

            } catch (exception: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data!")
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
        Log.d("LoadingJson", "CONTENTLISTINGPAGE-PAGE${page}.json")
        var pageNew: Int = page
            //if(page == 1) 1 else 2
        val jsonFileString =
            "CONTENTLISTINGPAGE-PAGE${pageNew}.json".getJsonDataFromAsset(context)

        Toast.makeText(context, "CONTENTLISTINGPAGE-PAGE${pageNew}.json", Toast.LENGTH_SHORT).show()

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