package com.mygdx.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.myhome.InitEntry;
import com.mygdx.game.superjumper.SuperJumper;
import com.mygdx.game.supmario.SuperMario;
import com.mygdx.game.supmario.gamesys.GameManager;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setManagerViewportSize();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useWakelock = true;
        int[] size = setManagerViewportSize();
        //initialize(new SuperMario(size[0], size[1]), config);
        initialize(new InitEntry(size[0],size[1]),config);
    }

    private int[] setManagerViewportSize() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            defaultDisplay.getRealSize(point);
        } else {
            defaultDisplay.getSize(point);
        }
        int x = point.x;
        int y = point.y - getStatusBarHeight(this);
        return new int[]{x, y};
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}
