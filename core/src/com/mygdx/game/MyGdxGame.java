package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.badlogic.gdx.Gdx.input;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	boolean jumping = false;
	boolean falling = false;
	Texture platformSkin;
	Sprite platform;
	Sprite player;
	TextureAtlas textureAtlas;
	TextureRegion textureRegion;
	int currentFrame = 1;
	int MAX_FRAME = 32;
	int jumpCount=0;


	@Override
	public void create() {
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/AnimateSquareFRAMES-packed/AnimateSquare.atlas"));
		textureRegion = textureAtlas.findRegion("Square");
		batch = new SpriteBatch();
		platformSkin = new Texture("Sprites/platform.png");
		platform = new Sprite(platformSkin);
		player = new Sprite(textureRegion);
		platform.setPosition(200,270);
		player.setPosition(Gdx.graphics.getWidth() / 2 - player.getWidth() / 2, 200);
		player.setScale(10f);
		platform.setScale(20f);

		Gdx.input.setInputProcessor(this);

	}


	@Override
	public void render() {
		//"Polling" type methods. Runs a check every frame
		if (input.isKeyPressed(Input.Keys.A)) {
			player.translateX(-1);
			currentFrame--;
			if (currentFrame < 1) {
				currentFrame = MAX_FRAME;
			}
			player.setRegion(textureAtlas.findRegion("Square", currentFrame));
		}
		if (input.isKeyPressed((Input.Keys.D))) {
			player.translateX(1);
			currentFrame++;
			if (currentFrame > MAX_FRAME) {
				currentFrame = 1;
			}
			player.setRegion(textureAtlas.findRegion("Square", currentFrame));
		}

		//jump logic, jumping and falling are booleans in my fields.
		if (jumpCount==60) {
			jumping = false;
			falling = true;
			jumpCount=0;
		}

		if (player.getY() == 200
			||
			(player.getBoundingRectangle().overlaps(platform.getBoundingRectangle())
			&&
			player.getY()<platform.getY())) {

			falling = false;
		}

		if(player.getY()>200 && !platform.getBoundingRectangle().overlaps(platform.getBoundingRectangle())){
			falling=true;
		}

		if (jumping) {
			player.translateY(2); jumpCount++;
		}
		if (falling) {
			player.translateY(-2);
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
		platform.draw(batch);

		/*IMPORTANT MATH NOTE: the coordinate system is such so
		 that the bottom left coordinate is 0,0 and the rest follows
		 so that it's like the first quadrant of the cartesian plane*/
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		platformSkin.dispose();
	}

	//"Event" type methods. Only occurs when key is pressed.


	@Override
	public boolean keyDown(int keycode) {
		if (jumping || falling) {
			return false;
		} else if (keycode == Input.Keys.W) {
			jumping = true;
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