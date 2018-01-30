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
 * Created by Gaetan on 05/01/2018.
 */

public class LevelChoiceScreen implements Screen {
	Stage stage;
	final BomberFight game;
	Image backgroundActor;
	ImageButton levelOneButtonActor;
	ImageButton levelTwoButtonActor;
	ImageButton exitButtonActor;
	ImageButton backButtonActor;
	float marginAera;

	public LevelChoiceScreen(final BomberFight game) {
		this.game = game;
		stage = new Stage(game.viewport,game.batch);
		Gdx.input.setInputProcessor(stage);
		marginAera = BomberFight.GameArea.x;

		//images init
		TextureRegion backgroundImage = new TextureRegion(new Texture("images/backgrounds/fond2.png"));
		TextureRegion levelOneUpImage = new TextureRegion(new Texture("images/buttons/bouton-niveau1.png"));
		TextureRegion levelOneDownImage = new TextureRegion(new Texture("images/buttons/bouton-niveau1_2.png"));
		TextureRegion levelTwoUpImage = new TextureRegion(new Texture("images/buttons/bouton-niveau2.png"));
		TextureRegion levelTwoDownImage = new TextureRegion(new Texture("images/buttons/bouton-niveau2_2.png"));
		TextureRegion exitButtonUpImage = new TextureRegion(new Texture("images/buttons/bouton_quitter.png"));
		TextureRegion exitButtonDownImage = new TextureRegion(new Texture("images/buttons/bouton_quitter2.png"));
		TextureRegion backButtonUpImage = new TextureRegion(new Texture("images/buttons/bouton-retour.png"));
		TextureRegion backButtonDownImage = new TextureRegion(new Texture("images/buttons/bouton-retour2.png"));

		// actors init
		backgroundActor = new Image(backgroundImage);
		levelOneButtonActor = new ImageButton(new TextureRegionDrawable(levelOneUpImage),new TextureRegionDrawable(levelOneDownImage));
		levelTwoButtonActor = new ImageButton(new TextureRegionDrawable(levelTwoUpImage),new TextureRegionDrawable(levelTwoDownImage));
		exitButtonActor = new ImageButton(new TextureRegionDrawable(exitButtonUpImage),new TextureRegionDrawable(exitButtonDownImage));
		backButtonActor = new ImageButton(new TextureRegionDrawable(backButtonUpImage),new TextureRegionDrawable(backButtonDownImage));

		//set position of actors
		backgroundActor.setPosition(marginAera,0);
		backButtonActor.setPosition(game.WIDTH/2-backButtonActor.getWidth()+marginAera,10);
		exitButtonActor.setPosition(backButtonActor.getX(),backButtonActor.getY()+exitButtonActor.getHeight()+10);
		levelOneButtonActor.setPosition(game.WIDTH/3-levelOneButtonActor.getWidth()/2,exitButtonActor.getY()+exitButtonActor.getHeight()+10);
		levelTwoButtonActor.setPosition(game.WIDTH/3*2-levelTwoButtonActor.getWidth()/2,levelOneButtonActor.getY());

		//set Listeners to actors
		levelOneButtonActor.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch down on level one button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch up on level one button");
				//game.setScreen(new NameChoiceScreen(game,"map1.txt"));
				game.setScreen(new GameScreen(game,"map1.txt",new String[]{"dfzefer","frefer"}));
			}
		});

		levelTwoButtonActor.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch down on level two button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch up on level two button");
				game.setScreen(new NameChoiceScreen(game,"map2.txt"));
			}
		});

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

		backButtonActor.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch down on back button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch up on back button");
				game.setScreen(new MainMenuScreen(game));
			}
		});

		//add actors to stage
		stage.addActor(backgroundActor);
		stage.addActor(levelOneButtonActor);
		stage.addActor(levelTwoButtonActor);
		stage.addActor(exitButtonActor);
		stage.addActor(backButtonActor);
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

