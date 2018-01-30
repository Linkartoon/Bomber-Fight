package com.gagus.bomberfight.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.gagus.bomberfight.BomberFight;
import com.gagus.bomberfight.Interfaces.CollisionGetter;

/**
 * Created by Gaetan on 24/12/2017.
 */

public class MapManager extends Actor{
	char[][] mapTable;
	FileHandle mapFile;
	String mapString;
	Stage stage;

	public MapManager(String fileName, Stage stage) {
		this.stage = stage;
		mapFile = Gdx.files.internal("maps/"+fileName);
		mapString = mapFile.readString();
		Gdx.app.log("map in string",mapString);

		generateMap();
		createWalls();
	}

	public void generateMap(){
		String[] map = mapString.split("\\n");
		mapTable = new char[map.length][map[map.length-1].length()];
		for(int y=0;y<map.length;y++){
			Gdx.app.log("line in string",map[y]);
			for(int x=0;x<map[y].length();x++){
				if((int)map[y].charAt(x) != 13) {
					mapTable[y][x] = map[y].charAt(x);
					//Gdx.app.log("char", String.valueOf(mapTable[y][x]));
				}
			}
		}
		for(int i = 0; i < mapTable.length / 2; i++)
		{
			char[] temp = mapTable[i];
			mapTable[i] = mapTable[mapTable.length - i - 1];
			mapTable[mapTable.length - i - 1] = temp;
		}
		Gdx.app.log("map height",String.valueOf(mapTable.length));
		Gdx.app.log("map width", String.valueOf(mapTable[0].length));
		//Gdx.app.log("map in table",mapTable.toString());

	}

	public void createWalls() {
		for(int y=0;y<mapTable.length;y++)
		{
			for(int x=0;x<mapTable[y].length;x++)
			{
				 if(mapTable[y][x] == 'k') {
				 	BreakableWall wall = new BreakableWall(new Vector2(x,y));
				 	stage.addActor(wall);
				 	wall.setZIndex(1);
				 }
				 else if (mapTable[y][x] == 'm'){
					 UnbreakableWall wall = new UnbreakableWall(new Vector2(x,y));
					 stage.addActor(wall);
					 wall.setZIndex(1);
				}
			}
		}
		//if(Gdx.input.isTouched()) Gdx.app.log("list of collision",walls.toString());
	}



}
