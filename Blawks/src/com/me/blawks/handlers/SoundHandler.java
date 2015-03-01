package com.me.blawks.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundHandler {

	private static final Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/backgroundMusic.mp3"));
	private static final Sound selectSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/selectNoise.wav"));
	private static final Sound blockMoveSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/blockMoveNoise.wav"));
	private static final Sound placeBlockSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/placeBlockNoise.wav"));
	private static final Sound cantPlaceSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/cantPlaceNoise.wav"));
	private static final Sound blockDestroyedSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/blockDestroyedNoise.wav"));
	
	public static void playBackgroundMusic(boolean isLooping) {
		backgroundMusic.setLooping(isLooping);
		backgroundMusic.play();
	}
	public static void pauseBackgroundMusic() {
		backgroundMusic.pause();
	}
	public static void playSelectSound() {
		selectSound.play();
	}
	public static void playBlockMoveSound() {
		blockMoveSound.play();
	}
	public static void playPlaceBlockSound() {
		placeBlockSound.play();
	}
	public static void playCantPlaceSound() {
		cantPlaceSound.play();
	}
	public static void playBlockDestroyedSound() {
		blockDestroyedSound.play();
	}
	public static void dispose() {
		backgroundMusic.dispose();
		selectSound.dispose();
		blockMoveSound.dispose();
		placeBlockSound.dispose();
		cantPlaceSound.dispose();
		blockDestroyedSound.dispose();
	}
	public static Music getBackgroundMusic() {
		return backgroundMusic;
	}

}
