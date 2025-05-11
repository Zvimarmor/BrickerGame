package bricker.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import bricker.gameobjects.HeartsPanel;
import bricker.main.Constants;

import java.util.Random;

/**
 * Factory class responsible for generating collision strategies for bricks.
 * Uses a random roll to determine the strategy assigned to each brick.
 */
public class BrickFactory {
	private final Random rand;
	private final GameObjectCollection gameObjects;
	private final Counter bricksNum;
	private final ImageReader imageReader;
	private final SoundReader soundReader;
	public final UserInputListener userInputListener;
	private final HeartsPanel heartsPanel;

	/**
	 * Constructs a BrickFactory.
	 */
	public BrickFactory(GameObjectCollection gameObjects,
						Counter bricksNum,
						ImageReader imageReader,
						SoundReader soundReader,
						UserInputListener userInputListener,
						HeartsPanel heartsPanel) {
		this.rand = new Random();
		this.gameObjects = gameObjects;
		this.bricksNum = bricksNum;
		this.imageReader = imageReader;
		this.soundReader = soundReader;
		this.userInputListener = userInputListener;
		this.heartsPanel = heartsPanel;
	}

	/**
	 * Returns a randomly selected CollisionStrategy based on a bounded roll.
	 */
	public CollisionStrategy getStrategy() {
		int roll = rand.nextInt(Constants.STRATEGY_ROLL_BOUND);

		switch (roll) {
			case 0:
				return new ExtraBallsStrategy(gameObjects, bricksNum, imageReader, soundReader);
			case 1:
				return new PaddleDuplicatorStrategy(gameObjects, bricksNum, imageReader, userInputListener);
			case 2:
				return new TurboModeStrategy(gameObjects, bricksNum, imageReader);
			case 3:
				return new LifeRestorationStrategy(gameObjects, bricksNum, imageReader, heartsPanel);
			case 4:
				return new DoubleStrategy(gameObjects, bricksNum, imageReader, soundReader,
						userInputListener, heartsPanel);
			default:
				return new BasicCollisionStrategy(gameObjects, bricksNum);
		}
	}
}
