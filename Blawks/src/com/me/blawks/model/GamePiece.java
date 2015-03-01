package com.me.blawks.model;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.blawks.handlers.SoundHandler;

//A piece consists of four blocks in a predetermined shape to be place on the board
public class GamePiece {

	private float blockSize;
	public static float blocksPerPiece = 3;
	//Number of blocks that span to the right or up from the reference block
	private int length;
	private int height;

	private Vector2 position;
	private final float SPEED = 10;

	//The location the player wants the piece to go to
	private Vector2 targetPosition;

	private Texture blockTexture1;
	private Texture blockTexture2;
	private Texture blockTexture3;
	private Color color1;
	private Color color2;
	private Color color3;

	//the blocks that make up the piece
	private Array<GameBlock> blocks;

	//Pieces generate color and shape randomly
	private Random random;

	public GamePiece(Vector2 position, float blockSize) {
		this.position = position;
		this.blockSize = blockSize;
		random = new Random();
		
		targetPosition = position.cpy();

		blocks = new Array<GameBlock>();
		pickColor();
		pickShape();

	}

	//randomly sets up the colors by initializing the correct texture
	public void pickColor() {
		int pick = random.nextInt(6);
		switch(pick) {
		case 0:
			blockTexture1 = new Texture(Gdx.files.internal("data/blockSprites/blueBlock.png"));
			color1 = Color.BLUE;
			break;
		case 1:
			blockTexture1 = new Texture(Gdx.files.internal("data/blockSprites/greenBlock.png"));
			color1 = Color.GREEN;
			break;
		case 2:
			blockTexture1 = new Texture(Gdx.files.internal("data/blockSprites/orangeBlock.png"));
			color1 = Color.ORANGE;
			break;
		case 3:
			blockTexture1 = new Texture(Gdx.files.internal("data/blockSprites/purpleBlock.png"));
			color1 = Color.MAGENTA;
			break;
		case 4:
			blockTexture1 = new Texture(Gdx.files.internal("data/blockSprites/redBlock.png"));
			color1 = Color.RED;
			break;
		case 5:
			blockTexture1 = new Texture(Gdx.files.internal("data/blockSprites/yellowBlock.png"));
			color1 = Color.YELLOW;
			break;
		}
		
		//2nd texture
		pick = random.nextInt(6);
		switch(pick) {
		case 0:
			blockTexture2 = new Texture(Gdx.files.internal("data/blockSprites/blueBlock.png"));
			color2 = Color.BLUE;
			break;
		case 1:
			blockTexture2 = new Texture(Gdx.files.internal("data/blockSprites/greenBlock.png"));
			color2 = Color.GREEN;
			break;
		case 2:
			blockTexture2 = new Texture(Gdx.files.internal("data/blockSprites/orangeBlock.png"));
			color2 = Color.ORANGE;
			break;
		case 3:
			blockTexture2 = new Texture(Gdx.files.internal("data/blockSprites/purpleBlock.png"));
			color2 = Color.MAGENTA;
			break;
		case 4:
			blockTexture2 = new Texture(Gdx.files.internal("data/blockSprites/redBlock.png"));
			color2 = Color.RED;
			break;
		case 5:
			blockTexture2 = new Texture(Gdx.files.internal("data/blockSprites/yellowBlock.png"));
			color2 = Color.YELLOW;
			break;
		}
		
		pick = random.nextInt(6);
		switch(pick) {
		case 0:
			blockTexture3 = new Texture(Gdx.files.internal("data/blockSprites/blueBlock.png"));
			color3 = Color.BLUE;
			break;
		case 1:
			blockTexture3 = new Texture(Gdx.files.internal("data/blockSprites/greenBlock.png"));
			color3 = Color.GREEN;
			break;
		case 2:
			blockTexture3 = new Texture(Gdx.files.internal("data/blockSprites/orangeBlock.png"));
			color3 = Color.ORANGE;
			break;
		case 3:
			blockTexture3 = new Texture(Gdx.files.internal("data/blockSprites/purpleBlock.png"));
			color3 = Color.MAGENTA;
			break;
		case 4:
			blockTexture3 = new Texture(Gdx.files.internal("data/blockSprites/redBlock.png"));
			color3 = Color.RED;
			break;
		case 5:
			blockTexture3 = new Texture(Gdx.files.internal("data/blockSprites/yellowBlock.png"));
			color3 = Color.YELLOW;
			break;
		}
	}

	//randomly sets up the piece shape and correctly initializes blocks
	public void pickShape() {
		int pick = random.nextInt(6);
		switch(pick) {
		case 0:
			createHorizontalLine();
			break;
		case 1:
			createVerticalLine();
			break;
		case 2:
			createRightL();
			break;
		case 3:
			createLeftL();
			break;
		case 4:
			createRightAngle();
			break;
		case 5:
			createLeftAngle();
			break;
		}
	}

	public void draw(SpriteBatch batch, float blockSize) {
		for (GameBlock block : blocks)
			batch.draw(block.getTexture(),block.getPosition().x,block.getPosition().y,blockSize,blockSize);
	}

