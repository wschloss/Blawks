package com.me.blawks.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.blawks.handlers.SoundHandler;
import com.me.blawks.screens.GameScreen;

//The game board has all blocks that have been already been placed.  It also contains the algorithm for
//finding squares of four block with the same color for scoring points
public class GameBoard {

	//Reference to screen to update score
	private GameScreen screen;

	//Position of lower right corner
	private Vector2 position;
	//the bounds of the game board
	private Rectangle bounds;

	private Texture boardTexture;

	private float blockSize;
	private float blocksPerSide = 12;

	//all blocks that have been placed
	private Array<GameBlock> placedBlocks;

	public GameBoard(Vector2 position, float blockSize, GameScreen screen) {
		this.position = position;
		this.blockSize = blockSize;
		this.screen = screen;
		bounds = new Rectangle(position.x,position.y, (blocksPerSide+.5f)*blockSize, (blocksPerSide+.5f)*blockSize);

		//init texture
		boardTexture = new Texture(Gdx.files.internal("data/boardSprites/board.png"));

		//initialize the array of blocks
		placedBlocks = new Array<GameBlock>(); 
	}

	//draw method for the board with placed pieces.  windowWidth and windowHeight are the
	//dimensions of the game window, not the actual board.
	public void draw(SpriteBatch batch) {
		//draw background
		batch.draw(boardTexture, position.x, position.y, (blocksPerSide+.5f)*blockSize, (blocksPerSide+.5f)*blockSize);
		//draw placed blocks
		for (GameBlock block : placedBlocks) {
			float xPos = position.x + blockSize/4f + blockSize*block.getxIndex();
			float yPos = position.y + blockSize/4f + blockSize*block.getyIndex();
			block.draw(batch,xPos,yPos, blockSize);
		}
	}

	//finds and removes all color matches with the lastPlacedBlocks
	public void processMatches(Array<GameBlock> lastPlacedBlocks) {
		//For each block, set to visited, process matches around it, if enough matches,
		//remove the block and matches.
		for (GameBlock placedBlock : lastPlacedBlocks) {
			placedBlock.setVisited(true);
			for (GameBlock block : placedBlocks) {
				block.setVisited(false);
			}
			//find matches
			Array<GameBlock> matches = checkForMatches(placedBlock);
			//matches.add(placedBlock);
			//Removal
			if (matches.size >= 4) {
				for (GameBlock match : matches) {
					placedBlocks.removeValue(match, false);
				}
				SoundHandler.playBlockDestroyedSound();
				//update score
				screen.getScoreBoard().updateScore((int) Math.pow(ScoreBoard.scoreBaseFactor, matches.size + GamePiece.blocksPerPiece));
			}
		}
	}

	
	//checks for and returns all blocks of same color that are in a sequence from the lastPlacedBlocks
	public Array<GameBlock> checkForMatches(GameBlock block) {
		//push all blocks around the last placed piece of same color to a new array
		Array<GameBlock> adjacentMatches = new Array<GameBlock>();
		for (GameBlock placedBlock : placedBlocks) {
			//Check if adjacent
			if ((Math.abs(block.getxIndex()-placedBlock.getxIndex()) + Math.abs(block.getyIndex()-placedBlock.getyIndex())) == 1) {
				//Check for a color match and if the block has already been processed
				if (block.getColor() == placedBlock.getColor() && !(placedBlock.isVisited())) {
					placedBlock.setVisited(true);
					adjacentMatches.add(placedBlock);
				}
			}
		}

		//Recursive call to find adjacent blocks to the adjacent ones found
		if (adjacentMatches.size != 0) {
			for (GameBlock match : adjacentMatches) {
				adjacentMatches.addAll(checkForMatches(match));
			}
		}

		return adjacentMatches;
	}

	//Used to add blocks from a piece to the board
	public void addBlocks(Array<GameBlock> blocks) {
		placedBlocks.addAll(blocks);
		SoundHandler.playPlaceBlockSound();
	}


	//Checks to see if there are empty places where the piece is going to be placed.
	//Returns false if there is already a block in at least one of the cells.
	public boolean canAddBlocks(Array<GameBlock> blocks) {
		for (GameBlock block : blocks) {
			for (GameBlock placedBlock: placedBlocks) {
				if (block.getxIndex() == placedBlock.getxIndex() && block.getyIndex() == placedBlock.getyIndex()) {
					SoundHandler.playCantPlaceSound();
					return false;
				}
			}
		}
		return true;
	}

	public void dispose() {
		boardTexture.dispose();
		for (GameBlock block : placedBlocks) {
			block.dispose();
		}
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public float getBlocksPerSide() {
		return blocksPerSide;
	}

	public void setBlocksPerSide(float blocksPerSide) {
		this.blocksPerSide = blocksPerSide;
	}

}
