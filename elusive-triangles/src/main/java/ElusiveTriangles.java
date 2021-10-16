import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;
import processing.core.PApplet;
import processing.core.PVector;

import static art.farbfetzen.artutils.ChaikinsCornerCutter.cut;

public class ElusiveTriangles extends PApplet {

    private final int backgroundColor = color(40, 50, 55);
    private final int foregroundColor = color(0, 200, 0);
    private final List<Vector2D> vertices = new ArrayList<>();
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
        noCursor();

        for (int i = 0; i < 30; i++) {
            vertices.add(new Vector2D(random(20, width - 20.f), random(20, height - 20.f)));
        }
        vertices.add(mousePoint);
        delaunayTriangulator = new DelaunayTriangulator(vertices);
    }

    @Override
    public void draw() {
        mousePoint.x = mouseX;
        mousePoint.y = mouseY;

        background(backgroundColor);

        stroke(foregroundColor);
        strokeWeight(10);
        for (final Vector2D point : vertices) {
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
        for (final Triangle2D triangle : delaunayTriangulator.getTriangles()) {
            List<PVector> corners = Arrays.asList(
                    new PVector((float) triangle.a.x, (float) triangle.a.y),
                    new PVector((float) triangle.b.x, (float) triangle.b.y),
                    new PVector((float) triangle.c.x, (float) triangle.c.y)
            );
            fill(foregroundColor);
            beginShape();
            for (final PVector p : corners) {
                vertex(p.x, p.y);
            }
            endShape(CLOSE);

            corners = cut(corners, 0.25f, 3, true);
            fill(backgroundColor);
            beginShape();
            for (final PVector corner : corners) {
                vertex(corner.x, corner.y);
            }
            endShape(CLOSE);
        }
    }

}
