package com.raphael.carvalho.android.popularmovies.detail.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.raphael.carvalho.android.popularmovies.util.SitesUrl;

public class Trailer implements Parcelable {
    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    private static final String SITE_YOU_TUBE = "YouTube";

    @SerializedName("id")
    private final String id;
    @SerializedName("iso_639_1")
    private final String iso6391;
    @SerializedName("iso_3166_1")
    private final String iso31661;
    @SerializedName("key")
    private final String key;
    @SerializedName("name")
    private final String name;
    @SerializedName("site")
    private final String site;
    @SerializedName("size")
    private final Integer size;
    @SerializedName("type")
    private final String type;

    private Trailer(Parcel in) {
        id = in.readString();
        iso6391 = in.readString();
        iso31661 = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        if (in.readByte() == 0) {
            size = null;
        } else {
            size = in.readInt();
        }
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso6391);
        dest.writeString(iso31661);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        if (size == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(size);
        }
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        if (SITE_YOU_TUBE.equals(site)) {
            return SitesUrl.buildYouTubeUri(key);

        } else return null;
    }
}