package com.mazghul.diagnal.presentation

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.mazghul.diagnal.R
import com.mazghul.diagnal.data.model.Content
import com.mazghul.diagnal.data.model.MovieResponse
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var adapter: MovieAdapter? = null
    private lateinit var movieGridViewModel: MovieGridViewModel
    private var movieAdapter = MovieAdapter()

    private lateinit var movies: PagedList<MovieResponse>
    private lateinit var movieObserver: Observer<PagedList<Content>>

    //private lateinit var toolbar: androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movieGridViewModel = ViewModelProvider(this).get(MovieGridViewModel::class.java)
        //toolbar = findViewById(R.id.toolbar)

        // val movieLiveData = movieGridViewModel.getMovie(this.applicationContext)
//        movieLiveData.observe(this, Observer {
//            //toolbar.title =it.page.title
//            adapter = MovieAdapter(this, it.page.contentItems.content as ArrayList<Content>)
//
//            movie_grid_view.adapter = adapter
//            movie_grid_view.numColumns = 3
//        }
//        )
        movieGridViewModel.initApplication(application)
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        supportActionBar!!.setBackgroundDrawable(getDrawable(R.drawable.nav_bar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //supportActionBar!!.setHomeAsUpIndicator(R.d)

        //supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
       // supportActionBar!!.a
        //toolBar.setAlpha(0.5f);
        // movieAdapter = MovieAdapter()


        movie_grid_view.numColumns = 3
//        movieGridViewModel.movieResponseLiveData.observe(this, Observer {
//            //toolbar.title =it.page.title
//
//            //movie_grid_view.adapter = adapter
//        }
//        )
        observeLiveData()
        initializeList()
    }

    private fun observeLiveData() {
        //observe live data emitted by view model
        movieObserver = Observer<PagedList<Content>> {
            supportActionBar!!.title = "Romantic Comedy"
            movieAdapter.submitList(it)
        }
        movieGridViewModel.getMovies().observe(this, movieObserver)
//        movieGridViewModel.getMovies().observe(this, Observer {
//            movieAdapter.submitList(it)
//        })


//        mainActivityViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
//            @Override
//            public void onChanged(@Nullable List<Movie> moviesFromLiveData) {
//                movies = (ArrayList<Movie>) moviesFromLiveData;
//                showOnRecyclerView();
//            }
//        });
    }

    private fun initializeList() {

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        ) {
            movie_grid_view1.layoutManager = GridLayoutManager(this, 3)
        } else {
            movie_grid_view1.layoutManager = GridLayoutManager(this, 7)
        }
        //movie_grid_view1.layoutManager = LinearLayoutManager(this)
        movie_grid_view1.addItemDecoration(
            GridItemDecoration(
                30,
                3,
                true
            )
        )
        movie_grid_view1.adapter = movieAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.moviemenu, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
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
                    if (newText != null || newText === "") {
                        resetObserverAndFilter("")
                    } else if( newText?.length!! > 2) {
                        resetObserverAndFilter(newText)
                    }
                return true
            }
        })

        searchView.setOnCloseListener {
            resetObserverAndFilter("")
            return@setOnCloseListener false
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun resetObserverAndFilter(query: String) {
        movieGridViewModel.getMovies().removeObserver(movieObserver)
        movieGridViewModel.filterMovie1(query, this@MainActivity)
        movieGridViewModel.getMovies().observeForever(movieObserver)
    }
}