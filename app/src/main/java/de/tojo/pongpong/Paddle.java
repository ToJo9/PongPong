package de.tojo.pongpong;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

// Die Paddles befinden sich außerhalb des Spielfelds
// Komposition mit Rect; Vererbung ist nicht möglich, weil Rect final ist
public class Paddle {

    private static final float PADDLE_VERTIKAL_GESCHWINDIGKEIT = 2.2f;
    private static final float PADDLE_HORIZONTAL_GESCHWINDIGKEIT = 2.5f;

    private final Rect rect;
    private Paint paint = new Paint();
    // sonst vertikal
    private final boolean isHorizontal;

    public Paddle(int left, int top, int right, int bottom) {
        rect = new Rect(left, top, right, bottom);
        paint.setColor(Color.WHITE);
        isHorizontal = rect.width() > rect.height();
    }

    public void move(float touchMoveX, float touchMoveY, Spielfeld spielfeld) {

        if(isHorizontal) {
            int paddleMoveX = (int) (PADDLE_HORIZONTAL_GESCHWINDIGKEIT * touchMoveX);
            rect.offset(paddleMoveX, 0);
        }
        else {
            int paddleMoveY = (int) (PADDLE_VERTIKAL_GESCHWINDIGKEIT * touchMoveY);
            rect.offset(0, paddleMoveY);
        }

        paddleImSpielfeldHalten(spielfeld);
    }

    private void paddleImSpielfeldHalten(Spielfeld spielfeld) {

        if(isHorizontal) {
            if(rect.left < spielfeld.getLeft()) {
                rect.offsetTo(spielfeld.getLeft(), rect.top);
            }
            else if(rect.right > spielfeld.getRight()) {
                rect.offsetTo(spielfeld.getRight() - rect.width(), rect.top);
            }
        }
        else {
            if(rect.top < spielfeld.getTop()) {
                rect.offsetTo(rect.left, spielfeld.getTop());
            }
            else if(rect.bottom > spielfeld.getBottom()) {
                rect.offsetTo(rect.left, spielfeld.getBottom() - rect.height());
            }
        }
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

    public Paint getPaint() {
        return paint;
    }
}
