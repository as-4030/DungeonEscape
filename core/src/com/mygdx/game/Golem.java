package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public class Golem extends Animal {
    TextureAtlas golemAtlas;
    Animation<TextureRegion> golemAnimation;

    boolean shootingStatus;
    Rock rock;

    public Golem(float xPosition, float yPosition) {
        super(xPosition, yPosition, 80, 85);
        this.golemAtlas = new TextureAtlas(Gdx.files.internal("golemPack.atlas"));
        this.golemAnimation = new Animation<TextureRegion>((1/15f), this.golemAtlas.getRegions(), Animation.PlayMode.LOOP);
        this.shootingStatus = false;
        this.rock = new Rock(xPosition, yPosition);
    }

    public boolean randomizeOdds() {
        int randomNum = new Random().nextInt(4);

        if (randomNum == 1) {
            return true;
        }

        return false;
    }

    @Override
    public boolean shoot(float timePassed) {
        this.shootingStatus = randomizeOdds();

        if (this.shootingStatus == true) {
            return true;
        }
        return false;
    }

    @Override
    public void die() {

    }
}
