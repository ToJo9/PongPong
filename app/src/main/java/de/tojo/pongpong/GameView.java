package de.tojo.pongpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;

public class GameView extends View {

    private int screenWidth;
    private int screenHeight;
    private Paint paint;
    private Rect paddleLinks, paddleOben, paddleRechts, paddleUnten;
    private int paddleKurzeSeite;
    private int paddleLangeSeite;

    public GameView(Context context) {
        super(context);

        screenWidth = getWidth();
        screenHeight = getHeight();

        paint = new Paint();
        paint.setColor(Color.WHITE);

        initPaddles();
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
        paddleKurzeSeite = (int) (0.2 * screenWidth);
        paddleLangeSeite = (int) (0.2 * screenHeight);

        int paddleKoordinateLinks;
        int paddleKoordinateOben;

        paddleKoordinateLinks = ;
        paddleKoordinateOben = ;
        paddleLinks = new Rect(paddleKoordinateLinks, paddleKoordinateOben, paddleKoordinateLinks + paddleKurzeSeite, paddleKoordinateOben + paddleLangeSeite);

        paddleKoordinateLinks = ;
        paddleKoordinateOben = ;
        paddleOben = new Rect(paddleKoordinateLinks, paddleKoordinateOben, paddleKoordinateLinks + paddleLangeSeite, paddleKoordinateOben + paddleKurzeSeite);

        paddleKoordinateLinks = ;
        paddleKoordinateOben = ;
        paddleRechts = new Rect(paddleKoordinateLinks, paddleKoordinateOben, paddleKoordinateLinks + paddleKurzeSeite, paddleKoordinateOben + paddleLangeSeite);

        paddleKoordinateLinks = ;
        paddleKoordinateOben = ;
        paddleUnten = new Rect(paddleKoordinateLinks, paddleKoordinateOben, paddleKoordinateLinks + paddleLangeSeite, paddleKoordinateOben + paddleKurzeSeite);
    }
}
