package de.tojo.pongpong;

public class Circle {

    protected float cx;
    protected float cy;
    protected float radius;

    public Circle() {

    }
    public Circle(float cx, float cy, float radius) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
    }

    //TODO: wahrscheinlich wird die offset()-Methode wie in Rect noch benötigt

    public void set(float cx, float cy, float radius) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
    }

    public float getCx() {
        return cx;
    }

    public void setCx(float cx) {
        this.cx = cx;
    }

    public float getCy() {
        return cy;
    }

    public void setCy(float cy) {
        this.cy = cy;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
