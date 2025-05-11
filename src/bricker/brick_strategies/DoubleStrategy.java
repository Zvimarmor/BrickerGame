package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import bricker.gameobjects.HeartsPanel;

import java.util.Random;

/**
 * A strategy that combines two or more other collision strategies at random.
 */
public class DoubleStrategy implements CollisionStrategy {
	private final Random rand;
	private final GameObjectCollection gameObjects;
	private final Counter bricksNum;
	private final ImageReader imageReader;
	private final SoundReader soundReader;
	public final UserInputListener userInputListener;
	private HeartsPanel heartsPanel;

	private CollisionStrategy strategy1;
	private CollisionStrategy strategy2;
	private CollisionStrategy strategy3 = null;

	/**
	 * Constructs the double strategy with randomly chosen sub-strategies.
	 */
	public DoubleStrategy(GameObjectCollection gameObjects,
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

		// build strategy one
		int strategy_num1 = rand.nextInt(4);
		strategy1 = getStrategy(strategy_num1);

		// build strategy two
		int strategy_num2 = rand.nextInt(5);
		if (strategy_num2 == 4) {
			// build double inside double - max depth 3
			strategy_num2 = rand.nextInt(4);
			int strategy_num3 = rand.nextInt(4);
			strategy3 = getStrategy(strategy_num3);
		}
		strategy2 = getStrategy(strategy_num2);
	}

	/**
	 * Utility method to generate a strategy given an index.
	 */
	private CollisionStrategy getStrategy(int num) {
		switch (num) {
			case 0:
				return new ExtraBallsStrategy(gameObjects, bricksNum, imageReader, soundReader);
			case 1:
				return new PaddleDuplicatorStrategy(gameObjects, bricksNum, imageReader, userInputListener);
			case 2:
				return new TurboModeStrategy(gameObjects, bricksNum, imageReader);
			case 3:
				return new LifeRestorationStrategy(gameObjects, bricksNum, imageReader, heartsPanel);
		}
		return null;
	}

	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		strategy1.onCollision(brick, collider);
		strategy2.onCollision(brick, collider);
		if (strategy3 != null) {
			strategy3.onCollision(brick, collider);
		}
	}
}
