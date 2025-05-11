package bricker.main;

import danogl.util.Vector2;

/**
 * A class for holding constant values used throughout the Bricker game.
 * These include dimensions, speeds, asset paths, tags, and configuration values.
 * All values are final where possible and should be accessed statically.
 *
 * @author Zvi marmor and Adi Zuarets
 */
public class Constants {

    /*** Window Settings ***/
    public static final Vector2 windowDimensions = new Vector2(700, 500);
    public static final int WINDOW_HEIGHT = (int) windowDimensions.y();
    public static final int BORDER_WIDTH = 3;
    public static final String GAME_TITLE = "Bricker Game";

    /*** Ball Settings ***/
    public static final Vector2 ballDimensions = new Vector2(20, 20);
    public static final float BALL_SPEED = 150;
    public static final float MINIS_BALLS_SPEED = (BALL_SPEED / 3) * 2;
    public static final float MOVEMENT_SPEED = 2 * BALL_SPEED;
    public static final float PUCK_PROPORTION_SIZE = 0.75f;

    /*** Paddle Settings ***/
    public static final Vector2 paddleDimensions = new Vector2(100, 15);
    public static final int MAX_HITS_PADDLE_DUPLICATOR = 4;
    public static final float PADDLE_OFFSET_Y = 30f;

    /*** Hearts Panel Settings ***/
    public static final Vector2 panelTopLeft = new Vector2(windowDimensions.x() - 200, 10);
    public static final Vector2 panelSize = new Vector2(200, 40);
    public static final float PADDING = 5f;
    public static final float FALLING_HEART_SPEED = 100;
    public static final int MAX_LIFE_NUM = 5;
    public static int IN_LIFE_NUM = 3;
    public static int CUR_LIFE_NUM = IN_LIFE_NUM;
    public static float totalWidth = panelSize.x() - 2 * PADDING;
    public static float objectWidth = totalWidth / (MAX_LIFE_NUM + 1);
    public static float objectHeight = panelSize.y() - 2 * PADDING;

    /*** Brick Grid Settings ***/
    public static final int ROW_BRICKS_NUM = 7;
    public static final int COL_BRICKS_NUM = 8;
    public static final float SPACING = 5f;
    public static final float BRICK_HEIGHT = 15f;

    /*** Strategy Settings ***/
    public static final int STRATEGY_ROLL_BOUND = 6;

    /*** Turbo Mode Settings ***/
    public static final float TURBO_FACTOR = 1.4f;
    public static final int TURBO_COLLISION_LIMIT = 6;

    /*** Asset Paths ***/
    public static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    public static final String BRICK_IMAGE_PATH = "assets/brick.png";
    public static final String BALL_IMAGE_PATH = "assets/ball.png";
    public static final String RED_BALL_IMAGE_PATH = "assets/redball.png";
    public static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    public static final String HEART_IMAGE_PATH = "assets/heart.png";
    public static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    public static final String COLLISION_SOUND_PATH = "assets/blop.wav";

    /*** Tags ***/
    public static final String MAIN_BALL_TAG = "Main_Ball";
    public static final String MINI_BALL_TAG = "Mini_Ball";
    public static final String MAIN_PADDLE_TAG = "MainPaddle";
    public static final String FALLING_HEART_TAG = "FallingHeart";
    public static final String BACKGROUND_TAG = "Background";
    public static final String BRICK_TAG = "Brick";
    public static final String BOUNDARY_TAG = "Boundary";
    public static final String HEART_PANEL_TAG = "Heart Panel";

    /*** Messages ***/
    public static final String WIN_MESSAGE = "You win! Play again?";
    public static final String LOSE_MESSAGE = "You lose! Play again?";
}
