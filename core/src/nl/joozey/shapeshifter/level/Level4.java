package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

import nl.joozey.shapeshifter.gameobject.FairyBlimp;
import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.gameobject.GameObjectObserver;
import nl.joozey.shapeshifter.gameobject.Jeff;
import nl.joozey.shapeshifter.gameobject.Rinn;
import nl.joozey.shapeshifter.util.CountTimer;

/**
 * Created by mint on 16-4-16.
 */
public class Level4 extends Level implements GameObjectObserver {

    private Jeff _jeff;
    private Rinn _rinn;
    private boolean _reshaped;

    public Level4() {
    }

    public void load(int dir) {
        super.load(dir);
        _loadStage(dir);
    }

    @Override
    public void drawScene(Batch batch) {
        _shapeRenderer.end();

        for (GameObject gameObject : _levelManager.getAllGameObjects()) {
            gameObject.draw(batch);
        }

        _shapeRenderer.begin();
    }

    @Override
    public Level getLeft() {
        return LevelManager.getInstance().level3;
    }

    @Override
    public void onChangeDimension(GameObject gameObject, Rectangle dimension) {
        if (gameObject == _rinn) {
            int rinnStage = _rinn.getStage();
            if (rinnStage == 0 && dimension.x > 600 && dimension.y < _floorLevel + 10) {
                _rinn.jump();
            }

            if (rinnStage == 0 && dimension.x > 680 && dimension.y > 200) {
                _rinn.stopMoving();
                _rinn.setStage(1);
            }

            if (rinnStage == 2 && dimension.x > 750) {
                _rinn.stopMoving();
                _rinn.setStage(3);
            }
        }
    }

    @Override
    public void onTouched(GameObject gameObject) {
        if (gameObject == _rinn) {
            int rinnStage = _rinn.getStage();
            if (rinnStage == 1) {
                if (_rinn.getDimension().x > 680) {
                    _rinn.moveRight();
                    _rinn.jump();
                    _rinn.setStage(2);
                }
            }

            if (rinnStage == 3) {
                _rinn.moveRight();
            }
        }

        if (gameObject instanceof FairyBlimp) {
            _startAnimation();
        }
    }

    private void _startAnimation() {
        _jeff.freeze();
        _reshapeLevel();

        new CountTimer(new CountTimer.Task() {
            private float _jeffsSavedX;
            @Override
            public void count(float amount) {
                Color randomColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
                _jeff.setColor(randomColor);
                _jeffsSavedX = _jeff.getPosition().x;
                if (amount > 0.5f) {
                    _jeff.freeze(false);
                    _jeff.setShape(0);
                    //fall down but stay where you are
                    _jeff.setPosition(_jeffsSavedX, _jeff.getPosition().y);
                }
            }

            @Override
            public void finish() {
            }
        }, 300f, 2, 0.01f);
    }

    private void _reshapeLevel() {
        _reshaped = true;
        LevelManager.getInstance().clearDialogs();
        _loadStage2Level();
    }

    private void _loadStage(int dir) {

        //floor
        _levelManager.createWall(this, 0, 0, Gdx.graphics.getWidth(), _floorLevel);

        //obstacles
        _levelManager.createWall(this, 150, _floorLevel + 50, 300, 70);
        _levelManager.createWall(this, 750, _floorLevel, 50, 400);
        _levelManager.createWall(this, 150, _floorLevel + 120, 100, 100);
        _levelManager.createWall(this, 650, _floorLevel, 100, 200);
        _levelManager.createWall(this, 100, 360, 350, 10);
        _levelManager.createWall(this, 150, 390, 300, 100);

        if (_reshaped) {
            _loadStage2Level();
        } else {
            _loadStage1Level();
        }

        //player
        if (dir == 0) {
            _jeff = _levelManager.createJeff(Level4.this, 20, _floorLevel);
        } else {
            _jeff = _levelManager.createJeff(Level4.this, 720, _floorLevel);
        }
    }

    private void _loadStage1Level() {
        //blimp
        _levelManager.createBlimp(this, 600, 520);

        //rinn
        _rinn = _levelManager.createRinn(Level4.this, 150, _floorLevel);
        _rinn.moveRight();
        _rinn.clearObservers();
        _rinn.observe(this);

        _levelManager.createDialog(this, 100, _floorLevel, 60, 80, "See the coloured floating blobs?", false);
        _levelManager.createDialog(this, 300, _floorLevel, 60, 80, "We call them fairy blimps.", false);
        _levelManager.createDialog(this, 500, _floorLevel, 60, 80, "They give everything their energy. They make this world green.", false);
        _levelManager.createDialog(this, 250, _floorLevel + 120, 100, 200, "Without them, we would not be able to shapeshift.", false);
        _levelManager.createDialog(this, 150, 490, 300, 100, "Aren't they beautiful?", false);
        _levelManager.createDialog(this, 150, _floorLevel + 220, 80, 40, "Did you know you can shapeshift in-air to reach places?", false);
    }

    private void _loadStage2Level() {
        _levelManager.createDialog(this, 500, _floorLevel, 60, 80, "... Jeff ...", true);
        _levelManager.createDialog(this, 250, _floorLevel + 120, 100, 200, "What have you done?", true);
        _levelManager.createDialog(this, 150, 490, 300, 100, "You ...  you killed it ... ", true);
    }
}
