package nl.joozey.shapeshifter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mint on 16-4-16.
 */
public class Level {

    private List<GameObject> _gameObjectList;

    public Level() {
        _gameObjectList = new ArrayList<>();

        _gameObjectList.add(new PlayerSquare());

        for(int i = 0; i < 10; i++) {
            float w = (float)Math.random() * 100 + 20;
            float h = (float)Math.random() * 100 + 20;
            float x = (float)Math.random() * Gdx.graphics.getWidth() - w;
            float y = (float)Math.random() * Gdx.graphics.getHeight() - h;
            _gameObjectList.add(new LevelObject(x, y, w, h));
        }

        CollisionHelper.setGameObjectList(_gameObjectList);
    }

    public void act() {
        for(GameObject gameObject : _gameObjectList) {
            gameObject.act();
        }
    }

    public void draw(Batch batch) {
        for(GameObject gameObject : _gameObjectList) {
            gameObject.draw(batch);
        }
    }
}
