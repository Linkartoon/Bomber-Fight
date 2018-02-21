package com.gagus.bomberfight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gagus.bomberfight.BomberFight;

/**
 * Created by Gaetan on 03/02/2018.
 */

public class SelectionModeMenu implements Screen {
	Stage stage;
	BomberFight game;
	BitmapFont font;
	TextButton localButton, lanButton;

	public SelectionModeMenu(final BomberFight game) {
		this.game = game;
		stage = new Stage(game.viewport,game.batch);
		Gdx.input.setInputProcessor(stage);

		font = new BitmapFont(Gdx.files.internal("fonts/arialBlack50.fnt"));
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;

		localButton = new TextButton("Local Game",textButtonStyle);
		localButton.setPosition(game.WIDTH/2-localButton.getWidth()/2,game.HEIGHT/1.5f-localButton.getHeight()/1.5f);
		localButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("Selection menu","Click on local game button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new LevelChoiceScreen(game));
			}
		});
		stage.addActor(localButton);

		lanButton = new TextButton("Lan Game",textButtonStyle);
		lanButton.setPosition(game.WIDTH/2-lanButton.getWidth()/2,game.HEIGHT/3-lanButton.getHeight()/3);
		lanButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("Selection menu","Click on Lan game button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new LocalGameMenu(game));
			}
		});
		stage.addActor(lanButton);

		//Add actions to actors
		for(Actor actor : stage.getActors()){
			actor.addAction(Actions.fadeOut(0f));
			actor.addAction(Actions.fadeIn(0.5f));
		}
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
