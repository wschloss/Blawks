package com.me.blawks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.me.blawks.handlers.SoundHandler;
import com.me.blawks.screens.SplashScreen;

public class Blawks extends Game {

	//Version string
	public static final String version = "1.1 Beta";
	private FPSLogger fpsLogger;


	@Override
	public void create() {
		//Set new splash screen on startup
		setScreen(new SplashScreen(this));

		//fps monitor
		fpsLogger = new FPSLogger();
	}

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
		SoundHandler.dispose();
	}

	@Override
	public void render() {		
		super.render();
		//Monitor fps
		//fpsLogger.log();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
