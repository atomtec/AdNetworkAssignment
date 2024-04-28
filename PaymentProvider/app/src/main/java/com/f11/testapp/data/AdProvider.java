package com.f11.testapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ad_providers")
public class AdProvider implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String alias;
    private String name;
    private String privacyUrl;

    public AdProvider(int id, String alias, String name, String privacyUrl) {
        this.id = id;
        this.alias = alias;
        this.name = name;
        this.privacyUrl = privacyUrl;
    }

    public AdProvider(Parcel in) {
        id = in.readInt();
        alias = in.readString();
        name = in.readString();
        privacyUrl = in.readString();
    }



    @Override
    public int describeContents() {
        return 0; // Usually return 0 or bit mask of content types
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(alias);
        parcel.writeString(name);
        parcel.writeString(privacyUrl);
    }

    public static final Creator<AdProvider> CREATOR = new Creator<AdProvider>() {
        @Override
        public AdProvider createFromParcel(Parcel in) {
            return new AdProvider(in);
        }

        @Override
        public AdProvider[] newArray(int size) {
            return new AdProvider[size];
        }
    };

    public String getAlias() {
        return alias;
    }

    public String getName() {
        return name;
    }

    public String getPrivacyUrl() {
        return privacyUrl;
    }

    public int getId() {
        return id;
    }

    public void setAlias(String newText) {
        alias = newText;
    }
    public void setName(String newText) {
        name = newText;
    }
    public void setPrivacyUrl(String newText) {
        privacyUrl = newText;
    }
}
