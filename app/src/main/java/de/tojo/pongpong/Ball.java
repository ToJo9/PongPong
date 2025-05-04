package de.tojo.pongpong;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Ball extends Circle {

    // Bei einem Abprallwinkel von 90 Grad würde der Ball orthogonal zum Paddle abprallen.
    // Dann wäre das Spiel zu einfach.
    // Ein Abprallwinkel von >90 Grad ist nicht möglich, weil der Ball sonst zurückspringen würde.
    // MAX_ABPRALLWINKEL_GRAD muss also <90 sein
    //TODO: alte berechnung zur berücksichtigung des abprallwinkels: richtungX = randomizer.nextFloat() * (1 - (2 * (WINKEL / 90))) + (WINKEL / 90);
    private static final float MAX_ABPRALLWINKEL_GRAD = 80f;

    private Paint paint = new Paint();
    private float richtungX;
    private float richtungY;
    private float geschwindigkeit = 0.06f;
    /*
    abstandZwischenBallCenterUndPaddleY und abstandZwischenBallUndPaddle könnten lokale Variablen
    sein. abstandZwischenBallCenterUndPaddleX wird aber global benötigt. Damit es einheitlich ist,
    sind alle global
     */
    /*
    wenn Ballmitte links vom Paddle ist: negativ
    wenn Ballmitte und Paddle sich auf der x-Ebene ueberschneiden: 0
    wenn Ballmitte rechts vom Paddle ist: positiv
     */
    private float abstandZwischenBallCenterUndPaddleX;
    /*
    wenn Ballmitte hoeher als Paddle ist: negativ
    wenn Ballmitte und Paddle sich auf der y-Ebene ueberschneiden: 0
    wenn Ballmitte niedriger als Paddle ist: positiv
     */
    private float abstandZwischenBallCenterUndPaddleY;
    /*
    wenn Ball und Paddle sich nicht beruehren: positiv
    wenn Ball und Paddle sich am Rand beruehren: 0
    wenn Ball und Paddle sich ueberschneiden: negativ (Wert gibt negativ an, wie weit Ball im Paddle
    ist)
     */
    private float abstandZwischenBallUndPaddle;

    public Ball(float cx, float cy, float radius) {
        super(cx, cy, radius);
        paint.setColor(Color.WHITE);
        genRandomRichtung();
    }

    // spielfeld.widthMm und spielfeld.heightMm müssen berücksichtigt werden, damit der Ball
    // unabhängig von der Bildschirmgröße und Auflösung immer gleich lange von Position A zu
    // Position B braucht. Sonst würde der Ball bei größeren Bildschirmen oder Bildschirmen mit mehr
    // Pixeln länger von Position A zu Position B brauchen
    // TODO: sobald eigene Klasse für spielfeld erstellt wurde: hier vllt das ganze spielfeld und
    // nicht nur die beiden Werte übergeben
    public void move(Spielfeld spielfeld) {
        float bewegungX = richtungX * geschwindigkeit * (spielfeld.getWidthMm() + spielfeld.getHeightMm());
        float bewegungY = richtungY * geschwindigkeit * (spielfeld.getWidthMm() + spielfeld.getHeightMm());

        offset(bewegungX, bewegungY, 0);
    }

    public boolean kollisionErkennen(Paddle paddle) {
        // Abstand zwischen der Mitte des Balls und der nächsten Stelle des Paddles auf x-Ebene
        // berechnen

        float abstandZwischenCentersX = cx - paddle.getCenterX();

        // ueberpruefen, ob Ball links vom Paddle ist
        if(abstandZwischenCentersX < -(paddle.getWidth() / 2f)) {
            abstandZwischenBallCenterUndPaddleX = abstandZwischenCentersX + (paddle.getWidth() / 2f);
        }
        // ueberpruefen, ob Ball rechts vom Paddle ist
        else if(abstandZwischenCentersX > (paddle.getWidth() / 2f)) {
            abstandZwischenBallCenterUndPaddleX = abstandZwischenCentersX - (paddle.getWidth() / 2f);
        }
        // wenn Ball im Bereich des Paddles ist
        else {
            abstandZwischenBallCenterUndPaddleX = 0;
        }

        // Abstand zwischen der Mitte des Balls und der nächsten Stelle des Paddles auf y-Ebene
        // berechnen

        float abstandZwischenCentersY = cy - paddle.getCenterY();

        // ueberpruefen, ob Ball hoeher als Paddle ist
        if(abstandZwischenCentersY < -(paddle.getHeight() / 2f)) {
            abstandZwischenBallCenterUndPaddleY = abstandZwischenCentersY + (paddle.getHeight() / 2f);
        }
        // ueberpruefen, ob Ball niedriger als Paddle ist
        else if(abstandZwischenCentersY > (paddle.getHeight() / 2f)) {
            abstandZwischenBallCenterUndPaddleY = abstandZwischenCentersY - (paddle.getHeight() / 2f);
        }
        // wenn Ball im Bereich des Paddles ist
        else {
            abstandZwischenBallCenterUndPaddleY = 0;
        }

        // allgemeinen Abstand zwischen der Mitte des Balls und der nächsten Stelle des Paddles
        // berechnen

        // Satz des Pythagoras
        // TODO: wie kann ich die beiden Zeilen mit Math.pow() buendig schreiben?
        float abstandZwischenBallCenterUndPaddle = (float) (Math.sqrt(
                Math.pow(abstandZwischenBallCenterUndPaddleX, 2) +
                        Math.pow(abstandZwischenBallCenterUndPaddleY, 2)
        ));
        abstandZwischenBallUndPaddle = abstandZwischenBallCenterUndPaddle - radius;

        if(abstandZwischenBallUndPaddle <= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void ueberschneidungMitPaddleVerhindern() {
        //TODO
    }

    public void richtungAendern() {
        /*
        Reflexionsformel: R = D - 2 * dot(D, N) * N
        R = reflektierter Vektor (Ausfallsvektor)
        D = Einfallsrichtungsvektor
                Vektor: (richtungX, richtungY)
        N = Normalenvektor (normalisierter Vektor von Mitte des Balls zum Kollisionspunkt mit dem
            Paddle)
                summeAbstaende = | abstandZwischenBallCenterUndPaddleX | +
                                 | abstandZwischenBallCenterUndPaddleY |
                Vektor: (abstandZwischenBallCenterUndPaddleX / summeAbstaende,
                        abstandZwischenBallCenterUndPaddleY / summeAbstaende)
        dot() = Skalarprodukt
         */
        float summeAbstaende = Math.abs(abstandZwischenBallCenterUndPaddleX) +
                Math.abs(abstandZwischenBallCenterUndPaddleY);

        float nX = abstandZwischenBallCenterUndPaddleX / summeAbstaende;
        float nY = abstandZwischenBallCenterUndPaddleY / summeAbstaende;

        float skalarprodukt = richtungX * nX + richtungY * nY;

        float rX = richtungX - 2 * skalarprodukt * nX;
        float rY = richtungY - 2 * skalarprodukt * nY;

        richtungX = rX;
        richtungY = rY;
    }

    private void genRandomRichtung() {
        Random randomizer = new Random();

        // müssen zsm 1 ergeben, damit der Ball sich jedes Mal gleich schnell bewegt
        richtungX = randomizer.nextFloat();
        richtungY = 1 - richtungX;

        if(randomizer.nextBoolean()) {
            richtungX *= -1;
        }

        if(randomizer.nextBoolean()) {
            richtungY *= -1;
        }
    }

    public Paint getPaint() {
        return paint;
    }
}
