package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import nl.joozey.shapeshifter.gameobject.FairyBlimp;
import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.gameobject.Jeff;
import nl.joozey.shapeshifter.gameobject.Rinn;

/**
 * Created by mint on 16-4-16.
 */
public class Level5 extends Level {

    private Jeff _jeff;
    private Rinn _rinn;

    public Level5() {
    }

    public void load(int dir) {
        super.load(dir);

        //floor
        _levelManager.createWall(this, 0, 0, Gdx.graphics.getWidth(), _floorLevel);

        //player
        if(dir == 0) {
            _jeff = _levelManager.createJeff(Level5.this, 20, _floorLevel + 400);
        } else {
            _jeff = _levelManager.createJeff(Level5.this, 720, _floorLevel + 400);
        }

        //blimp
        if(_jeff.getPower() < 2) {
            _levelManager.createBlimp(this, 500, 150);
        }

        //obstacles
        _levelManager.createWall(this, 0, _floorLevel, 150, 400);
        _levelManager.createWall(this, 150, _floorLevel, 10, 300);
        _levelManager.createWall(this, 160, _floorLevel, 10, 200);
        _levelManager.createWall(this, 170, _floorLevel, 10, 100);
        _levelManager.createWall(this, 550, _floorLevel, 250, 400);

        Rinn rinn = _levelManager.getRinn();
        if(rinn == null || rinn.getStage() <= 3) {
            _rinn = _levelManager.createRinn(Level5.this, 600, _floorLevel + 400);
            _rinn.moveRight();
            _rinn.setStage(4);
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
        return LevelManager.getInstance().level4;
    }

    @Override
    public Level getRight() {
        return LevelManager.getInstance().level6;
    }


    @Override
    public void onTouched(GameObject gameObject) {
        if(gameObject instanceof FairyBlimp) {
            _jeff.setPower(2);
            _jeff.rainbow(300);
            setMessage("That felt ecstatic! You feel like P is more useful!");
            _levelManager.createDialog(this, 170, _floorLevel, 100, 60, "You can climb walls! Combine your new power and shapes.", false);
        }
    }
}
