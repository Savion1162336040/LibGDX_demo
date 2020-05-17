package com.mygdx.game.supmario;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.supmario.gamesys.GameManager;
import com.mygdx.game.supmario.screens.PlayScreen;

public class SuperMario extends Game {
    public SpriteBatch batch;

    private GameManager gameManager;
    private float w = 20f, h = 15;

    public SuperMario(int w, int h) {
        this.w = this.h * (w * 1f / h);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        if (GameManager.instance != null) {
            gameManager = GameManager.instance;
        } else {
            gameManager = new GameManager(w, h);
        }

        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }


    @Override
    public void dispose() {
        super.dispose();
        gameManager.dispose();
    }

}
