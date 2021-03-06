package com.silas.themovies.ui.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.silas.themovies.R
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.model.dto.response.PagedMovies
import com.silas.themovies.model.type.BackDropType
import com.silas.themovies.ui.LoadingState
import com.silas.themovies.ui.generic.GenericActivity
import com.silas.themovies.ui.main.MainActivity.Companion.KEY_MOVIE
import com.silas.themovies.ui.main.movies.MovieAdapter
import com.silas.themovies.utils.extensions.*
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.activity_detail_movie.text_view_detail_movie_popularity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * After selecting a movie to view details, this class goes into action to search for details,
 * related movies, as well as allowing you to make it favorite or not
 *
 * @property detailsViewModel of our business layer, responsible for searching the movie details
 * @property movieDetails Movie selected for detail viewing
 * @property pagedRelatedMovies Contains the page, the total pages, the total films and the updated films
 * @property relatedAdapter Used to adapt RecyclerView data and respond to your changes
 * @property currentPageRelated Current page viewed by the user, or that will be loaded soon
 *
 * @author Silas at 24/02/2020
 */

class DetailMovieActivity : GenericActivity(), View.OnClickListener {

    private val detailsViewModel by viewModel<DetailsViewModel>()
    private lateinit var movieDetails: Movie

    private lateinit var pagedRelatedMovies: PagedMovies
    private lateinit var relatedAdapter: MovieAdapter
    private var currentPageRelated = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)
        initViews()
        observeLoadDetails()
        observeIsFavorite()
        observeUpdateFavorite()
        observeRelatedMovies()
        observeLoading()
        observeMessage()
        loadDetails()
    }

    override fun onClick(view: View?) {
        view?.let {
            when (it) {
                image_view_detail_movie_favorite -> {
                    movieDetails.hasFavorite = !movieDetails.hasFavorite
                    updateFavorite()
                }
            }
        }
    }

    private fun isReceiveParams(): Boolean {
        return intent.getParcelableExtra<Movie>(KEY_MOVIE)?.let {
            movieDetails = it
            true
        } ?: run { false }
    }

    private fun initViews() {
        if (isReceiveParams()) {
            setUpCustomToolbar(toolbar_movie_detail, movieDetails.title, true)
            loadDetails()
        } else {
            onMessage(getString(R.string.detail_movie_error_data_init))
            onBackPressed()
        }
    }

    private fun initListeners(){
        image_view_detail_movie_favorite.setOnClickListener(this)
    }

    private fun loadViews() {
        image_view_detail_movie.setUpImage(BackDropType.W_500.resolution + movieDetails.endPointBackDrop)

        text_view_detail_movie_popularity.text =
            getString(R.string.detail_movie_text_popularity_description,  movieDetails.popularity)
        text_view_detail_movie_release_date.text =
            getString(R.string.detail_movie_text_release_date, movieDetails.releaseDate.formatDate())
        movieDetails.genres?.let {
            text_view_detail_movie_genre.text =
                getString(R.string.detail_movie_text_genre, it.convertInTextList())
        }
        text_view_detail_movie_vote_average.text =
            getString(R.string.detail_movie_text_vote_average, movieDetails.voteAverage)
        text_view_detail_movie_synopsis_description.text = movieDetails.overview
    }

    private fun observeLoading(){
        detailsViewModel.loadingLiveData.observe(this, Observer { state ->
            when (state.name) {
                LoadingState.SHOW.name -> showProgress()
                LoadingState.HIDE.name -> hideProgress()
            }
        })
    }

    private fun observeLoadDetails() {
        detailsViewModel.movieDetailsLiveData.observe(this, Observer { details ->
            details?.let {
                movieDetails = it
                loadViews()
                checkIsFavorite(it.id)
                loadRelated()
                initListeners()
            }
        })
    }

    private fun observeIsFavorite() {
        detailsViewModel.movieDetailsLiveData.observe(this, Observer {
            movieDetails.hasFavorite = it?.hasFavorite ?: false
            updateViewFavorite()
        })
    }

    private fun observeRelatedMovies() {
        detailsViewModel.pagedRelatedLiveData.observe(this, Observer {
            if (currentPageRelated > 1) {
                this.pagedRelatedMovies.updatePage(it)
                this.relatedAdapter.notifyDataSetChanged()
            } else {
                this.pagedRelatedMovies = it
                setUpRecyclerView()
            }
        })
    }

    private fun observeUpdateFavorite() {
        detailsViewModel.updateFavoritesLiveData.observe(this, Observer {
            updateViewFavorite()
            onMessage(getString(R.string.detail_movie_favorite_message))
        })
    }

    private fun observeMessage(){
        detailsViewModel.errorLiveData.observe(this, Observer { message ->
            onMessage(message)
        })
    }

    private fun loadDetails() = detailsViewModel.loadDetails(movieDetails.id)

    private fun checkIsFavorite(movieId: Long) = detailsViewModel.loadFavoriteId(movieId)

    private fun loadRelated() = detailsViewModel.loadRelated(currentPageRelated, movieDetails.id)

    private fun updateFavorite() = detailsViewModel.updateFavorite(movieDetails)

    private fun setUpRecyclerView() {
        recycler_view_detail_movie.layoutManager = GridLayoutManager(
            this,
            1, RecyclerView.HORIZONTAL,
            false)

        this.relatedAdapter = MovieAdapter(pagedRelatedMovies.results, true) { _, movieSelected ->
            startActivity<DetailMovieActivity>(KEY_MOVIE to movieSelected)
            finish()
        }.apply {
            recycler_view_detail_movie.adapter = this
        }

        recycler_view_detail_movie.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                (recyclerView.layoutManager as LinearLayoutManager).apply {
                    if (isPaginationNecessary(newState, pagedRelatedMovies)) {
                        currentPageRelated++
                        loadRelated()
                    }
                }
            }
        })
    }

    private fun updateViewFavorite(){
        image_view_detail_movie_favorite
            .setImageDrawable(
                myGetDrawable(
                    if (movieDetails.hasFavorite) R.drawable.ic_favorite
                    else R.drawable.ic_not_favorite))
    }
}
