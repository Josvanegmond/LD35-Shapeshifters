package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;
import java.util.Objects;

import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.gameobject.Jeff;
import nl.joozey.shapeshifter.main.Constants;
import nl.joozey.shapeshifter.util.InputManager;

/**
 * Created by mint on 16-4-16.
 */
public class Level implements InputProcessor {

    protected ShapeRenderer _shapeRenderer;
    protected LevelManager _levelManager;
    protected String _message = "";
    protected boolean _showPowerups;

    protected float _floorLevel = 70;

    public Level() {}

    public void load(int dir) {
        InputManager.getInstance().addProcessor(this);
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);

        _levelManager = LevelManager.getInstance();
    }

    public void unload() {
        InputManager.getInstance().removeProcessor(this);
    }

    public void run() {
        for (GameObject gameObject : _levelManager.getAllGameObjects()) {
            gameObject.run();
        }
    }

    public void drawScene(Batch batch) {

    }

    public final void draw(Batch batch) {
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float alpha = LevelManager.getInstance().getDimFactor();

        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(Constants.grayBackground, 0, 0);

        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(Constants.greenBackground, 0, 0);

        batch.end();
        batch.begin();

        _shapeRenderer.begin();

        //game scene
        drawScene(batch);

        //dialog box
        _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.setColor(Color.BLACK);
        _shapeRenderer.rect(40, 20, 720, _floorLevel - 40);

        //powerups
        List<String> unlockedPowerupList = _levelManager.getPowerupHints();
        Jeff jeff = _levelManager.getJeff();
        if(jeff != null) {
            int unlockedPowerupCount = jeff.getPower();
            if (_showPowerups && unlockedPowerupCount > 0) {
                _shapeRenderer.rect(60, 60, 680, 480);
                for (int i = 0; i < unlockedPowerupCount; i++) {
                    Color color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
                    _shapeRenderer.setColor(color);
                    _shapeRenderer.rect(70, 510 - i * 25, 15, 15);
                }
            }
        }

        _shapeRenderer.end();

        batch.end();
        batch.begin();

        Constants.font.setColor(Color.WHITE);
        if(!Objects.equals(_message, "")) {
            Constants.font.draw(batch, _message, 60, 38);
        }

        //powerups
        if(jeff != null) {
            int unlockedPowerupCount = jeff.getPower();
            if (_showPowerups && unlockedPowerupCount > 0) {
                for (int i = 0; i < unlockedPowerupCount; i++) {
                    Constants.font.draw(batch, unlockedPowerupList.get(i), 100, 520 - i * 25);
                }
            }
        }
    }

    public void setMessage(String message) {
        _message = message;
    }

    public Level getLeft() { return null; }

    public Level getRight() { return null; }

    public void onTouched(GameObject gameObject) {

    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.TAB) {
            _showPowerups = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.TAB) {
            _showPowerups = false;
        }
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
