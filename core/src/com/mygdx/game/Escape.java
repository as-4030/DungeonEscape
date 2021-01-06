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
	private float dogX;
	private float dogY;

	private Music music;

	private Golem golemOne;
	private Golem golemTwo;

	private Rock rockOne;
	private Rock rockTwo;

	boolean lookingLeft = false;

	private Urchin urchinOne;
	private Urchin urchinTwo;
	private Urchin urchinThree;
	private Urchin urchinFour;

	TextureAtlas gameOverAtlas;
	Animation<TextureRegion> gameOverAnimation;
	
	@Override
	public void create() {
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.batch = new SpriteBatch();
		this.background = new Texture("background.jpg");
		
		this.dogX = -400;
		this.dogY = -250;
		this.dog = new Dog(this.dogX, this.dogY);

		this.golemOne = new Golem(340, -50);
		this.golemTwo = new Golem(340, 100);

		this.rockOne = new Rock(340, -50);
		this.rockTwo = new Rock(340, 100);

		this.urchinOne = new Urchin(0, 0);
		this.urchinTwo = new Urchin(-200, -50);
		this.urchinThree = new Urchin(375, 40);
		this.urchinFour = new Urchin(-250, 250);

		this.music = Gdx.audio.newMusic(Gdx.files.internal("thememusic.mp3"));
		this.music.setLooping(true);

		this.gameOverAtlas = new TextureAtlas(Gdx.files.internal("gameOverPack.atlas"));
		this.gameOverAnimation = new Animation<TextureRegion>((1/15f), this.gameOverAtlas.getRegions(), Animation.PlayMode.LOOP);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.music.play();
		this.music.setVolume(0.1f);

		this.batch.setProjectionMatrix(this.camera.combined);
		this.batch.begin();

		this.timePassed += Gdx.graphics.getDeltaTime();
		int moveInput = movementInput();
		physicalMovement(moveInput);

		this.batch.draw(this.background, -1100, -610);

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

		this.batch.draw(this.urchinOne.getRegion(), this.urchinOne.xPosition, this.urchinOne.yPosition, 40, 40);
		this.batch.draw(this.urchinTwo.getRegion(), this.urchinTwo.xPosition, this.urchinTwo.yPosition, 40, 40);
		this.batch.draw(this.urchinThree.getRegion(), this.urchinThree.xPosition, this.urchinThree.yPosition, 40, 40);
		this.batch.draw(this.urchinFour.getRegion(), this.urchinFour.xPosition, this.urchinFour.yPosition, 40, 40);

		if (moveInput == 3) {
			this.batch.draw(this.dog.dogAnimation.getKeyFrame(this.timePassed, true), this.dogX - 40, this.dogY, 64, 59);
			this.lookingLeft = true;
		} else if (!(moveInput == 0)) {
			this.batch.draw(this.dog.dogAnimation.getKeyFrame(this.timePassed, true), this.dogX, this.dogY, -64, 59);
			this.lookingLeft = false;
		} else if ((moveInput == 0) && (this.lookingLeft == true)) {
			this.batch.draw(this.dog.dogAnimation.getKeyFrame(1, true), this.dogX - 40, this.dogY, 64, 59);
		} else if ((moveInput == 0) && (this.lookingLeft == false)) {
			this.batch.draw(this.dog.dogAnimation.getKeyFrame(1, true), this.dogX, this.dogY, -64, 59);
		}

		if (((Math.abs(this.dogY - this.rockOne.rockY) < 35) || (Math.abs(this.dogY - this.rockTwo.rockY) < 35)) && ((Math.abs(this.dogX - this.rockOne.rockX) < 8) || (Math.abs(this.dogX - this.rockTwo.rockX) < 8))) {
			this.dogX = 2000;
			this.dogY = 2000;
		}

		if (((Math.abs(this.urchinOne.xPosition - this.dogX) < 12) && (Math.abs(this.dogY - this.urchinOne.yPosition) < 8))) {
			this.dogX = 2000;
			this.dogY = 2000;
		}

		if (((Math.abs(this.urchinTwo.xPosition - this.dogX) < 12) && (Math.abs(this.dogY - this.urchinTwo.yPosition) < 8))) {
			this.dogX = 2000;
			this.dogY = 2000;
		}

		if (((Math.abs(this.urchinThree.xPosition - this.dogX) < 12) && (Math.abs(this.dogY - this.urchinThree.yPosition) < 8))) {
			this.dogX = 2000;
			this.dogY = 2000;
		}

		if (((Math.abs(this.urchinFour.xPosition - this.dogX) < 12) && (Math.abs(this.dogY - this.urchinFour.yPosition) < 8))) {
			this.dogX = 2000;
			this.dogY = 2000;
		}

		gameOver(this.timePassed);

		this.batch.end();
	}

	public void gameOver(float timePassed) {
		if (this.dogX > 1500) {
			this.batch.draw(this.gameOverAnimation.getKeyFrame(timePassed, true), -250, -150, 500, 350);
			this.dogX = 1750;
			this.dogY = 1750;
		}
	}
	
	@Override
	public void dispose() {
		this.batch.dispose();
		this.background.dispose();
		this.dog.atlas.dispose();
		this.music.dispose();
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
	}

	public void physicalMovement(int inputNum) {
		if (inputNum == 1) {
			this.dogY = this.dogY + 5;
		} else if (inputNum == 2) {
			this.dogY = this.dogY - 5;
		} else if (inputNum == 3) {
			this.dogX = this.dogX - 5;
		} else if (inputNum == 4) {
			this.dogX = this.dogX + 5;
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