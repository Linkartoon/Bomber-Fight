package com.gagus.bomberfight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.gagus.bomberfight.BomberFight;

import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gaetan on 24/12/2017.
 */

public class ScoresScreen implements Screen {

	BomberFight game;
	Stage stage;
	float marginAera;
	String[][] scoresSorted;
	BitmapFont font;

	// scores position
	int squareNameXStart = 88;
	int squareNameXEnd = 313;
	int squareNameWidth = squareNameXEnd-squareNameXStart;
	int squareYStart = BomberFight.HEIGHT - 57;
	int squareYEnd = BomberFight.HEIGHT - 33;
	int squareHeight = squareYEnd-squareYStart;
	int squareScoreXStart = 313;
	int squareScoreXEnd = 407;
	int squareScoreWidth = squareScoreXEnd-squareScoreXStart;

	int positionNameXStart = 88;
	int positionScoreXStart = 313;
	int positionYStart = BomberFight.HEIGHT - 57;

	Image backgroundActor;
	ImageButton menuButtonActor;

	public ScoresScreen(final BomberFight game) {
		this.game = game;
		stage = new Stage(game.viewport,game.batch);
		Gdx.input.setInputProcessor(stage);
		marginAera = BomberFight.GameArea.x;
		font = new BitmapFont(Gdx.files.internal("fonts/comicSansMs20.fnt"));

		//scores
		scoresSorted = null;
		getScores();

		//textures init
		TextureRegion backgroundImage = new TextureRegion(new Texture(Gdx.files.internal("images/backgrounds/image_scores.png")));
		TextureRegion menuButtonUpImage = new TextureRegion(new Texture("images/buttons/bouton-menu.png"));
		TextureRegion menuButtonDownImage = new TextureRegion(new Texture("images/buttons/bouton-menu2.png"));
		//actor init
		backgroundActor = new Image(backgroundImage);
		menuButtonActor = new ImageButton(new TextureRegionDrawable(menuButtonUpImage),new TextureRegionDrawable(menuButtonDownImage));
		//set position
		backgroundActor.setPosition(marginAera,0);
		menuButtonActor.setPosition(BomberFight.GameArea.getWidth()+marginAera-menuButtonActor.getWidth()-10,10);
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
		//add actor
		stage.addActor(backgroundActor);
		stage.addActor(menuButtonActor);
		createScoresActors();
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

	private void getScores(){
		FileHandle file = Gdx.files.local("scores.txt");
		Gdx.app.log("file path",file.file().getAbsolutePath());
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

		if(scores != null){
			String[][] scoresUnsorted= new String[scores.values().size()][2];
			int index = 0;
			for(String name : scores.keySet()){
				scoresUnsorted[index] = new String[]{name,String.valueOf(scores.get(name))};
				index++;
			}
			int length = scores.keySet().size();
			scoresUnsorted = sortScores(scoresUnsorted,length);
			if(length > 10) length = 10;
			scoresSorted = new String[length][2];
			for(int i = 0;i< length;i++) scoresSorted[i] = scoresUnsorted[i];
			for(String[] var : scoresSorted) {
				Gdx.app.log("final scores set", var[0]+" "+var[1]);
			}
		}

	}

	private String[][] sortScores(String[][] scores,int length){
		String[] tmp;
		boolean permut;
		do{
			permut = false;
			for(int i = 0; i< length-1; i++){
				if(Integer.parseInt(scores[i][1]) <  Integer.parseInt(scores[i+1][1])){
					tmp = scores[i];
					scores[i] = scores[i+1];
					scores[i+1] = tmp;
					permut = true;
				}
			}
		}while(permut);
		return scores;
	}

	private void createScoresActors(){
		Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
		for(int i =0; i< scoresSorted.length;i++){
			Label labelName = new Label(scoresSorted[i][0],labelStyle);
			Label labelScore = new Label(scoresSorted[i][1],labelStyle);

			labelName.setWidth(squareNameWidth);
			labelName.setHeight(squareHeight);
			labelName.setPosition(positionNameXStart+marginAera,positionYStart-squareHeight*i);
			labelName.setAlignment(Align.center);

			labelScore.setWidth(squareScoreWidth);
			labelScore.setHeight(squareHeight);
			labelScore.setPosition(positionScoreXStart+marginAera,positionYStart-squareHeight*i);
			labelScore.setAlignment(Align.center);

			stage.addActor(labelName);
			stage.addActor(labelScore);
		}
	}
}
