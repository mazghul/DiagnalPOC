package com.mazghul.diagnal.presentation

import android.app.Application
import android.content.Context
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mazghul.diagnal.data.model.Content
import com.mazghul.diagnal.data.model.MovieDataSource
import com.mazghul.diagnal.data.model.MovieResponse
import java.io.IOException
import java.util.*


class MovieGridViewModel : ViewModel() {

    private lateinit var totalMovieResponse: MovieResponse
    private var movieResponse = MutableLiveData<MovieResponse>()
    val movieResponseLiveData: LiveData<MovieResponse> get() = movieResponse
    private lateinit var application: Application


    fun initApplication(application: Application) {
        this.application = application
    }

    fun getMovie(context: Context) = liveData {
        val movieResponse = getWorkoutFromFile(context, 1)
        emit(movieResponse)
    }

    fun getMovie1(context: Context) {
        totalMovieResponse = getWorkoutFromFile(context, 1)
        movieResponse.value = totalMovieResponse.copy()
    }

    fun filterMovie(query: String, context: Context) {
        val movieResponseList: MovieResponse? = getWorkoutFromFile(context, 1)

        movieResponseList?.let {
            val filteredMovies = it.page.contentItems.content.filter { it1 ->
                it1.name.toLowerCase(
                    Locale.ROOT
                ).startsWith(query.toLowerCase(Locale.ROOT))
            }
            movieResponseList.page.contentItems.content = filteredMovies
            movieResponse.value = movieResponseList
        }
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


    var contentLiveData: LiveData<PagedList<Content>>
    var filterText: MutableLiveData<String> = MutableLiveData()

    val config: PagedList.Config



    init {
        config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(true)
            .build()
        contentLiveData = initializedPagedListBuilder(config).build()
    }

    fun getMovies(): LiveData<PagedList<Content>> = contentLiveData

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Content> {

        val dataSourceFactory = object : DataSource.Factory<Int, Content>() {
            override fun create(): DataSource<Int, Content> {
                return MovieDataSource(viewModelScope, application.applicationContext)
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }

    private fun initializedSearchPagedListBuilder(config: PagedList.Config, query: String):
            LivePagedListBuilder<Int, Content> {

        val dataSourceFactory = object : DataSource.Factory<Int, Content>() {
            override fun create(): DataSource<Int, Content> {
                return MovieDataSource(viewModelScope, application.applicationContext, query)
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }

        fun filterMovie1(query: String, context: Context) {
            //contentLiveData.removeObserver(context)
            contentLiveData = initializedSearchPagedListBuilder(config, query).build()

        }
//
//    fun filterMovie1(query: String, context: Context) {
//        val movieResponseList: MovieResponse? = getWorkoutFromFile(context, 1)
//
//        contentLiveData.let {
//            val filteredMovies = it.value!!.filter { it1 ->
//                it1.name.toLowerCase(
//                    Locale.ROOT
//                ).startsWith(query.toLowerCase(Locale.ROOT))
//            }
//            contentLiveData = filteredMovies
//
//        }
//
//    }

}