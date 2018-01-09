package com.gagus.bomberfight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gagus.bomberfight.BomberFight;

/**
 * Created by Gaetan on 06/01/2018.
 */

public class NameChoiceScreen implements Screen {
	String names[];
	int limitSize;
	int playerCountAct;
	int playersCount;
	String mapName;

	//Input listener
	Input.TextInputListener textListener;
	String title;

	final BomberFight game;

	//Stage
	Stage stage;
	Image backgroundActor;
	float marginAera;


	public NameChoiceScreen(final BomberFight game, String mapName) {
		this.game = game;
		stage = new Stage(game.viewport,game.batch);
		Gdx.input.setInputProcessor(stage);
		marginAera = BomberFight.GameArea.x;

		textListener = new Input.TextInputListener()
		{
			@Override
			public void input(String input)
			{
				tryName(input);
			}

			@Override
			public void canceled()
			{
				enterName();
			}
		};

		playersCount = 2;
		names = new String[playersCount];
		limitSize = 10;
		playerCountAct = 0;
		this.mapName = mapName;

		title = "Entrez le nom du joueur "+String.valueOf(playerCountAct+1);

		// textures
		TextureRegion backGroundImage = new TextureRegion(new Texture(Gdx.files.internal("images/backgrounds/fond2.png")));
		// Actors creation
		backgroundActor = new Image(backGroundImage);
		//actor position
		backgroundActor.setPosition(marginAera,0);
		//add actor to stage
		stage.addActor(backgroundActor);
		enterName();
	}

	private void tryName(String name){
		if(name.length() <= 10 && name.length() >=4){
			names[playerCountAct] = name;
			playerCountAct++;
			title = "Entrez le nom du joueur "+String.valueOf(playerCountAct+1);
		}
		if(playerCountAct < names.length) enterName();
		else end();
	}

	private void enterName(){
		Gdx.input.getTextInput(textListener,title,"","");
	}

	private void end(){
		game.setScreen(new GameScreen(game,mapName,names));
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
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
