package com.mygdx.game.supmario.actors.enemies;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.supmario.actors.Mario;
import com.mygdx.game.supmario.actors.RigidBody;
import com.mygdx.game.supmario.screens.PlayScreen;

/**
 * Created by yichen on 10/13/15.
 *
 * Enemy
 */
public abstract class Enemy extends RigidBody {

    protected TextureAtlas textureAtlas;

    protected boolean active = false;

    public Enemy(PlayScreen playScreen, float x, float y) {
        super(playScreen, x, y);
        this.textureAtlas = playScreen.getTextureAtlas();
    }

    public abstract void getDamage(int damage);

    public void interact(Mario mario) {

    }

}
