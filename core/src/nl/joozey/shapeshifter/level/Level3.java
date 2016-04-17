package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.gameobject.Jeff;
import nl.joozey.shapeshifter.gameobject.Rinn;

/**
 * Created by mint on 16-4-16.
 */
public class Level3 extends Level {

    private Jeff _jeff;
    private Rinn _rinn;

    public Level3() {
    }

    public void load(int dir) {
        super.load(dir);

        //floor
        _levelManager.createWall(this, 0, 0, Gdx.graphics.getWidth(), _floorLevel);

        //blimp
        if(_jeff != null && _jeff.getPower() <= 6) {
            _levelManager.createBlimp(this, 300, 530);
            _jeff.setPower(7);
            _jeff.rainbow(10);
            setMessage("The surge feels empty");
        }

        //rinn
        _rinn = _levelManager.createRinn(Level3.this, 650, _floorLevel);
        if (_rinn.getStage() != 0) {
            _rinn.freeze();
            _rinn.setPosition(-100, 0);
            _rinn.setSize(0, 0);

        } else {
            _rinn.clearObservers();
            _rinn.stopMoving();
            _rinn.jump(false);

            //obstacles
            _levelManager.createDialog(this, 100, _floorLevel, 60, 200, "Hi Jeff.", false);
            _levelManager.createDialog(this, 300, _floorLevel, 60, 200, "I saw you fell from the sky.", false);
            _levelManager.createDialog(this, 500, _floorLevel, 60, 200, "I'm Rinn. Will you join me?", false);
        }

        //player
        if (dir == 0) {
            _jeff = _levelManager.createJeff(Level3.this, 20, _floorLevel);
        } else {
            _jeff = _levelManager.createJeff(Level3.this, 720, _floorLevel);
        }

        if(_jeff.getPower() > 2) {
            _levelManager.createCorrupt(this, 300, _floorLevel, 200, 100);
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
    public Level getRight() {
        return LevelManager.getInstance().level4;
    }

    @Override
    public Level getLeft() {
        return LevelManager.getInstance().level2;
    }

    @Override
    public void onTouched(GameObject gameObject) {
        if (_rinn.getStage() == 0 && gameObject instanceof Rinn) {
            ((Rinn) gameObject).moveRight();
        }
    }
}
