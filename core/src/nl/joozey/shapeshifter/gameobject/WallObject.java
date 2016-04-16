package nl.joozey.shapeshifter.gameobject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import nl.joozey.shapeshifter.level.LevelManager;
import nl.joozey.shapeshifter.main.Constants;
import nl.joozey.shapeshifter.util.HSL;

/**
 * Created by mint on 16-4-16.
 */
public class WallObject extends GameObject {

    private ShapeRenderer _shapeRenderer;
    private HSL _color;

    public WallObject(float x, float y, float w, float h) {
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);
        _color = new HSL(new Color(Constants.GREEN_ENV_COLOR));

        setPosition(x, y);
        setSize(w, h);
    }

    @Override
    public void draw(Batch batch) {
        Vector2 pos = getPosition();
        Vector2 size = getSize();

        float takenBlimps = LevelManager.getInstance().getTakenBlimpCount();
        float totalBlimps = LevelManager.getInstance().getBlimpCount();

        if(totalBlimps > 0) {
            _color.s = 1f - takenBlimps / totalBlimps;
            _color.l = 0.6f - (takenBlimps / totalBlimps) * .4f;
        }

        _shapeRenderer.setColor(_color.toRGB());
        _shapeRenderer.begin();
        _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.rect(pos.x, pos.y, size.x, size.y);
        _shapeRenderer.end();
    }

    @Override
    public void act() {

    }
}
