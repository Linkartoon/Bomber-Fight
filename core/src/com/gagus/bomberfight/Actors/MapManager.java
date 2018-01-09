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
	Texture unbreakableBlock;
	Texture breakableBlock;
	Texture backgroundImage;
	float marginAera;
	Array<Rectangle> walls;
	Array<Rectangle> wallsAround;
	Stage stage;
	CollisionGetter collisionGetter;

	public MapManager(String fileName, Stage stage, CollisionGetter collisionGetter) {
		this.stage = stage;
		this.collisionGetter = collisionGetter;
		marginAera = BomberFight.GameArea.x;
		mapFile = Gdx.files.internal("maps/"+fileName);
		mapString = mapFile.readString();
		Gdx.app.log("map in string",mapString);
		unbreakableBlock = new Texture(Gdx.files.internal("images/walls/mur1.png"));
		breakableBlock = new Texture(Gdx.files.internal("images/walls/kure.png"));
		backgroundImage = new Texture(Gdx.files.internal("images/backgrounds/herbe.png"));
		generateMap();
	}

	public void generateMap(){
		wallsAround = new Array<Rectangle>();
		String[] map = mapString.split("\\n");
		mapTable = new char[map.length][map[map.length-1].length()];
		for(int y=0;y<map.length;y++){
			Gdx.app.log("line in string",map[y]);
			for(int x=0;x<map[y].length();x++){
				if((int)map[y].charAt(x) != 13) {
					mapTable[y][x] = map[y].charAt(x);
					if(y == 0 || y == map.length-1) wallsAround.add((new Rectangle(x*BomberFight.SQUARESIZE,y*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE)));
					else if(x == 0 || x == map[y].length()-1) wallsAround.add((new Rectangle(x*BomberFight.SQUARESIZE,y*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE)));
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

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(backgroundImage,marginAera,0);
		for(int y=0;y<mapTable.length;y++){
			for(int x=0;x<mapTable[0].length;x++){
				if(mapTable[y][x] == 'k') batch.draw(breakableBlock,x*breakableBlock.getWidth()+marginAera,y*breakableBlock.getHeight());
				else if(mapTable[y][x] == 'm') batch.draw(unbreakableBlock,x*unbreakableBlock.getWidth()+marginAera,y*unbreakableBlock.getHeight());
			}
		}
	}

	@Override
	public void act(float delta) {
		walls = new Array<Rectangle>();
		for(int y=0;y<mapTable.length;y++)
		{
			for(int x=0;x<mapTable[y].length;x++)
			{
				 if(mapTable[y][x] == 'k' || mapTable[y][x] == 'm')
					walls.add(new Rectangle(x*BomberFight.SQUARESIZE,y*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE));
			}
		}
		//if(Gdx.input.isTouched()) Gdx.app.log("list of collision",walls.toString());
	}

	public char[][] getMapChar(){
		return mapTable;
	}

	public Array<Rectangle> getWalls(){
		return walls;
	}

	public Vector2 getSquareByPosition(Vector2 position){
		return new Vector2((int)position.x/BomberFight.SQUARESIZE,(int)position.y/BomberFight.SQUARESIZE);
	}

	public Array<Rectangle> getWallsAround()
	{
		return wallsAround;
	}

	public void destroyBlock(Vector2 squarePosition)
	{
		mapTable[(int)squarePosition.y][(int)squarePosition.x] = '0';
		int bonusRandom = (int)(Math.random()*4);
		Gdx.app.log("bonusRandom",String.valueOf(bonusRandom));
		if(bonusRandom == 0 || bonusRandom == 1 || bonusRandom == 3) {
			Bonus up = new Bonus(bonusRandom, squarePosition, collisionGetter);
			stage.addActor(up);
			up.setZIndex(2);
		}
	}

}
