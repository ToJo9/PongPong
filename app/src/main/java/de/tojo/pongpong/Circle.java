package de.tojo.pongpong;

public class Circle {

    public float cx;
    public float cy;
    public float radius;

    public Circle() {

    }
    public Circle(float cx, float cy, float radius) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
    }

    public void set(float cx, float cy, float radius) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
    }
    //TODO: wahrscheinlich wird die offset()-Methode wie in Rect noch benötigt
}
