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
public class Level6 extends Level {

    private Jeff _jeff;

    public Level6() {
    }

    public void load(int dir) {
        super.load(dir);

        //player
        if(dir == 0) {
            _jeff = _levelManager.createJeff(Level6.this, 20, _floorLevel + 400);
        } else {
            _jeff = _levelManager.createJeff(Level6.this, 720, _floorLevel + 400);
        }

        //floor
        _levelManager.createWall(this, 0, 0, Gdx.graphics.getWidth(), _floorLevel);

        //blimp
        if(_jeff.getPower() < 3) {
            _levelManager.createBlimp(this, 600, 250);
        }

        //obstacles
        _levelManager.createWall(this, 0, _floorLevel, 100, 400);
        _levelManager.createWall(this, 80, _floorLevel, 270, 150);
        _levelManager.createWall(this, 350, _floorLevel, 350, 80);
        _levelManager.createWall(this, 700, _floorLevel, 100, 320);
        _levelManager.createWall(this, 650, _floorLevel + 250, 150, 20, true);

        _levelManager.createWall(this, 450, 300, 50, 150, true);

        _levelManager.createDialog(this, 80, _floorLevel + 150, 150, 100, "Nasty flies. Maybe you can kick them away?", false);
        _levelManager.createCorrupt(this, 450, _floorLevel + 200, 100, 100);
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
        return LevelManager.getInstance().level5;
    }

    @Override
    public Level getRight() {
        return LevelManager.getInstance().level1;
    }

    @Override
    public void onTouched(GameObject gameObject) {
        if(gameObject instanceof FairyBlimp) {
            _jeff.setPower(3);
            _jeff.rainbow(200);
            setMessage("You feel super-empowered! Don't forget TAB!");
        }
    }
}
