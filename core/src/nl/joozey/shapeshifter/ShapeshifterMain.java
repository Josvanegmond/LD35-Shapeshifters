package nl.joozey.shapeshifter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShapeshifterMain extends ApplicationAdapter {

	private SpriteBatch _batch;
	private Level _level;

	@Override
	public void create () {
		_batch = new SpriteBatch();
		_level = new Level();

        Gdx.input.setInputProcessor(InputManager.getInstance());
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_batch.begin();

        _level.act();
		_level.draw(_batch);

		_batch.end();
	}
}
