package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Dog extends Animal {
    TextureAtlas atlas;
    Animation<TextureRegion> dogAnimation;
    boolean alive;

    public Dog(float xPosition, float yPosition) {
        super(xPosition, yPosition, 64, 59);
        this.atlas = new TextureAtlas(Gdx.files.internal("jolteonPack.atlas"));
        this.dogAnimation = new Animation<TextureRegion>((1/15f), this.atlas.getRegions(), Animation.PlayMode.LOOP);
        this.alive = true;
    }

    @Override
    public void die() {
        this.alive = false;
    }

    @Override
    public boolean shoot(float timePassed) {
        return false;
    }
}