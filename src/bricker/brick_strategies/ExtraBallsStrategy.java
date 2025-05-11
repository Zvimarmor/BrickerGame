package bricker.brick_strategies;

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
import bricker.gameobjects.Puck;
import bricker.main.Constants;

import java.util.Random;

/**
 * Strategy that create two mini-pucks when a brick is destroyed.
 * Each puck inherits a random upward velocity and does not cost a life.
 */
public class ExtraBallsStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjects;
	private final Counter bricksCounter;
	private final ImageReader imageReader;
	private final SoundReader soundReader;

	/**
	 * Constructs the ExtraBallsStrategy with game dependencies.
	 *
	 * @param gameObjects   Collection managing game objects.
	 * @param bricksCounter Counter tracking remaining bricks.
	 * @param imageReader   Loader for image assets.
	 * @param soundReader   Loader for sound assets.
	 */
	public ExtraBallsStrategy(GameObjectCollection gameObjects, Counter bricksCounter,
							  ImageReader imageReader, SoundReader soundReader) {
		this.gameObjects = gameObjects;
		this.bricksCounter = bricksCounter;
		this.imageReader = imageReader;
		this.soundReader = soundReader;
	}

	/**
	 * Called when a brick is hit: removes brick, decrements counter,
	 * and spawns two mini-pucks with random upward velocities.
	 *
	 * @param brick    The brick that was hit.
	 * @param collider The object that hit the brick.
	 */
	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		// Remove brick and update counter
		if (gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS)) {
			bricksCounter.decrement();
		}

		// Load assets for mini-pucks
		Renderable puckImage = imageReader.readImage(Constants.PUCK_IMAGE_PATH, true);
		Sound collisionSound = soundReader.readSound(Constants.COLLISION_SOUND_PATH);

		// Center position of original brick
		Vector2 brickCenter = brick.getCenter();
		Vector2 puckSize = Constants.ballDimensions.mult(Constants.PUCK_PROPORTION_SIZE);

		// Spawn two mini-pucks
		for (int i = 0; i < 2; i++) {
			GameObject puck = new Puck(brickCenter, puckSize, puckImage, collisionSound, gameObjects);
			puck.setTag(Constants.MINI_BALL_TAG);
			puck.setVelocity(randomUpperHalfVelocity());
			gameObjects.addGameObject(puck);
		}
	}

	/**
	 * Generates a random velocity vector pointing upward within the upper semicircle.
	 * Uses a uniform random angle in [0, PI] and scales by predefined speed.
	 *
	 * @return A Vector2 velocity for a mini-puck.
	 */
	private Vector2 randomUpperHalfVelocity() {
		Random random = new Random();
		double angle = random.nextDouble() * Math.PI;  // Angle between 0 and PI
		float speed = Constants.MINIS_BALLS_SPEED;
		float velX = (float) Math.cos(angle) * speed;
		float velY = (float) Math.sin(angle) * speed;
		// Ensure upward motion by taking negative absolute Y
		return new Vector2(velX, -Math.abs(velY));
	}
}
