package nl.joozey.shapeshifter.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.joozey.shapeshifter.level.Level;
import nl.joozey.shapeshifter.level.Level1;
import nl.joozey.shapeshifter.util.InputManager;

public class ShapeshifterMain extends ApplicationAdapter {

    private SpriteBatch _batch;
    private Level _level;

    @Override
    public void create() {
        _batch = new SpriteBatch();
        _level = new Level1();

        Gdx.input.setInputProcessor(InputManager.getInstance());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _batch.begin();

        _level.run();
        _level.draw(_batch);

        _batch.end();
    }
}
