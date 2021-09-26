import processing.core.PApplet;
import processing.core.PVector;

public class ChaosGame extends PApplet {

    private static final int MAX_FRAMES = 600;

    private static int defaultWidth = 1200;
    private static int defaultHeight = 800;

    private PVector[] vertices;
    private PVector tracePoint;
    private int frameCounter;
    private int nVertices;
    private float lerpAmount;

    public static void main(final String[] args) {
        if (args.length == 2) {
            defaultWidth = Integer.parseInt(args[0]);
            defaultHeight = Integer.parseInt(args[1]);
        }
        PApplet.main(ChaosGame.class.getName());
    }

    @Override
    public void settings() {
        size(defaultWidth, defaultHeight);
    }

    @Override
    public void setup() {
        strokeWeight(3);
        colorMode(HSB, 360, 100, 100);
        refresh();
    }

    @Override
    public void draw() {
        frameCounter++;
        if (frameCounter > MAX_FRAMES) {
            refresh();
        }

        for (int i = 0; i < 10; i++) {
            final int j = (int) random(nVertices);
            tracePoint.lerp(vertices[j], lerpAmount);
            point(tracePoint.x, tracePoint.y);
        }
    }

    private void refresh() {
        frameCounter = 0;

        nVertices = (int) random(3, 9);
        lerpAmount = 0.33f + 0.057f * nVertices;
        vertices = new PVector[nVertices];
        final PVector center = new PVector(width / 2f, height / 2f);
        final float radius = min(width, height) / 2f * 0.9f;
        final float twoPi = (float) Math.PI * 2;
        final float theta = random(twoPi);
        for (int i = 0; i < nVertices; i++) {
            final float angle = twoPi / nVertices * i;
            final PVector vertex = new PVector(sin(angle), cos(angle));
            vertex.rotate(theta);
            vertex.mult(radius);
            vertex.add(center);
            vertices[i] = vertex;
        }

        final float backgroundHue = random(360);
        final float foregroundHue = (backgroundHue + 180) % 360;  // opposite side of the color wheel
        background(backgroundHue, 100, 20);
        stroke(foregroundHue, 100, 100);

        tracePoint = vertices[0].copy();
        // Run the tracePoint for a while so there is less of a chance
        // for random points outside the desired shape.
        for (int i = 0; i < 10; i++) {
            final int j = (int) random(nVertices);
            tracePoint.lerp(vertices[j], lerpAmount);
        }
    }

    @Override
    public void keyPressed() {
        if (key == ENTER) {
            refresh();
        } else if (key == ' ') {
            if (isLooping()) {
                noLoop();
            } else {
                loop();
            }
        }
    }

}
