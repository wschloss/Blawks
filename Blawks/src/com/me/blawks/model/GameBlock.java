package com.me.blawks.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


//One game block.  A piece contains three of these
public class GameBlock {
	
	private Vector2 position;
	private int xIndex;
	private int yIndex;
	
	//color and texture
	private Color color;
	private Texture texture;
	
	//tells match check algorithm if this block has been processed already
	private boolean visited;
	
	public GameBlock(Vector2 position, int xIndex, int yIndex,Color color, Texture texture) {
		this.position = position;
		this.xIndex = xIndex;
		this.yIndex = yIndex;
		this.color = color;
		this.texture = texture;
		visited = false;
	}

	public void draw(SpriteBatch batch, float xPosition, float yPosition, float size) {
		batch.draw(texture, xPosition, yPosition, size, size);
	}
	
	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public int getxIndex() {
		return xIndex;
	}

	public void setxIndex(int xIndex) {
		this.xIndex = xIndex;
	}

	public int getyIndex() {
		return yIndex;
	}

	public void setyIndex(int yIndex) {
		this.yIndex = yIndex;
	}

	
	public void dispose() {
		texture.dispose();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
}
