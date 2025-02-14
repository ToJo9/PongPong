package de.tojo.pongpong;

import java.util.Random;

public class Ball extends Circle {

    // Bei einem Abprallwinkel von 90 Grad würde der Ball orthogonal zum Paddle abprallen.
    // Dann wäre das Spiel zu einfach.
    // Ein Abprallwinkel von >90 Grad ist nicht möglich, weil der Ball sonst zurückspringen würde.
    // MAX_ABPRALLWINKEL_GRAD muss also <90 sein
    //TODO: alte berechnung zur berücksichtigung des abprallwinkels: richtungX = randomizer.nextFloat() * (1 - (2 * (WINKEL / 90))) + (WINKEL / 90);
    private static final float MAX_ABPRALLWINKEL_GRAD = 80f;

    private float richtungX;
    private float richtungY;
    private float geschwindigkeit = 0.08f;
    public Ball() {
        genRandomRichtung();
    }

    public Ball(float cx, float cy, float radius) {
        super(cx, cy, radius);
        genRandomRichtung();
    }

    // spielfeldWidthMm und spielfeldHeightMm müssen berücksichtigt werden, damit der Ball
    // unabhängig von der Bildschirmgröße und Auflösung immer gleich lange von Position A zu
    // Position B braucht. Sonst würde der Ball bei größeren Bildschirmen oder Bildschirmen mit mehr
    // Pixeln länger von Position A zu Position B brauchen
    // TODO: sobald eigene Klasse für spielfeld erstellt wurde: hier vllt das ganze spielfeld und
    // nicht nur die beiden Werte übergeben
    public void move(float spielfeldWidthMm, float spielfeldHeightMm) {
        float bewegungX = richtungX * geschwindigkeit * spielfeldWidthMm;
        float bewegungY = richtungY * geschwindigkeit * spielfeldHeightMm;

        offset(bewegungX, bewegungY, 0);
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
}
