package brick_strategies;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;
import gameobjects.Puck;

import java.util.Random;

/**
 * A strategy that spawns two additional pucks when a brick is destroyed.
 */
public class ExtraBallsStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjects;
	private final Counter bricksCounter;
	private final ImageReader imageReader;
	private final SoundReader soundReader;

	public ExtraBallsStrategy(GameObjectCollection gameObjects, Counter bricksCounter,
							  ImageReader imageReader, SoundReader soundReader) {
		this.gameObjects = gameObjects;
		this.bricksCounter = bricksCounter;
		this.imageReader = imageReader;
		this.soundReader = soundReader;
	}

	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS);
		bricksCounter.decrement();

		Renderable puckImage = imageReader.readImage("assets/mockBall.png", true);
		Sound collisionSound = soundReader.readSound("assets/blop.wav");

		Vector2 brickCenter = brick.getCenter();
		Vector2 puckSize = new Vector2(15, 15); // assuming normal ball is 20x20

		for (int i = 0; i < 2; i++) {
			GameObject puck = new Puck(brickCenter, puckSize, puckImage, collisionSound,gameObjects);
			puck.setVelocity(randomUpperHalfVelocity());
			gameObjects.addGameObject(puck);
		}
	}

	private Vector2 randomUpperHalfVelocity() {
		Random random = new Random();
		double angle = random.nextDouble() * Math.PI; // [0, PI]
		float speed = 150;
		float velX = (float)Math.cos(angle) * speed;
		float velY = (float)Math.sin(angle) * speed;
		return new Vector2(velX, -Math.abs(velY)); // ensure upward direction
	}
}
