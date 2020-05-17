package com.mygdx.game.myhome;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class GameManager implements Disposable {
    public static final float STEP = 1 / 60.0f;
    public static float timeScale = 1;

    /**
     * 显示与实际缩放比例
     */
    public static final float UNIT_SCALE = 16;

    /**
     * 窗口缩放后宽度
     */
    public static float V_WIDTH = 10;
    /**
     * 窗口缩放后高度
     */
    public static float V_HEIGHT = 20;
    /**
     * 重力
     */
    public static final Vector2 GRAVITY = new Vector2(0.0f, 0f);//-9.8f * 4);

    /**
     * 资源管理器
     */
    private AssetManager assetManager;
    /**
     * 音乐路径
     */
    public static final String musicPath = "home/audio/";

    public static final int GROUND_HIT = 1;
    public static final int WALL_HIT = 1 << 1;
    public static final int CRATE_HIT = 1 << 2;
    public static final int MARIO_HIT = 1 << 3;

    private static GameManager instance;

//    public static GameManager getInstance(float x, float y) {
//        if (instance == null) {
//            instance = new GameManager(x, y);
//        }
//        return instance;
//    }

    public GameManager(float w, float h) {
        V_WIDTH = w;
        V_HEIGHT = h;
        createAsset();
    }


    private void createAsset() {
        assetManager = new AssetManager();
        assetManager.load("home/audio/main_bg.mp3", Music.class);
        assetManager.finishLoading();
    }


    private String currentMusic = "";

    public void playMusic(String filename) {
        playMusic(filename, true);
    }

    public void playMusic(String filename, boolean loop) {
        if (!currentMusic.equals(filename)) {
            stopMusic();
            currentMusic = filename;
        }

        if (isPlayingMusic(currentMusic)) {
            return;
        }
        assetManager.get(musicPath + filename, Music.class).setLooping(loop);
        assetManager.get(musicPath + filename, Music.class).play();
    }

    public boolean isPlayingMusic() {
        return isPlayingMusic(currentMusic);
    }

    public void pauseMusic() {
        if (currentMusic.length() > 0) {
            assetManager.get(musicPath + currentMusic, Music.class).pause();
        }
    }

    public void resumeMusic() {
        if (currentMusic.length() > 0) {
            if (!isPlayingMusic(currentMusic)) {
                playMusic(currentMusic);
            }
        }
    }

    public void stopMusic() {
        if (currentMusic.length() > 0) {
            assetManager.get(musicPath + currentMusic, Music.class).stop();
        }
    }

    public boolean isPlayingMusic(String filename) {
        return assetManager.get(musicPath + filename, Music.class).isPlaying();
    }


    @Override
    public void dispose() {
        if (assetManager != null) {
            assetManager.dispose();
        }
    }
}
