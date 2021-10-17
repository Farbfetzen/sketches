import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Vector2D;

public class ElusiveTriangles {

    public static void main(final String[] args) throws NotEnoughPointsException {
        final int nMax = 2000;
        final int nRepetitions = 100;
        final Random random = new Random();
        final Map<Integer, Long> result = new LinkedHashMap<>();
        final Stopwatch stopwatch = Stopwatch.createUnstarted();

        for (int n = 10; n <= nMax; n += 10) {
            for (int r = 0; r < nRepetitions; r++) {
                final List<Vector2D> vertices = new ArrayList<>(n);
                for (int i = 0; i < n; i++) {
                    vertices.add(new Vector2D(random.nextDouble(), random.nextDouble()));
                }
                final DelaunayTriangulator triangulator = new DelaunayTriangulator(vertices);

                stopwatch.start();
                triangulator.triangulate();
                stopwatch.stop();
            }
            result.put(n, stopwatch.elapsed(TimeUnit.MILLISECONDS));
            stopwatch.reset();
        }
        result.forEach((key, value) -> System.out.printf("%d, %d%n", key, value));
        // Looks like time goes up quadratically! Maybe because for every point that is inserted all triangles are searched.
        // Maybe a tree would be smarter, starting with the super triangle.
    }

}
