package com.silas.themovies.utils.extensions

import com.silas.themovies.model.entity.Genre
import org.junit.Assert.assertTrue
import org.junit.Test

class ListKtTest {

    @Test
    fun `Convert list of genre in list of string success`() {
        val genres = arrayListOf(
            Genre(1, "Genre 1"),
            Genre(2, "Genre 2"),
            Genre(3, "Genre 3"),
            Genre(4, "Genre 4"),
            Genre(5, "Genre 5")
        )

        val result = genres.convertInTextList()

        assertTrue("Converted error", result.isNotBlank())
    }
}