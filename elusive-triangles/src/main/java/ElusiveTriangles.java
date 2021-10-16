import java.util.ArrayList;
import java.util.List;

import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;
import processing.core.PApplet;

public class ElusiveTriangles extends PApplet {

    private final List<Vector2D> points = new ArrayList<>();
    private final Vector2D mousePoint = new Vector2D(0, 0);
    private DelaunayTriangulator delaunayTriangulator;

    public static void main(final String[] args) {
        PApplet.main(ElusiveTriangles.class);
    }

    @Override
    public void settings() {
        size(1200, 800);
    }

    @Override
    public void setup() {
        stroke(0, 200, 0);
        noCursor();

        for (int i = 0; i < 30; i++) {
            points.add(new Vector2D(random(20, width - 20.f), random(20, height - 20.f)));
        }
        points.add(mousePoint);
        delaunayTriangulator = new DelaunayTriangulator(points);
    }

    @Override
    public void draw() {
        mousePoint.x = mouseX;
        mousePoint.y = mouseY;

        background(40, 50, 55);

        strokeWeight(10);
        for (final Vector2D point : points) {
            final float x = (float) point.x;
            final float y = (float) point.y;
            point(x, y);
        }

        try {
            delaunayTriangulator.triangulate();
        } catch (final NotEnoughPointsException e) {
            e.printStackTrace();
            exit();
        }
        strokeWeight(1);
        noFill();
        for (final Triangle2D triangle : delaunayTriangulator.getTriangles()) {
            beginShape();
            vertex((float) triangle.a.x, (float) triangle.a.y);
            vertex((float) triangle.b.x, (float) triangle.b.y);
            vertex((float) triangle.c.x, (float) triangle.c.y);
            endShape(CLOSE);
        }
    }

}
