package com.gk.medidores.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "measures")
public class Measure implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "coord_lat")
    private Float coordLat;

    @ColumnInfo(name = "coord_long")
    private Float coordLong;

    @ColumnInfo(name = "device_id")
    private String deviceId;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    @ColumnInfo(name = "created_at")
    private Long createdAt;

    @ColumnInfo(name = "power")
    private Float power;

    @ColumnInfo(name = "address")
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getCoordLat() {
        return coordLat;
    }

    public void setCoordLat(Float coordLat) {
        this.coordLat = coordLat;
    }

    public Float getCoordLong() {
        return coordLong;
    }

    public void setCoordLong(Float coordLong) {
        this.coordLong = coordLong;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Float getPower() {
        return power;
    }

    public void setPower(Float power) {
        this.power = power;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Measure>() {
        @Override
        public Measure createFromParcel(Parcel source) {
            return new Measure(source);
        }

        @Override
        public Measure[] newArray(int size) {
            return new Measure[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] stringArray = new String[3];
        float[] floatArray = new float[3];

        stringArray[0] = this.deviceId;
        stringArray[1] = this.address;
        if(this.imagePath != null) stringArray[2] = this.imagePath;

        floatArray[0] = this.power;
        if(this.coordLat != null) floatArray[1] = this.coordLat;
        if(this.coordLong != null) floatArray[2] = this.coordLong;

        dest.writeInt(this.id);
        dest.writeLong(this.createdAt);
        dest.writeStringArray(stringArray);
        dest.writeFloatArray(floatArray);
    }

    public Measure() {}
    public Measure(Parcel parcel) {
        this.id = parcel.readInt();
        this.createdAt = parcel.readLong();

        String[] sArray = new String[3];
        parcel.readStringArray(sArray);

        this.deviceId = sArray[0];
        this.address = sArray[1];
        this.imagePath = sArray[2];

        float[] fArray = new float[3];
        parcel.readFloatArray(fArray);

        this.power = fArray[0];
        this.coordLat = fArray[1];
        this.coordLong = fArray[2];
    }
}
