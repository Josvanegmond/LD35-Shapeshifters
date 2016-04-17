package nl.joozey.shapeshifter.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nl.joozey.shapeshifter.level.LevelManager;
import nl.joozey.shapeshifter.util.InputManager;

public class ShapeshifterMain extends ApplicationAdapter {

    private SpriteBatch _batch;

    @Override
    public void create() {
        _batch = new SpriteBatch();

        Gdx.input.setInputProcessor(InputManager.getInstance());
        LevelManager.getInstance().loadLevel(LevelManager.getInstance().level5, 0);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _batch.begin();

        LevelManager.getInstance().run(_batch);

        _batch.end();
    }
}
