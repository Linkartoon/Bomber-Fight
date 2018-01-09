package com.gagus.bomberfight.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gagus.bomberfight.BomberFight;

/**
 * Created by Gaetan on 05/01/2018.
 */

public class CountdownStartGame {
	//variables for countdown
	int countdown;
	float timeElapsed;
	boolean countdownFinished;
	BitmapFont countdownFont;
	Label countdownLabel;
	float marginArea;

	public CountdownStartGame() {
		marginArea = BomberFight.GameArea.x;
		countdown = 3;
		timeElapsed = 0;
		countdownFinished = false;
		countdownFont = new BitmapFont(Gdx.files.internal("fonts/arialBlack50.fnt"));
		Label.LabelStyle labelStyle = new Label.LabelStyle(countdownFont,Color.BLACK);
		countdownLabel = new Label(String.valueOf(countdown),labelStyle);
		countdownLabel.setPosition(BomberFight.WIDTH/2-countdownLabel.getWidth()/2,BomberFight.HEIGHT/2);
		Gdx.app.log("position countdown",String.valueOf(countdownLabel.getX())+" "+String.valueOf(countdownLabel.getY()));
		Gdx.app.log("size countdown",String.valueOf(countdownLabel.getWidth())+" "+String.valueOf(countdownLabel.getHeight()));
	}

	public void draw(Batch batch, Camera camera) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if(countdown > 0)
		countdownFont.draw(batch,String.valueOf(countdown),countdownLabel.getX(),countdownLabel.getY());
		else countdownFont.draw(batch,"GO",countdownLabel.getX(),countdownLabel.getY());
		batch.end();
	}

	public void act(float delta) {
		timeElapsed+=delta;
		if(timeElapsed >= 1){
			countdown--;
			timeElapsed=0;
			countdownLabel.setText(String.valueOf(countdown));
			Gdx.app.log("countdown",countdownLabel.getText().toString());
		}
		if(countdown == 0) countdownLabel.setText("GO");
		if(countdown < 0) {
			countdownFinished = true;
		}
		countdownLabel.setPosition(BomberFight.WIDTH/2-countdownLabel.getWidth()/2,BomberFight.HEIGHT/2);
	}

	public boolean isCountdownFinished(){
		return countdownFinished;
	}
}
