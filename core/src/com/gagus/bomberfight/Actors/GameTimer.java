package com.gagus.bomberfight.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gagus.bomberfight.BomberFight;
import com.gagus.bomberfight.Interfaces.GameTimerListener;

/**
 * Created by Gaetan on 08/01/2018.
 */

public class GameTimer extends Actor {
	int countdown;
	float timeElapsed;
	boolean countdownFinished;
	BitmapFont countdownFont;
	Label countdownLabel;
	float marginArea;
	GameTimerListener gameTimerListener;

	public GameTimer(GameTimerListener gameTimerListener) {
		this.gameTimerListener = gameTimerListener;
		marginArea = BomberFight.GameArea.x;
		countdown = 180;
		timeElapsed = 0;
		countdownFinished = false;
		countdownFont = new BitmapFont(Gdx.files.internal("fonts/arialBlack30.fnt"));
		Label.LabelStyle labelStyle = new Label.LabelStyle(countdownFont, Color.BLACK);
		countdownLabel = new Label(String.valueOf(countdown),labelStyle);
		countdownLabel.setPosition(BomberFight.WIDTH/2-countdownLabel.getWidth()/2,BomberFight.HEIGHT-countdownLabel.getHeight());
		Gdx.app.log("position countdown",String.valueOf(countdownLabel.getX())+" "+String.valueOf(countdownLabel.getY()));
		Gdx.app.log("size countdown",String.valueOf(countdownLabel.getWidth())+" "+String.valueOf(countdownLabel.getHeight()));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		countdownLabel.draw(batch,parentAlpha);
	}

	@Override
	public void act(float delta) {
		timeElapsed+=delta;
		if(timeElapsed >= 1){
			countdown--;
			timeElapsed--;
			countdownLabel.setText(String.valueOf(countdown));
			Gdx.app.log("countdown",countdownLabel.getText().toString());
		}
		if(countdown == 0) countdownLabel.setText("Game Over");
		if(countdown == 0) {
			countdownFinished = true;
			gameTimerListener.OnGameTimerFinished();
		}
		countdownLabel.setPosition(BomberFight.WIDTH/2-countdownLabel.getWidth()/2,BomberFight.HEIGHT-countdownLabel.getHeight());

	}
}
