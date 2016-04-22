package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import nl.joozey.shapeshifter.gameobject.FairyBlimp;
import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.gameobject.GameObjectObserver;
import nl.joozey.shapeshifter.gameobject.Jeff;
import nl.joozey.shapeshifter.gameobject.Rinn;
import nl.joozey.shapeshifter.util.CountTimer;
import nl.joozey.shapeshifter.util.InputManager;

/**
 * Created by mint on 16-4-16.
 */
public class Level2 extends Level implements GameObjectObserver {

    private Jeff _jeff;
    private Rinn _rinn;

    public Level2() {
    }

    @Override
    public void unload() {
        _jeff.clearObservers();
    }

    public void load(int dir) {
        super.load(dir);

        //player
        if(dir == 0) {
            _jeff = _levelManager.createJeff(Level2.this, 20, _floorLevel);
        } else {
            _jeff = _levelManager.createJeff(Level2.this, 720, _floorLevel);
        }

        _jeff.observe(this);

        //floor
        _levelManager.createWall(this, 0, 0, Gdx.graphics.getWidth(), _floorLevel);

        //blimp
        if(_jeff.getPower() < 4) {
            _levelManager.createBlimp(this, 280, 380);
        }

        //obstacles
        _levelManager.createWall(this, 150, _floorLevel + 40, 190, 200, true);
        _levelManager.createWall(this, 500, _floorLevel, 80, 80);

        Rinn rinn = _levelManager.getRinn();
        if(rinn == null || rinn.getStage() == 0) {
            _levelManager.createDialog(this, 123, _floorLevel, 30, 60, "Now, concentrate, focus on your shape and press TWO!", false);
            _levelManager.createDialog(this, 423, _floorLevel, 30, 60, "Fantastic! Now press ONE and come meet me in the next area.", false);
        }

        if(rinn != null && rinn.getStage() == 4) {
            _rinn = _levelManager.createRinn(Level2.this, 200, _floorLevel + 240);
            _levelManager.createDialog(this, 0, _floorLevel, 100, 100, "Jeff ... please stop ... please ...", true);
            _levelManager.createCorruptEnemy(this, 250, _floorLevel + 20);
            _rinn.stopMoving();
        }
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
        return LevelManager.getInstance().level1;
    }

    @Override
    public Level getRight() {
        return LevelManager.getInstance().level3;
    }

    @Override
    public void setBroken(boolean broken) {
        super.setBroken(broken);
        if(_rinn != null && (_rinn.getStage() == 4 || _rinn.getStage() == 5) && broken) {
            _rinn.moveRight();
            _rinn.setStage(6);
            _rinn.morph(0);
            _levelManager.createDialog(this, 300, _floorLevel, 200, 100, "I'm hurt ...", true);
        }
    }

    @Override
    public void onTouched(GameObject gameObject) {
        if(gameObject instanceof FairyBlimp) {
            _jeff.setPower(5);
            _jeff.rainbow(100);
            setMessage("You feel the power flow through you");
        }

        if(_rinn != null && gameObject == _rinn) {
            if(_rinn.getStage() == 5) {
                CountTimer fallTimer = new CountTimer(new CountTimer.Task() {
                    private float _jeffsSavedX = _jeff.getPosition().x;
                    @Override
                    public void count(float amount) {
                        _jeff.setPosition(_jeffsSavedX, _jeff.getPosition().y);
                    }

                    @Override
                    public void finish() {
                    }
                }, 100, 0, 0.01f);
            }
            if(_rinn.getStage() == 6) {
                _rinn.setStage(7);
                _jeff.setPower(4);
                _jeff.redbow(400);
                setMessage("When you touch Rinn, you absorb her being. You shiver.");
                LevelManager.getInstance().clearDialogs();
            }
        }
    }

    @Override
    public void onChangeDimension(GameObject gameObject, Rectangle dimension) {
        if(_rinn != null && _jeff != null && gameObject == _jeff) {
            if(_rinn.getStage() == 4 && dimension.y > _floorLevel + 200) {
                _rinn.morph(1);
                _rinn.setStage(5);
            }

            if(_rinn.getStage() == 5) {
                boolean left = _rinn.getPosition().x > _jeff.getPosition().x;

                if(_rinn.getPosition().x + _rinn.getSize().x * .5f > 150 && left) {
                    _rinn.moveLeft();
                }  else {

                    if(_rinn.getPosition().x + _rinn.getSize().x * .5f < 350 && !left){
                        _rinn.moveRight();
                    } else {
                        _rinn.stopMoving();
                    }
                }
            }
        }
    }
}
