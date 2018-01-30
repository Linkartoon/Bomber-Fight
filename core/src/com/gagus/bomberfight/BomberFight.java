package com.gagus.bomberfight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gagus.bomberfight.Screens.MainMenuScreen;

public class BomberFight extends Game {
	public OrthographicCamera camera;
	public Viewport viewport;
	public SpriteBatch batch;
	// Variables gloables
	public static final int HEIGHT = 330;
	public static final int WIDTH = 330*16/9;
	public static final int SQUARESIZE = 30;
	public static final Rectangle GameArea = new Rectangle((WIDTH-450)/2,0,450,330);
	
	@Override
	public void create () {
		Gdx.app.log("gameAera",GameArea.toString());
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new FitViewport(WIDTH,HEIGHT,camera);
		viewport.apply();
		camera.position.set(WIDTH/2,HEIGHT/2,0);
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width,height);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public static Vector2 getSquareByPosition(Vector2 position){
		return new Vector2((int)position.x/BomberFight.SQUARESIZE,(int)position.y/BomberFight.SQUARESIZE);
	}
}
