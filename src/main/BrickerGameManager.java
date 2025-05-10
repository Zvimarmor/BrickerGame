package main;

import brick_strategies.BrickFactory;
import brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;
import gameobjects.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Main class that manages the Bricker game.
 * Responsible for initializing game objects and game logic.
 */
public class BrickerGameManager extends GameManager {
	// Final Constants
	private final int BORDER_WIDTH   = Constants.BORDER_WIDTH;
	private final float BALL_SPEED   = Constants.BALL_SPEED;
	private final int MAX_LIFE_NUM   = Constants.MAX_LIFE_NUM;
	private int CUR_LIFE_NUM = Constants.CUR_LIFE_NUM;
	private int IN_LIFE_NUM = Constants.IN_LIFE_NUM;

	// Brick grid dimensions
	private int rowBricksNum;
	private int colBricksNum;
	private Vector2 windowDimensions;

	// Game dependencies
	private ImageReader imageReader;
	private SoundReader soundReader;
	private WindowController windowController;
	private UserInputListener userInputListener;

	// Game objects
	private Ball ball;
	private GameObject[] currentHeartsObjects = new GameObject[0];
	private HeartsPanel heartsPanel;
	private Counter BRICKS_NUM;

	/**
	 * Constructor for the game manager.
	 * @param windowTitle The title of the game window.
	 * @param windowDimensions The size of the game window.
	 * @param rowBricksNum Number of rows of bricks.
	 * @param colBricksNum Number of columns of bricks.
	 */
	public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
							  int rowBricksNum, int colBricksNum) {
		super(windowTitle, windowDimensions);
		this.windowDimensions = windowDimensions;
		this.rowBricksNum = rowBricksNum;
		this.colBricksNum = colBricksNum;
		this.BRICKS_NUM = new Counter(colBricksNum * rowBricksNum);
	}

	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader,
							   UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		this.imageReader       = imageReader;
		this.soundReader       = soundReader;
		this.windowController  = windowController;
		this.userInputListener = inputListener;

		createBackground(imageReader, windowController);
		createHeartsPanel();
		createBall(imageReader, soundReader);
		createPaddle(imageReader, inputListener);
		createBoundaries();
		createBricks(imageReader);
	}

	private void createHeartsPanel() {
		heartsPanel = new HeartsPanel(imageReader, IN_LIFE_NUM,gameObjects());
		heartsPanel.setTag("Heart Panel");
	}

	private void createBackground(ImageReader imageReader, WindowController windowController) {
		Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
		GameObject background = new GameObject(
				new Vector2(0, 0),
				windowController.getWindowDimensions(),
				backgroundImage);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		background.setTag("Background");
		gameObjects().addGameObject(background, Layer.BACKGROUND);
	}

	private void createBall(ImageReader imageReader, SoundReader soundReader) {
		Renderable ballImage = imageReader.readImage("assets/ball.png", true);
		Sound collisionSound = soundReader.readSound("assets/blop.wav");
		ball = new Ball(new Vector2(0, 0), main.Constants.ballDimensions, ballImage, collisionSound);
		ball.setCenter(windowDimensions.mult(0.5f));
		ball.setTag("Main_Ball");
		gameObjects().addGameObject(ball, Layer.DEFAULT);

		float ballVelX = BALL_SPEED;
		float ballVelY = BALL_SPEED;
		Random rand = new Random();
		if (rand.nextBoolean()) ballVelX *= -1;
		if (rand.nextBoolean()) ballVelY *= -1;
		ball.setVelocity(new Vector2(ballVelX, ballVelY));
	}

	private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
		Renderable paddleImage = imageReader.readImage(Constants.PADDLE_IMAGE_PATH, true);
		GameObject paddle = new Paddle(
				new Vector2(0, 0),
				Constants.paddleDimensions,
				paddleImage,
				inputListener
		);
		paddle.setCenter(new Vector2(
				windowDimensions.x() / 2,
				windowDimensions.y() - 30));
		paddle.setTag("MainPaddle");
		gameObjects().addGameObject(paddle, Layer.DEFAULT);
	}

	private void createBoundaries() {
		GameObject rightBoundary = new GameObject(
				new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
				new Vector2(BORDER_WIDTH, windowDimensions.y()),
				null);
		rightBoundary.setTag("Boundary");
		gameObjects().addGameObject(rightBoundary, Layer.STATIC_OBJECTS);

		GameObject leftBoundary = new GameObject(
				new Vector2(0, 0),
				new Vector2(BORDER_WIDTH, windowDimensions.y()),
				null);
		rightBoundary.setTag("Boundary");
		gameObjects().addGameObject(leftBoundary, Layer.STATIC_OBJECTS);

		GameObject topBoundary = new GameObject(
				new Vector2(0, 0),
				new Vector2(windowDimensions.x(), BORDER_WIDTH),
				null);
		rightBoundary.setTag("Boundary");
		gameObjects().addGameObject(topBoundary, Layer.STATIC_OBJECTS);
	}

	private void createBricks(ImageReader imageReader) {
		Renderable brickImage = imageReader.readImage("assets/brick.png", false);

		float spacing     = Constants.SPACING;
		float brickHeight = Constants.BRICK_HEIGHT;
		float availableWidth = windowDimensions.x() - 2 * BORDER_WIDTH;
		float brickWidth     = (availableWidth - (colBricksNum - 1) * spacing) / colBricksNum;
		BrickFactory factory = new BrickFactory(gameObjects(), BRICKS_NUM, imageReader, soundReader,userInputListener, heartsPanel);

		float startX = BORDER_WIDTH;
		float startY = BORDER_WIDTH;

		for (int row = 0; row < rowBricksNum; row++) {
			for (int col = 0; col < colBricksNum; col++) {
				CollisionStrategy collisionStrategy = factory.getStrategy();
				Vector2 topLeft = new Vector2(
						startX + col * (brickWidth + spacing),
						startY + row * (brickHeight + spacing)
				);
				Brick brick = new Brick(
						topLeft,
						new Vector2(brickWidth, brickHeight),
						brickImage,
						collisionStrategy
				);
				brick.setTag("Brick");
				gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
			}
		}
	}

	public void checkEndGame() {
		if (BRICKS_NUM.value() == 0 || userInputListener.isKeyPressed(KeyEvent.VK_W)) {
			if (windowController.openYesNoDialog("You win! Play again?")) {
				CUR_LIFE_NUM = IN_LIFE_NUM;
				BRICKS_NUM = new Counter(rowBricksNum * colBricksNum);
				windowController.resetGame();
				return;
			} else windowController.closeWindow();
		}

		else if (ball.getCenter().y() > windowDimensions.y()) {
			heartsPanel.removeHeart(gameObjects());
			CUR_LIFE_NUM = heartsPanel.getLifeNum();
			createBall(imageReader, soundReader);
			if (CUR_LIFE_NUM == 0) {
				if (windowController.openYesNoDialog("You lose! Play again?")) {
					CUR_LIFE_NUM = IN_LIFE_NUM;
					windowController.resetGame();
				} else windowController.closeWindow();
			}
		}
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		checkEndGame();
	}

	public static void main(String[] args) {
		int rowBricksNum = Constants.ROW_BRICKS_NUM;
		int colBricksNum = Constants.COL_BRICKS_NUM;
		if (args.length == 2) {
			colBricksNum = Integer.parseInt(args[0]);
			rowBricksNum = Integer.parseInt(args[1]);
		}
		new BrickerGameManager(
				"Bricker Game",
				Constants.windowDimensions,
				rowBricksNum,
				colBricksNum
		).run();
	}
}
