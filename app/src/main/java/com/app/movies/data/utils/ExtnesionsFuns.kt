package com.app.movies.data.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.model.MovieEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
fun afterDelay(delayInTime: Long, listener: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        listener.invoke()
    }, delayInTime)
}
fun View.beGone() {
    visibility = View.GONE
}

fun View.beVisible() {
    visibility = View.VISIBLE
}

fun View.beInVisible() {
    visibility = View.INVISIBLE
}

fun Context.showToast(msg : String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

@RequiresApi(Build.VERSION_CODES.O)
fun addConsecutiveDates(originalDate: String, daysToAdd: Int): List<String> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val currentDate = LocalDate.now()
    var startDate = LocalDate.parse(originalDate, formatter)

    if (startDate.isBefore(currentDate)) {
        startDate = currentDate
    }

    val datesList = mutableListOf<String>()

    for (i in 0 until daysToAdd) {
        val modifiedDate = startDate.plusDays(i.toLong())
        val formattedDate = modifiedDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                " " + modifiedDate.dayOfMonth
        datesList.add(formattedDate)
    }

    return datesList
}

fun getGenresById(ids: ArrayList<Int>): List<String> {
    val genresMap = mapOf(
        28 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Science Fiction",
        10770 to "TV Movie",
        53 to "Thriller",
        10752 to "War",
        37 to "Western",
        10759 to "Action & Adventure",
        10762 to "Kids",
        10763 to "News",
        10764 to "Reality",
        10765 to "Sci-Fi & Fantasy",
        10766 to "Soap",
        10767 to "Talk",
        10768 to "War & Politics"
    )

    val genresList = mutableListOf<String>()
    ids.forEach { id ->
        genresMap[id]?.let { genre ->
            genresList.add(genre)
        }
    }
    return genresList
}

fun ApiResponse.Results.toEntity(): MovieEntity {
    val genreIdString = genreIds.joinToString(",")
    return MovieEntity(
        id = id!!,
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIdString,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}