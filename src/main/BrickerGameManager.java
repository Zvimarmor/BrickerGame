package main;

import brick_strategies.BrickFactory;
import brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import danogl.util.Counter;
import gameobjects.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.Constants;

/**
 * Main class that manages the Bricker game.
 * Responsible for initializing game objects and game logic.
 */
public class BrickerGameManager extends GameManager {
	// Final Constants
	private int BORDER_WIDTH = Constants.BORDER_WIDTH;// Width of the screen borders
	private float BALL_SPEED = Constants.BALL_SPEED;// Initial speed of the ball
	private int MAX_LIFE_NUM = Constants.MAX_LIFE_NUM;
	private int LIFE_NUM = Constants.LIFE_NUM; // Number of player lives

	// get in the constructor
	private int rowBricksNum; // Number of brick rows
	private int colBricksNum; // Number of brick columns
	private Vector2 windowDimensions; // Window dimensions

	// initializeGame - game parameters
	private ImageReader imageReader;
	private SoundReader soundReader;
	private WindowController windowController;
	private UserInputListener userInputListener;
	private Ball ball;                      // Ball instance
	private List<GameObject> currentHeartsObjects = new ArrayList<>();
	private HeartsPanel heartsPanel;


	private danogl.util.Counter BRICKS_NUM;             // Number of bricks remaining

	/**
	 * Constructor for the game manager.
	 * @param windowTitle The title of the game window.
	 * @param windowDimensions The size of the game window.
	 * @param rowBricksNum Number of rows of bricks.
	 * @param colBricksNum Number of columns of bricks.
	 */
	public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int rowBricksNum, int colBricksNum) {
		super(windowTitle, windowDimensions);
		this.windowDimensions = windowDimensions;
		this.rowBricksNum = rowBricksNum;
		this.colBricksNum = colBricksNum;
		BRICKS_NUM = new danogl.util.Counter(colBricksNum * rowBricksNum);
	}

	/**
	 * Initializes the game — creates all game objects.
	 */
	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader,
							   UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		this.imageReader = imageReader;
		this.soundReader = soundReader;
		this.windowController = windowController;
		this.userInputListener = inputListener;

		createBackground(imageReader, windowController);  // Background image
		createHeartsPanel();
		createBall(imageReader, soundReader);             // Ball object
		createPaddle(imageReader, inputListener);         // Paddle object
		createBoundaries();                               // Invisible screen boundaries
		createBricks(imageReader);                        // Bricks grid
	}

	/**
	 * Adds a heart panel to show the life the user have.
	 */
	private void createHeartsPanel() {
		// remove the current hearts panel if exists
		for (GameObject obj : currentHeartsObjects) {
			gameObjects().removeGameObject(obj, Layer.UI);
		}
		currentHeartsObjects.clear();

		//creates the new panel
		heartsPanel = new HeartsPanel(imageReader, LIFE_NUM);
		gameObjects().addGameObject(heartsPanel, Layer.UI);
		currentHeartsObjects.add(heartsPanel);

		gameObjects().addGameObject(heartsPanel.getTextObject(), Layer.UI);
		currentHeartsObjects.add(heartsPanel.getTextObject());

		for (GameObject heart : heartsPanel.getHeartObjects()) {
			gameObjects().addGameObject(heart, Layer.UI);
			currentHeartsObjects.add(heart);
		}
	}

