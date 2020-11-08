package com.mazghul.diagnal.data.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineScope


class MovieDataSourceFactory(
    private val viewModelScope: CoroutineScope,
    private val context: Context)
    : DataSource.Factory<Int, Content>() {

    val MovieDataSourceLiveData = MutableLiveData<Content>()

    override fun create(): DataSource<Int, Content> {
        // newsDataSourceLiveData.postValue(newsDataSource)
        return MovieDataSource(viewModelScope, context)
    }
}