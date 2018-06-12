package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import static com.badlogic.gdx.Gdx.input;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	boolean jumping = false;
	boolean falling = false;
	Texture platformSkin;
	Sprite floatingPlatform;
	Sprite floor;
	Sprite player;
	TextureAtlas textureAtlas;
	TextureRegion textureRegion;
	int currentFrame = 1;
	int MAX_FRAME = 32;
	float jumpCount=2;
	float accelDown =1;
	float accelUp=0;
	boolean walledOff=false;
	int currentScore=0;
	Label score;
	String stringScore;
	BitmapFont font;
	Label.LabelStyle scoreStyle;
	World world;
	Body body;


	//TODO for emphasis
	//Here's the tutorial I'm using for the physcis stuff: http://www.gamefromscratch.com/post/2014/08/27/LibGDX-Tutorial-13-Physics-with-Box2D-Part-1-A-Basic-Physics-Simulations.aspx
	@Override
	public void create() {
		/*world = new World (new Vector2(0,-98f),true);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type=BodyDef.BodyType.DynamicBody;
		PolygonShape=*/
		font=new BitmapFont(Gdx.files.internal("Score.fnt"));
		stringScore = new String("Score="+currentScore);
		scoreStyle=new Label.LabelStyle(font, null);
		score=new Label(stringScore,scoreStyle);
		score.setPosition(20,400);
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/AnimateSquareFRAMES-packed/AnimateSquare.atlas"));
		textureRegion = textureAtlas.findRegion("Square");
		batch = new SpriteBatch();
		platformSkin = new Texture("Sprites/platform.png");
		floatingPlatform = new Sprite(platformSkin);
		floor=new Sprite(platformSkin);
		player = new Sprite(textureRegion);
		floatingPlatform.setPosition(50,200);
		player.setPosition(Gdx.graphics.getWidth() / 2, 200);
		player.setScale(10f);
		floatingPlatform.setScale(20f);
		floor.setScale(200,10);
		floor.setPosition(Gdx.graphics.getWidth()/2,165);

		Gdx.input.setInputProcessor(this);

	}


	@Override
	public void render() {
		//"Polling" type methods. Runs a check every frame

		floatingPlatform.translateX(5+(currentScore));
		if (input.isKeyPressed(Input.Keys.A)) {
			player.translateX(-1);
			System.out.println(player.getX());
			currentFrame--;
			if (currentFrame < 1) {
				currentFrame = MAX_FRAME;
			}
			player.setRegion(textureAtlas.findRegion("Square", currentFrame));
		}
		if (input.isKeyPressed((Input.Keys.D))) {
			player.translateX(1);
			System.out.println(player.getX());
			currentFrame++;
			if (currentFrame > MAX_FRAME) {
				currentFrame = 1;
			}
			player.setRegion(textureAtlas.findRegion("Square", currentFrame));
		}

		if(player.getX()>Gdx.graphics.getWidth()){
			player.setX(0);
		}
		if(player.getX()<0){
			player.setX(Gdx.graphics.getWidth());
		}
		if(floatingPlatform.getX()>Gdx.graphics.getWidth()){
			score.setText("Score="+currentScore);
			currentScore++;
			floatingPlatform.setX(0);
		}

		if (player.getBoundingRectangle().overlaps(floatingPlatform.getBoundingRectangle())){
			currentScore=0;
		}



		//jump logic, jumping and falling are booleans in my fields.
		if (jumpCount>30) {
			jumping = false;
			falling = true;
			jumpCount=2;
		}

		if(player.getX()<200){
			player.setX(200);
		}

		if (player.getBoundingRectangle().overlaps(floor.getBoundingRectangle())) {

			falling = false;
		}

		if(!jumping && !player.getBoundingRectangle().overlaps(floor.getBoundingRectangle())
					&& !player.getBoundingRectangle().overlaps(floatingPlatform.getBoundingRectangle())){
			falling=true;
			accelDown =1;
		} else{falling=false;}

		if(jumping && player.getBoundingRectangle().overlaps(floatingPlatform.getBoundingRectangle())
				   && player.getY()<220 && player.getY()>floor.getY()+floor.getRegionHeight()){
			player.translateY(-5);
			falling=true;
			jumping=false;
			accelDown=1;
		}


		if(jumping && player.getBoundingRectangle().overlaps(floatingPlatform.getBoundingRectangle())
				&& (player.getX()-(floatingPlatform.getX()+80))<1){
			falling=true;
		}

		if(!player.getBoundingRectangle().overlaps(floatingPlatform.getBoundingRectangle())){
			if(floatingPlatform.getX()==Gdx.graphics.getWidth()-5){
				currentScore++;
			}
		} else{currentScore=0;}

		if (jumping) {
			player.translateY(accelUp); jumpCount++;
			accelUp-=0.2;
			System.out.println(player.getY());
		}
		if (falling) {
			accelDown +=currentScore+1;
			player.translateY(-(1+ accelDown));
		}


		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		/*batch.draw(player, player.getX(), player.getY(),  //Starting point (bottom left corner of picture)
				player.getWidth()/2, player.getHeight()/2, //Origin (point of rotation)
				player.getWidth(), player.getHeight(), //Size of image
				player.getScaleX(), player.getScaleY(), player.getRotation()); //How much to scale image up by, and rotation amount
		*/
		player.draw(batch);
		floatingPlatform.draw(batch);
		floor.draw(batch);
		score.draw(batch,1);

		/*IMPORTANT MATH NOTE: the coordinate system is such so
		 that the bottom left coordinate is 0,0 and the rest follows
		 so that it's like the first quadrant of the cartesian plane*/
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	//"Event" type methods. Only occurs when key is pressed.


	@Override
	public boolean keyDown(int keycode) {
		if (jumping || falling) {
			return false;
		} else if (keycode == Input.Keys.W) {
			jumping = true;
			accelUp=8;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}


	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}





	//Forced android imports I'm not using
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

}