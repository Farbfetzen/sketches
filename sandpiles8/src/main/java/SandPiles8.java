// https://en.wikipedia.org/wiki/Abelian_sandpile_model
// https://www.youtube.com/watch?v=1MtEUErz7Gg

import processing.core.PApplet;

public class SandPiles8 extends PApplet {

    private static final int N_SAND = 500_000;
    private static final int SQUARE_SIZE = 2;
    private static final int SQUARE_SIZE_TWICE = SQUARE_SIZE * 2;
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
    private int pileHeightMinus1;
    private int pileWidthMinus2;
    private int pileHeightMinus2;

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
        pileWidth = width / SQUARE_SIZE_TWICE;
        pileHeight = height / SQUARE_SIZE_TWICE;
        pileWidthMinus1 = pileWidth - 1;
        pileWidthPlus1 = pileWidth + 1;
        pileHeightMinus1 = pileHeight - 1;
        pileWidthMinus2 = pileWidth - 2;
        pileHeightMinus2 = pileHeight - 2;
        pile = new int[pileWidth * pileHeight];
        pile[pile.length - 1] = N_SAND;
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
                final int n = pile[xyToPileIndex(x, y)];
                fill(n < 8 ? colors[n] : color(255));
                final int squareX = x * SQUARE_SIZE;
                final int squareY = y * SQUARE_SIZE;
                final int rightX = width - squareX - SQUARE_SIZE_TWICE;
                final int bottomY = height - squareY - SQUARE_SIZE_TWICE;
                square(squareX, squareY, SQUARE_SIZE);  // top left quadrant
                square(rightX, squareY, SQUARE_SIZE);  // top right quadrant
                square(squareX, bottomY, SQUARE_SIZE);  // bottom left quadrant
                square(rightX, bottomY, SQUARE_SIZE);  // bottom right quadrant
            }
        }
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            animateFast = !animateFast;
        } else if (key == RETURN || key == ENTER) {
            animate = false;
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
                    final int nToDistributeTwice = nToDistribute + nToDistribute;
                    nextPile[i] -= n - n % 8;

                    final boolean leftOk = x > 0;
                    final boolean topOk = y > 0;
                    final boolean rightOk = x < pileWidthMinus1;
                    final boolean bottomOk = y < pileHeightMinus1;
                    final boolean topLeftOk = topOk && leftOk;
                    final boolean topRightOk = topOk && rightOk;
                    final boolean bottomLeftOk = bottomOk && leftOk;
                    final boolean bottomRightOk = bottomOk && rightOk;
                    final boolean bleedsToTheRight = x == pileWidthMinus2;
                    final boolean bleedsToTheBottom = y == pileHeightMinus2;

                    if (leftOk) {
                        nextPile[i - 1] += nToDistribute;
                    }
                    if (topLeftOk) {
                        nextPile[i - pileWidthPlus1] += nToDistribute;
                    }
                    if (topOk) {
                        nextPile[i - pileWidth] += nToDistribute;
                    }
                    if (topRightOk) {
                        if (bleedsToTheRight) {
                            nextPile[i - pileWidthMinus1] += nToDistributeTwice;
                        } else {
                            nextPile[i - pileWidthMinus1] += nToDistribute;
                        }
                    }
                    if (rightOk) {
                        if (bleedsToTheRight) {
                            nextPile[i + 1] += nToDistributeTwice;
                        } else {
                            nextPile[i + 1] += nToDistribute;
                        }
                    }
                    if (bottomRightOk) {
                        if (bleedsToTheRight && bleedsToTheBottom) {
                            nextPile[i + pileWidthPlus1] += nToDistribute * 4;
                        } else if (bleedsToTheRight || bleedsToTheBottom) {
                            nextPile[i + pileWidthPlus1] += nToDistributeTwice;
                        } else {
                            nextPile[i + pileWidthPlus1] += nToDistribute;
                        }
                    }
                    if (bottomOk) {
                        if (bleedsToTheBottom) {
                            nextPile[i + pileWidth] += nToDistributeTwice;
                        } else {
                            nextPile[i + pileWidth] += nToDistribute;
                        }
                    }
                    if (bottomLeftOk) {
                        if (bleedsToTheBottom) {
                            nextPile[i + pileWidthMinus1] += nToDistributeTwice;
                        } else {
                            nextPile[i + pileWidthMinus1] += nToDistribute;
                        }
                    }
                }
            }
        }
        System.arraycopy(nextPile, 0, pile, 0, pile.length);
        return notFinished;
    }
}
