package de.tojo.pongpong;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class GameView extends View {

    private static final float PADDLE_VERTIKAL_LAENGE_KURZE_SEITE_PROZENTUAL = 0.01f;
    private static final float PADDLE_VERTIKAL_LAENGE_LANGE_SEITE_PROZENTUAL = 0.12f;
    private static final float PADDLE_HORIZONTAL_LAENGE_KURZE_SEITE_PROZENTUAL = 0.005f;
    private static final float PADDLE_HORIZONTAL_LAENGE_LANGE_SEITE_PROZENTUAL = 0.22f;
    private static final float BALL_RADIUS_PROZENTUAL = 0.01f;
    // prozentualer Abstand des Spielfelds zum Rand
    private static final float ABSTAND_ZUM_RAND_HORIZONTAL_PROZENTUAL = 0.026f;
    private static final float ABSTAND_ZUM_RAND_VERTIKAL_PROZENTUAL = 0.015f;

    private Handler handler = new Handler();
    private int screenWidth;
    private int screenHeight;
    private int screenHeightOhneLeisten;
    //erstmal auf 0, für den Fall, dass die Ermittlung in onSizeChanged() nicht funktioniert
    private int benachrichtigungsleisteHeight = 0;
    //erstmal auf 0, für den Fall, dass die Ermittlung in onSizeChanged() nicht funktioniert
    private int navigationsleisteHeight = 0;
    private boolean isRunning = false;
    private int punktzahl = 0;
    private Spielfeld spielfeld;
    private Paddle paddleLinks, paddleOben, paddleRechts, paddleUnten;
    private Ball ball;
    // Abstand des Spielfelds zum Rand
    private int abstandZumRandHorizontal;
    private int abstandZumRandVertikal;
    private float lastTouchPosX;
    private float lastTouchPosY;
    private final Runnable moveBallRunnable = new Runnable() {
        @Override
        public void run() {
            ball.move(spielfeld);
            kollisionenErkennen();

            invalidate();

            handler.postDelayed(moveBallRunnable, 16);
        }
    };

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hintergrund));
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

        canvas.drawRect(paddleLinks.getLeft(), paddleLinks.getTop(), paddleLinks.getRight(), paddleLinks.getBottom(), paddleLinks.getPaint());
        canvas.drawRect(paddleOben.getLeft(), paddleOben.getTop(), paddleOben.getRight(), paddleOben.getBottom(), paddleOben.getPaint());
        canvas.drawRect(paddleRechts.getLeft(), paddleRechts.getTop(), paddleRechts.getRight(), paddleRechts.getBottom(), paddleRechts.getPaint());
        canvas.drawRect(paddleUnten.getLeft(), paddleUnten.getTop(), paddleUnten.getRight(), paddleUnten.getBottom(), paddleUnten.getPaint());
        canvas.drawCircle(ball.cx, ball.cy, ball.radius, ball.getPaint());
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
                float touchMoveX = event.getX() - lastTouchPosX;
                float touchMoveY = event.getY() - lastTouchPosY;

                lastTouchPosX = event.getX();
                lastTouchPosY = event.getY();

                paddleLinks.move(touchMoveX, touchMoveY, spielfeld);
                paddleOben.move(touchMoveX, touchMoveY, spielfeld);
                paddleRechts.move(touchMoveX, touchMoveY, spielfeld);
                paddleUnten.move(touchMoveX, touchMoveY, spielfeld);

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

        DisplayMetrics dm = getResources().getDisplayMetrics();

        spielfeld = new Spielfeld(
                abstandZumRandHorizontal,
                benachrichtigungsleisteHeight + abstandZumRandVertikal,
                screenWidth - abstandZumRandHorizontal,
                screenHeight - navigationsleisteHeight - abstandZumRandVertikal,
                dm);
    }

    private void initPaddles() {
        int paddleVertikalLaengeKurzeSeite = (int) (PADDLE_VERTIKAL_LAENGE_KURZE_SEITE_PROZENTUAL * spielfeld.getWidth());
        int paddleVertikalLaengeLangeSeite = (int) (PADDLE_VERTIKAL_LAENGE_LANGE_SEITE_PROZENTUAL * spielfeld.getHeight());
        int paddleHorizontalLaengeKurzeSeite = (int) (PADDLE_HORIZONTAL_LAENGE_KURZE_SEITE_PROZENTUAL * spielfeld.getHeight());
        int paddleHorizontalLaengeLangeSeite = (int) (PADDLE_HORIZONTAL_LAENGE_LANGE_SEITE_PROZENTUAL * spielfeld.getWidth());

        paddleLinks = new Paddle(
                spielfeld.getLeft() - paddleVertikalLaengeKurzeSeite,
                spielfeld.getTop() + (spielfeld.getHeight() / 2) - (paddleVertikalLaengeLangeSeite / 2),
                spielfeld.getLeft(),
                spielfeld.getBottom() - (spielfeld.getHeight() / 2) + (paddleVertikalLaengeLangeSeite / 2));
        paddleOben = new Paddle(
                spielfeld.getLeft() + (spielfeld.getWidth() / 2) - (paddleHorizontalLaengeLangeSeite / 2),
                spielfeld.getTop() - paddleHorizontalLaengeKurzeSeite,
                spielfeld.getRight() - (spielfeld.getWidth() / 2) + (paddleHorizontalLaengeLangeSeite / 2),
                spielfeld.getTop());
        paddleRechts = new Paddle(
                spielfeld.getRight(),
                spielfeld.getTop() + (spielfeld.getHeight() / 2) - (paddleVertikalLaengeLangeSeite / 2),
                spielfeld.getRight() + paddleVertikalLaengeKurzeSeite,
                spielfeld.getBottom() - (spielfeld.getHeight() / 2) + (paddleVertikalLaengeLangeSeite / 2));
        paddleUnten = new Paddle(
                spielfeld.getLeft() + (spielfeld.getWidth() / 2) - (paddleHorizontalLaengeLangeSeite / 2),
                spielfeld.getBottom(),
                spielfeld.getRight() - (spielfeld.getWidth() / 2) + (paddleHorizontalLaengeLangeSeite / 2),
                spielfeld.getBottom() + paddleHorizontalLaengeKurzeSeite);
    }

    private void initBall() {
        float ballRadius = BALL_RADIUS_PROZENTUAL * spielfeld.getWidth();
        ball = new Ball(spielfeld.getLeft() + (spielfeld.getWidth() / 2f), spielfeld.getTop() + (spielfeld.getHeight() / 2f), ballRadius);
    }

    private void kollisionenErkennen() {
        if (ball.kollisionErkennen(paddleLinks)) {
            kollisionMitPaddle();
            return;
        }
        if (ball.kollisionErkennen(paddleOben)) {
            kollisionMitPaddle();
            return;
        }
        if (ball.kollisionErkennen(paddleRechts)) {
            kollisionMitPaddle();
            return;
        }
        if (ball.kollisionErkennen(paddleUnten)) {
            kollisionMitPaddle();
            return;
        }
    }

    /*
    TODO: Es gibt gerade auch Punkte, wenn man den Ball mit dem Paddle nur streift und er dann
    sofort aus dem Spielfeld fliegt. Eigentlich sollte es dafür keinen Punkt mehr geben
     */
    private void kollisionMitPaddle() {
        ball.ueberschneidungMitPaddleVerhindern();
        punktzahl++;
        ball.richtungAendern();
    }
}
