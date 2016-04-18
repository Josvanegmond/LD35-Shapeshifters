package nl.joozey.shapeshifter.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import nl.joozey.shapeshifter.level.Level;
import nl.joozey.shapeshifter.level.LevelManager;

/**
 * Created by mint on 16-4-16.
 */
public class CorruptEnemy extends GameObject {

    private ShapeRenderer _shapeRenderer;
    private Vector2 _initPosition;
    private float _angle;
    private float _radius;
    private float _speed;
    private Level _level;
    private boolean _absorbing;
    private float _alpha = 1f;

    private boolean _killed;

    public CorruptEnemy(Level level, float x, float y) {
        _level = level;
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);

        _angle = (float) Math.random() * 360;
        _speed = 50 + (float) Math.random() * 30;
        _radius = 5;

        setPosition(x, y);
        setSize(20, 20);

        _initPosition = getPosition();
    }

    @Override
    public void draw(Batch batch) {
        if (!_killed) {
            Vector2 pos = getPosition();
            Vector2 size = getSize();

            _shapeRenderer.setColor(Color.BLACK);
            if(_absorbing) {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                _shapeRenderer.setColor(0, 0, 0, _alpha);
            }
            _shapeRenderer.begin();
            _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            _shapeRenderer.circle(pos.x, pos.y, size.x);
            _shapeRenderer.end();
        }
    }

    @Override
    public void act() {
        if (!_killed) {
            _angle += _speed * Gdx.graphics.getDeltaTime();

            Vector2 pos = getPosition();

            Jeff jeff = LevelManager.getInstance().getJeff();
            if(jeff != null) {
                Vector2 dir = jeff.getPosition().cpy().add(jeff.getSize().cpy().scl(0.5f)).sub(pos).nor();

                pos.x += (float) Math.cos(Math.toRadians(_angle)) * _radius + dir.x * _speed * Gdx.graphics.getDeltaTime();
                pos.y += (float) Math.sin(Math.toRadians(_angle)) * _radius + dir.y * _speed * Gdx.graphics.getDeltaTime();
                setPosition(pos);
            }
        }
    }

    @Override
    public boolean isGrabbableBy(GameObject gameObject) {
        return true;
    }

    @Override
    public void hitObject(GameObject gameObject) {
        if (_absorbing == false && gameObject instanceof Jeff) {
            Jeff jeff = (Jeff) gameObject;
            jeff.redbow(50);
            jeff.decrease();
        }
    }

    public void invade() {
        _absorbing = true;
        _radius = 0;
        _speed += 15f * Gdx.graphics.getDeltaTime();
    }

    public void getAbsorbed() {
        _absorbing = true;
        _radius = 0;
        _speed += 15f * Gdx.graphics.getDeltaTime();

        Jeff jeff = LevelManager.getInstance().getJeff();
        if(jeff != null) {
            Vector2 dir = jeff.getPosition().cpy().add(jeff.getSize().cpy().scl(0.5f)).sub(getPosition());
            if (dir.len() < 10 || _alpha < 0.1f) {
                _alpha = 0;
                _killed = true;
            }
            _alpha = 1f - 1f / (dir.len() / 10f);
        }
    }
}
