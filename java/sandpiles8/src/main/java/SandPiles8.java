// https://en.wikipedia.org/wiki/Abelian_sandpile_model
// https://www.youtube.com/watch?v=1MtEUErz7Gg

import processing.core.PApplet;

import java.util.Arrays;

public class SandPiles8 extends PApplet {

    private static final int N_SAND = 250_000;
    private static final int SQUARE_SIZE = 2;
    private static int defaultWidth = 800;
    private static int defaultHeight = 800;
    private static boolean animate = true;
    private static boolean animateFast = false;
    private final int[] colors = new int[] {
            color(10, 63, 255),
            color(60, 117, 255),
            color(111, 171, 255),
            color(164, 199, 182),
            color(218, 212, 72),
            color(236, 190, 0),
            color(179, 95, 0),
            color(123, 0, 0)
    };
    private int pileWidth;
    private int pileHeight;
    private int[] pile;
    private int[] nextPile;
    private int pileWidthMinus1;
    private int pileWidthPlus1;
    private int pileHeightPlus1;

    public static void main(final String[] args) {
        if (args.length == 2) {
            defaultWidth = Integer.parseInt(args[0]);
            defaultHeight = Integer.parseInt(args[1]);
        }
        PApplet.main(SandPiles8.class.getName());
    }

    @Override
    public void settings() {
        size(defaultWidth, defaultHeight);
    }

    @Override
    public void setup() {
        background(colors[0]);
        if (!animate) {
            noLoop();
        }
        noStroke();
        pileWidth = (int) Math.ceil((double) width / SQUARE_SIZE);
        pileHeight = (int) Math.ceil((double) height / SQUARE_SIZE);
        pileWidthMinus1 = pileWidth - 1;
        pileWidthPlus1 = pileWidth + 1;
        pileHeightPlus1 = pileHeight + 1;
        pile = new int[pileWidth * pileHeight];
        pile[pile.length / 2 - pileWidth / 2 - 1] = N_SAND;
        nextPile = pile.clone();
    }

    @Override
    public void draw() {
        if (animate) {
            topple();
            if (animateFast){
                for (int i = 0; i < 20; i++) {
                    topple();
                }
            }
        } else {
            while (topple()) { }  // Run until the pile has toppled completely.
        }

        for (int x = 0; x < pileWidth; x++) {
            for (int y = 0; y < pileHeight; y++) {
                final int i = xyToPileIndex(x, y);
                final int n = pile[i];
                fill(n < 8 ? colors[n] : color(255));
                final int squareX = x * SQUARE_SIZE;
                final int squareY = y * SQUARE_SIZE;
                square(squareX, squareY, SQUARE_SIZE);
            }
        }
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            animateFast = !animateFast;
        } else if (key == RETURN || key == ENTER) {
            animate = false;
        } else if (key == 'p') {  // DEBUG
            System.out.println(Arrays.stream(pile).sum());
        }
    }

    private int xyToPileIndex(final int x, final int y) {
        return x + y * pileWidth;
    }

    private boolean topple() {
        boolean notFinished = false;
        for (int x = 0; x < pileWidth; x++) {
            for (int y = 0; y < pileHeight; y++) {
                final int i = xyToPileIndex(x, y);
                final int n = pile[i];
                if (n >= 8) {
                    notFinished = true;
                    final int nToDistribute = n / 8;
                    nextPile[i] -= n - n % 8;

                    final boolean leftOk = x > 0;
                    final boolean topOk = y > 0;
                    final boolean rightOk = x < pileWidthMinus1;
                    final boolean bottomOk = y < pileHeightPlus1;
                    final boolean topLeftOk = topOk && leftOk;
                    final boolean topRightOk = topOk && rightOk;
                    final boolean bottomLeftOk = bottomOk && leftOk;
                    final boolean bottomRightOk = bottomOk && rightOk;

                    if (leftOk) {
                        nextPile[i - 1] += nToDistribute;
                    }
                    if (topOk) {
                        nextPile[i - pileWidth] += nToDistribute;
                    }
                    if (rightOk) {
                        nextPile[i + 1] += nToDistribute;
                    }
                    if (bottomOk) {
                        nextPile[i + pileWidth] += nToDistribute;
                    }
                    if (topLeftOk) {
                        nextPile[i - pileWidthMinus1] += nToDistribute;
                    }
                    if (topRightOk) {
                        nextPile[i - pileWidthPlus1] += nToDistribute;
                    }
                    if (bottomLeftOk) {
                        nextPile[i + pileWidthMinus1] += nToDistribute;
                    }
                    if (bottomRightOk) {
                        nextPile[i + pileWidthPlus1] += nToDistribute;
                    }
                }
            }
        }
        System.arraycopy(nextPile, 0, pile, 0, pile.length);
        return notFinished;
    }
}
