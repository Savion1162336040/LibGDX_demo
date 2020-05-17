package com.mygdx.game.supmario.actors.stageitems;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.supmario.gamesys.GameManager;
import com.mygdx.game.supmario.screens.PlayScreen;

/**
 * Created by yichen on 10/18/15.
 *
 * Flag
 */
public class Flag extends Actor {

    Sprite flagSprite;

    public Flag(PlayScreen playScreen, float x, float y) {
        flagSprite = new Sprite(new TextureRegion(playScreen.getTextureAtlas().findRegion("Flag"), 0, 0, 16, 16));
        flagSprite.setBounds(x, y, 16 / GameManager.PPM, 16 / GameManager.PPM);
        setBounds(flagSprite.getX(), flagSprite.getY(), flagSprite.getWidth(), flagSprite.getHeight());
    }

    @Override
    protected void positionChanged() {
        flagSprite.setPosition(getX(), getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        flagSprite.draw(batch);

    }
}
