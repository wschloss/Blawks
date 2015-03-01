package com.me.blawks.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ScoreBoard {
	
	private Vector2 position;
	private Texture background;
	private BitmapFont scoreFont;
	private int score;
	private float width, height;
	
	//points are awarded as scoreBaseFactor to the number of blocks destroyed power
	public static int scoreBaseFactor = 2;

	public ScoreBoard(Vector2 position, float width, float height) {
		this.position = position;
		this.width = width;
		this.height = height;
		score = 0;
		scoreFont = new BitmapFont(Gdx.files.internal("data/fonts/font.fnt"));
		scoreFont.setColor(Color.BLACK);
		scoreFont.setScale(.8f);
		background = new Texture(Gdx.files.internal("data/Backgrounds/scoreBackground.png"));
	}
	
	public void updateScore(int scoredPoints) {
		score += scoredPoints;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(background,position.x,position.y,width,height);
		scoreFont.draw(batch, String.valueOf(score), position.x + width/3f, position.y + height/2);
	}
	
	//Saves and ranks the current score
	public void saveScore() {
		
		FileHandle input = Gdx.files.local("scores.txt");
		if (!(input.exists())) {
			input.writeString("0", false);
		}
		//split input at newline characters
		String splitStr[] = input.readString().split("\\r?\\n");
		
		//Parse integers from string segments
		Array<Integer> scores = new Array<Integer>();
		for (int i = 0; i < splitStr.length; i++) {
			scores.add(Integer.parseInt(splitStr[i]));
		}
		//add new score and remove lowest if more than ten scores
		scores.add(score);
		if (scores.size > 10) {
			int lowest = scores.first();
			for (Integer scr : scores) {
				if (scr < lowest)
					lowest = scr;
			}
			scores.removeValue(lowest, false);
		}
		//sort and print back to file
		scores.sort();
		String printOut = "";
		for (int i = scores.size - 1; i >= 0; i--) {
			printOut += String.valueOf(scores.get(i));
			printOut += "\n";
		}
		input.writeString(printOut, false);
	
	}
	
	public void dispose() {
		scoreFont.dispose();
		background.dispose();
	}

}
