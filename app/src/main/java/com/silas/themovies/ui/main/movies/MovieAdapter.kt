package com.silas.themovies.ui.main.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.silas.themovies.R
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.model.type.BackDropType
import com.silas.themovies.model.type.PosterPathType
import com.silas.themovies.utils.extensions.myGetColor
import com.silas.themovies.utils.extensions.setUpImage
import kotlinx.android.synthetic.main.item_movie.view.*

/**
 * Fragments adapter with ViewPagers of movies
 *
 * @param listMovies List of films to adapt to RecyclerView
 * @param isRelated Flag to check if the items to be adapted are popular or related
 * @param onClickMovie High-Order-Function responsible for passing a click listener
 * of items in the current list
 *
 * @author Silas at 27/02/2020
 */
class MovieAdapter(private var listMovies: ArrayList<Movie>,
                   private var isRelated: Boolean = false,
                   private val onClickMovie: ((Int, Movie) -> Unit)? = null)
    : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(
                if (isRelated) R.layout.item_movie_related else R.layout.item_movie,
                parent,
                false).run {
                MovieViewHolder(this)
        }
    }

    override fun getItemCount() = listMovies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        listMovies[position].let { itMovie ->
            holder.itemView.apply {
                val circularProgressDrawable = CircularProgressDrawable(context).apply {
                    strokeWidth = 8f
                    centerRadius = 32f
                    setColorSchemeColors(context.myGetColor(R.color.colorAccent))
                    start()
                }
                image_view_movie.setUpImage(
                    if (isRelated) BackDropType.W_780.resolution + itMovie.endPointBackDrop
                    else PosterPathType.W_780.resolution + itMovie.endPointPosterPath,
                    circularProgressDrawable)
                text_view_detail_movie_title.text = itMovie.title
                text_view_detail_movie_popularity.text =
                    context.getString(
                        R.string.main_item_movie_text_popularity_description,
                        itMovie.popularity
                    )

                setOnClickListener {
                    onClickMovie?.invoke(position, itMovie)
                }
            }
        }
    }

    class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}