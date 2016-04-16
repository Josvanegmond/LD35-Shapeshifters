package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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

        //floor
        _levelManager.createWall(this, 0, 0, Gdx.graphics.getWidth(), _floorLevel);

        //blimp
        _levelManager.createBlimp(this, 480, 480);

        //obstacles
        _levelManager.createWall(this, 0, _floorLevel, 20, 320);
        _levelManager.createWall(this, 20, _floorLevel, 80, 80);
        _levelManager.createWall(this, 350, _floorLevel, 80, 80);
        _levelManager.createWall(this, 500, _floorLevel, 80, 160);

        _levelManager.createDialog(this, 323, 66, 30, 60, "You hear a voice shouting: USE SPACE TO JUMP!", false);

        //player
        _jeff = _levelManager.createJeff(Level1.this, 100, 480);
        _jeff.freeze();

        _startLevelAnimation();
    }

    private void _startLevelAnimation() {
        new CountTimer(new CountTimer.Task() {
            @Override
            public void count(float amount) {
                if (amount > .3f && amount <= .6f) {
                    _skyRectHeight += 0.1f;
                }
            }

            @Override
            public void finish() {
                _levelStarted = true;
                _jeff.freeze(false);
            }
        }, 2000, 0, 0.001f)
                .start();
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
}
