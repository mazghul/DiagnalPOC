package com.mazghul.diagnal.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.mazghul.diagnal.R
import com.mazghul.diagnal.data.model.Content
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var movieGridViewModel: MovieGridViewModel
    private var movieAdapter = MovieAdapter()

    private lateinit var movieObserver: Observer<PagedList<Content>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCenter.start(
            application, "9ad16882-3011-4af7-8092-d7d224f0484f",
            Analytics::class.java, Crashes::class.java
        )
        movieGridViewModel = ViewModelProvider(this).get(MovieGridViewModel::class.java)
        movieGridViewModel.initApplication(application)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        observeLiveData()
        initializeList()
    }

    private fun observeLiveData() {
        movieObserver = Observer<PagedList<Content>> {
            supportActionBar!!.title = movieGridViewModel.getTitle()

            if(movieGridViewModel.getContentCount() < 1) {
                no_results.text = getString(R.string.no_data)
            } else {
                no_results.text = ""
            }
            movieAdapter.submitList(it)
        }
        movieGridViewModel.getMovies().observe(this, movieObserver)
    }

    private fun initializeList() {
        if (this.resources.configuration.orientation ==
            Configuration.ORIENTATION_PORTRAIT
        ) {
            movie_grid_view.layoutManager = GridLayoutManager(this, 3)
        } else {
            movie_grid_view.layoutManager = GridLayoutManager(this, 7)
        }
        movie_grid_view.addItemDecoration(
            GridItemDecoration(
                30,
                3,
                true
            )
        )
        movie_grid_view.adapter = movieAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.moviemenu, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setPadding(-16, 0, 0, 0)
        searchView.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.length!! < 3) {
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter minimum 3 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    resetObserverAndFilter(query)
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText === "") {
                    resetObserverAndFilter("")
                } else if (newText?.length!! > 2) {
                    resetObserverAndFilter(newText)
                }
                return true
            }
        })

        searchView.setOnCloseListener {
            //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            resetObserverAndFilter("")
            return@setOnCloseListener false
        }

//        searchView.setOnSearchClickListener {
//            supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun resetObserverAndFilter(query: String) {
        movieGridViewModel.getMovies().removeObserver(movieObserver)
        movieGridViewModel.filterMovie(query)
        movieGridViewModel.getMovies().observeForever(movieObserver)
    }
}