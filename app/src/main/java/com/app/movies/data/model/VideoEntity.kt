package com.app.movies.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "video_table")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true) val serial: Int,
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val genreIds: ArrayList<Int> = arrayListOf(),
    val id: Int? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null
)