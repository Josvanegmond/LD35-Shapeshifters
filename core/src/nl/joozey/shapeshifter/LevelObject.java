package nl.joozey.shapeshifter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by mint on 16-4-16.
 */
public class LevelObject extends GameObject {

    private ShapeRenderer _shapeRenderer;

    public LevelObject(float x, float y, float w, float h) {
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);

        setPosition(x, y);
        setSize(w, h);
    }

    @Override
    public void draw(Batch batch) {
        Vector2 pos = getPosition();
        Vector2 size = getSize();

        _shapeRenderer.setColor(Color.GRAY);
        _shapeRenderer.begin();
        _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.rect(pos.x, pos.y, size.x, size.y);
        _shapeRenderer.end();
    }

    @Override
    public void act() {

    }
}
