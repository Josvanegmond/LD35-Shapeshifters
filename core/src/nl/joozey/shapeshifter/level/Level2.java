package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.gameobject.Jeff;
import nl.joozey.shapeshifter.gameobject.Rinn;
import nl.joozey.shapeshifter.util.CountTimer;
import nl.joozey.shapeshifter.util.InputManager;

/**
 * Created by mint on 16-4-16.
 */
public class Level2 extends Level {

    private Jeff _jeff;

    public Level2() {
    }

    public void load(int dir) {
        super.load(dir);

        //floor
        _levelManager.createWall(this, 0, 0, Gdx.graphics.getWidth(), _floorLevel);

        //blimp
        _levelManager.createBlimp(this, 280, 380);

        //obstacles
        _levelManager.createWall(this, 150, _floorLevel + 40, 190, 200);
        _levelManager.createWall(this, 500, _floorLevel, 80, 80);

        Rinn rinn = _levelManager.getRinn();
        if(rinn != null && rinn.getStage() == 0) {
            _levelManager.createDialog(this, 123, _floorLevel, 30, 60, "Now, concentrate, focus on your shape and press TWO!", false);
            _levelManager.createDialog(this, 423, _floorLevel, 30, 60, "Fantastic! Now press ONE and come meet me in the next area.", false);
        }

        //player
        if(dir == 0) {
            _jeff = _levelManager.createJeff(Level2.this, 20, _floorLevel);
        } else {
            _jeff = _levelManager.createJeff(Level2.this, 720, _floorLevel);
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
}