	public void update() {
		//move main piece (farthest left) towards target position
		Vector2 temp = position.cpy();
		position.lerp(targetPosition, SPEED*Gdx.graphics.getDeltaTime());
		//update every block position by same amount
		for (GameBlock block : blocks) {
			block.getPosition().add(position.cpy().sub(temp));
		}
	}

	//Shifting function move the piece by one game block distance
	public void shiftRight(Rectangle bounds) {
		if (bounds.contains(position.x + (length + 1)*blockSize, position.y)) {
			targetPosition.x += blockSize;
			for (GameBlock block : blocks) {
				block.setxIndex(block.getxIndex() + 1);
			}
			SoundHandler.playBlockMoveSound();
		}
	}

	public void shiftLeft(Rectangle bounds) {
		if (bounds.contains(position.x - blockSize, position.y)) {
			targetPosition.x -= blockSize;
			for (GameBlock block : blocks) {
				block.setxIndex(block.getxIndex() - 1);
			}
			SoundHandler.playBlockMoveSound();
		}
	}

	public void shiftUp(Rectangle bounds) {
		if (bounds.contains(position.x, position.y + (height+1)*blockSize)) {
			targetPosition.y += blockSize;
			for (GameBlock block : blocks) {
				block.setyIndex(block.getyIndex() + 1);
			}
			SoundHandler.playBlockMoveSound();
		}
	}

	public void shiftDown(Rectangle bounds) {
		if (bounds.contains(position.x, position.y - blockSize)) {
			targetPosition.y -= blockSize;
			for (GameBlock block : blocks) {
				block.setyIndex(block.getyIndex() - 1);
			}
			SoundHandler.playBlockMoveSound();
		}
	}

	public void dispose() {
		blockTexture1.dispose();
		blockTexture2.dispose();
		blockTexture3.dispose();
		for (GameBlock block : blocks)
			block.dispose();
	}

	public Color getColor() {
		return color1;
	}

	public Array<GameBlock> getBlocks() {
		return blocks;
	}

	public void createHorizontalLine() {
		//reference block
		blocks.add(new GameBlock(position.cpy(),6,6,color1,blockTexture1));
		//two blocks to the right
		blocks.add(new GameBlock(new Vector2(position.x + blockSize,position.y),7,6,color2,blockTexture2));
		blocks.add(new GameBlock(new Vector2(position.x + 2*blockSize,position.y),8,6,color3,blockTexture3));

		//initialize dimension variables
		length = 3;
		height = 1;
	}

	public void createVerticalLine() {
		//reference block
		blocks.add(new GameBlock(position.cpy(),6,6,color1,blockTexture1));
		//two blocks to above
		blocks.add(new GameBlock(new Vector2(position.x,position.y + blockSize),6,7,color2,blockTexture2));
		blocks.add(new GameBlock(new Vector2(position.x,position.y+ 2*blockSize),6,8,color3,blockTexture3));

		//initialize dimension variables
		length = 1;
		height = 3;
	}

	public void createRightL() {
		//reference block
		blocks.add(new GameBlock(position.cpy(),6,6,color1,blockTexture1));
		//one above and one to the right
		blocks.add(new GameBlock(new Vector2(position.x,position.y + blockSize),6,7,color2,blockTexture2));
		blocks.add(new GameBlock(new Vector2(position.x + blockSize,position.y),7,6,color3,blockTexture3));

		//initialize dimension variables
		length = 2;
		height = 2;
	}

	public void createLeftL() {
		//reference block
		blocks.add(new GameBlock(position.cpy(),6,6,color1,blockTexture1));
		//one to the right and above that
		blocks.add(new GameBlock(new Vector2(position.x + blockSize,position.y),7,6,color2,blockTexture2));
		blocks.add(new GameBlock(new Vector2(position.x + blockSize,position.y + blockSize),7,7,color3,blockTexture3));

		//initialize dimension variables
		length = 2;
		height = 2;
	}

	public void createRightAngle() {
		//reference block is invisible, add the block above first
		blocks.add(new GameBlock(new Vector2(position.x,position.y + blockSize),6,7,color1,blockTexture1));
		//one to the right and below that
		blocks.add(new GameBlock(new Vector2(position.x + blockSize,position.y+blockSize),7,7,color2,blockTexture2));
		blocks.add(new GameBlock(new Vector2(position.x + blockSize,position.y),7,6,color3,blockTexture3));

		//initialize dimension variables
		length = 2;
		height = 2;
	}

	public void createLeftAngle() {
		//reference block
		blocks.add(new GameBlock(position.cpy(),6,6,color1,blockTexture1));
		//one above and one to the right of that
		blocks.add(new GameBlock(new Vector2(position.x,position.y + blockSize),6,7,color2,blockTexture2));
		blocks.add(new GameBlock(new Vector2(position.x + blockSize,position.y + blockSize),7,7,color3,blockTexture3));

		//initialize dimension variables
		length = 2;
		height = 2;
	}
}
