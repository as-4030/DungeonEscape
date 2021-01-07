package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Animal {
    Rectangle boundingBox;
    float width;
    float height;

    public Animal(float xPosition, float yPosition,
                  float width, float height) {
        this.boundingBox =  new Rectangle(xPosition - width / 2, yPosition - height / 2, width, height);
        this.width = width;
        this.height = height;
    }


    public abstract void die();

    public abstract boolean shoot(float timePassed);

    public void setWidth(float newWidth) {
        this.width = newWidth;
    }

}