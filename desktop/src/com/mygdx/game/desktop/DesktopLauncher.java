package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.myhome.InitEntry;
import com.mygdx.game.supmario.SuperMario;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Super Jumper";
//		new LwjglApplication(new SuperMario(15,20), config);
        new LwjglApplication(new InitEntry(15,20),config);
	}
}
