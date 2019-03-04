package com.raphael.carvalho.android.popularmovies.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.raphael.carvalho.android.popularmovies.util.DateUtil;

import java.text.ParseException;
import java.util.List;

import static com.raphael.carvalho.android.popularmovies.util.DateUtil.DAY;
import static com.raphael.carvalho.android.popularmovies.util.DateUtil.MOUNTH;
import static com.raphael.carvalho.android.popularmovies.util.DateUtil.YEAR;

public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private static final String INPUT_FORMAT = YEAR + "-" + MOUNTH + "-" + DAY;
    private static final String OUTPUT_FORMAT = YEAR;

    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("id")
    private Integer id;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Float voteAverage;
    @SerializedName("title")
    private String title;
    @SerializedName("popularity")
    private Float popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("genre_ids")
    private List<Integer> genreIds = null;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("adult")
    private Boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;

    private Movie(Parcel in) {
        if (in.readByte() == 0) {
            voteCount = null;
        } else {
            voteCount = in.readInt();
        }
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        byte tmpVideo = in.readByte();
        video = tmpVideo == 0 ? null : tmpVideo == 1;
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readFloat();
        }
        title = in.readString();
        if (in.readByte() == 0) {
            popularity = null;
        } else {
            popularity = in.readFloat();
        }
        posterPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        backdropPath = in.readString();
        byte tmpAdult = in.readByte();
        adult = tmpAdult == 0 ? null : tmpAdult == 1;
        overview = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (voteCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(voteCount);
        }
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeByte((byte) (video == null ? 0 : video ? 1 : 2));
        if (voteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(voteAverage);
        }
        dest.writeString(title);
        if (popularity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(popularity);
        }
        dest.writeString(posterPath);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(backdropPath);
        dest.writeByte((byte) (adult == null ? 0 : adult ? 1 : 2));
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Integer getId() {
        return id;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        try {
            return DateUtil.format(releaseDate, INPUT_FORMAT, OUTPUT_FORMAT);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
