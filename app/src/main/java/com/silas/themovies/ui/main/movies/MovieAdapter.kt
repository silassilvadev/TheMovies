package com.silas.themovies.ui.main.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silas.themovies.R
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.dto.response.PagedListMovies
import com.silas.themovies.model.entity.type.BackDropType
import com.silas.themovies.model.entity.type.PosterPathType
import com.silas.themovies.utils.extensions.myGetDrawable
import com.silas.themovies.utils.extensions.setUpImage
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter(private var listMovies: ArrayList<Movie>,
                   private var isRelated: Boolean = false,
                   private val onClickMovie: ((View, Movie) -> Unit)? = null)
    : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(
                if (isRelated) R.layout.item_movie_related else R.layout.item_movie,
                parent,
                false).run {
                MovieViewHolder(
                    this
                )
        }
    }

    override fun getItemCount() = listMovies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        listMovies[position].let { itMovie ->
            holder.itemView.apply {
                image_view_movie.setUpImage(
                            if (isRelated) BackDropType.W_780.resolution + itMovie.endPointBackDrop
                            else PosterPathType.W_780.resolution + itMovie.endPointPosterPath,
                    context.myGetDrawable(R.drawable.ic_movie_error))
                text_view_detail_movie_title.text = itMovie.title
                text_view_detail_movie_popularity.text =
                    context.getString(
                        R.string.main_item_movie_text_popularity_description,
                        itMovie.popularity
                    )

                setOnClickListener { itView ->
                    onClickMovie?.invoke(itView, itMovie)
                }
            }
        }
    }

    internal fun updateMovies(pagedListMovies: PagedListMovies): Int {
        val lastPosition = itemCount - 1
        (pagedListMovies.results as ArrayList<Movie>).apply {
            if (pagedListMovies.page > 1) listMovies.addAll(this) else listMovies = this
        }
        notifyDataSetChanged()
        return lastPosition
    }

    class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}