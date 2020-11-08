package com.mazghul.diagnal.presentation

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mazghul.diagnal.data.model.Content
import com.mazghul.diagnal.datasouce.MovieDataSource


class MovieGridViewModel : ViewModel() {

    private lateinit var application: Application
    private var contentLiveData: LiveData<PagedList<Content>>
    private val config: PagedList.Config
    private var title: String = ""
    private var contentCount: Int = 0

    fun initApplication(application: Application) {
        this.application = application
    }

    init {
        config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(true)
            .build()
        contentLiveData = initializedPagedListBuilder(config).build()
    }

    fun getMovies(): LiveData<PagedList<Content>> = contentLiveData

    fun getTitle(): String = title

    fun getContentCount(): Int = contentCount

    fun setContentDetails(title: String, contentCount: Int) {
        this.title = title
        this.contentCount = contentCount
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Content> {
        return createDataSource("")
    }

    private fun initializedSearchPagedListBuilder(config: PagedList.Config, query: String):
            LivePagedListBuilder<Int, Content> {
        return createDataSource(query)
    }

    private fun createDataSource(query: String): LivePagedListBuilder<Int, Content> {
        val dataSourceFactory = object : DataSource.Factory<Int, Content>() {
            override fun create(): DataSource<Int, Content> {
                return MovieDataSource(
                    viewModelScope,
                    application.applicationContext,
                    query,
                    this@MovieGridViewModel
                )
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }

    fun filterMovie(query: String) {
        contentLiveData = initializedSearchPagedListBuilder(config, query).build()
    }

}