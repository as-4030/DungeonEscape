package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Escape extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;
	private OrthographicCamera camera;
	float timePassed = 0.0001f;

	private Dog dog;
	private Float dogDeathX;
	private Float dogDeathY;
	boolean lookingLeft = false;
	boolean victory = false;

	private Music themeMusic;
	private Music deathMusic;

	private Golem golemOne;
	private Golem golemTwo;

	private Rock rockOne;
	private Rock rockTwo;

	private Urchin urchinOne;
	private Urchin urchinTwo;
	private Urchin urchinThree;
	private Urchin urchinFour;

	private Ladder ladder;

	TextureAtlas gameOverAtlas;
	Animation<TextureRegion> gameOverAnimation;

	TextureAtlas deathAtlas;
	Animation<TextureRegion> tombstoneAnimation;

	TextureAtlas poofAtlas;
	Animation<TextureRegion> poofAnimation;

	TextureAtlas winnerAtlas;
	Animation<TextureRegion> winnerAnimation;
	
	@Override
	public void create() {
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.batch = new SpriteBatch();
		this.background = new Texture("background.jpg");

		this.dog = new Dog(-400, -250);
		this.dogDeathX = null;
		this.dogDeathY = null;

		this.golemOne = new Golem(340, -50);
		this.golemTwo = new Golem(340, 100);

		this.rockOne = new Rock(340, -50);
		this.rockTwo = new Rock(340, 100);

		this.urchinOne = new Urchin(-300, -300);
		this.urchinTwo = new Urchin(-200, -200);
		this.urchinThree = new Urchin(0, 200);
		this.urchinFour = new Urchin(-250, 250);

		this.themeMusic = Gdx.audio.newMusic(Gdx.files.internal("thememusic.mp3"));
		this.themeMusic.setLooping(true);
		this.themeMusic.setVolume(0.1f);

		this.deathMusic = Gdx.audio.newMusic(Gdx.files.internal("deathMusic.mp3"));
		this.deathMusic.setLooping(true);
		this.deathMusic.setVolume(0.05f);

		this.gameOverAtlas = new TextureAtlas(Gdx.files.internal("gameOverPack.atlas"));
		this.gameOverAnimation = new Animation<TextureRegion>((1/15f), this.gameOverAtlas.getRegions(), Animation.PlayMode.LOOP);

		this.deathAtlas = new TextureAtlas(Gdx.files.internal("deathPack.atlas"));
		this.tombstoneAnimation = new Animation<TextureRegion>((1/4.5f), this.deathAtlas.getRegions(), Animation.PlayMode.NORMAL);

		this.poofAtlas = new TextureAtlas(Gdx.files.internal("poofPack.atlas"));
		this.poofAnimation = new Animation<TextureRegion>((1/4f), this.poofAtlas.getRegions(), Animation.PlayMode.NORMAL);

		this.winnerAtlas = new TextureAtlas(Gdx.files.internal("winnerPack.atlas"));
		this.winnerAnimation = new Animation<TextureRegion>((1/4f), this.winnerAtlas.getRegions(), Animation.PlayMode.LOOP);

		this.ladder = new Ladder(380, 230);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.themeMusic.play();

		this.batch.setProjectionMatrix(this.camera.combined);
		this.batch.begin();

		this.timePassed += Gdx.graphics.getDeltaTime();
		int moveInput = movementInput();
		physicalMovement(moveInput);

		this.batch.draw(this.background, -1100, -610);

		this.batch.draw(this.ladder.getRegion(), this.ladder.xPosition, this.ladder.yPosition, 40, 60);

		drawGolems();
		drawUrchins();

		movingAround(moveInput);

		collisionDetection();

		gameOverDetector(this.timePassed);
		victoryDetector(this.timePassed);

		if (Gdx.input.isKeyPressed(Input.Keys.U)) {
			System.out.println("Urchin X: " + this.urchinOne.xPosition);
			System.out.println("Urchin Y: " + this.urchinOne.yPosition);
			System.out.println("Dog X: " + this.dog.xPosition);
			System.out.println("Dog Y: " + this.dog.yPosition);
		}

		this.batch.end();
	}

	public void drawGolems() {
		this.batch.draw(this.golemOne.golemAnimation.getKeyFrame(this.timePassed, true), 350, -50, 80, 85);
		this.batch.draw(this.golemTwo.golemAnimation.getKeyFrame(this.timePassed, true), 350, 100, 80, 85);

		this.batch.draw(this.rockOne.rockAnimation.getKeyFrame(timePassed, true), this.rockOne.rockX, this.rockOne.rockY, 45, 45);
		this.rockOne.rockX = this.rockOne.rockX - 11;
		if (this.rockOne.rockX < -600) {
			this.rockOne.rockX = 340;
		}

		this.batch.draw(this.rockTwo.rockAnimation.getKeyFrame(timePassed, true), this.rockTwo.rockX, this.rockTwo.rockY, 45, 45);
		this.rockTwo.rockX = this.rockTwo.rockX - 11;
		if (this.rockTwo.rockX < -600) {
			this.rockTwo.rockX = 340;
		}
	}

	public void drawUrchins() {
		this.batch.draw(this.urchinOne.getRegion(), this.urchinOne.xPosition, this.urchinOne.yPosition, 90, 90);
		this.batch.draw(this.urchinTwo.getRegion(), this.urchinTwo.xPosition, this.urchinTwo.yPosition, 90, 90);
		this.batch.draw(this.urchinThree.getRegion(), this.urchinThree.xPosition, this.urchinThree.yPosition, 90, 90);
		this.batch.draw(this.urchinFour.getRegion(), this.urchinFour.xPosition, this.urchinFour.yPosition, 90, 90);
	}

	public void movingAround(int moveInput) {
		if (moveInput == 3) {
			this.batch.draw(this.dog.dogAnimation.getKeyFrame(this.timePassed, true), this.dog.xPosition - 40, this.dog.yPosition, 64, 59);
			this.lookingLeft = true;
		} else if (!(moveInput == 0)) {
			this.batch.draw(this.dog.dogAnimation.getKeyFrame(this.timePassed, true), this.dog.xPosition, this.dog.yPosition, -64, 59);
			this.lookingLeft = false;
		} else if ((moveInput == 0) && (this.lookingLeft == true)) {
			this.batch.draw(this.dog.dogAnimation.getKeyFrame(1, true), this.dog.xPosition - 40, this.dog.yPosition, 64, 59);
		} else if ((moveInput == 0) && (this.lookingLeft == false)) {
			this.batch.draw(this.dog.dogAnimation.getKeyFrame(1, true), this.dog.xPosition, this.dog.yPosition, -64, 59);
		}
	}

	public void collisionDetection() {
		boolean withinRock = (Math.abs(this.dog.xPosition - this.rockOne.rockX) < 25) && (Math.abs(this.dog.yPosition - this.rockOne.rockY) < 25) || (Math.abs(this.dog.xPosition - this.rockTwo.rockX) < 25 && Math.abs(this.dog.yPosition - this.rockTwo.rockY) < 25);
		boolean withinUrchin1 = ((((this.dog.xPosition - this.urchinOne.xPosition) < 40) && ((this.dog.xPosition - this.urchinOne.xPosition) > -15)) && (this.dog.yPosition - this.urchinOne.yPosition) < 30) && ((this.dog.yPosition - this.urchinOne.yPosition) > 0);
		boolean withinUrchin2 = ((((this.dog.xPosition - this.urchinTwo.xPosition) < 40) && ((this.dog.xPosition - this.urchinTwo.xPosition) > -15)) && (this.dog.yPosition - this.urchinTwo.yPosition) < 30) && ((this.dog.yPosition - this.urchinTwo.yPosition) > 0);
		boolean withinUrchin3 = ((((this.dog.xPosition - this.urchinThree.xPosition) < 40) && ((this.dog.xPosition - this.urchinThree.xPosition) > -15)) && (this.dog.yPosition - this.urchinThree.yPosition) < 30) && ((this.dog.yPosition - this.urchinThree.yPosition) > 0);
		boolean withinUrchin4 = ((((this.dog.xPosition - this.urchinFour.xPosition) < 40) && ((this.dog.xPosition - this.urchinFour.xPosition) > -15)) && (this.dog.yPosition - this.urchinFour.yPosition) < 30) && ((this.dog.yPosition - this.urchinFour.yPosition) > 0);

		if (withinRock || withinUrchin1 || withinUrchin2 || withinUrchin3 || withinUrchin4) {
			this.dogDeathX = this.dog.xPosition;
			this.dogDeathY = this.dog.yPosition;
			this.dog.xPosition = 2000;
			this.dog.yPosition = 2000;
		}
	}

	public void gameOverDetector(float timePassed) {
		if (this.dog.xPosition > 1500) {
			drawDeath();
			this.batch.draw(this.gameOverAnimation.getKeyFrame(timePassed, true), -250, -150, 500, 350);
			this.themeMusic.stop();
			this.deathMusic.play();
			this.dog.xPosition = 1750;
			this.dog.yPosition = 1750;
		}
	}

	public void victoryDetector(float timePassed) {
		if (Math.abs(this.dog.xPosition - this.ladder.xPosition) < 50 && Math.abs(this.dog.yPosition - this.ladder.yPosition) < 50) {
			this.victory = true;
			this.dog.xPosition = -2500;
			this.dog.yPosition = -2500;
		}

		if (this.victory == true) {
			this.batch.draw(this.winnerAnimation.getKeyFrame(timePassed, true), -200, -50, 400, 200);
		}
	}

	public void drawDeath() {
		this.batch.draw(this.tombstoneAnimation.getKeyFrame(timePassed, false), this.dogDeathX, this.dogDeathY, 64, 59);
		this.batch.draw(this.poofAnimation.getKeyFrame(timePassed, false), this.dogDeathX, this.dogDeathY, 64, 59);
	}
	
	@Override
	public void dispose() {
		this.batch.dispose();
		this.background.dispose();
		this.dog.atlas.dispose();
		this.themeMusic.dispose();
		this.deathMusic.dispose();
		this.gameOverAtlas.dispose();
		this.dog.atlas.dispose();
		this.golemOne.golemAtlas.dispose();
		this.golemTwo.golemAtlas.dispose();
		this.rockOne.rockAtlas.dispose();
		this.rockTwo.rockAtlas.dispose();
		this.urchinOne.atlas.dispose();
		this.urchinTwo.atlas.dispose();
		this.urchinThree.atlas.dispose();
		this.urchinFour.atlas.dispose();
		this.deathAtlas.dispose();
		this.poofAtlas.dispose();
		this.ladder.atlas.dispose();
		this.winnerAtlas.dispose();

	}

	public void physicalMovement(int inputNum) {
		if (inputNum == 1) {
			this.dog.yPosition = this.dog.yPosition + 5;
		} else if (inputNum == 2) {
			this.dog.yPosition = this.dog.yPosition - 5;
		} else if (inputNum == 3) {
			this.dog.xPosition = this.dog.xPosition - 5;
		} else if (inputNum == 4) {
			this.dog.xPosition = this.dog.xPosition + 5;
		}
	}

	public int movementInput() {
		boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
		boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
		boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
		boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);

		if (upPressed) {
			return 1;
		} else if (downPressed) {
			return 2;
		} else if (leftPressed) {
			return 3;
		} else if (rightPressed) {
			return 4;
		}

		return 0;
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}