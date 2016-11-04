package com.example.kostya.channeltask.model.acc_model;

/**
 * Created by kostya on 04.11.16.
 */

public class AccelerometerData {
    private String date;
    private float x;
    private float y;
    private float z;

    public AccelerometerData() {

    }

    public AccelerometerData(String date, float x, float y, float z) {
        this.date = date;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
