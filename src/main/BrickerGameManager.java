package main;

import brick_strategies.BasicCollisionStrategy;
import brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import gameobjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main class that manages the Bricker game.
 * Responsible for initializing game objects and game logic.
 */
public class BrickerGameManager extends GameManager {
	// Constants and game parameters
	private int BORDER_WIDTH = 3;           // Width of the screen borders
	private float BALL_SPEED = 150;         // Initial speed of the ball
	private int rowNum;                     // Number of brick rows
	private int colNum;                     // Number of brick columns
	private Vector2 windowDimensions;       // Window dimensions
	private ImageReader imageReader;
	private SoundReader soundReader;
	private WindowController windowController;
	private Ball ball;                      // Ball instance
	private List<GameObject> currentHeartsPanelObjects = new ArrayList<>();
	private int MAX_LIFE_NUM;
	private int LIFE_NUM = 3;// Number of player lives
	private int bricks_num;                 // Number of bricks remaining

	/**
	 * Constructor for the game manager.
	 * @param windowTitle The title of the game window.
	 * @param windowDimensions The size of the game window.
	 * @param rowNum Number of rows of bricks.
	 * @param colNum Number of columns of bricks.
	 */
	public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int rowNum, int colNum) {
		super(windowTitle, windowDimensions);
		this.rowNum = rowNum;
		this.colNum = colNum;
	}

	/**
	 * Initializes the game — creates all game objects.
	 */
	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader,
							   UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		this.windowDimensions = windowController.getWindowDimensions();
		this.imageReader = imageReader;
		this.soundReader = soundReader;
		this.windowController = windowController;

		createBackground(imageReader, windowController);  // Background image
		createHeartsPanel(imageReader);
		createBall(imageReader, soundReader);             // Ball object
		createPaddle(imageReader, inputListener);         // Paddle object
		createBoundaries();                               // Invisible screen boundaries
		createBricks(imageReader);                        // Bricks grid
	}

	private void createHeartsPanel(ImageReader imageReader) {
		// הסר את הפאנל הקודם אם קיים
		for (GameObject obj : currentHeartsPanelObjects) {
			gameObjects().removeGameObject(obj, Layer.UI);
		}
		currentHeartsPanelObjects.clear();

		// פרמטרים גרפיים
		Vector2 panelTopLeft = new Vector2(windowDimensions.x() - 200, 10);
		Vector2 panelSize = new Vector2(150, 30);
		Renderable heartImage = imageReader.readImage("assets/heart.png", true);

		float totalWidth = panelSize.x();
		float totalHeight = panelSize.y();
		float objectWidth = totalWidth / (LIFE_NUM + 1); // אחד לטקסט, שאר ללבבות

		// טקסט נומרי
		TextRenderable textRenderable = new TextRenderable(Integer.toString(LIFE_NUM));
		if (LIFE_NUM >= 3) textRenderable.setColor(java.awt.Color.GREEN);
		else if (LIFE_NUM == 2) textRenderable.setColor(java.awt.Color.YELLOW);
		else textRenderable.setColor(java.awt.Color.RED);

		GameObject textObject = new GameObject(
				panelTopLeft,
				new Vector2(objectWidth, totalHeight),
				textRenderable
		);
		gameObjects().addGameObject(textObject, Layer.UI);
		currentHeartsPanelObjects.add(textObject);

		// לבבות
		for (int i = 0; i < LIFE_NUM; i++) {
			float xPos = panelTopLeft.x() + objectWidth + i * objectWidth;
			GameObject heart = new GameObject(
					new Vector2(xPos, panelTopLeft.y()),
					new Vector2(objectWidth, totalHeight),
					heartImage
			);
			gameObjects().addGameObject(heart, Layer.UI);
			currentHeartsPanelObjects.add(heart);
		}
	}



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
		CollisionStrategy collisionStrategy = new BasicCollisionStrategy(gameObjects());
		Renderable brickImage = imageReader.readImage("assets/brick.png", false);

		float spacing = 5;
		float brickHeight = 15;
		float availableWidth = windowDimensions.x() - 2 * BORDER_WIDTH;
		float brickWidth = (availableWidth - (colNum - 1) * spacing) / colNum;

		float startX = BORDER_WIDTH;
		float startY = BORDER_WIDTH;

		// Create each brick at its calculated position
		for (int row = 0; row < rowNum; row++) {
			for (int col = 0; col < colNum; col++) {
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
				bricks_num++;
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

	public void checkEndGame(){
		float ballHeight = ball.getCenter().y();
		String prompt = "";

		if (ballHeight > windowDimensions.y()) {
			LIFE_NUM--;
			createHeartsPanel(imageReader);
			createBall(imageReader,soundReader);

			if (LIFE_NUM == 0) {
				// Game over
				prompt = "You lose! Play again?";
				if (windowController.openYesNoDialog(prompt)) {
					LIFE_NUM = 3;
					windowController.resetGame();
				}
				else {
					windowController.closeWindow();
				}
			}
		}
	}

	/**
	 * Entry point — starts the game with default (or CLI-provided) row/col values.
	 */
	public static void main(String[] args) {
		int rowNum = 7;
		int colNum = 8;
		if (args.length == 2) {
			colNum = Integer.parseInt(args[0]);
			rowNum = Integer.parseInt(args[1]);
		}
		new BrickerGameManager("Bricker Game", new Vector2(700, 500), rowNum, colNum).run();
	}
}