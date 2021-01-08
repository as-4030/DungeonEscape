package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ladder {

    TextureAtlas atlas;
    TextureRegion ladder;

    float xPosition;
    float yPosition;

    public Ladder(float xPosition, float yPosition) {
        this.atlas = new TextureAtlas(Gdx.files.internal("sittingStillObjects.atlas"));
        this.ladder = this.atlas.findRegion("ladder");
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public TextureRegion getRegion() {
        return this.ladder;
    }
}
