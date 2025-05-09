// Constants.java
package main;

import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Constants {
    public static final Vector2 windowDimensions   = new Vector2(700, 500);
    public static final int BORDER_WIDTH           = 3;
    public static final float BALL_SPEED           = 150f;
    public static final float MOVEMENT_SPEED       = 300f;
    public static final Vector2 paddleDimensions   = new Vector2(100, 15);

    // HeartsPanel
    public static final int MAX_LIFE_NUM           = 3;
    public static int LIFE_NUM                     = 3;

    // Bricks grid defaults
    public static final int ROW_BRICKS_NUM         = 7;
    public static final int COL_BRICKS_NUM         = 8;
    public static final float SPACING              = 5f;
    public static final float BRICK_HEIGHT         = 15f;

    // Ball dimensions
    public static final Vector2 ballDimensions     = new Vector2(20, 20);

    // Strategy selection
    public static final int STRATEGY_ROLL_BOUND    = 10;

    // PaddleDuplicator strategy
    public static final String PADDLE_IMAGE_PATH               = "assets/paddle.png";
    public static final int MAX_HITS_PADDLE_DUPLICATOR         = 4;

    // for extra uses
    public static final Vector2 panelTopLeft       = new Vector2(windowDimensions.x() - 200, 10);
    public static final Vector2 panelSize          = new Vector2(200, 40);
}