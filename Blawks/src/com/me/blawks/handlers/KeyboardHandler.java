package com.me.blawks.handlers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.me.blawks.Blawks;
import com.me.blawks.model.GameBoard;
import com.me.blawks.model.GamePiece;
import com.me.blawks.screens.GameScreen;

public class KeyboardHandler implements InputProcessor {

	//access to the game instance, the current piece, and the game board
	private Blawks gameInstance;
	private GameScreen screen;
	private GameBoard board;
	private GamePiece currentPiece;

	public KeyboardHandler(Blawks gameInstance, GameScreen screen, GameBoard board) {
		this.gameInstance = gameInstance;
		this.screen = screen;
		this.board = board;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.W:
			if (currentPiece != null)
				currentPiece.shiftUp(board.getBounds());
			break;
		case Keys.A:
			if (currentPiece != null)
				currentPiece.shiftLeft(board.getBounds());
			break;
		case Keys.S:
			if (currentPiece != null)
				currentPiece.shiftDown(board.getBounds());
			break;
		case Keys.D:
			if (currentPiece != null)
				currentPiece.shiftRight(board.getBounds());
			break;
		case Keys.ENTER:
			if (currentPiece != null) {
				if (board.canAddBlocks(currentPiece.getBlocks())) {
					board.addBlocks(currentPiece.getBlocks());
					board.processMatches(currentPiece.getBlocks());
					currentPiece = null;
					screen.setCurrentPiece(null);
				}
			}
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCurrentPiece(GamePiece currentPiece) {
		this.currentPiece = currentPiece;
	}


}
