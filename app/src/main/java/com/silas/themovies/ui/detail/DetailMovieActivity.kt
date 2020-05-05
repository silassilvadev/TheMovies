package com.silas.themovies.ui.detail

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.silas.themovies.R
import com.silas.themovies.di.ModulesFactory.DETAILS_MODULE
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.entity.PagedMovies
import com.silas.themovies.model.type.BackDrop
import com.silas.themovies.model.state.LoadingState
import com.silas.themovies.ui.generic.GenericActivity
import com.silas.themovies.ui.main.MainActivity.Companion.KEY_MOVIE_SELECTED
import com.silas.themovies.ui.main.movies.MoviesAdapter
import com.silas.themovies.utils.custom.PaginationListener
import com.silas.themovies.utils.extensions.*
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.activity_detail_movie.text_view_detail_movie_popularity
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

/**
 * After selecting a movie to view details, this class goes into action to search for details,
 * related movies, as well as allowing you to make it favorite or not
 *
 * @property detailsPresenter of our business layer, responsible for searching the movie details
 * @property movie Movie selected for detail viewing
 * @property relatedAdapter Used to adapt RecyclerView data and respond to your changes
 *
 * @author Silas at 24/02/2020
 */

class DetailMovieActivity : GenericActivity(), View.OnClickListener, DetailsContract.View {

    private lateinit var relatedAdapter: MoviesAdapter

    private val detailsScope = getKoin().getOrCreateScope("details", named(DETAILS_MODULE))
    private val detailsPresenter by detailsScope.inject<DetailsPresenter> {
        parametersOf(this)
    }
    private var paginationListener: PaginationListener? = null
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        if (isReceiveParams()) detailsPresenter.loadDetails(this.movie.id)
        else onMessage(getString(R.string.detail_movie_error_data_init))

        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        detailsScope.close()
    }

    override fun onClick(view: View?) {
        view?.let {
            when (it) {
                image_view_detail_movie_favorite -> detailsPresenter.updateFavorite(this.movie.apply {
                    hasFavorite = !hasFavorite
                })
            }
        }
    }

    override fun updateMovieDetails(updatedMovie: Movie) {
        updateViews(updatedMovie)
        loadDataRelated()
    }

    override fun isFavorite(isFavorite: Boolean) {
        updateViewFavorite(isFavorite)
    }

    override fun updateRelated(pagedRelated: PagedMovies) {
        this.relatedAdapter.updateMovies(pagedRelated.results)
        setUpPaginationRelated(pagedRelated.totalResults)
    }

    override fun updateFavorite(isFavorite: Boolean) {
        updateViewFavorite(isFavorite)
        onMessage(
            if (isFavorite) getString(R.string.detail_movie_favorite_message)
            else getString(R.string.detail_movie_not_favorite_message)
        )
    }

    override fun responseError(message: String) {
        onMessage(message)
    }

    override fun updateLoading(state: LoadingState) {
        when (state) {
            LoadingState.Show -> showProgress()
            LoadingState.Hide -> hideProgress()
        }
    }

    private fun isReceiveParams(): Boolean {
        return intent.getParcelableExtra<Movie>(KEY_MOVIE_SELECTED)?.let {
            this.movie = it
            true
        } ?: false
    }

    private fun initViews(){
        setUpRecyclerView()
        setUpCustomToolbar(toolbar_movie_detail, this.movie.title, true)
    }

    private fun setUpRecyclerView() {
        this.relatedAdapter =
            MoviesAdapter(isRelated = true) { movieSelected ->
                startActivity<DetailMovieActivity>(KEY_MOVIE_SELECTED to movieSelected)
                finish()
            }

        recycler_view_detail_movie.layoutManager = GridLayoutManager(
            this,
            1, RecyclerView.HORIZONTAL,
            false)
        recycler_view_detail_movie.adapter = this.relatedAdapter
    }

    private fun setUpPaginationRelated(totalResults: Int){
        if (this.paginationListener == null) {
            this.paginationListener = PaginationListener(totalResults) {
                this.detailsPresenter.loadRelated(this.movie.id, true)
                paginationListener?.isLoadingEnable = true
            }.apply {
                recycler_view_detail_movie.addOnScrollListener(this)
            }
        } else paginationListener?.isLoadingEnable = false
    }

    private fun updateViews(updatedMovie: Movie) {
        image_view_detail_movie.setUpImage(BackDrop.W500(updatedMovie.backdropPath).endPoint)
        image_view_detail_movie_favorite.setOnClickListener(this)
        text_view_detail_movie_popularity.text =
            getString(R.string.detail_movie_text_popularity_description,  updatedMovie.popularity)
        text_view_detail_movie_release_date.text =
            getString(R.string.detail_movie_text_release_date, updatedMovie.releaseDate.formatDate())
        updatedMovie.genres?.let {
            text_view_detail_movie_genre.text =
                getString(R.string.detail_movie_text_genre, it.convertInTextList())
        }
        text_view_detail_movie_vote_average.text =
            getString(R.string.detail_movie_text_vote_average, updatedMovie.voteAverage)
        text_view_detail_movie_synopsis_description.text = updatedMovie.overview
    }

    private fun loadDataRelated() {
        detailsPresenter.checkIsFavorite(this.movie.id)
        detailsPresenter.loadRelated(this.movie.id)
        paginationListener?.isLoadingEnable = true
    }

    private fun updateViewFavorite(isFavorite: Boolean){
        image_view_detail_movie_favorite.setImageDrawable(
            myGetDrawable(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_not_favorite)
        )
    }
}
