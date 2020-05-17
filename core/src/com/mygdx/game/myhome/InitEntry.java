package com.mygdx.game.myhome;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class InitEntry extends Game {

    SpriteBatch spriteBatch;

    GameManager gameManager;


    private float x = 20, y = 15;

    public InitEntry(float x, float y) {
        this.x = this.y * (x * 1f / y);
    }

    @Override
    public void create() {
        gameManager = new GameManager(x,y);
        spriteBatch = new SpriteBatch();

        setScreen(new HomeScreen(this));


    }

    @Override
    public void render() {
        super.render();
    }


    @Override
    public void dispose() {
        super.dispose();
        if (gameManager!=null){
            gameManager.dispose();
        }
        spriteBatch.dispose();
    }
}
