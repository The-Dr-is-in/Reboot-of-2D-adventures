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
	Texture img;
	Sprite sprite;
	TextureAtlas textureAtlas;
	TextureRegion textureRegion;


	@Override
	public void create () {
		textureAtlas=new TextureAtlas();
		batch = new SpriteBatch();
		img = new Texture("PixelHouse.png");
		sprite = new Sprite(img);
		sprite.setPosition(Gdx.graphics.getWidth()/2-sprite.getWidth()/2, Gdx.graphics.getHeight()/2-sprite.getHeight()/2);
		sprite.setScale(25f);
		Gdx.input.setInputProcessor(this);

	}

	float SpriteSize =25;

	@Override
	public void render () {
		sprite.setScale(SpriteSize);
		//"Polling" type methods. Runs a check every frame
		if(input.isKeyPressed(Input.Keys.A)){ sprite.translateX(-3f); sprite.rotate(5f);}
		if(input.isKeyPressed(Input.Keys.D)){ sprite.translateX(3f); sprite.rotate(-5f);}
		if(input.isKeyPressed(Input.Keys.W)){ if(SpriteSize <35) SpriteSize +=0.5;}
		if(input.isKeyPressed(Input.Keys.S)){ if(SpriteSize >14) SpriteSize -=0.5;}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(sprite, sprite.getX(), sprite.getY(),  //Starting point (bottom left corner of picture)
				sprite.getWidth()/2, sprite.getHeight()/2, //Origin (point of rotation)
				sprite.getWidth(), sprite.getHeight(), //Size of image
				sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation()); //How much to scale image up by, and rotation amount

		/*IMPORTANT MATH NOTE: the coordinate system is such so
		 that the bottom left coordinate is 0,0 and the rest follows
		 so that it's like the first quadrant of the cartesian plane*/
		batch.end();
	}

	@Override
	public void dispose (){
		batch.dispose();
		img.dispose();
	}

	//"Event" type methods. Only occurs when key is pressed.
	@Override
	public boolean keyDown(int keycode) {
		if(keycode==Input.Keys.E){sprite.translate(10,10);}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode==Input.Keys.E){sprite.translate(-10,-10);}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

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

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
