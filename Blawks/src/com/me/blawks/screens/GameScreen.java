package com.me.blawks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.me.blawks.Blawks;
import com.me.blawks.handlers.ControllerHandler;
import com.me.blawks.handlers.KeyboardHandler;
import com.me.blawks.handlers.SoundHandler;
import com.me.blawks.model.GameBoard;
import com.me.blawks.model.GamePiece;
import com.me.blawks.model.ScoreBoard;

public class GameScreen implements Screen{

	//reference for changing screens
	private Blawks gameInstance;

	//Input handling storage
	private InputMultiplexer inputMultiplexer;
	private KeyboardHandler keyboardHandler;
	private ControllerHandler controllerHandler;
	private Controller controller;

	private Texture background;

	//Members for button creation
	private TextButton menuButton;
	private TextButton upButton;
	private TextButton leftButton;
	private TextButton rightButton;
	private TextButton downButton;
	private TextButton placeButton;
	private TextButton restartButton;
	private TextureAtlas buttonAtlas;
	private Skin skin;
	private Stage stage;
	private BitmapFont font;
	private final float BUTTON_WIDTH = Gdx.graphics.getWidth()*.083f;
	private final float BUTTON_HEIGHT = Gdx.graphics.getHeight()*.125f;

	//Game objects and drawing members
	private SpriteBatch batch;
	private GameBoard board;
	private GamePiece currentPiece;
	private ScoreBoard scoreBoard;

	//Game block size - A board is 8x8 blocks
	public final float GAME_BLOCK_SIZE = Gdx.graphics.getHeight()*.065f;

	public GameScreen(Blawks gameInstance) {
		this.gameInstance = gameInstance;

		batch = new SpriteBatch();

		background = new Texture(Gdx.files.internal("data/Backgrounds/GameScreen.jpg"));

		//init game objects, pieces are initialized in updateGameObjects() function
		board = new GameBoard(new Vector2(Gdx.graphics.getWidth()/6 + BUTTON_WIDTH + 25,Gdx.graphics.getHeight()/14),GAME_BLOCK_SIZE,this);
		scoreBoard = new ScoreBoard(new Vector2(board.getPosition().x + 13f*GAME_BLOCK_SIZE - 10,Gdx.graphics.getHeight()/2),Gdx.graphics.getWidth()/7,Gdx.graphics.getHeight()/10);

		//Setup input handler
		inputMultiplexer = new InputMultiplexer();
		keyboardHandler = new KeyboardHandler(gameInstance,this,board);
		controllerHandler = new ControllerHandler(gameInstance,this,board);

		if (Controllers.getControllers().size != 0) {
			controller = Controllers.getControllers().first();
			controller.addListener(controllerHandler);
		}

		inputMultiplexer.addProcessor(keyboardHandler);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		updateGameObjects();
		stage.act(delta);

		//Draw background and game objects
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		board.draw(batch);
		scoreBoard.draw(batch);
		if (currentPiece != null)
			currentPiece.draw(batch, GAME_BLOCK_SIZE);
		batch.end();
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
		buttonAtlas.dispose();
		skin.dispose();
		font.dispose();
		stage.dispose();
		batch.dispose();
		board.dispose();
		currentPiece.dispose();
	}

	//calls update on all game members and creates new pieces once current is placed
	public void updateGameObjects() {
		if (currentPiece == null) {
			Vector2 piecePosition = new Vector2();
			piecePosition.x = board.getPosition().x + board.getBlocksPerSide()*GAME_BLOCK_SIZE/2 + GAME_BLOCK_SIZE/4f;
			piecePosition.y = board.getPosition().y + board.getBlocksPerSide()*GAME_BLOCK_SIZE/2 + GAME_BLOCK_SIZE/4f;
			currentPiece = new GamePiece(piecePosition,GAME_BLOCK_SIZE);
			keyboardHandler.setCurrentPiece(currentPiece);
			controllerHandler.setCurrentPiece(currentPiece);
		} else currentPiece.update();
	}

	//Sets up the menu buttons and adds them to the stage to be rendered
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

		restartButton = new TextButton("Start\nOver",style);
		restartButton.setWidth(BUTTON_WIDTH);
		restartButton.setHeight(BUTTON_HEIGHT);
		restartButton.setX(Gdx.graphics.getWidth() - 50 - BUTTON_WIDTH);
		restartButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - 20);

		upButton = new TextButton("Up",style);
		upButton.setWidth(BUTTON_WIDTH);
		upButton.setHeight(BUTTON_HEIGHT);
		upButton.setX(BUTTON_WIDTH + 20);
		upButton.setY(2*BUTTON_HEIGHT + 20);

		leftButton = new TextButton("Left",style);
		leftButton.setWidth(BUTTON_WIDTH);
		leftButton.setHeight(BUTTON_HEIGHT);
		leftButton.setX(20);
		leftButton.setY(BUTTON_HEIGHT + 20);

		rightButton = new TextButton("Right",style);
		rightButton.setWidth(BUTTON_WIDTH);
		rightButton.setHeight(BUTTON_HEIGHT);
		rightButton.setX(2*BUTTON_WIDTH + 20);
		rightButton.setY(BUTTON_HEIGHT + 20);

		downButton = new TextButton("Down",style);
		downButton.setWidth(BUTTON_WIDTH);
		downButton.setHeight(BUTTON_HEIGHT);
		downButton.setX(BUTTON_WIDTH + 20);
		downButton.setY(20);

		placeButton = new TextButton("Place",style);
		placeButton.setWidth(BUTTON_WIDTH);
		placeButton.setHeight(BUTTON_HEIGHT);
		placeButton.setX(board.getPosition().x + 13f*GAME_BLOCK_SIZE - 10);
		placeButton.setY(20 + BUTTON_HEIGHT);

		//Create listeners
		menuButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playSelectSound();
				//save score
				scoreBoard.saveScore();
				//Change the screen when the button is let go
				gameInstance.setScreen(new MainMenu(gameInstance,false));
			}
		});

		restartButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playSelectSound();
				//save score
				scoreBoard.saveScore();
				//Change the screen when the button is let go
				gameInstance.setScreen(new GameScreen(gameInstance));
			}
		});

		upButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				currentPiece.shiftUp(board.getBounds());
			}
		});

		leftButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				currentPiece.shiftLeft(board.getBounds());
			}
		});

		rightButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				currentPiece.shiftRight(board.getBounds());
			}
		});

		downButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				currentPiece.shiftDown(board.getBounds());
			}
		});

		placeButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (currentPiece != null) {
					if (board.canAddBlocks(currentPiece.getBlocks())) {
						board.addBlocks(currentPiece.getBlocks());
						board.processMatches(currentPiece.getBlocks());
						currentPiece = null;
					}
				}
			}
		});

		//Add to stage
		stage.addActor(menuButton);
		stage.addActor(restartButton);
		stage.addActor(upButton);
		stage.addActor(leftButton);
		stage.addActor(rightButton);
		stage.addActor(downButton);
		stage.addActor(placeButton);

		//set as an input listener
		inputMultiplexer.addProcessor(stage);
	}

	public void setCurrentPiece(GamePiece piece) {
		currentPiece = piece;
	}

	public ScoreBoard getScoreBoard() {
		return scoreBoard;
	}

	public Controller getController() {
		return controller;
	}

}
