package com.silas.themovies.ui.main.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.silas.themovies.R
import com.silas.themovies.model.dto.response.PagedListMovies
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.ui.detail.DetailMovieActivity
import com.silas.themovies.ui.generic.GenericFragment
import com.silas.themovies.ui.main.MainActivity
import com.silas.themovies.utils.extensions.hideProgress
import com.silas.themovies.utils.extensions.myGetColor
import com.silas.themovies.utils.extensions.showProgress
import com.silas.themovies.utils.extensions.startActivity
import kotlinx.android.synthetic.main.fragment_movies.*
import org.koin.android.ext.android.inject


/**
 * A simple [Fragment] subclass.
 */
class MoviesFragment(internal val typeFragment: TypeFragment): GenericFragment() {

    private val moviesViewModel by inject<MoviesViewModel>()
    private var moviesAdapter: MovieAdapter? = null
    private var pagedListMovies: PagedListMovies? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            initVies()
            initListeners()
            loadMovies((it as MainActivity).currentQuery)
        }
    }

    internal fun loadMovies(query: String = "", page: Int = 1) {
        if (typeFragment == TypeFragment.POPULARS) getPopulars(query, page) else getFavorites(query)
    }

    private fun initVies(){
        swipe_refresh_layout_movies.setColorSchemeColors(requireContext().myGetColor (R.color.colorAccent))
        swipe_refresh_layout_movies.isEnabled = false
    }

    private fun initListeners(){
        swipe_refresh_layout_movies.setOnRefreshListener {
            loadMovies("", 1)
            swipe_refresh_layout_movies.isRefreshing = false
        }
        recycler_view_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                (recyclerView.layoutManager as? GridLayoutManager)?.apply {
                    swipe_refresh_layout_movies.isEnabled = findFirstVisibleItemPosition() <= 1
                    pagedListMovies?.let { itPagedList ->
                        if (itPagedList.page < itPagedList.totalPages
                            && childCount < itPagedList.totalResults
                            && (findLastVisibleItemPosition() - 1) >= (itemCount - 2)) {
                            loadMovies(page = itPagedList.page + 1)
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun getPopulars(query: String, page: Int){
        showProgress()
        moviesViewModel.getPopulars(page, query).observe(viewLifecycleOwner, Observer {
            hideProgress()
            this.pagedListMovies = it
            if (it.page > 1) {
                val lastPosition = moviesAdapter?.updateMovies(it) ?: 1
                recycler_view_movies.scrollToPosition(lastPosition)
            } else setUpRecyclerView(it.results)
        })
    }

    private fun getFavorites(query: String) {
        showProgress()
        moviesViewModel.getFavorites(query).observe(viewLifecycleOwner, Observer {
            hideProgress()
            setUpRecyclerView(it)
        })
    }

    private fun setUpRecyclerView(movies: List<Movie>) {
        recycler_view_movies.layoutManager = GridLayoutManager(activity, 2)
        moviesAdapter = MovieAdapter(movies as ArrayList<Movie>) { _, movie ->
            startActivity<DetailMovieActivity>(MainActivity.KEY_MOVIE to movie)
        }.apply {
            recycler_view_movies.adapter = this
        }
    }

}
