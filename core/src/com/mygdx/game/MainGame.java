package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * 游戏主程序的启动入口类
 */
public class MainGame extends ApplicationAdapter implements InputProcessor {
    Texture texture;
    Texture texture2;
    OrthographicCamera cam;
    SpriteBatch batch;

    final Sprite[][] sprites = new Sprite[10][10];
    final Matrix4 matrix = new Matrix4();

    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;

    @Override
    public void create() {

        map = new TmxMapLoader().load("home/maps/home_map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        cam.update();
//
//        batch.setProjectionMatrix(cam.combined);
//        batch.setTransformMatrix(matrix);
//        batch.begin();
//        for(int z = 0; z < 10; z++) {
//            for(int x = 0; x < 10; x++) {
//                sprites[x][z].draw(batch);
//            }
//        }
//        batch.end();
        batch.begin();

//        //背景
//        batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
//        batch.draw(texture,0,0);
//
//        //前景
//
//        batch.setBlendFunction(GL20.GL_ONE,GL20.GL_ZERO);
//        batch.draw(texture2,0,0);

        drawBackground(batch);
        int x = 25;
        int y = 50;
        int spriteWidth = 200;
        int spriteHeight = 200;
        drawAlphaMask(batch, x, y, spriteWidth, spriteHeight);

        drawForeground(batch, x, y, spriteWidth, spriteHeight);


        batch.end();


    }


    private void drawBackground(SpriteBatch batch) {
        //regular blending mode
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        //... draw background entities/tiles here ...
        //batch.draw(texture,0,0);


        //flush the batch to the GPU
        batch.flush();
    }

    private void drawAlphaMask(SpriteBatch batch, float x, float y, float width, float height) {
        //disable RGB color, only enable ALPHA to the frame buffer
        Gdx.gl.glColorMask(false, false, false, true);

        //change the blending function for our alpha map
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);

        //draw alpha mask sprite(s)
        batch.draw(texture2, x, y, width, height);

        //flush the batch to the GPU
        batch.flush();
    }

    private void drawForeground(SpriteBatch batch, int clipX, int clipY, int clipWidth, int clipHeight) {
        //now that the buffer has our alpha, we simply draw the sprite with the mask applied
        Gdx.gl.glColorMask(true, true, true, true);
        batch.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);

        //The scissor test is optional, but it depends
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(clipX, clipY, clipWidth, clipHeight);

        //draw our sprite to be masked
        batch.draw(texture, 0, 0, 250, 250);

        //remember to flush before changing GL states again
        batch.flush();

        //disable scissor before continuing
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}