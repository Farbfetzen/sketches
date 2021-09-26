// https://www.youtube.com/watch?v=_UtCli1SgjI
// TODO: Expand to allow arbitrarily shaped toothpicks, maybe with different numbers of endpoints.

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.*;

public class ToothpicksSketch extends PApplet {

    private static final int halfLength = 25;
    private final Set<IntPoint> freeEnds = new HashSet<>();
    private final Set<IntPoint> blockedEnds = new HashSet<>();
    private final List<Toothpick> toothpicks = new ArrayList<>();
    private final PVector center = new PVector();
    private boolean horizontal = true;
    private float growMargin;
    private float scaleFactor = 1;
    private int steps = 1;

    public static void main(final String[] args) {
        PApplet.main(ToothpicksSketch.class.getName());
    }

    @Override
    public void settings() {
        size(1200, 800);
    }

    @Override
    public void setup() {
        noLoop();
        growMargin = min(width, height) - halfLength * 2;
        stroke(255, 128, 0);
        center.x = width / 2f;
        center.y = height / 2f;
        translate(center.x, center.y);
        final Toothpick t = new Toothpick(new IntPoint(0, 0), horizontal, halfLength);
        toothpicks.add(t);
        freeEnds.add(t.end1);
        freeEnds.add(t.end2);
    }

    @Override
    public void draw() {
        background(16);
        translate(center.x, center.y);
        scale(scaleFactor);
        grow();
        for (final Toothpick t : toothpicks) {
            line(t.end1.x, t.end1.y, t.end2.x, t.end2.y);
            // glitched version:
            // line(t.end1.x, t.end2.y, t.end1.y, t.end2.x);
        }
    }

    @Override
    public void keyPressed() {
        if (key == '\n' && !isLooping()) {
            redraw();
        } else if (key == ' ') {
            if (isLooping()) {
                noLoop();
                frameRate(60);
            } else {
                loop();
                frameRate(5);
            }
        }
    }

    private void grow() {
        steps++;
        if (steps * halfLength * scaleFactor >= growMargin) {
            scaleFactor *= 0.75;
        }

        horizontal = !horizontal;
        final Set<IntPoint> clonedSet = Set.copyOf(freeEnds);
        println(clonedSet.size());
        for (final IntPoint p : clonedSet) {
            final Toothpick t = new Toothpick(new IntPoint(p.x, p.y), horizontal, halfLength);
            toothpicks.add(t);
            freeEnds.remove(p);
            blockedEnds.add(p);
            addOrRemoveNewEnds(t.end1);
            addOrRemoveNewEnds(t.end2);
        }
    }

    private void addOrRemoveNewEnds(final IntPoint p) {
        if (blockedEnds.contains(p)) {
            return;
        }
        if (freeEnds.contains(p)) {
            freeEnds.remove(p);
            blockedEnds.add(p);
        } else {
            freeEnds.add(p);
        }
    }

}

/**
 * I'm using integers for the endpoints of the toothpicks
 * to avoid float inaccuracies when comparing them.
 */
@AllArgsConstructor
@EqualsAndHashCode
class IntPoint {
    final int x;
    final int y;
}

class Toothpick {
    final IntPoint end1;
    final IntPoint end2;
    final boolean horizontal;

    Toothpick(final IntPoint center, final boolean horizontal, final int halfLength) {
        if (horizontal) {
            end1 = new IntPoint(center.x - halfLength, center.y);
            end2 = new IntPoint(center.x + halfLength, center.y);
        } else {
            end1 = new IntPoint(center.x, center.y - halfLength);
            end2 = new IntPoint(center.x, center.y + halfLength);
        }
        this.horizontal = horizontal;
    }

}
