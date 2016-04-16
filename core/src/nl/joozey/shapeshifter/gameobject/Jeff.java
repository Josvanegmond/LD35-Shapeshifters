package nl.joozey.shapeshifter.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import nl.joozey.shapeshifter.main.Constants;
import nl.joozey.shapeshifter.util.InputManager;
import nl.joozey.shapeshifter.helper.CollisionHelper;

/**
 * Created by mint on 16-4-16.
 */
public class Jeff extends GameObject implements InputProcessor {

    private ShapeRenderer _shapeRenderer;

    private boolean _moveRight;
    private boolean _moveLeft;
    private float _speed = 200f;
    private float _jumpForceModifier = 0f;
    private float _jumpForce = 1000f;
    private float _gravity = 400;
    private float _angle;
    private boolean _hitFloor;

    private int _shape;
    private Color _color;

    private Timer _barrelRollTimer;
    private Timer.Task _barrelRollTask;

    public Jeff(float x, float y) {
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);

        System.out.println("Adding inputprocessor");
        InputManager.getInstance().addProcessor(this);

        setPosition(x, y);
        _setShape(0);
        _color = new Color(Constants.JEFF_COLOR);

        _barrelRollTimer = new Timer();
        _barrelRollTask = new Timer.Task() {
            @Override
            public void run() {
                _angle++;
                if (_angle > 360) {
                    _angle = 0;
                }
            }
        };
    }

    private void _morph(int startShape, int endShape) {
        switch (endShape) {
            case 0:
                setSize(30, 60);
                _color = new Color(Constants.JEFF_COLOR);
                _jumpForce = 800f;
                break;
            case 1:
                setSize(150, 10);
                _color = new Color(Constants.JEFF_COLOR).add(0.2f, 0.2f, 0.2f, 1f);
                _jumpForce = 700f;
                break;
            case 2:
                setSize(80, 80);
                _color = new Color(Constants.JEFF_COLOR).add(0.4f, 0.3f, 0.2f, 1f);
                _jumpForce = 500f;
                break;
            case 3:
                setSize(15, 15);
                _color = new Color(Constants.JEFF_COLOR).add(.25f, 0.1f, -0.1f, 1f);
                _jumpForce = 1100f;
                break;
            case 4:
                setSize(40, 40);
                _color = new Color(Constants.JEFF_COLOR).add(.05f, -0.2f, -.48f, 1f);
                _jumpForce = 700f;
                break;
        }
    }

    private void _setShape(int shape) {
        _morph(_shape, shape);
        _shape = shape;
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

        dimension = CollisionHelper.check(this, dimension);
        if(dimension.y == this.getPosition().y) {
            if(_jumpForceModifier > 0 && !_hitFloor) {
                _jumpForceModifier = _gravity - 1;
            }
            _hitFloor = true;
        } else {
            _hitFloor = false;
        }

        setPosition(dimension.x, dimension.y);
    }

    @Override
    public void draw(Batch batch) {
        Vector2 pos = getPosition();
        Vector2 size = getSize();

        _shapeRenderer.setColor(_color);
        _shapeRenderer.begin();
        _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        if (_shape < 3) {
            _shapeRenderer.rect(pos.x, pos.y, size.x * .5f, size.y * .5f, size.x, size.y, 1, 1, _angle);
        }

        if (_shape >= 3) {
            _shapeRenderer.circle(pos.x + size.x * .5f, pos.y + size.x * .5f, size.x);
        }

        _shapeRenderer.end();
    }

    @Override
    public void hitObject(GameObject gameObject) {

    }

    @Override
    public boolean keyDown(int keycode) {

        System.out.println("Down Keycode: " + keycode);
        if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
            _moveRight = true;
        }

        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
            _moveLeft = true;
        }

        if (keycode == Input.Keys.SPACE) {
            if (_hitFloor) {
                _jumpForceModifier = _jumpForce;
            } else if(_shape == 0) {
                synchronized (_barrelRollTask) {
                    if (!_barrelRollTask.isScheduled()) {
                        _barrelRollTimer.scheduleTask(_barrelRollTask, 0, 0.001f, 360);
                    }
                }
            }
        }

        if (keycode == Input.Keys.E || keycode == Input.Keys.ENTER) {
        }

        if (keycode == Input.Keys.NUM_1) {
            _setShape(0);
        }

        if (keycode == Input.Keys.NUM_2) {
            _setShape(1);
        }

        if (keycode == Input.Keys.NUM_3) {
            _setShape(2);
        }

        if (keycode == Input.Keys.NUM_4) {
            _setShape(3);
        }

        if (keycode == Input.Keys.NUM_5) {
            _setShape(4);
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
            _moveRight = false;
        }

        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
            _moveLeft = false;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
