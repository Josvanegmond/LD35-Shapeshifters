package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import nl.joozey.shapeshifter.gameobject.FairyBlimp;
import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.gameobject.Jeff;
import nl.joozey.shapeshifter.util.CountTimer;

/**
 * Created by mint on 16-4-16.
 */
public class Level1 extends Level {

    private boolean _levelStarted;
    private float _skyRectHeight;
    private Jeff _jeff;

    public Level1() {
    }

    public void load(int dir) {
        super.load(dir);

        //player
        if (dir == -1) {
            _jeff = _levelManager.createJeff(Level1.this, 115, 480);
        } else if (dir == 0) {
            _jeff = _levelManager.createJeff(Level1.this, 20, _floorLevel + 320);
        } else {
            _jeff = _levelManager.createJeff(Level1.this, 720, _floorLevel);
        }

        //floor
        _levelManager.createWall(this, 0, 0, Gdx.graphics.getWidth(), _floorLevel);

        //blimp
        if (_jeff.getPower() < 6) {
            _levelManager.createBlimp(this, 480, 480);
        }

        //obstacles
        _levelManager.createWall(this, -50, _floorLevel, 70, 320);
        _levelManager.createWall(this, 20, _floorLevel, 60, 80);
        _levelManager.createWall(this, 350, _floorLevel, 80, 80);
        _levelManager.createWall(this, 500, _floorLevel, 80, 160);

        if (_jeff.getPower() > 2) {
            _levelManager.createCorrupt(this, 450, _floorLevel + 160, 100, 100);
            _levelManager.createCorrupt(this, 250, _floorLevel + 200, 50, 50);
        }

        if (_jeff.getPower() == 0) {
            _levelManager.createDialog(this, 100, _floorLevel, 30, 60, "You hear a voice shouting: HEY! COME HERE!", true);
            _levelManager.createDialog(this, 323, 66, 30, 60, "USE SPACE TO JUMP!", false);

            if (!_levelStarted) {
                _jeff.freeze();
                _startLevel();
            }
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
                    _skyRectHeight += 1f;
                }
            }

            @Override
            public void finish() {
                _jeff.freeze(false);
            }
        }, 200, 0, 0.01f)
                .start();
    }

    @Override
    public void onTouched(GameObject gameObject) {
        if(gameObject instanceof FairyBlimp) {
            _jeff.setPower(6);
            _jeff.rainbow(50);
            setMessage("You feel all powerful.");
        }
    }

    @Override
    public Level getRight() {
        return LevelManager.getInstance().level2;
    }

    @Override
    public Level getLeft() {
        return LevelManager.getInstance().level6;
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
    public boolean keyUp(int keycode) {
        if (!_levelStarted && keycode == Input.Keys.SPACE) {
            _levelStarted = true;
            _startLevelAnimation();
        }
        return false;
    }
}