//	private void createHeartsPanel(ImageReader imageReader) {
//		// remove current panel if exist
//		for (GameObject obj : currentHeartsObjects) {
//			gameObjects().removeGameObject(obj, Layer.UI);
//		}
//		currentHeartsObjects.clear();
//
//		// init the graphic parameters for the panel of hearts
//		Vector2 panelTopLeft = new Vector2(windowDimensions.x() - 200, 10);
//		Vector2 panelSize = new Vector2(200, 30);
//		Renderable heartImage = imageReader.readImage("assets/heart.png", true);
//
//		float totalWidth = panelSize.x();
//		float totalHeight = panelSize.y();
//		float objectWidth = totalWidth / (MAX_LIFE_NUM + 1);
//
//		// init the text visualization
//		TextRenderable textRenderable = new TextRenderable(Integer.toString(LIFE_NUM));
//		if (LIFE_NUM >= 3) textRenderable.setColor(Color.green);
//		else if (LIFE_NUM == 2) textRenderable.setColor(Color.yellow);
//		else textRenderable.setColor(Color.red);
//
//		GameObject textObject = new GameObject(
//				panelTopLeft,
//				new Vector2(objectWidth, totalHeight),
//				textRenderable
//		);
//		gameObjects().addGameObject(textObject, Layer.UI);
//		currentHeartsObjects.add(textObject);
//
//		// creates the hearts
//		for (int i = 0; i < LIFE_NUM; i++) {
//			float xPos = panelTopLeft.x() + objectWidth + i * objectWidth;
//			GameObject heart = new GameObject(
//					new Vector2(xPos, panelTopLeft.y()),
//					new Vector2(objectWidth, totalHeight),
//					heartImage
//			);
//			gameObjects().addGameObject(heart, Layer.UI);
//			currentHeartsObjects.add(heart);
//		}
//	}

	/**
	 * Adds a background image stretched to cover the window.
	 */
	private void createBackground(ImageReader imageReader, WindowController windowController) {
		Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
		GameObject background = new GameObject(
				new Vector2(0, 0),
				windowController.getWindowDimensions(),
				backgroundImage
		);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		gameObjects().addGameObject(background, Layer.BACKGROUND);
	}

	/**
	 * Creates a ball in the center of the screen with random initial velocity direction.
	 */
	private void createBall(ImageReader imageReader, SoundReader soundReader) {
		Renderable ballImage = imageReader.readImage("assets/ball.png", true);
		Sound collisionSound = soundReader.readSound("assets/blop.wav");
		ball = new Ball(new Vector2(0, 0), new Vector2(20, 20), ballImage, collisionSound);
		ball.setCenter(windowDimensions.mult(0.5f));
		this.gameObjects().addGameObject(ball, Layer.DEFAULT);

		// Set random initial velocity direction
		float ballVelX = BALL_SPEED;
		float ballVelY = BALL_SPEED;
		Random rand = new Random();
		if (rand.nextBoolean()) {
			ballVelX *= -1;
		}
		if (rand.nextBoolean()) {
			ballVelY *= -1;
		}
		ball.setVelocity(new Vector2(ballVelX, ballVelY));
	}

	/**
	 * Creates the player-controlled paddle at the bottom center of the screen.
	 */
	private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
		Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
		GameObject paddle = new Paddle(new Vector2(0, 0), new Vector2(100, 15), paddleImage, inputListener);
		paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));
		gameObjects().addGameObject(paddle, Layer.DEFAULT);
	}

	/**
	 * Creates invisible borders (walls) at the top, left, and right of the screen.
	 * Prevents the ball from escaping horizontally or from the top.
	 */
	private void createBoundaries() {
		// Right wall
		GameObject rightBoundary = new GameObject(
				new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
				new Vector2(BORDER_WIDTH, windowDimensions.y()),
				null  // No image: invisible
		);
		gameObjects().addGameObject(rightBoundary, Layer.STATIC_OBJECTS);

		// Left wall
		GameObject leftBoundary = new GameObject(
				new Vector2(0, 0),
				new Vector2(BORDER_WIDTH, windowDimensions.y()),
				null
		);
		gameObjects().addGameObject(leftBoundary, Layer.STATIC_OBJECTS);

		// Top wall
		GameObject topBoundary = new GameObject(
				new Vector2(0, 0),
				new Vector2(windowDimensions.x(), BORDER_WIDTH),
				null
		);
		gameObjects().addGameObject(topBoundary, Layer.STATIC_OBJECTS);
	}

	/**
	 * Creates a grid of bricks with spacing.
	 */
	private void createBricks(ImageReader imageReader) {
		Renderable brickImage = imageReader.readImage("assets/brick.png", false);

		float spacing = 5;
		float brickHeight = 15;
		float availableWidth = windowDimensions.x() - 2 * BORDER_WIDTH;
		float brickWidth = (availableWidth - (colBricksNum - 1) * spacing) / colBricksNum;
		BrickFactory factory = new BrickFactory(gameObjects(), BRICKS_NUM, imageReader, soundReader);

		float startX = BORDER_WIDTH;
		float startY = BORDER_WIDTH;

		// Create each brick at its calculated position
		for (int row = 0; row < rowBricksNum; row++) {
			for (int col = 0; col < colBricksNum; col++) {
				CollisionStrategy collisionStrategy = factory.getStrategy();
				Vector2 brickTopLeftCorner = new Vector2(
						startX + col * (brickWidth + spacing),
						startY + row * (brickHeight + spacing)
				);
				Brick brick = new Brick(
						brickTopLeftCorner,
						new Vector2(brickWidth, brickHeight),
						brickImage,
						collisionStrategy
				);
				gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
			}
		}
	}

	/**
	 * Game update logic — checks if the ball has fallen below the screen (i.e., the player lost a life).
	 */
	@Override
	public void update(float delta) {
		super.update(delta);
		checkEndGame();
	}

	public void checkEndGame() {
		float ballHeight = ball.getCenter().y();
		String prompt = "";

		if (BRICKS_NUM.value() == 0 || userInputListener.isKeyPressed(KeyEvent.VK_W)) {
			prompt = "You win! Play again?";
			if (windowController.openYesNoDialog(prompt)) {
				LIFE_NUM = MAX_LIFE_NUM;
				BRICKS_NUM = new Counter(rowBricksNum * colBricksNum);
				windowController.resetGame();
			} else {
				windowController.closeWindow();
			}
		}

		if (ballHeight > windowDimensions.y()) {
			LIFE_NUM--;
			createHeartsPanel();
			LIFE_NUM = heartsPanel.getLifeNum();

			createBall(imageReader, soundReader);

			if (LIFE_NUM == 0) {
				// Game over
				prompt = "You lose! Play again?";
				if (windowController.openYesNoDialog(prompt)) {
					LIFE_NUM = MAX_LIFE_NUM;
					windowController.resetGame();
				} else {
					windowController.closeWindow();
				}
			}
		}
	}

	/**
	 * Entry point — starts the game with default (or CLI-provided) row/col values.
	 */
	public static void main(String[] args) {
		int rowBricksNum = 7;
		int colBricksNum = 8;
		if (args.length == 2) {
			colBricksNum = Integer.parseInt(args[0]);
			rowBricksNum = Integer.parseInt(args[1]);
		}
		new BrickerGameManager("Bricker Game",
				Constants.windowDimensions, rowBricksNum, colBricksNum).run();
	}
}