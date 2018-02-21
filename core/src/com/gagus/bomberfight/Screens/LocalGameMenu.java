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

public class LocalGameMenu implements Screen {
	Stage stage;
	BomberFight game;
	BitmapFont font;
	TextButton serverButton, clientButton;

	public LocalGameMenu(final BomberFight game) {
		this.game = game;
		stage = new Stage(game.viewport,game.batch);
		Gdx.input.setInputProcessor(stage);

		font = new BitmapFont(Gdx.files.internal("fonts/arialBlack50.fnt"));
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;

		serverButton = new TextButton("server",textButtonStyle);
		serverButton.setPosition(game.WIDTH/2-serverButton.getWidth()/2,game.HEIGHT/1.5f-serverButton.getHeight()/1.5f);
		serverButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("LocalGameMenu","Click on server button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new LocalServerLobbyScreen(game));
			}
		});
		stage.addActor(serverButton);

		clientButton = new TextButton("client",textButtonStyle);
		clientButton.setPosition(game.WIDTH/2-clientButton.getWidth()/2,game.HEIGHT/3-clientButton.getHeight()/3);
		clientButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("LocalGameMenu","Click on client button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new LocalClientLobbyScreen(game));
			}
		});
		stage.addActor(clientButton);

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
