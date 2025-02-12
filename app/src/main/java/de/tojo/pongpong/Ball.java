package de.tojo.pongpong;

public class Ball extends Circle {

    // TODO: richtungX und richtungY sind hier erstmal nur testweise initialisiert.
    // sie müssen random generiert werden. das passiert woanders. dabei muss beachtet werden, dass
    // der Ball nicht immer unterschiedlich schnell startet
    private float richtungX = 0.5f;
    private float richtungY = 0.5f;
    private float geschwindigkeit = 8;
    public Ball() {

    }

    public Ball(float cx, float cy, float radius) {
        super(cx, cy, radius);
    }

    public void move() {
        float bewegungX = richtungX * geschwindigkeit;
        float bewegungY = richtungY * geschwindigkeit;

        offset(bewegungX, bewegungY, 0);
    }
}
