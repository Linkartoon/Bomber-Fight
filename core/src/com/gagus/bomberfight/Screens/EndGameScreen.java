package com.gagus.bomberfight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.gagus.bomberfight.Actors.Player;
import com.gagus.bomberfight.BomberFight;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gaetan on 02/01/2018.
 */

public class EndGameScreen implements Screen {
	Stage stage;
	BomberFight game;
	Player winner;
	float marginAera;
	ImageButton menuButtonActor;
	ImageButton scoresButtonActor;
	ImageButton exitButtonActor;
	Image backgroundActor;

	//text for winner
	Label textPlayerActor;
	Label textWinnerActor;
	Label textWinActor;
	BitmapFont fontWinner;

	//text for equality
	Label textEqualityActor;


	public EndGameScreen(final BomberFight game, Player winner, boolean equality) {
		this.game = game;
		this.winner = winner;
		stage = new Stage(game.viewport,game.batch);
		Gdx.input.setInputProcessor(stage);
		marginAera = BomberFight.GameArea.x;

		// labelstyle init
		fontWinner = new BitmapFont(Gdx.files.internal("fonts/comicSansMs20.fnt"));
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = fontWinner;

		// textures init
		TextureRegion backgroundImage = new TextureRegion(new Texture("images/backgrounds/fondfin.png"));
		TextureRegion menuButtonUpImage = new TextureRegion(new Texture("images/buttons/bouton-menu.png"));
		TextureRegion menuButtonDownImage = new TextureRegion(new Texture("images/buttons/bouton-menu2.png"));
		TextureRegion scoresButtonUpImage = new TextureRegion(new Texture("images/buttons/bouton_score.png"));
		TextureRegion scoresButtonDownImage = new TextureRegion(new Texture("images/buttons/bouton_score2.png"));
		TextureRegion exitButtonUpImage = new TextureRegion(new Texture("images/buttons/bouton_quitter.png"));
		TextureRegion exitButtonDownImage = new TextureRegion(new Texture("images/buttons/bouton_quitter2.png"));

		// Actors's init
		backgroundActor = new Image(backgroundImage);
		menuButtonActor = new ImageButton(new TextureRegionDrawable(menuButtonUpImage),new TextureRegionDrawable(menuButtonDownImage));
		scoresButtonActor = new ImageButton(new TextureRegionDrawable(scoresButtonUpImage),new TextureRegionDrawable(scoresButtonDownImage));
		exitButtonActor = new ImageButton(new TextureRegionDrawable(exitButtonUpImage),new TextureRegionDrawable(exitButtonDownImage));

		//set positions of actors
		backgroundActor.setPosition(marginAera,0);
		menuButtonActor.setPosition(game.WIDTH/4-menuButtonUpImage.getRegionWidth()/2,game.HEIGHT*(0.01f*100/3)*2f-menuButtonUpImage.getRegionHeight()/2);
		scoresButtonActor.setPosition(game.WIDTH/4-scoresButtonUpImage.getRegionWidth()/2,game.HEIGHT*(0.01f*100/3)*1.5f-scoresButtonUpImage.getRegionHeight()/2);
		exitButtonActor.setPosition(game.WIDTH/4-exitButtonUpImage.getRegionWidth()/2,game.HEIGHT*(0.01f*100/3)*1f-exitButtonUpImage.getRegionHeight()/2);

		//add Listeners to actors
		menuButtonActor.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch down on menu button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch up on menu button");
				game.setScreen(new MainMenuScreen(game));
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
		stage.addActor(menuButtonActor);
		stage.addActor(scoresButtonActor);
		stage.addActor(exitButtonActor);

		// text actors init
		if(equality){
			textEqualityActor = new Label("égalité...",labelStyle);
			//position
			textEqualityActor.setPosition(250+marginAera,20);
			textEqualityActor.setWidth(120);
			textEqualityActor.setAlignment(Align.center);
			//add actors text to stage
			stage.addActor(textEqualityActor);
		}
		else {
			saveScore();
			textPlayerActor = new Label("Le joueur ", labelStyle);
			textWinnerActor = new Label(winner.getName(), labelStyle);
			textWinActor = new Label("a gagné", labelStyle);
			//position
			textWinActor.setPosition(250+marginAera,0);
			textWinnerActor.setPosition(250+marginAera,textPlayerActor.getY()+textPlayerActor.getHeight());
			textPlayerActor.setPosition(250+marginAera,textWinnerActor.getY()+textWinnerActor.getHeight());
			//set caracts for text winner
			textPlayerActor.setWidth(120);
			textWinnerActor.setWidth(120);
			textWinActor.setWidth(120);
			textPlayerActor.setAlignment(Align.center);
			textWinnerActor.setAlignment(Align.center);
			textWinActor.setAlignment(Align.center);
			//add actors text to stage
			stage.addActor(textPlayerActor);
			stage.addActor(textWinnerActor);
			stage.addActor(textWinActor);
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

	private void saveScore(){
		FileHandle file = Gdx.files.local("scores.txt");
		Gdx.app.log("file path",file.file().getAbsolutePath());
		if(!file.exists()){
			try {
				file.file().createNewFile();
				Gdx.app.log("file path",file.file().getAbsolutePath());
				Gdx.app.log("scores file","created");
			}
			catch (Exception e){
				e.printStackTrace();
				Gdx.app.log("scores file","created FAIL");
			}
		}
		Map<String, Integer> scores = new HashMap<String, Integer>();
		//read file
		try{
			Gdx.app.log("scores file is directory",String.valueOf(file.isDirectory()));
			ObjectInputStream scoreInput = new ObjectInputStream(file.read());
			scores = (HashMap<String, Integer>) scoreInput.readObject();
			try {
				Gdx.app.log("file content", scores.toString());
			}
			catch (Exception e){
				e.printStackTrace();
				Gdx.app.log("file content","read fail");
			}
			scoreInput.close();
		}
		catch (Exception e){
			//TODO fix crash
			e.printStackTrace();
			Gdx.app.log("read file","fail");
		}
		if(scores.get(winner.getName()) != null){
				int score = scores.get(winner.getName())+1;
				Gdx.app.log("new player score",String.valueOf(score));
				scores.put(winner.getName(),score);
				Gdx.app.log("save succes",winner.getName()+" "+scores.get(winner.getName()));
		}
		else{
			scores.put(winner.getName(),1);
			Gdx.app.log("save succes",winner.getName()+" "+scores.get(winner.getName()));
		}

		// rewrite file
		if(scores != null) {
			try {
				ObjectOutputStream scoreOutput = new ObjectOutputStream(file.write(false));
				scoreOutput.writeObject(scores);
				scoreOutput.close();
			} catch (Exception e) {
				//TODO fix crash
				e.printStackTrace();
				Gdx.app.log("write file","fail");
			}
		}



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
