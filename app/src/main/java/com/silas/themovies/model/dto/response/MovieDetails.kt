package com.silas.themovies.model.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.silas.themovies.model.entity.Genre
import kotlinx.android.parcel.Parcelize

@Parcelize
class MovieDetails(val id: Long,
                   val title: String,
                   val genre: List<Genre>,
                   val overview: String,
                   @SerializedName("backdrop_path") val endPointBackDrop: String,
                   var hasFavorite: Boolean = false): Parcelable

/*
{
    "imdb_id": "tt2935510",
    "original_language": "en",
    "original_title": "Ad Astra",
    "overview": "Roy McBride é um engenheiro espacial, portador de um leve grau de autismo, que decide empreender a maior jornada de sua vida: viajar para o espaço, cruzar a galáxia e tentar descobrir o que aconteceu com seu pai, um astronauta que se perdeu há vinte anos atrás no caminho para Netuno.",
    "popularity": 355.269,
    "poster_path": "/dJ3VPQTg2gST26IKIk75TNHViB0.jpg",
    "production_companies": [
        {
            "id": 490,
            "logo_path": null,
            "name": "New Regency Productions",
            "origin_country": ""
        },
        {
            "id": 79963,
            "logo_path": null,
            "name": "Keep Your Head",
            "origin_country": ""
        },
        {
            "id": 73492,
            "logo_path": null,
            "name": "MadRiver Pictures",
            "origin_country": ""
        },
        {
            "id": 81,
            "logo_path": "/8wOfUhA7vwU2gbPjQy7Vv3EiF0o.png",
            "name": "Plan B Entertainment",
            "origin_country": "US"
        },
        {
            "id": 30666,
            "logo_path": "/g8LmDZVFWDRJW72sj0nAj1gh8ac.png",
            "name": "RT Features",
            "origin_country": "BR"
        },
        {
            "id": 30148,
            "logo_path": null,
            "name": "Bona Film Group",
            "origin_country": "CN"
        },
        {
            "id": 22213,
            "logo_path": "/qx9K6bFWJupwde0xQDwOvXkOaL8.png",
            "name": "TSG Entertainment",
            "origin_country": "US"
        }
    ],
    "production_countries": [
        {
            "iso_3166_1": "BR",
            "name": "Brazil"
        },
        {
            "iso_3166_1": "CN",
            "name": "China"
        },
        {
            "iso_3166_1": "US",
            "name": "United States of America"
        }
    ],
    "release_date": "2019-09-17",
    "revenue": 127175922,
    "runtime": 123,
    "spoken_languages": [
        {
            "iso_639_1": "en",
            "name": "English"
        },
        {
            "iso_639_1": "no",
            "name": "Norsk"
        }
    ],
    "status": "Released",
    "tagline": "As respostas que procuramos estão fora do nosso alcance.",
    "title": "Ad Astra - Rumo às Estrelas",
    "video": false,
    "vote_average": 6.0,
    "vote_count": 2410
}
*/
