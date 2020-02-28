package com.silas.themovies.ui.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.silas.themovies.R
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.dto.response.MovieDetails
import com.silas.themovies.model.dto.response.PagedListMovies
import com.silas.themovies.model.entity.type.BackDropType
import com.silas.themovies.ui.generic.GenericActivity
import com.silas.themovies.ui.main.MainActivity.Companion.KEY_MOVIE
import com.silas.themovies.ui.main.movies.MovieAdapter
import com.silas.themovies.utils.extensions.*
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.activity_detail_movie.text_view_detail_movie_popularity
import org.koin.android.ext.android.inject

class DetailMovieActivity : GenericActivity(), View.OnClickListener {

    private val detailsViewModel by inject<DetailsViewModel>()
    private lateinit var movie: Movie
    private var movieDetail: MovieDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)
        initViews()
    }

    override fun onClick(view: View?) {
        view?.let {
            when (it) {
                image_view_detail_movie_favorite -> {
                    updateFavorite(movieDetail?.let { itMovieDetails ->
                        !itMovieDetails.hasFavorite
                    } ?: run { false })
                }
            }
        }
    }

    private fun isReceiveParams(): Boolean {
        return intent.getSerializableExtra(KEY_MOVIE)?.let {
            movie = it as Movie
            true
        } ?: run { false }
    }

    private fun initViews() {
        if (isReceiveParams()) {
            setUpCustomToolbar(toolbar_movie_detail, movie.title, true)
            initListeners()
            loadDetails()
        } else {
            onResponseError(getString(R.string.detail_movie_error_data_init))
            onBackPressed()
        }
    }

    private fun setUpToolbar(){
        setSupportActionBar(toolbar_movie_detail)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = movie.title
        }
    }

    private fun initListeners(){
        image_view_detail_movie_favorite.setOnClickListener(this)
    }

    private fun loadDetails(){
        showProgress()
        detailsViewModel.loadDetails(movie.id).observe(this, Observer {
            hideProgress()
            movieDetail = it
            loadViews()
            movieIsFavorite(it.id)
            loadRelated()
            initListeners()
        })
    }

    private fun loadViews() {
        movieDetail?.apply {
            image_view_detail_movie.setUpImage(BackDropType.W_500.resolution + endPointBackDrop)
            val listGenre = ArrayList<String>().apply {
                movieDetail?.genre?.forEach { add(it.name) }
            }
            text_view_detail_movie_popularity.text =
                getString(R.string.detail_movie_text_popularity_description,  movie.popularity)
            text_view_detail_movie_date_header.text =
                getString(R.string.detail_movie_text_release_date, movie.date.simpleMaskDate())
            text_view_detail_movie_genre.text =
                getString(R.string.detail_movie_text_genre, listGenre.convertInTextList())
            text_view_detail_movie_synopsis_description.text = movieDetail?.overview
        }
    }

    private fun movieIsFavorite(movieId: Long){
        showProgress()
        detailsViewModel.loadFavoriteId(movieId).observe(this, Observer {
            hideProgress()
            movieDetail?.hasFavorite = it?.hasFavorite ?: false
            updateViewFavorite()
        })
    }

    private fun loadRelated() {
        showProgress()
        detailsViewModel.searchRelated(1, movie.id).observe(this, Observer {
            hideProgress()
            setUpRecyclerView(it)
        })
    }

    private fun updateFavorite(isFavorite: Boolean) {
        movie.hasFavorite = isFavorite
        showProgress()
        if (isFavorite) {
            detailsViewModel.saveFavorite(movie).observe(this, Observer {
                hideProgress()
                movieDetail?.hasFavorite = isFavorite
                updateViewFavorite()
                Snackbar.make(coordinator_layout_detail_movie,
                    getString(R.string.detail_movie_favorite_message),
                    Snackbar.LENGTH_SHORT).show()
            })
        } else {
            detailsViewModel.removeFavorite(movie).observe(this, Observer {
                hideProgress()
                movieDetail?.hasFavorite = isFavorite
                updateViewFavorite()
                Snackbar.make(coordinator_layout_detail_movie,
                    getString(R.string.detail_movie_not_favorite_message),
                    Snackbar.LENGTH_SHORT).show()
            })
        }
    }

    private fun setUpRecyclerView(pagedListMovies: PagedListMovies) {
        recycler_view_detail_movie.apply {
            layoutManager = GridLayoutManager(
                this@DetailMovieActivity,
                1, RecyclerView.HORIZONTAL,
                false)
            adapter = MovieAdapter(
                pagedListMovies.results as ArrayList<Movie>
                , true) { _, movieSelected ->
                startActivity<DetailMovieActivity>(KEY_MOVIE to movieSelected)
                finish()
            }
        }
    }

    private fun updateViewFavorite() {
        image_view_detail_movie_favorite.setImageDrawable(myGetDrawable(
            if (movieDetail?.hasFavorite == true) R.drawable.ic_favorite
            else R.drawable.ic_not_favorite))
    }
}
