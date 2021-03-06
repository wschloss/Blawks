package com.me.blawks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.blawks.Blawks;
import com.me.blawks.handlers.SoundHandler;

public class SplashScreen implements Screen {

	//reference to change the screen
	private Blawks gameInstance;

	private Texture gimmeGamesLogo;
	private Sprite fadingSprite;
	private SpriteBatch batch;

	private final float FADE_IN_TIME = 2;
	private final float DISPLAY_TIME = 1;
	private final float FADE_OUT_TIME = 2;
	//keeps track of time for fading purposes;
	private float timeCounter;
	//keeps track of current fading event
	private boolean fadingIn;
	private boolean fadingOut;
	private boolean displaying;

	public SplashScreen(Blawks gameInstance) {
		this.gameInstance = gameInstance;
		//initialize gimmeGamesLogo
		gimmeGamesLogo = new Texture(Gdx.files.internal("data/Backgrounds/GimmeGamesLogo.png"));
		fadingSprite = new Sprite(gimmeGamesLogo);
		fadingSprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//initialize fade variables
		timeCounter = 0;
		fadingIn = true;
		fadingOut = false;
		displaying = false;

		batch = new SpriteBatch();

		//start music
		SoundHandler.playBackgroundMusic(true);
	}

	@Override
	public void render(float delta) {
		//clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();

		if ( fadingIn && timeCounter <= FADE_IN_TIME ) {
			//draw with an increasing alpha
			fadingSprite.draw(batch, timeCounter/FADE_IN_TIME);
		} else {
			if (fadingIn) {
				//runs first time timeCounter is over the desired time
				fadingIn = false;
				//reset for next phase
				timeCounter = 0;
				displaying = true;
			}
		}

		if ( displaying && timeCounter <= DISPLAY_TIME) {
			//draw with full alpha
			fadingSprite.draw(batch, 1);
		} else {
			if (displaying) {
				displaying = false;
				timeCounter = 0;
				fadingOut = true;
			}
		}

		if ( fadingOut && timeCounter <= FADE_OUT_TIME) {
			//draw with decreasing alpha
			fadingSprite.draw(batch, 1f - timeCounter/FADE_OUT_TIME);
		} else {
			if( fadingOut) {
				//runs after done fading out
				fadingOut = false;
				timeCounter = 0;
				//SET MENU SCREEN HERE
				gameInstance.setScreen(new MainMenu(gameInstance,true));
			}
		}


		batch.end();

		//increment counter
		timeCounter += delta;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();
		gimmeGamesLogo.dispose();
	}

}
