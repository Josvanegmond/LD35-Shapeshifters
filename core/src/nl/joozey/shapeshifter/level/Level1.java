package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.gameobject.Jeff;
import nl.joozey.shapeshifter.util.CountTimer;
import nl.joozey.shapeshifter.util.InputManager;

/**
 * Created by mint on 16-4-16.
 */
public class Level1 extends Level implements InputProcessor {

    private boolean _levelStarted;
    private float _skyRectHeight;
    private Jeff _jeff;

    public Level1() {
    }

    public void load(int dir) {
        super.load(dir);
        InputManager.getInstance().addProcessor(this);

        //floor
        _levelManager.createWall(this, 0, 0, Gdx.graphics.getWidth(), _floorLevel);

        //blimp
        _levelManager.createBlimp(this, 480, 480);

        //obstacles
        _levelManager.createWall(this, 0, _floorLevel, 20, 320);
        _levelManager.createWall(this, 20, _floorLevel, 60, 80);
        _levelManager.createWall(this, 350, _floorLevel, 80, 80);
        _levelManager.createWall(this, 500, _floorLevel, 80, 160);

        _levelManager.createDialog(this, 100, _floorLevel, 30, 60, "You hear a voice shouting: HEY! COME HERE!", true);
        _levelManager.createDialog(this, 323, 66, 30, 60, "USE SPACE TO JUMP!", false);

        //player
        if(dir == -1) {
            _jeff = _levelManager.createJeff(Level1.this, 115, 480);
        } else if(dir == 0) {
            _jeff = _levelManager.createJeff(Level1.this, 20, _floorLevel);
        } else {
            _jeff = _levelManager.createJeff(Level1.this, 720, _floorLevel);
        }

        if(!_levelStarted) {
            _jeff.freeze();
            _startLevel();
        }
    }

    private void _startLevel() {
        setMessage("Press space to begin!");
    }

    private void _startLevelAnimation() {
        setMessage("");
        new CountTimer(new CountTimer.Task() {
            @Override
            public void count(float amount) {
                if (amount > .3f && amount <= .6f) {
                    _skyRectHeight += 0.1f;
                }
            }

            @Override
            public void finish() {
                _jeff.freeze(false);
            }
        }, 2000, 0, 0.001f)
                .start();
    }

    @Override
    public Level getRight() {
        return LevelManager.getInstance().level2;
    }

    @Override
    public void drawScene(Batch batch) {
        _shapeRenderer.setColor(Color.BLACK);
        _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.rect(98, 540 - _skyRectHeight, 34, _skyRectHeight);
        _shapeRenderer.end();

        for (GameObject gameObject : _levelManager.getAllGameObjects()) {
            gameObject.draw(batch);
        }

        _shapeRenderer.begin();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!_levelStarted && keycode == Input.Keys.SPACE) {
            _levelStarted = true;
            _startLevelAnimation();
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
