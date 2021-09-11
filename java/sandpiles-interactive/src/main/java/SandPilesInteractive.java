// https://en.wikipedia.org/wiki/Abelian_sandpile_model
// https://www.youtube.com/watch?v=1MtEUErz7Gg

import processing.core.PApplet;

public class SandPilesInteractive extends PApplet {

    private static int defaultWidth = 800;
    private static int defaultHeight = 800;

    private static final float N_SAND_MIN = 40f;
    private static final float N_SAND_MAX = 4_001f;
    private static final int SQUARE_SIZE = 3;
    private final int[] colors = new int[] {
            color(10, 63, 255),
            color(128, 190, 255),
            color(255, 222, 0),
            color(123, 0, 0)
    };
    private static boolean animateFast = false;
    private int pileWidth;
    private int pileHeight;
    private int[] pile;
    private int[] nextPile;

    public static void main(final String[] args) {
        if (args.length == 2) {
            defaultWidth = Integer.parseInt(args[0]);
            defaultHeight = Integer.parseInt(args[1]);
        }
        PApplet.main(SandPilesInteractive.class.getName());
    }

    @Override
    public void settings() {
        size(defaultWidth, defaultHeight);
    }

    @Override
    public void setup() {
        background(colors[0]);
        noStroke();
        pileWidth = (int) Math.ceil((double) width / SQUARE_SIZE);
        pileHeight = (int) Math.ceil((double) height / SQUARE_SIZE);
        pile = new int[pileWidth * pileHeight];
        nextPile = pile.clone();
    }

    @Override
    public void draw() {
        if (animateFast) {
            for (int i = 0; i < 10; i++) {
                topple();
            }
        } else {
            topple();
        }

        for (int x = 0; x < pileWidth; x++) {
            for (int y = 0; y < pileHeight; y++) {
                final int i = xyToPileIndex(x, y);
                final int n = pile[i];
                fill(n < 4 ? colors[n] : color(255));
                final int squareX = x * SQUARE_SIZE;
                final int squareY = y * SQUARE_SIZE;
                square(squareX, squareY, SQUARE_SIZE);
            }
        }
    }

    @Override
    public void mousePressed() {
        final int pileX = mouseX / SQUARE_SIZE;
        final int pileY = mouseY / SQUARE_SIZE;
        final int i = xyToPileIndex(pileX, pileY);
        final int n = (int) random(N_SAND_MIN, N_SAND_MAX);
        pile[i] += n;
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            animateFast = !animateFast;
        }
    }

    private int xyToPileIndex(final int x, final int y) {
        return x + y * pileWidth;
    }

    private void topple() {
        for (int x = 0; x < pileWidth; x++) {
            for (int y = 0; y < pileHeight; y++) {
                final int i = xyToPileIndex(x, y);
                final int n = pile[i];
                if (n >= 4) {
                    final int nToDistribute = n / 4;
                    nextPile[i] = n % 4;

                    // left neighbor
                    if (x > 0) {
                        nextPile[i - 1] += nToDistribute;
                    }

                    // right neighbor
                    if (x < pileWidth - 1) {
                        nextPile[i + 1] += nToDistribute;
                    }

                    // top neighbor
                    if (y > 0) {
                        nextPile[i - pileWidth] += nToDistribute;
                    }

                    // bottom neighbor
                    if (y < pileHeight - 1) {
                        nextPile[i + pileWidth] += nToDistribute;
                    }
                }
            }
        }
        System.arraycopy(nextPile, 0, pile, 0, pile.length);
    }
}
