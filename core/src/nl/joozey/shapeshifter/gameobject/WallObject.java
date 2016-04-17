package nl.joozey.shapeshifter.gameobject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import nl.joozey.shapeshifter.level.LevelManager;
import nl.joozey.shapeshifter.main.Constants;
import nl.joozey.shapeshifter.util.CountTimer;
import nl.joozey.shapeshifter.util.HSL;

/**
 * Created by mint on 16-4-16.
 */
public class WallObject extends GameObject {

    private ShapeRenderer _shapeRenderer;
    private HSL _color;
    private boolean _breakable;

    public WallObject(float x, float y, float w, float h, boolean breakable) {
        _breakable = breakable;
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

        LevelManager levelManager = LevelManager.getInstance();
        _color.s = levelManager.getDimFactor();
        _color.l = 0.6f - (1f - LevelManager.getInstance().getDimFactor()) * .4f;

        _shapeRenderer.setColor(_color.toRGB());
        _shapeRenderer.begin();
        _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.rect(pos.x + levelManager.getShakeX(), pos.y + levelManager.getShakeY(), size.x, size.y);
        _shapeRenderer.end();
    }

    @Override
    public void act() {
        if(LevelManager.getInstance().isShaking() && _breakable) {
            _fall();
        }
    }

    private void _fall() {
        CountTimer fallTimer = new CountTimer(new CountTimer.Task() {
            private float _fall;
            private float _fallSpeed = 10;
            @Override
            public void count(float amount) {
                setPosition(getPosition().x, getPosition().y - _fall);
                _fall += amount * _fallSpeed;
            }

            @Override
            public void finish() {

            }
        }, 1000, (float)Math.random(), 0.01f);
    }
}
