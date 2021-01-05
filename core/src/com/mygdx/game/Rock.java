package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Rock {
    TextureAtlas rockAtlas;
    Animation<TextureRegion> rockAnimation;
    float rockX;
    float rockY;

    public Rock(float xPosition, float yPosition) {
        this.rockAtlas = new TextureAtlas(Gdx.files.internal("rockPack.atlas"));
        this.rockAnimation = new Animation<TextureRegion>((1/150f), this.rockAtlas.getRegions(), Animation.PlayMode.LOOP);
        this.rockX = xPosition;
        this.rockY = yPosition;
    }

}
