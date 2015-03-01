package com.me.blawks.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.math.Vector3;
import com.me.blawks.Blawks;
import com.me.blawks.model.GameBoard;
import com.me.blawks.model.GamePiece;
import com.me.blawks.screens.GameScreen;
import com.me.blawks.screens.MainMenu;
import com.me.blawks.screens.OptionsScreen;
import com.me.blawks.screens.ScoresScreen;

public class ControllerHandler implements ControllerListener {

	private Blawks gameInstance;
	private Screen screen;
	private GameBoard board;
	private GamePiece currentPiece;

	public ControllerHandler(Blawks gameInstance, Screen screen, GameBoard board) {
		this.gameInstance = gameInstance;
		this.screen = screen;
		this.board = board;

	}

	public void setCurrentPiece(GamePiece currentPiece) {
		this.currentPiece = currentPiece;
	}

	@Override
	public boolean accelerometerMoved(Controller arg0, int arg1, Vector3 arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean axisMoved(Controller arg0, int arg1, float arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean buttonDown(Controller arg0, int arg1) {
		if (screen.getClass().equals(MainMenu.class)) {

			if (arg1 == Ouya.BUTTON_O) {
				SoundHandler.playSelectSound();
				gameInstance.setScreen(new GameScreen(gameInstance));
			} else if (arg1 == Ouya.BUTTON_A) {
				SoundHandler.playSelectSound();
				gameInstance.setScreen(new OptionsScreen(gameInstance));
			} else if (arg1 == Ouya.BUTTON_U) {
				SoundHandler.playSelectSound();
				gameInstance.setScreen(new ScoresScreen(gameInstance));
			}

		} else if (screen.getClass().equals(OptionsScreen.class)) {

			if (arg1 == Ouya.BUTTON_O) {
				SoundHandler.playSelectSound();
				if (SoundHandler.getBackgroundMusic().isPlaying()) {
					SoundHandler.pauseBackgroundMusic();
				} else SoundHandler.playBackgroundMusic(true);
			} else if (arg1 == Ouya.BUTTON_A) {
				SoundHandler.playSelectSound();
				gameInstance.setScreen(new MainMenu(gameInstance, false));
			} else if (arg1 == Ouya.BUTTON_U) {
				SoundHandler.playSelectSound();
				Gdx.files.local("scores.txt").writeString("0", false);
			}

		} else if (screen.getClass().equals(ScoresScreen.class)) {

			if (arg1 == Ouya.BUTTON_A) {
				SoundHandler.playSelectSound();
				gameInstance.setScreen(new MainMenu(gameInstance,false));
			}

		} else if (screen.getClass().equals(GameScreen.class)) {

			if (arg1 == Ouya.BUTTON_O) {
				if (currentPiece != null) {
					if (board.canAddBlocks(currentPiece.getBlocks())) {
						board.addBlocks(currentPiece.getBlocks());
						board.processMatches(currentPiece.getBlocks());
						currentPiece = null;
						((GameScreen) screen).setCurrentPiece(null);
					}
				}
			} else if (arg1 == Ouya.BUTTON_A) {
				SoundHandler.playSelectSound();
				((GameScreen) screen).getScoreBoard().saveScore();
				gameInstance.setScreen(new MainMenu(gameInstance,false));
			} else if (arg1 == Ouya.BUTTON_DPAD_UP || ((GameScreen) screen).getController().getAxis(Ouya.AXIS_LEFT_Y) > Ouya.STICK_DEADZONE) {
				currentPiece.shiftUp(board.getBounds());
			} else if (arg1 == Ouya.BUTTON_DPAD_DOWN || ((GameScreen) screen).getController().getAxis(Ouya.AXIS_LEFT_Y) < -Ouya.STICK_DEADZONE) {
				currentPiece.shiftDown(board.getBounds());
			} else if (arg1 == Ouya.BUTTON_DPAD_LEFT || ((GameScreen) screen).getController().getAxis(Ouya.AXIS_LEFT_X) < -Ouya.STICK_DEADZONE) {
				currentPiece.shiftLeft(board.getBounds());
			} else if (arg1 == Ouya.BUTTON_DPAD_RIGHT || ((GameScreen) screen).getController().getAxis(Ouya.AXIS_LEFT_X) > Ouya.STICK_DEADZONE) {
				currentPiece.shiftRight(board.getBounds());
			} else if (arg1 == Ouya.BUTTON_U) {
				SoundHandler.playSelectSound();
				((GameScreen) screen).getScoreBoard().saveScore();
				gameInstance.setScreen(new GameScreen(gameInstance));
			}

		}


		return true;
	}

	@Override
	public boolean buttonUp(Controller arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void connected(Controller arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnected(Controller arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean povMoved(Controller arg0, int arg1, PovDirection arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

}
