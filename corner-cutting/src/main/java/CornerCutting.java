import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

import art.farbfetzen.processingutilities.ChaikinsCornerCutter;

public class CornerCutting extends PApplet {

    private static final boolean CLOSED = true;

    private List<PVector> testShape;

    public static void main(final String[] args) {
        PApplet.main(CornerCutting.class.getName());
    }

    @Override
    public void settings() {
        size(800, 800);
    }

    @Override
    public void setup() {
        noLoop();
        testShape = new ArrayList<>();
        testShape.add(new PVector(100, 100));
        testShape.add(new PVector(700, 200));
        testShape.add(new PVector(750, 750));
        testShape.add(new PVector(50, 700));
        testShape.add(new PVector(350, 350));
    }

    @Override
    public void draw() {
        background(16, 16, 16);
        beginShape();
        noFill();
        stroke(220, 220, 0);
        strokeWeight(2);
        for (final PVector pv : testShape) {
            vertex(pv.x, pv.y);
        }
        if (CLOSED) {
            endShape(CLOSE);
        } else {
            endShape();
        }
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            testShape = ChaikinsCornerCutter.cut(testShape, 0.2f, 1, CLOSED);
            redraw();
        }
    }

}
