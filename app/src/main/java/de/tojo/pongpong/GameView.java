package de.tojo.pongpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.NonNull;

public class GameView extends View {

    private static final float PADDLE_VERTIKAL_LAENGE_KURZE_SEITE = 0.01f;
    private static final float PADDLE_VERTIKAL_LAENGE_LANGE_SEITE = 0.12f;
    private static final float PADDLE_HORIZONTAL_LAENGE_KURZE_SEITE = 0.005f;
    private static final float PADDLE_HORIZONTAL_LAENGE_LANGE_SEITE = 0.22f;
    private static final float ABSTAND_ZUM_RAND_HORIZONTAL_PROZENTUAL = 0.023f;
    private static final float ABSTAND_ZUM_RAND_VERTIKAL_PROZENTUAL = 0.015f;

    private int screenWidth;
    private int screenHeight;
    private int screenHeightOhneLeisten;
    //erstmal auf 0, für den Fall, dass die Ermittlung in onSizeChanged() nicht funktioniert
    private int benachrichtigungsleisteHeight = 0;
    //erstmal auf 0, für den Fall, dass die Ermittlung in onSizeChanged() nicht funktioniert
    private int navigationsleisteHeight = 0;
    private Paint paint;
    private Rect paddleLinks, paddleOben, paddleRechts, paddleUnten;
    private int paddleVertikalLaengeKurzeSeite;
    private int paddleVertikalLaengeLangeSeite;
    private int paddleHorizontalLaengeKurzeSeite;
    private int paddleHorizontalLaengeLangeSeite;

    public GameView(Context context) {
        super(context);

        paint = new Paint();
        paint.setColor(Color.WHITE);
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

        initPaddles();
//        initBall();
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(paddleLinks, paint);
        canvas.drawRect(paddleOben, paint);
        canvas.drawRect(paddleRechts, paint);
        canvas.drawRect(paddleUnten, paint);
    }

    private void initPaddles() {
        paddleVertikalLaengeKurzeSeite = (int) (PADDLE_VERTIKAL_LAENGE_KURZE_SEITE * screenWidth);
        paddleVertikalLaengeLangeSeite = (int) (PADDLE_VERTIKAL_LAENGE_LANGE_SEITE * screenHeightOhneLeisten);
        paddleHorizontalLaengeKurzeSeite = (int) (PADDLE_HORIZONTAL_LAENGE_KURZE_SEITE * screenHeightOhneLeisten);
        paddleHorizontalLaengeLangeSeite = (int) (PADDLE_HORIZONTAL_LAENGE_LANGE_SEITE * screenWidth);

        int abstandZumRandHorizontal = (int) (ABSTAND_ZUM_RAND_HORIZONTAL_PROZENTUAL * screenWidth);
        int abstandZumRandVertikal = (int) (ABSTAND_ZUM_RAND_VERTIKAL_PROZENTUAL * screenHeightOhneLeisten);

        paddleLinks = new Rect(abstandZumRandHorizontal, benachrichtigungsleisteHeight + ((screenHeightOhneLeisten / 2) - (paddleVertikalLaengeLangeSeite / 2)), abstandZumRandHorizontal + paddleVertikalLaengeKurzeSeite, benachrichtigungsleisteHeight + (screenHeightOhneLeisten / 2) + (paddleVertikalLaengeLangeSeite / 2));
        paddleOben = new Rect((screenWidth / 2) - (paddleHorizontalLaengeLangeSeite / 2), benachrichtigungsleisteHeight + abstandZumRandVertikal, (screenWidth / 2) + (paddleHorizontalLaengeLangeSeite / 2), benachrichtigungsleisteHeight + abstandZumRandVertikal + paddleHorizontalLaengeKurzeSeite);
        paddleRechts = new Rect(screenWidth - abstandZumRandHorizontal - paddleVertikalLaengeKurzeSeite, benachrichtigungsleisteHeight + (screenHeightOhneLeisten / 2) - (paddleVertikalLaengeLangeSeite / 2), screenWidth - abstandZumRandHorizontal, benachrichtigungsleisteHeight + (screenHeightOhneLeisten / 2) + (paddleVertikalLaengeLangeSeite / 2));
        paddleUnten = new Rect((screenWidth / 2) - (paddleHorizontalLaengeLangeSeite / 2), screenHeight - navigationsleisteHeight - abstandZumRandVertikal - paddleHorizontalLaengeKurzeSeite, (screenWidth / 2) + (paddleHorizontalLaengeLangeSeite / 2), screenHeight - navigationsleisteHeight - abstandZumRandVertikal);
    }

    private void initBall() {
        // TODO: ball erstellen
    }
}
