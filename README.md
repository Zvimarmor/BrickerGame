# Bricker Game

A Java-based Breakout-style game built on the Danogl framework. 
Bricker features customizable brick collision strategies (extra balls, paddle duplication, turbo mode, life restoration), a heart-based life panel, and configurable grid size.

---

## Features

* **Multiple collision strategies**:

  * Extra balls spawn additional balls on brick hits
  * Paddle duplicator creates a second paddle for a limited number of hits
  * Turbo mode multiplies ball speed and changes color temporarily
  * Life restoration strategy grants extra life on brick destruction
* **Lives panel** with falling-heart animation when lives are lost
* **Configurable grid**: support for custom numbers of rows and columns via CLI args
* **Constants-based configuration** for easy tuning of speeds, sizes, asset paths, and tags

---

## Project Structure

```
src/
└── bricker/
    ├── main/
    │   ├── BrickerGameManager.java
    │   └── Constants.java
    ├── gameobjects/
    │   ├── Ball.java
    │   ├── Brick.java
    │   ├── HeartsPanel.java
    │   ├── Paddle.java
    │   └── Puck.java
    └── brick_strategies/
        ├── BrickFactory.java
        ├── CollisionStrategy.java
        ├── DoubleStrategy.java
        ├── ExtraBallsStrategy.java
        ├── LifeRestorationStrategy.java
        ├── PaddleDuplicatorStrategy.java
        └── TurboModeStrategy.java
```

---

## Prerequisites

* **Java 17** or higher
* **Danogl** library (DanoGameLab.jar)
* **Maven** or **Gradle** (optional)
* **IntelliJ IDEA** (recommended)

---

## Build & Run

### Using IntelliJ IDEA

1. Import as a Gradle/Maven project or create a new Java project.
2. Add `DanoGameLab.jar` to the project classpath (Project Structure > Libraries).
3. Run `BrickerGameManager.main()`.

### Command Line (macOS/Linux)

```bash
# From project root:
mkdir -p out
javac -d out -cp ~/source/to/project/DanoGameLab.jar \ #add here your path
    $(find src -name "*.java")
java -cp "out:~/source/to/project/DanoGameLab.jar" \ #add here your path
    bricker.main.BrickerGameManager [cols rows]
```

Replace `[cols rows]` with desired grid dimensions (defaults to 8 columns, 7 rows).

---

## Assets

All assets are stored under `src/bricker/assets`:

* `ball.png`, `redball.png`
* `brick.png`
* `paddle.png`
* `heart.png`
* `DARK_BG2_small.jpeg`
* `blop.wav`

---

