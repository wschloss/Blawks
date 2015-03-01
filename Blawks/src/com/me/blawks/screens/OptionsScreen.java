package com.me.blawks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

public class OptionsScreen implements Screen {

	//reference to change screens
	private Blawks gameInstance;

	//Background texture and drawing members
	private Texture background;
	private SpriteBatch batch;

	//Members for button creation
	private TextButton backButton, toggleMusicButton,resetScoresButton;
	private TextureAtlas buttonAtlas;
	private Skin skin;
	private Stage stage;
	private BitmapFont font;
	private final float BUTTON_WIDTH = 300f;
	private final float BUTTON_HEIGHT = 100f;

	//controller listening
	private ControllerHandler controllerHandler;
	private Controller controller;

	public OptionsScreen(Blawks gameInstance) {
		this.gameInstance = gameInstance;

		background = new Texture(Gdx.files.internal("data/Backgrounds/optionsScreen.png"));
		batch = new SpriteBatch();

		controllerHandler = new ControllerHandler(gameInstance,this,null);
		if (Controllers.getControllers().size != 0) {
			controller = Controllers.getControllers().first();
			controller.addListener(controllerHandler);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(background, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.end();

		stage.act(delta);

		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		if (stage == null)
			stage = new Stage(width,height,true);
		stage.clear();
		//reinit buttons
		initializeButtons();

	}

	@Override
	public void show() {
		//init button creation variables
		buttonAtlas = new TextureAtlas(Gdx.files.internal("data/buttons/button.atlas"));
		skin = new Skin();
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
		skin.dispose();
		buttonAtlas.dispose();
		stage.dispose();
		font.dispose();
		batch.dispose();
		background.dispose();
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
		backButton = new TextButton("Main Menu",style);
		backButton.setWidth(BUTTON_WIDTH);
		backButton.setHeight(BUTTON_HEIGHT);
		backButton.setX(Gdx.graphics.getWidth()/2);
		backButton.setY(Gdx.graphics.getHeight()/2 - 2*BUTTON_HEIGHT - 40);

		toggleMusicButton = new TextButton("Toggle Music",style);
		toggleMusicButton.setWidth(BUTTON_WIDTH);
		toggleMusicButton.setHeight(BUTTON_HEIGHT);
		toggleMusicButton.setX(Gdx.graphics.getWidth()/2);
		toggleMusicButton.setY(Gdx.graphics.getHeight()/2);

		resetScoresButton = new TextButton("Reset Scores",style);
		resetScoresButton.setWidth(BUTTON_WIDTH);
		resetScoresButton.setHeight(BUTTON_HEIGHT);
		resetScoresButton.setX(Gdx.graphics.getWidth()/2);
		resetScoresButton.setY(Gdx.graphics.getHeight()/2 - BUTTON_HEIGHT - 20);

		//Create listeners
		backButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playSelectSound();
				//Change the screen when the button is let go
				gameInstance.setScreen(new MainMenu(gameInstance,false));
			}
		});

		toggleMusicButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playSelectSound();
				if (SoundHandler.getBackgroundMusic().isPlaying()) {
					SoundHandler.pauseBackgroundMusic();
				} else SoundHandler.playBackgroundMusic(true);
			}
		});

		resetScoresButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playSelectSound();
				Gdx.files.local("scores.txt").writeString("0", false);
			}
		});

		//Add to stage
		stage.addActor(backButton);
		stage.addActor(toggleMusicButton);
		stage.addActor(resetScoresButton);

		//set as input listener
		Gdx.input.setInputProcessor(stage);
	}

}
