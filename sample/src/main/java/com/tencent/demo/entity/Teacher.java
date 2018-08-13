package com.tencent.demo.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Teacher implements Parcelable {

    public int level;
    public String name;

    public Teacher(){}
    protected Teacher(Parcel in) {
        level = in.readInt();
        name = in.readString();
    }

    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(level);
        parcel.writeString(name);
    }
}
