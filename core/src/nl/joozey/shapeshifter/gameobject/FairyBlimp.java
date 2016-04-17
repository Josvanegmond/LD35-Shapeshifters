package nl.joozey.shapeshifter.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import nl.joozey.shapeshifter.level.Level;
import nl.joozey.shapeshifter.level.LevelManager;

/**
 * Created by mint on 16-4-16.
 */
public class FairyBlimp extends GameObject {

    private ShapeRenderer _shapeRenderer;
    private Vector2 _initPosition;
    private float _angle;
    private float _radius;
    private float _speed;
    private Level _level;

    private boolean _grabbed;

    public FairyBlimp(Level level, float x, float y) {
        _level = level;
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);

        _angle = (float) Math.random() * 360;
        _speed = 50 + (float) Math.random() * 50;
        _radius = 20;

        setPosition(x, y);
        setSize(20, 20);

        _initPosition = getPosition();
    }

    @Override
    public void draw(Batch batch) {
        if (!_grabbed) {
            Color color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
            Vector2 pos = getPosition();
            Vector2 size = getSize();

            _shapeRenderer.setColor(color);
            _shapeRenderer.begin();
            _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            _shapeRenderer.rect(pos.x, pos.y, size.x, size.y);
            _shapeRenderer.end();
        }
    }

    @Override
    public void act() {
        if (!_grabbed) {
            _angle += _speed * Gdx.graphics.getDeltaTime();

            Vector2 pos = getPosition();
            pos.x = (float) Math.cos(Math.toRadians(_angle)) * _radius + _initPosition.x;
            pos.y = (float) Math.sin(Math.toRadians(_angle)) * _radius + _initPosition.y;
            setPosition(pos);
        }
    }

    @Override
    public boolean isGrabbableBy(GameObject gameObject) {
        if (!_grabbed && gameObject instanceof Jeff) {
            return true;
        }
        return false;
    }

    @Override
    public void hitObject(GameObject gameObject) {
        if (gameObject instanceof Jeff) {
            _level.onTouched(this);
            if (!_grabbed) {
                _grabbed = true;
                LevelManager.getInstance().dim();
                setPosition(-100, 0);
                setSize(0, 0);
            }
        }
    }

    public boolean isGrabbed() {
        return _grabbed;
    }
}
