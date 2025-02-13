package de.tojo.pongpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.NonNull;

public class GameView extends View {

    private static final float PADDLE_VERTIKAL_LAENGE_KURZE_SEITE_PROZENTUAL = 0.01f;
    private static final float PADDLE_VERTIKAL_LAENGE_LANGE_SEITE_PROZENTUAL = 0.12f;
    private static final float PADDLE_HORIZONTAL_LAENGE_KURZE_SEITE_PROZENTUAL = 0.005f;
    private static final float PADDLE_HORIZONTAL_LAENGE_LANGE_SEITE_PROZENTUAL = 0.22f;
    private static final float BALL_RADIUS_PROZENTUAL = 0.01f;
    private static final float ABSTAND_ZUM_RAND_HORIZONTAL_PROZENTUAL = 0.026f;
    private static final float ABSTAND_ZUM_RAND_VERTIKAL_PROZENTUAL = 0.015f;
    private static final float PADDLE_VERTIKAL_GESCHWINDIGKEIT = 2.2f;
    private static final float PADDLE_HORIZONTAL_GESCHWINDIGKEIT = 2.5f;

    private boolean isRunning = false;
    private Handler handler = new Handler();
    private int screenWidth;
    private int screenHeight;
    private int screenHeightOhneLeisten;
    //erstmal auf 0, für den Fall, dass die Ermittlung in onSizeChanged() nicht funktioniert
    private int benachrichtigungsleisteHeight = 0;
    //erstmal auf 0, für den Fall, dass die Ermittlung in onSizeChanged() nicht funktioniert
    private int navigationsleisteHeight = 0;
    private Paint paint;
    private Rect spielfeld;
    private Rect paddleLinks, paddleOben, paddleRechts, paddleUnten;
    private Ball ball;
    private int paddleVertikalLaengeKurzeSeite;
    private int paddleVertikalLaengeLangeSeite;
    private int paddleHorizontalLaengeKurzeSeite;
    private int paddleHorizontalLaengeLangeSeite;
    private int abstandZumRandHorizontal;
    private int abstandZumRandVertikal;
    private float lastTouchPosX;
    private float lastTouchPosY;
    private final Runnable moveBallRunnable = new Runnable() {
        @Override
        public void run() {
            ball.move();

            invalidate();

            handler.postDelayed(moveBallRunnable, 16);
        }
    };

    public GameView(Context context) {
        super(context);

        paint = new Paint();
        paint.setColor(Color.WHITE);

        spielfeld = new Rect();
        paddleLinks = new Rect();
        paddleOben = new Rect();
        paddleRechts = new Rect();
        paddleUnten = new Rect();
        ball = new Ball();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;
        //erstmal auf h, für den Fall, dass die Ermittlung unten nicht funktioniert
        screenHeightOhneLeisten = h;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsets insets = getRootWindowInsets();

            if(insets != null) {
                benachrichtigungsleisteHeight = insets.getInsets(WindowInsets.Type.statusBars()).top;
                navigationsleisteHeight = insets.getInsets(WindowInsets.Type.navigationBars()).bottom;

                screenHeightOhneLeisten = h - benachrichtigungsleisteHeight - navigationsleisteHeight;
            }
        }

