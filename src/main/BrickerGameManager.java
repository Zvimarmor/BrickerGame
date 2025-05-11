package main;

import brick_strategies.BrickFactory;
import brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;
import gameobjects.*;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Main class that manages the Bricker game.
 * Responsible for initializing game objects and game logic.
 *
 * <p>Includes paddle, ball, hearts panel, bricks, boundaries, and game state logic.</p>
 *
 * @author
 */
public class BrickerGameManager extends GameManager {
	// === Constants ===
	private final int BORDER_WIDTH = Constants.BORDER_WIDTH;
	private final float BALL_SPEED = Constants.BALL_SPEED;
	private final int MAX_LIFE_NUM = Constants.MAX_LIFE_NUM;

	// === Life tracking ===
	private int CUR_LIFE_NUM = Constants.CUR_LIFE_NUM;
	private int IN_LIFE_NUM = Constants.IN_LIFE_NUM;

	// === Brick grid dimensions ===
	private int rowBricksNum;
	private int colBricksNum;

	private Vector2 windowDimensions;

	// === Game dependencies ===
	private ImageReader imageReader;
	private SoundReader soundReader;
	private WindowController windowController;
	private UserInputListener userInputListener;

	// === Game objects ===
	private Ball ball;
	private GameObject[] currentHeartsObjects = new GameObject[0];
	private HeartsPanel heartsPanel;
	private Counter BRICKS_NUM;

