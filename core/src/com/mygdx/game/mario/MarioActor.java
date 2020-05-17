package com.mygdx.game.mario;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.superjumper.Animation;

public class MarioActor extends Actor {
    Vector2 vector2;

    private float x;
    private float y;

    private Texture textureIdle;
    private Texture textureRightWalk;

    Animation animationLeft;
    Animation animationRight;
    Animation animationIdle;

    enum STATE {
        STATE_IDLE,
        STATE_LEFT,
        STATE_RIGHT;
    }

    STATE state;

    public MarioActor(float x, float y) {
        this.x = x;
        this.y = y;
        this.state = STATE.STATE_IDLE;
    }

    private void show() {
        textureIdle = new Texture(Gdx.files.internal("mario/mario_right_idle"));
        textureRightWalk = new Texture(Gdx.files.internal("mario/mario_right_walk"));
        TextureRegion textureRegion =new TextureRegion(textureIdle,64,64);

        //左
        //右

        //停止

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
