package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Urchin {

    TextureAtlas atlas;
    TextureRegion urchin;

    float xPosition;
    float yPosition;

    public Urchin(float xPosition, float yPosition) {
        this.atlas = new TextureAtlas("sittingStillObjects.atlas");
        this.urchin = this.atlas.findRegion("urchin2");
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public TextureRegion getRegion() {
        return this.urchin;
    }

}
