package de.tojo.pongpong;

public class Circle {

    // TODO: "protected" ist nicht richtig; man darf in gameview nicht ball.cx aufrufen dürfen
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

    public void offset(float cx, float cy, float radius) {
        this.cx += cx;
        this.cy += cy;
        this.radius += radius;
    }

    public float getLeft() {
        return cx - radius;
    }

    public float getTop() {
        return cy - radius;
    }

    public float getRight() {
        return cx + radius;
    }

    public float getBottom() {
        return cy + radius;
    }

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
