package com.keyf.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MinecraftDrop extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	public Texture menuBackground;

	public void create() {
		batch = new SpriteBatch();

		batch = new SpriteBatch();
		font = new BitmapFont();
		menuBackground = new Texture(Gdx.files.internal("menuBackground.png"));
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}