	/**
	 * Constructs the Bricker game manager with the given game configuration.
	 *
	 * @param windowTitle      The title of the game window.
	 * @param windowDimensions The dimensions of the window.
	 * @param rowBricksNum     Number of brick rows.
	 * @param colBricksNum     Number of brick columns.
	 */
	public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
							  int rowBricksNum, int colBricksNum) {
		super(windowTitle, windowDimensions);
		this.windowDimensions = windowDimensions;
		this.rowBricksNum = rowBricksNum;
		this.colBricksNum = colBricksNum;
		this.BRICKS_NUM = new Counter(colBricksNum * rowBricksNum);
	}

	/**
	 * Initializes the game window and all game objects.
	 */
	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader,
							   UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		this.imageReader = imageReader;
		this.soundReader = soundReader;
		this.windowController = windowController;
		this.userInputListener = inputListener;

		createBackground();
		createHeartsPanel();
		createBall();
		createPaddle();
		createBoundaries();
		createBricks();
	}

	/**
	 * Creates the hearts panel representing player lives.
	 */
	private void createHeartsPanel() {
		heartsPanel = new HeartsPanel(imageReader, IN_LIFE_NUM, gameObjects());
		heartsPanel.setTag(Constants.HEART_PANEL_TAG);
	}

	/**
	 * Adds the static background image to the game.
	 */
	private void createBackground() {
		Renderable backgroundImage = imageReader.readImage(Constants.BACKGROUND_IMAGE_PATH, false);
		GameObject background = new GameObject(
				new Vector2(0, 0),
				windowController.getWindowDimensions(),
				backgroundImage);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		background.setTag(Constants.BACKGROUND_TAG);
		gameObjects().addGameObject(background, Layer.BACKGROUND);
	}

	/**
	 * Initializes the main ball and adds it to the game.
	 */
	private void createBall() {
		Renderable ballImage = imageReader.readImage(Constants.BALL_IMAGE_PATH, true);
		Sound collisionSound = soundReader.readSound(Constants.COLLISION_SOUND_PATH);
		ball = new Ball(new Vector2(0, 0), Constants.ballDimensions, ballImage, collisionSound);
		ball.setCenter(windowDimensions.mult(0.5f));
		ball.setTag(Constants.MAIN_BALL_TAG);
		gameObjects().addGameObject(ball, Layer.DEFAULT);

		Random rand = new Random();
		float ballVelX = BALL_SPEED * (rand.nextBoolean() ? -1 : 1);
		float ballVelY = BALL_SPEED * (rand.nextBoolean() ? -1 : 1);
		ball.setVelocity(new Vector2(ballVelX, ballVelY));
	}

	/**
	 * Creates the paddle controlled by the player.
	 */
	private void createPaddle() {
		Renderable paddleImage = imageReader.readImage(Constants.PADDLE_IMAGE_PATH, true);
		GameObject paddle = new Paddle(
				new Vector2(0, 0),
				Constants.paddleDimensions,
				paddleImage,
				userInputListener);
		paddle.setCenter(new Vector2(
				windowDimensions.x() / 2,
				windowDimensions.y() - Constants.PADDLE_OFFSET_Y));
		paddle.setTag(Constants.MAIN_PADDLE_TAG);
		gameObjects().addGameObject(paddle, Layer.DEFAULT);
	}

	/**
	 * Adds invisible boundaries to the game to prevent objects from leaving the screen.
	 */
	private void createBoundaries() {
		GameObject rightBoundary = new GameObject(
				new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
				new Vector2(BORDER_WIDTH, windowDimensions.y()),
				null);
		rightBoundary.setTag(Constants.BOUNDARY_TAG);
		gameObjects().addGameObject(rightBoundary, Layer.STATIC_OBJECTS);

		GameObject leftBoundary = new GameObject(
				new Vector2(0, 0),
				new Vector2(BORDER_WIDTH, windowDimensions.y()),
				null);
		leftBoundary.setTag(Constants.BOUNDARY_TAG);
		gameObjects().addGameObject(leftBoundary, Layer.STATIC_OBJECTS);

		GameObject topBoundary = new GameObject(
				new Vector2(0, 0),
				new Vector2(windowDimensions.x(), BORDER_WIDTH),
				null);
		topBoundary.setTag(Constants.BOUNDARY_TAG);
		gameObjects().addGameObject(topBoundary, Layer.STATIC_OBJECTS);
	}

	/**
	 * Generates a grid of bricks with a collision strategy.
	 */
	private void createBricks() {
		Renderable brickImage = imageReader.readImage(Constants.BRICK_IMAGE_PATH, false);

		float spacing = Constants.SPACING;
		float brickHeight = Constants.BRICK_HEIGHT;
		float availableWidth = windowDimensions.x() - 2 * BORDER_WIDTH;
		float brickWidth = (availableWidth - (colBricksNum - 1) * spacing) / colBricksNum;

		BrickFactory factory = new BrickFactory(
				gameObjects(), BRICKS_NUM, imageReader, soundReader, userInputListener, heartsPanel);

		for (int row = 0; row < rowBricksNum; row++) {
			for (int col = 0; col < colBricksNum; col++) {
				CollisionStrategy strategy = factory.getStrategy();
				Vector2 topLeft = new Vector2(
						BORDER_WIDTH + col * (brickWidth + spacing),
						BORDER_WIDTH + row * (brickHeight + spacing));
				Brick brick = new Brick(topLeft,
						new Vector2(brickWidth, brickHeight),
						brickImage,
						strategy);
				brick.setTag(Constants.BRICK_TAG);
				gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
			}
		}
	}

	/**
	 * Checks whether the game has ended and restarts or closes the window.
	 */
	public void checkEndGame() {
		if (BRICKS_NUM.value() == 0 || userInputListener.isKeyPressed(KeyEvent.VK_W)) {
			if (windowController.openYesNoDialog(Constants.WIN_MESSAGE)) {
				CUR_LIFE_NUM = IN_LIFE_NUM;
				BRICKS_NUM = new Counter(rowBricksNum * colBricksNum);
				windowController.resetGame();
			} else {
				windowController.closeWindow();
			}
		} else if (ball.getCenter().y() > windowDimensions.y()) {
			heartsPanel.removeHeart(gameObjects());
			CUR_LIFE_NUM = heartsPanel.getLifeNum();
			createBall();
			if (CUR_LIFE_NUM == 0) {
				if (windowController.openYesNoDialog(Constants.LOSE_MESSAGE)) {
					CUR_LIFE_NUM = IN_LIFE_NUM;
					windowController.resetGame();
				} else {
					windowController.closeWindow();
				}
			}
		}
	}

	/**
	 * Called once per frame. Updates game state.
	 *
	 * @param delta Time since last frame.
	 */
	@Override
	public void update(float delta) {
		super.update(delta);
		checkEndGame();
	}

	/**
	 * Entry point. Launches the game with optional arguments for brick dimensions.
	 *
	 * @param args Command line arguments for brick rows and columns.
	 */
	public static void main(String[] args) {
		int rowBricksNum = Constants.ROW_BRICKS_NUM;
		int colBricksNum = Constants.COL_BRICKS_NUM;
		if (args.length == 2) {
			colBricksNum = Integer.parseInt(args[0]);
			rowBricksNum = Integer.parseInt(args[1]);
		}
		new BrickerGameManager(
				Constants.GAME_TITLE,
				Constants.windowDimensions,
				rowBricksNum,
				colBricksNum).run();
	}
}
