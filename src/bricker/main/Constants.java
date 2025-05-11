package bricker.main;

import danogl.util.Vector2;

/**
 * A class for holding constant values used throughout the Bricker game.
 * These include dimensions, speeds, asset paths, object tags, and configuration values.
 * All fields are static and final when applicable, and should be accessed statically.
 * The constants are grouped by functionality: window setup, game object configuration,
 * asset paths, and game-specific logic parameters.
 *
 * Authors: Zvi Marmor and Adi Zuarets
 */
public class Constants {

    /*** Window Settings ***/

    /** Dimensions of the game window (width x height). */
    public static final Vector2 windowDimensions = new Vector2(700, 500);

    /** Height of the game window in pixels. */
    public static final int WINDOW_HEIGHT = (int) windowDimensions.y();

    /** Width in pixels of the invisible borders surrounding the game area. */
    public static final int BORDER_WIDTH = 50;

    /** Title of the game window. */
    public static final String GAME_TITLE = "Bricker Game";

    /*** Ball Settings ***/

    /** Dimensions (width x height) of the main ball. */
    public static final Vector2 ballDimensions = new Vector2(20, 20);

    /** Default speed of the main ball in pixels per second. */
    public static final float BALL_SPEED = 150;

    /** Speed of the smaller "puck" balls (2/3 of main ball speed) . */
    public static final float MINIS_BALLS_SPEED = (BALL_SPEED / 3) * 2;

    /** Default movement speed (used by some objects like paddle). */
    public static final float MOVEMENT_SPEED = 2 * BALL_SPEED;

    /** Scaling factor applied to puck ball size (relative to main ball). */
    public static final float PUCK_PROPORTION_SIZE = 0.75f;

    /*** Paddle Settings ***/

    /** Dimensions of the paddle (width x height). */
    public static final Vector2 paddleDimensions = new Vector2(100, 15);

    /** Number of allowed hits for a duplicate paddle before disappearing. */
    public static final int MAX_HITS_PADDLE_DUPLICATOR = 4;

    /** Vertical offset from bottom of screen where paddle is positioned. */
    public static final float PADDLE_OFFSET_Y = 30f;

    /*** Hearts Panel Settings ***/

    /** Top-left position of the hearts panel on the screen. */
    public static final Vector2 panelTopLeft = new Vector2(windowDimensions.x() - 200, 10);

    /** Total size (width x height) of the panel that displays player lives. */
    public static final Vector2 panelSize = new Vector2(200, 40);

    /** Padding around heart icons and text inside the panel. */
    public static final float PADDING = 5f;

    /** Speed at which falling hearts descend (pixels per second). */
    public static final float FALLING_HEART_SPEED = 100;

    /** Maximum number of lives the player can have. */
    public static final int MAX_LIFE_NUM = 4;

    /** Initial number of lives at the start of the game. */
    public static int IN_LIFE_NUM = 3;

    /** Current number of player lives. This value is updated at runtime. */
    public static int CUR_LIFE_NUM = IN_LIFE_NUM;

    /** Total available width in the panel (excluding padding). */
    public static float totalWidth = panelSize.x() - PADDING;

    /** Width of each individual heart icon in the panel. */
    public static float objectWidth = totalWidth / (MAX_LIFE_NUM + 1);

    /** Height of heart icons after padding is removed. */
    public static float objectHeight = panelSize.y() - PADDING;

    /** Constant to make the falling heart apear in the middle of the broke brick. */
    public static float HeartPosPositionDiscounting = 0.5f;

    /*** Brick Grid Settings ***/

    /** Number of brick rows in the initial game grid. */
    public static final int ROW_BRICKS_NUM = 7;

    /** Number of brick columns in the initial game grid. */
    public static final int COL_BRICKS_NUM = 8;

    /** Horizontal and vertical spacing between bricks. */
    public static final float SPACING = 5f;

    /** Height of each individual brick in the grid. */
    public static final float BRICK_HEIGHT = 15f;

    /*** Strategy Settings ***/

    /** Max number (exclusive) for random selection of strategies. */
    public static final int STRATEGY_ROLL_BOUND = 10;

    /** Number of basic strategies available non-double strategies. */
    public static final int DOUBLE_STRATEGY_INDEX_BOUND = 4;

    /** Number of basic strategies available with double strategies. */
    public static final int DOUBLE_STRATEGY_INDEX_BOUND_WITH = DOUBLE_STRATEGY_INDEX_BOUND + 1;

    /*** Turbo Mode Settings ***/

    /** Speed multiplier applied when turbo mode is activated. */
    public static final float TURBO_FACTOR = 1.4f;

    /** Number of collisions after which turbo mode ends. */
    public static final int TURBO_COLLISION_LIMIT = 6;

    /*** Asset Paths ***/

    /** File path for the paddle image. */
    public static final String PADDLE_IMAGE_PATH = "assets/paddle.png";

    /** File path for the brick image. */
    public static final String BRICK_IMAGE_PATH = "assets/brick.png";

    /** File path for the default ball image. */
    public static final String BALL_IMAGE_PATH = "assets/ball.png";

    /** File path for the red (turbo) ball image. */
    public static final String RED_BALL_IMAGE_PATH = "assets/redball.png";

    /** File path for puck (small ball) image. */
    public static final String PUCK_IMAGE_PATH = "assets/mockBall.png";

    /** File path for heart icon image. */
    public static final String HEART_IMAGE_PATH = "assets/heart.png";

    /** File path for background image. */
    public static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";

    /** File path for collision sound effect. */
    public static final String COLLISION_SOUND_PATH = "assets/blop.wav";

    /*** Tags ***/

    /** Tag used to identify the main ball. */
    public static final String MAIN_BALL_TAG = "Main_Ball";

    /** Tag used to identify mini balls (pucks). */
    public static final String MINI_BALL_TAG = "Mini_Ball";

    /** Tag used to identify the main paddle. */
    public static final String MAIN_PADDLE_TAG = "MainPaddle";

    /** Tag used to identify falling heart objects. */
    public static final String FALLING_HEART_TAG = "FallingHeart";

    /** Tag used to identify the background object. */
    public static final String BACKGROUND_TAG = "Background";

    /** Tag used to identify brick objects. */
    public static final String BRICK_TAG = "Brick";

    /** Tag used to identify boundary walls. */
    public static final String BOUNDARY_TAG = "Boundary";

    /** Tag used to identify the UI panel showing lives. */
    public static final String HEART_PANEL_TAG = "Heart Panel";

    /*** Messages ***/

    /** Message shown to the player upon winning. */
    public static final String WIN_MESSAGE = "You win! Play again?";

    /** Message shown to the player upon losing. */
    public static final String LOSE_MESSAGE = "You lose! Play again?";
}