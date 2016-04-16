package nl.joozey.shapeshifter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by mint on 16-4-16.
 */
public class FairyBlimp extends GameObject {

    private ShapeRenderer _shapeRenderer;
    private Vector2 _initPosition;
    private float _angle;
    private float _radius;
    private float _speed;

    private boolean _grabbed;

    public FairyBlimp(float x, float y) {
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);

        _angle = (float)Math.random() * 360;
        _speed = 50 + (float)Math.random() * 50;
        _radius = 20;

        setPosition(x, y);
        setSize(20, 20);

        _initPosition = getPosition();
    }

    @Override
    public void draw(Batch batch) {
        if(!_grabbed) {
            Color _color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
            Vector2 pos = getPosition();
            Vector2 size = getSize();

            _shapeRenderer.setColor(_color);
            _shapeRenderer.begin();
            _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            _shapeRenderer.rect(pos.x, pos.y, size.x, size.y);
            _shapeRenderer.end();
        }
    }

    @Override
    public void act() {
        if(!_grabbed) {
            _angle += _speed * Gdx.graphics.getDeltaTime();

            Vector2 pos = getPosition();
            pos.x = (float) Math.cos(Math.toRadians(_angle)) * _radius + _initPosition.x;
            pos.y = (float) Math.sin(Math.toRadians(_angle)) * _radius + _initPosition.y;
            setPosition(pos);
        }
    }

    @Override
    public boolean isGrabbableBy(GameObject gameObject) {
        _grabbed = true;
        setPosition(-100, 0);
        setSize(0, 0);
        return true;
    }
}
