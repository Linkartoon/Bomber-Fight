package com.gagus.bomberfight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gagus.bomberfight.BomberFight;

/**
 * Created by Gaetan on 24/12/2017.
 */

public class MainMenuScreen implements Screen {

	Stage stage;
	final BomberFight game;
	Image backgroundActor;
	ImageButton playButtonActor;
	ImageButton scoresButtonActor;
	ImageButton exitButtonActor;
	float marginAera;

	public MainMenuScreen(final BomberFight game) {
		this.game = game;
		stage = new Stage(game.viewport,game.batch);
		Gdx.input.setInputProcessor(stage);
		marginAera = BomberFight.GameArea.x;

		// Image's init
		TextureRegion backgroundImage = new TextureRegion(new Texture("images/backgrounds/fond.png"));
		TextureRegion playButtonUpImage = new TextureRegion(new Texture("images/buttons/bouton_jouer.png"));
		TextureRegion playButtonDownImage = new TextureRegion(new Texture("images/buttons/bouton_jouer2.png"));
		TextureRegion scoresButtonUpImage = new TextureRegion(new Texture("images/buttons/bouton_score.png"));
		TextureRegion scoresButtonDownImage = new TextureRegion(new Texture("images/buttons/bouton_score2.png"));
		TextureRegion exitButtonUpImage = new TextureRegion(new Texture("images/buttons/bouton_quitter.png"));
		TextureRegion exitButtonDownImage = new TextureRegion(new Texture("images/buttons/bouton_quitter2.png"));

		// Actors's init
		backgroundActor = new Image(backgroundImage);
		playButtonActor = new ImageButton(new TextureRegionDrawable(playButtonUpImage),new TextureRegionDrawable(playButtonDownImage));
		scoresButtonActor = new ImageButton(new TextureRegionDrawable(scoresButtonUpImage),new TextureRegionDrawable(scoresButtonDownImage));
		exitButtonActor = new ImageButton(new TextureRegionDrawable(exitButtonUpImage),new TextureRegionDrawable(exitButtonDownImage));
		//set positions of actors
		backgroundActor.setPosition(marginAera,0);
		playButtonActor.setPosition(game.WIDTH/2-playButtonUpImage.getRegionWidth()/2,game.HEIGHT*(0.01f*100/3)*2f-playButtonUpImage.getRegionHeight()/2);
		scoresButtonActor.setPosition(game.WIDTH/2-scoresButtonUpImage.getRegionWidth()/2,game.HEIGHT*(0.01f*100/3)*1.5f-scoresButtonUpImage.getRegionHeight()/2);
		exitButtonActor.setPosition(game.WIDTH/2-exitButtonUpImage.getRegionWidth()/2,game.HEIGHT*(0.01f*100/3)*1f-exitButtonUpImage.getRegionHeight()/2);
		//add Listeners to actors
		exitButtonActor.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch down on exit button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch up on exit button");
				Gdx.app.exit();
			}
		});
		playButtonActor.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch down on play button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch up on play button");
				game.setScreen(new LevelChoiceScreen(game));
			}
		});

		scoresButtonActor.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch down on scores button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch up on scores button");
				game.setScreen(new ScoresScreen(game));
			}
		});

		// Add actor to stage
		stage.addActor(backgroundActor);
		stage.addActor(playButtonActor);
		stage.addActor(scoresButtonActor);
		stage.addActor(exitButtonActor);
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
		stage.dispose();
	}
}
