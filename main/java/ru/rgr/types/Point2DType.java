package ru.rgr.types;

public class Point2DType {
    float x, y;

    public Point2DType(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }
    public float getY() {
        return this.y;
    }

    public void setPoint2DValue(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(this.x) + ";" + String.valueOf(this.y) + ")";
    }
}
