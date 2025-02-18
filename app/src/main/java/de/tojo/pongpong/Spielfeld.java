package de.tojo.pongpong;

import android.graphics.Rect;
import android.util.DisplayMetrics;

// Die Paddles befinden sich außerhalb des Spielfelds
// Komposition mit Rect; Vererbung ist nicht möglich, weil Rect final ist
public class Spielfeld {

    private Rect rect;
    private float widthMm;
    private float heightMm;

    public Spielfeld(int left, int top, int right, int bottom, DisplayMetrics dmGameView) {
        rect = new Rect(left, top, right, bottom);
        calcWidthMmHeightMm(dmGameView);
    }

    private void calcWidthMmHeightMm(DisplayMetrics dmGameView) {
        // dpmm = dots per millimeter
        // 1 Zoll = 25,4 mm
        float xdpmm = dmGameView.xdpi / 25.4f;
        float ydpmm = dmGameView.ydpi / 25.4f;
        widthMm = rect.width() / xdpmm;
        heightMm = rect.height() / ydpmm;
    }

    public int getWidth() {
        return rect.width();
    }

    public int getHeight() {
        return rect.height();
    }

    public float getWidthMm() {
        return widthMm;
    }

    public float getHeightMm() {
        return heightMm;
    }

    public int getLeft() {
        return rect.left;
    }

    public int getTop() {
        return rect.top;
    }

    public int getRight() {
        return rect.right;
    }

    public int getBottom() {
        return rect.bottom;
    }
}
