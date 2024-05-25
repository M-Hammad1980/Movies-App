package com.app.movies.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val adult: Boolean?,
    val backdropPath: String?,
    val genreIds: String,  // Store as a JSON string or comma-separated values
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    val voteAverage: Double?,
    val voteCount: Int?
) {
    fun toResult(): ApiResponse.Results {
        val genreIdList = genreIds.split(",").mapNotNull { it.toIntOrNull() } as ArrayList<Int>
        return ApiResponse.Results(
            adult, backdropPath, genreIdList, id, originalLanguage, originalTitle, overview, popularity,
            posterPath, releaseDate, title, video, voteAverage, voteCount
        )
    }
}