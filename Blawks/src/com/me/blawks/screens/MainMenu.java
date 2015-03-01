package com.me.blawks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.me.blawks.Blawks;
import com.me.blawks.handlers.ControllerHandler;
import com.me.blawks.handlers.SoundHandler;

public class MainMenu implements Screen {
	//reference for setting new screens
	private Blawks gameInstance;

	//for fading in and displaying main menu background
	private Texture logoBackgroundTexture;
	private Sprite logoBackground;
	private SpriteBatch batch;
	private final float FADE_IN_TIME = 2;
	private boolean fadingIn;
	private float timeCounter;

	//Members for button creation
	private TextButton playButton, optionsButton, scoresButton;
	private TextureAtlas buttonAtlas;
	private Skin skin;
	private Stage stage;
	private BitmapFont font;
	private final float BUTTON_WIDTH = 300f;
	private final float BUTTON_HEIGHT = 100f;

	//Controller listening
	private ControllerHandler controllerHandler;
	private Controller controller;

	public MainMenu(Blawks gameInstance, boolean fadeIn) {
		this.gameInstance = gameInstance;
		//initialize background image objects
		logoBackgroundTexture = new Texture(Gdx.files.internal("data/Backgrounds/MainMenu.png"));
		logoBackground = new Sprite(logoBackgroundTexture);
		logoBackground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		fadingIn = fadeIn;

		//timeCounter set accordingly since the background alpha is set as timeCounter/FADE_IN_TIME
		if (fadeIn) {
			timeCounter = 0;
		} else {
			timeCounter = FADE_IN_TIME;
		}

		batch = new SpriteBatch();

		controllerHandler = new ControllerHandler(gameInstance,this,null);
		if (Controllers.getControllers().size != 0) {
			controller = Controllers.getControllers().first();
			controller.addListener(controllerHandler);
		}

	}

	@Override
	public void render(float delta) {
		//clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		//process button input
		stage.act(delta);

		batch.begin();
		//draw background
		logoBackground.draw(batch, timeCounter/FADE_IN_TIME);
		batch.end();

		//draw button stage on top of images in the batch
		stage.draw();

		//increment time if fading in
		if ( fadingIn && timeCounter <= FADE_IN_TIME ) {
			timeCounter += delta;
		} else if (fadingIn) {
			//setup the menu buttons now
			initializeButtons();
			fadingIn = false;
		}

	}

	@Override
	public void resize(int width, int height) {
		if (stage == null)
			stage = new Stage(width,height,true);
		stage.clear();
		//reinit buttons if done fading in
		if (!fadingIn)
			initializeButtons();
	}

	@Override
	public void show() {
		//Initialize needed variables
		skin = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("data/buttons/button.atlas"));
		skin.addRegions(buttonAtlas);
		font = new BitmapFont(Gdx.files.internal("data/fonts/font.fnt"));
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		if (controller != null)
			controller.removeListener(controllerHandler);
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
		logoBackgroundTexture.dispose();
		font.dispose();
		batch.dispose();
		skin.dispose();
		buttonAtlas.dispose();
		stage.dispose();
	}

	//Sets up the menu buttons and adds them to the stage to be rendered
	public void initializeButtons() {

		//Design button style
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("buttonUnpressed");
		style.down = skin.getDrawable("buttonPressed");
		style.font = font;
		style.fontColor = Color.BLACK;

		//Create buttons from style
		playButton = new TextButton("New Game",style);
		playButton.setWidth(BUTTON_WIDTH);
		playButton.setHeight(BUTTON_HEIGHT);
		playButton.setX(Gdx.graphics.getWidth()/2);
		playButton.setY(Gdx.graphics.getHeight()/2);

		scoresButton = new TextButton("Scores",style);
		scoresButton.setWidth(BUTTON_WIDTH);
		scoresButton.setHeight(BUTTON_HEIGHT);
		scoresButton.setX(Gdx.graphics.getWidth()/2);
		scoresButton.setY(Gdx.graphics.getHeight()/2 - BUTTON_HEIGHT - 20);

		optionsButton = new TextButton("Options",style);
		optionsButton.setWidth(BUTTON_WIDTH);
		optionsButton.setHeight(BUTTON_HEIGHT);
		optionsButton.setX(Gdx.graphics.getWidth()/2);
		optionsButton.setY(Gdx.graphics.getHeight()/2 - 2*BUTTON_HEIGHT - 40);

		//Create listeners
		playButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playSelectSound();
				//Change the screen when the button is let go
				gameInstance.setScreen(new GameScreen(gameInstance));
			}
		});

		scoresButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playSelectSound();
				//Change the screen when the button is let go
				gameInstance.setScreen(new ScoresScreen(gameInstance));
			}
		});

		optionsButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playSelectSound();
				//Change the screen when the button is let go
				gameInstance.setScreen(new OptionsScreen(gameInstance));
			}
		});

		//Add to stage
		stage.addActor(playButton);
		stage.addActor(scoresButton);
		stage.addActor(optionsButton);

		//set as input listener
		Gdx.input.setInputProcessor(stage);
	}

}
