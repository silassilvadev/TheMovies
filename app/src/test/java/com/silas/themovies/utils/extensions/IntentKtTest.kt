package com.silas.themovies.utils.extensions

import android.content.Intent
import android.os.Parcelable
import com.silas.themovies.model.dto.response.Movie
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IntentKtTest {

    private lateinit var pagedListMovies: Parcelable
    private lateinit var movie: Movie
    private lateinit var arrayListMovie: ArrayList<Movie>
    private lateinit var intent: Intent

    companion object {
        private const val KEY_STRING = "String"
        private const val KEY_INT = "Int"
        private const val KEY_LONG = "Long"
        private const val KEY_DOUBLE = "Double"
        private const val KEY_FLOAT = "Float"
        private const val KEY_SERIALIZABLE = "Serializable"
        private const val KEY_PARCELABLE = "Parcelable"
        private const val KEY_PARCELABLE_ARRAY = "ParcelableArray"
    }

    @Before
    fun setUp() {
        pagedListMovies = mockk()
        movie = mockk()
        arrayListMovie = mockk()
        this.intent = mockk(relaxed = true)
    }

    @Test
    fun `Add parameters in Intent success`() {
        intent.setupParams(KEY_STRING to "test",
            KEY_INT to 1,
            KEY_LONG to 1L,
            KEY_DOUBLE to 1.0,
            KEY_FLOAT to 1f,
            KEY_SERIALIZABLE to pagedListMovies,
            KEY_PARCELABLE to movie,
            KEY_PARCELABLE_ARRAY to arrayListMovie)

        val isSuccess = intent.getStringExtra(KEY_STRING) != null
                && intent.getIntExtra(KEY_INT, -1) > -1
                && intent.getLongExtra(KEY_LONG, -1L) > -1L
                && intent.getDoubleExtra(KEY_DOUBLE, -1.0) > -1.0
                && intent.getFloatExtra(KEY_FLOAT, -1f) > -1f
                && intent.getSerializableExtra(KEY_SERIALIZABLE) != null
                && intent.getParcelableExtra<Movie>(KEY_PARCELABLE) != null
                && !intent.getParcelableArrayListExtra<Movie>(KEY_PARCELABLE_ARRAY).isNullOrEmpty()

        assertTrue("Data successfully added to Intent", isSuccess)
    }
}