package nl.joozey.shapeshifter.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import nl.joozey.shapeshifter.helper.CollisionHelper;
import nl.joozey.shapeshifter.level.Level;
import nl.joozey.shapeshifter.main.Constants;

/**
 * Created by mint on 17-4-16.
 */
public class Rinn extends GameObject {

    private ShapeRenderer _shapeRenderer;

    private boolean _moveRight;
    private boolean _moveLeft;
    private float _speed = 220f;
    private float _jumpForceModifier = 0f;
    private float _jumpForce = 1100f;
    private float _gravity = 400;
    private float _angle;
    private boolean _hitFloor;
    private boolean _jumpPressed;
    private boolean _actionPressed;
    private int _rinnStage = 0;

    private Level _level;

    private int _shape;
    private Color _color;

    public Rinn(Level level, float x, float y) {
        _level = level;
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);
        _color = Constants.RINN_COLOR;
        setPosition(x, y);
        morph(0);
    }

    public void morph(int shape) {
        _shape = shape;
        switch (shape) {
            case 0:
                setPosition(getPosition().add(getSize().x * .5f - getSize().x * .25f, 0));
                setSize(40, 40);
                break;

            case 1:
                setPosition(getPosition().add(getSize().x * .5f - 75, 0));
                setSize(150, 10);
                break;
        }
    }

    @Override
    public void draw(Batch batch) {
        if (_rinnStage < 7) {
            Vector2 pos = getPosition();
            Vector2 size = getSize();

            _shapeRenderer.setColor(_color);
            _shapeRenderer.begin();

            _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            if (_shape == 0) {
                _shapeRenderer.circle(pos.x + size.x * .5f, pos.y + size.y * .5f, size.x * .5f);
            }

            if (_shape == 1) {
                _shapeRenderer.rect(pos.x, pos.y, size.x * .5f, size.y * .5f, size.x, size.y, 1, 1, _angle);
            }

            _shapeRenderer.end();
        }
    }

    @Override
    public void act() {
        float factor = Gdx.graphics.getDeltaTime();
        Rectangle dimension = getDimension();

        dimension.y += _jumpForceModifier * factor;
        _jumpForceModifier = Math.max(0, _jumpForceModifier - 700 * factor);

        dimension.y -= _gravity * factor;

        if (_moveLeft) {
            dimension.x -= _speed * factor;
        }

        if (_moveRight) {
            dimension.x += _speed * factor;
        }

        if (_jumpPressed && _hitFloor) {
            _jumpForceModifier = _jumpForce;
            _jumpPressed = false;
        }

        dimension = CollisionHelper.check(this, dimension);
        if (dimension.y == this.getPosition().y) {
            if (_jumpForceModifier > 0 && !_hitFloor) {
                _jumpForceModifier = _gravity - 1;
            }
            _hitFloor = true;
        } else {
            _hitFloor = false;
        }

        setPosition(dimension.x, dimension.y);
    }

    public void jump() {
        _jumpPressed = true;
    }

    public void jump(boolean jump) {
        _jumpPressed = jump;
    }

    @Override
    public boolean isGrabbableBy(GameObject gameObject) {
        if (gameObject instanceof Jeff) {
            return true;
        }

        return false;
    }

    @Override
    public void hitObject(GameObject gameObject) {
        if (gameObject instanceof Jeff) {
            _level.onTouched(this);
        }
    }

    public void setLevel(Level level) {
        _level = level;
    }

    public void moveLeft() {
        _moveLeft = true;
        _moveRight = false;
    }

    public void moveRight() {
        _moveRight = true;
        _moveLeft = false;
    }

    public void stopMoving() {
        _moveRight = false;
        _moveLeft = false;
    }

    public void setStage(int stage) {
        _rinnStage = stage;
    }

    public int getStage() {
        return _rinnStage;
    }
}
