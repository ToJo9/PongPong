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
    private float geschwindigkeit = 8;
    public Ball() {
        genRandomRichtung();
    }

    public Ball(float cx, float cy, float radius) {
        super(cx, cy, radius);
        genRandomRichtung();
    }

    public void move() {
        float bewegungX = richtungX * geschwindigkeit;
        float bewegungY = richtungY * geschwindigkeit;

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
