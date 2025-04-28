package main;

import brick_strategies.BasicCollisionStrategy;
import brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import gameobjects.Ball;
import gameobjects.Brick;
import gameobjects.Paddle;

import java.util.Random;

public class BrickerGameManager extends GameManager {
	private int  BORDER_WIDTH = 3;
	private float BALL_SPEED = 250;
	private int rowNum;
	private int colNum;
	// todo check
	public BrickerGameManager(String windowTitle, Vector2 windowDimensions,int rowNum,int colNum) {
		super(windowTitle, windowDimensions);
		this.rowNum = rowNum;
		this.colNum = colNum;
	}


	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
							   WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);

		//Create Background
		Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg",false);
		GameObject background = new GameObject(
				new Vector2(0,0),
				windowController.getWindowDimensions(),
				backgroundImage
		);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		gameObjects().addGameObject(background, Layer.BACKGROUND);

		// ------------------- Create ball object ----------------------------
		Renderable ballImage =
				imageReader.readImage("assets/ball.png",true);
		Sound collisionSound = soundReader.readSound("assets/blop.wav");
		GameObject ball = new Ball(new Vector2(0,0),new Vector2(20,20),ballImage,
				collisionSound);
		// Set the ball to the center
		Vector2 windowDimensions = windowController.getWindowDimensions();
		ball.setCenter(windowDimensions.mult(0.5f));
		this.gameObjects().addGameObject(ball, Layer.DEFAULT);
		// Set the velocity of the ball
		// Determinate the direction of the ball - in random
		float ballVelX = BALL_SPEED;
		float ballVelY = BALL_SPEED;
		Random rand = new Random();
		if (rand.nextBoolean()) {
			ballVelX *= -1;
		}
		if (rand.nextBoolean()){
			ballVelY *= -1;
		}
		ball.setVelocity(new Vector2(ballVelX,ballVelY));

		//-------------------- Create paddle object -----------------------------
		Renderable paddleImage =
				imageReader.readImage("assets/paddle.png",true);
		GameObject paddle =
				new Paddle(new Vector2(0,0),new Vector2(100,15),paddleImage
				,inputListener);
		paddle.setCenter(
				new Vector2(windowDimensions.x()/2,windowDimensions.y()-30));
		gameObjects().addGameObject(paddle);

		// ---------------- Create boundaries ---------------------------
		// Right Wall
		GameObject rightBoundary = new GameObject(
				new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
				new Vector2(BORDER_WIDTH, windowDimensions.y()),
				null
		);
		gameObjects().addGameObject(rightBoundary,Layer.STATIC_OBJECTS);

		// Left Wall
		GameObject leftBoundary = new GameObject(
				new Vector2(0, 0),
				new Vector2(BORDER_WIDTH, windowDimensions.y()),
				null
		);
		gameObjects().addGameObject(leftBoundary,Layer.STATIC_OBJECTS);

		// Top Wall
		GameObject topBoundary = new GameObject(
				new Vector2(0, 0),
				new Vector2(windowDimensions.x(), BORDER_WIDTH),
				null);
		gameObjects().addGameObject(topBoundary,Layer.STATIC_OBJECTS);

		//-------------------- Create Bricks --------------------------
		// Create all Bricks with calculated spacing
		CollisionStrategy collisionStrategy = new BasicCollisionStrategy(gameObjects());
		Renderable brickImage = imageReader.readImage("assets/brick.png", false);

		float spacing = 5; // space between bricks
		float brickHeight = 15;

		// Available width for bricks (excluding left and right borders)
		float availableWidth = windowDimensions.x() - 2 * BORDER_WIDTH;
		// Width of each brick, considering spacing between columns
		float brickWidth = (availableWidth - (colNum - 1) * spacing) / colNum;

		float startX = BORDER_WIDTH; // start placing bricks after the left border
		float startY = BORDER_WIDTH; // some space from the top of the window

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
//				gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
				gameObjects().addGameObject(brick);
			}
		}

	}



	public static void main(String[] args) {
		int rowNum = 7;
		int colNum = 8;
		if (args.length == 2) {
			rowNum = Integer.parseInt(args[0]);
			colNum = Integer.parseInt(args[1]);
		};
		new BrickerGameManager("Bricker Game", new Vector2(700,500),
				rowNum,colNum).run();
	}
}

