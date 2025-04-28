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
	int  BORDER_WIDTH = 3;
	float BALL_SPEED = 250;
	int rowNum;
	int colNum;
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
		// todo - loop to create all the Bricks
		//int index_rows = windowDimensions.x() / rowNum;
		CollisionStrategy collisionStrategy = new BasicCollisionStrategy(gameObjects());
		Renderable brickImage =
				imageReader.readImage("assets/brick.png",false);
		Brick brick = new Brick(new Vector2(0,0),new Vector2(windowDimensions.x(),15)
				,brickImage,collisionStrategy);
		gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
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

