package com.me.blawks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.me.blawks.Blawks;
import com.me.blawks.handlers.ControllerHandler;
import com.me.blawks.handlers.SoundHandler;

public class ScoresScreen implements Screen{

	private Blawks gameInstance;
	private Texture background;
	private SpriteBatch batch;
	private BitmapFont font;
	private Label label;

	//button creation
	private TextButton menuButton;
	private TextureAtlas buttonAtlas;
	private Skin skin;
	private Stage stage;
	private final float BUTTON_WIDTH = Gdx.graphics.getWidth()*.083f;
	private final float BUTTON_HEIGHT = Gdx.graphics.getHeight()*.125f;

	//controller handling
	private ControllerHandler controllerHandler;
	private Controller controller;

	public ScoresScreen(Blawks gameInstance) {
		this.gameInstance = gameInstance;
		background = new Texture(Gdx.files.internal("data/Backgrounds/scoreBackground.png"));
		batch = new SpriteBatch();

		font = new BitmapFont(Gdx.files.internal("data/fonts/font.fnt"));

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

		//process button input
		stage.act(delta);


		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		//draws button and scores
		stage.draw();


	}

	@Override
	public void resize(int width, int height) {
		if ( stage == null)
			stage = new Stage(width,height,true);
		stage.clear();
		//reinit buttons
		initializeButtons();
	}

	@Override
	public void show() {

		buttonAtlas = new TextureAtlas(Gdx.files.internal("data/buttons/button.atlas"));
		skin = new Skin();
		skin.addRegions(buttonAtlas);

		//load up scores on first show
		FileHandle input = Gdx.files.local("scores.txt");
		if (!(input.exists())) {
			//create empty file
			input.writeString("0", false);
		}

		LabelStyle style = new LabelStyle(font,Color.BLACK);
		label = new Label(input.readString(), style);
		label.setX(Gdx.graphics.getWidth()/4);
		label.setY(Gdx.graphics.getHeight()/12);
		label.setWidth(Gdx.graphics.getWidth()/2);
		label.setHeight(5*Gdx.graphics.getHeight()/6);
		label.setAlignment(Align.center);
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
		background.dispose();
		batch.dispose();
		font.dispose();
		buttonAtlas.dispose();
		skin.dispose();
		stage.dispose();
	}

	public void initializeButtons() {
		//Design button style
		TextButtonStyle style = new TextButtonStyle();
		style.up = skin.getDrawable("buttonUnpressed");
		style.down = skin.getDrawable("buttonPressed");
		font.setScale(.75f);
		style.font = font;
		style.fontColor = Color.BLACK;

		//Create buttons from style
		menuButton = new TextButton("Main\nMenu",style);
		menuButton.setWidth(BUTTON_WIDTH);
		menuButton.setHeight(BUTTON_HEIGHT);
		menuButton.setX(50);
		menuButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - 20);

		//Create listeners
		menuButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playSelectSound();
				//Change the screen when the button is let go
				gameInstance.setScreen(new MainMenu(gameInstance,false));
			}
		});

		//Add to stage
		stage.addActor(menuButton);
		stage.addActor(label);

		//set as an input listener
		Gdx.input.setInputProcessor(stage);
	}

}
