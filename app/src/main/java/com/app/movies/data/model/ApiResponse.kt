package com.app.movies.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ApiResponse (
    @SerializedName("dates") var dates: Dates? = Dates(),
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<Results> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null
) {

    data class Dates (
        @SerializedName("maximum") var maximum: String? = null,
        @SerializedName("minimum") var minimum: String? = null
    )


    data class Results (
        @SerializedName("adult") var adult: Boolean? = null,
        @SerializedName("backdrop_path") var backdropPath: String? = null,
        @SerializedName("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
        @SerializedName("id") var id: Int? = null,
        @SerializedName("original_language") var originalLanguage: String? = null,
        @SerializedName("original_title") var originalTitle: String? = null,
        @SerializedName("overview") var overview: String? = null,
        @SerializedName("popularity") var popularity: Double? = null,
        @SerializedName("poster_path") var posterPath: String? = null,
        @SerializedName("release_date") var releaseDate: String? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("video") var video: Boolean? = null,
        @SerializedName("vote_average") var voteAverage: Double? = null,
        @SerializedName("vote_count") var voteCount: Int? = null
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readArrayList(Int::class.java.classLoader) as ArrayList<Int>,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Int::class.java.classLoader) as? Int
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(adult)
            parcel.writeString(backdropPath)
            parcel.writeList(genreIds)
            parcel.writeValue(id)
            parcel.writeString(originalLanguage)
            parcel.writeString(originalTitle)
            parcel.writeString(overview)
            parcel.writeValue(popularity)
            parcel.writeString(posterPath)
            parcel.writeString(releaseDate)
            parcel.writeString(title)
            parcel.writeValue(video)
            parcel.writeValue(voteAverage)
            parcel.writeValue(voteCount)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Results> {
            override fun createFromParcel(parcel: Parcel): Results {
                return Results(parcel)
            }

            override fun newArray(size: Int): Array<Results?> {
                return arrayOfNulls(size)
            }
        }
    }
}