        initSpielfeld();
        initPaddles();
        initBall();
        invalidate();
        if(!isRunning) {
            startGame();
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(paddleLinks, paint);
        canvas.drawRect(paddleOben, paint);
        canvas.drawRect(paddleRechts, paint);
        canvas.drawRect(paddleUnten, paint);
        canvas.drawCircle(ball.cx, ball.cy, ball.radius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchPosX = event.getX();
                lastTouchPosY = event.getY();
                return true;

            case MotionEvent.ACTION_UP:
                performClick();
                return true;

            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getX() - lastTouchPosX;
                float deltaY = event.getY() - lastTouchPosY;

                lastTouchPosX = event.getX();
                lastTouchPosY = event.getY();

                int moveX = (int) (PADDLE_HORIZONTAL_GESCHWINDIGKEIT * deltaX);
                int moveY = (int) (PADDLE_VERTIKAL_GESCHWINDIGKEIT * deltaY);

                paddleLinks.offset(0, moveY);
                paddleOben.offset(moveX, 0);
                paddleRechts.offset(0, moveY);
                paddleUnten.offset(moveX, 0);

                paddlesImSpielfeldHalten();

                invalidate();
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void startGame() {
        handler.post(moveBallRunnable);
        isRunning = true;
    }

    // wird benötigt, weil die Paddles und der Ball vor dem Neustart erst wieder zurückgesetzt
    // werden müssen
    private void restartGame() {
        initPaddles();
        initBall();
        invalidate();
        startGame();
    }

    private void endGame() {
        handler.removeCallbacks(moveBallRunnable);
        isRunning = false;
    }

    private void initSpielfeld() {
        abstandZumRandHorizontal = (int) (ABSTAND_ZUM_RAND_HORIZONTAL_PROZENTUAL * screenWidth);
        abstandZumRandVertikal = (int) (ABSTAND_ZUM_RAND_VERTIKAL_PROZENTUAL * screenHeightOhneLeisten);

        spielfeld.set(
                abstandZumRandHorizontal,
                benachrichtigungsleisteHeight + abstandZumRandVertikal,
                screenWidth - abstandZumRandHorizontal,
                screenHeight - navigationsleisteHeight - abstandZumRandVertikal);
    }

    private void initPaddles() {
        paddleVertikalLaengeKurzeSeite = (int) (PADDLE_VERTIKAL_LAENGE_KURZE_SEITE_PROZENTUAL * spielfeld.width());
        paddleVertikalLaengeLangeSeite = (int) (PADDLE_VERTIKAL_LAENGE_LANGE_SEITE_PROZENTUAL * spielfeld.height());
        paddleHorizontalLaengeKurzeSeite = (int) (PADDLE_HORIZONTAL_LAENGE_KURZE_SEITE_PROZENTUAL * spielfeld.height());
        paddleHorizontalLaengeLangeSeite = (int) (PADDLE_HORIZONTAL_LAENGE_LANGE_SEITE_PROZENTUAL * spielfeld.width());

        paddleLinks.set(
                spielfeld.left - paddleVertikalLaengeKurzeSeite,
                spielfeld.top + (spielfeld.height() / 2) - (paddleVertikalLaengeLangeSeite / 2),
                spielfeld.left,
                spielfeld.bottom - (spielfeld.height() / 2) + (paddleVertikalLaengeLangeSeite / 2));
        paddleOben.set(
                spielfeld.left + (spielfeld.width() / 2) - (paddleHorizontalLaengeLangeSeite / 2),
                spielfeld.top - paddleHorizontalLaengeKurzeSeite,
                spielfeld.right - (spielfeld.width() / 2) + (paddleHorizontalLaengeLangeSeite / 2),
                spielfeld.top);
        paddleRechts.set(
                spielfeld.right,
                spielfeld.top + (spielfeld.height() / 2) - (paddleVertikalLaengeLangeSeite / 2),
                spielfeld.right + paddleVertikalLaengeKurzeSeite,
                spielfeld.bottom - (spielfeld.height() / 2) + (paddleVertikalLaengeLangeSeite / 2));
        paddleUnten.set(
                spielfeld.left + (spielfeld.width() / 2) - (paddleHorizontalLaengeLangeSeite / 2),
                spielfeld.bottom,
                spielfeld.right - (spielfeld.width() / 2) + (paddleHorizontalLaengeLangeSeite / 2),
                spielfeld.bottom + paddleHorizontalLaengeKurzeSeite);
    }

    private void initBall() {
        float ballRadius = BALL_RADIUS_PROZENTUAL * spielfeld.width();
        ball.set(spielfeld.left + (spielfeld.width() / 2f), spielfeld.top + (spielfeld.height() / 2f), ballRadius);
    }

    private void paddlesImSpielfeldHalten() {
        if(paddleLinks.top < spielfeld.top) {
            paddleLinks.offsetTo(paddleLinks.left, spielfeld.top);
            paddleRechts.offsetTo(paddleRechts.left, spielfeld.top);
        }
        else if(paddleLinks.bottom > spielfeld.bottom) {
            paddleLinks.offsetTo(paddleLinks.left, spielfeld.bottom - paddleVertikalLaengeLangeSeite);
            paddleRechts.offsetTo(paddleRechts.left, spielfeld.bottom - paddleVertikalLaengeLangeSeite);
        }

        if(paddleOben.left < spielfeld.left) {
            paddleOben.offsetTo(spielfeld.left, paddleOben.top);
            paddleUnten.offsetTo(spielfeld.left, paddleUnten.top);
        }
        else if(paddleOben.right > spielfeld.right) {
            paddleOben.offsetTo(spielfeld.right - paddleHorizontalLaengeLangeSeite, paddleOben.top);
            paddleUnten.offsetTo(spielfeld.right - paddleHorizontalLaengeLangeSeite, paddleUnten.top);
        }
    }
}
