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

import nl.joozey.shapeshifter.helper.CollisionHelper;
import nl.joozey.shapeshifter.level.Level;
import nl.joozey.shapeshifter.level.LevelManager;
import nl.joozey.shapeshifter.main.Constants;
import nl.joozey.shapeshifter.util.CountTimer;
import nl.joozey.shapeshifter.util.InputManager;

/**
 * Created by mint on 16-4-16.
 */
public class Jeff extends GameObject implements InputProcessor {

    private ShapeRenderer _shapeRenderer;

    private boolean _moveRight;
    private boolean _moveLeft;
    private float _speed = 180f;
    private float _jumpForceModifier = 0f;
    private float _jumpForce = 1000f;
    private float _gravity = 400;
    private float _angle;
    private boolean _hitFloor;
    private boolean _jumpPressed;
    private boolean _actionPressed;
    private int _powerLevel;

    private int _shape;
    private int _previousShape;
    private Color _color;

    private Timer _barrelRollTimer;
    private Timer.Task _barrelRollTask;

    private Level _level;

    public Jeff(Level level, float x, float y) {
        _level = level;
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);

        System.out.println("Adding inputprocessor");
        InputManager.getInstance().addProcessor(this);

        setPosition(x, y);
        setShape(0);
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

    public void setColor(Color color) {
        _color = color;
    }

    private void _morph(int startShape, int endShape) {
        switch (endShape) {
            case 0:
                setPosition(getPosition().add(getSize().x * .5f - 15, 0));
                setSize(30, 60);
                _color = new Color(Constants.JEFF_COLOR);
                _jumpForce = 800f;
                break;
            case 1:
                setPosition(getPosition().add(getSize().x * .5f - 75, 0));
                setSize(150, 10);
                _color = new Color(Constants.JEFF_COLOR).add(0.2f, 0.2f, 0.2f, 1f);
                _jumpForce = 700f;
                break;
            case 2:
                setPosition(getPosition().add(getSize().x * .5f - 40, 0));
                setSize(80, 80);
                _color = new Color(Constants.JEFF_COLOR).add(0.4f, 0.3f, 0.2f, 1f);
                _jumpForce = 500f;
                break;
            case 3:
                setPosition(getPosition().add(getSize().x * .5f - 7.5f, 0));
                setSize(30, 30);
                _color = new Color(Constants.JEFF_COLOR).add(.25f, 0.1f, -0.1f, 1f);
                _jumpForce = 1100f;
                break;
            case 4:
                setPosition(getPosition().add(getSize().x * .5f - 20, 0));
                setSize(60, 60);
                _color = new Color(Constants.JEFF_COLOR).add(.05f, -0.2f, -.48f, 1f);
                _jumpForce = 700f;
                break;
        }
    }

    public void setShape(int shape) {
        _morph(_shape, shape);
        _previousShape = _shape;
        _shape = shape;
    }

    public void setLevel(Level level) {
        _level = level;
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

        if (_moveLeft || _moveRight) {
            System.out.println(dimension.x + ", " + dimension.y + ", " + dimension.width + ", " + dimension.height);
        }

        if (_jumpPressed && _hitFloor) {
            _jumpForceModifier = _jumpForce;
        }

        if (_actionPressed) {
            if (_shape == 0) {
                synchronized (_barrelRollTask) {
                    if (!_barrelRollTask.isScheduled()) {
                        _barrelRollTimer.scheduleTask(_barrelRollTask, 0, 0.001f, 360);
                    }
                }
            }

            if (_powerLevel >= 1 && _shape == 1) {
                setSize(250, 10);
            }

        } else {
            if (_powerLevel >= 1 && _shape == 1) {
                setSize(150, 10);
            }
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

        if (dimension.x + dimension.width * .5f < 0) {
            LevelManager.getInstance().loadLeft();
        }

        if (dimension.x + dimension.width * .5f > Gdx.graphics.getWidth()) {
            LevelManager.getInstance().loadRight();
        }
    }

    public void rainbow(float duration) {
        CountTimer rainbowTimer = new CountTimer(new CountTimer.Task() {
            @Override
            public void count(float amount) {
                _color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
            }

            @Override
            public void finish() {
                setShape(_shape);
            }
        }, duration, 0, 0.01f);
    }

    public void setPower(int power) {
        _powerLevel = power;
    }

    public int getPower() {
        return _powerLevel;
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
            _shapeRenderer.circle(pos.x + size.x * .5f, pos.y + size.y * .5f, size.x * .5f);
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
            _jumpPressed = true;
        }

        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.E || keycode == Input.Keys.F ||
                keycode == Input.Keys.SHIFT_RIGHT || keycode == Input.Keys.SHIFT_LEFT) {
            _actionPressed = true;
        }

        if (keycode == Input.Keys.NUM_1) {
            setShape(0);
        }

        if (keycode == Input.Keys.NUM_2) {
            setShape(1);
        }

        if (_powerLevel >= 2 && keycode == Input.Keys.NUM_3) {
            setShape(2);
        }

        if (_powerLevel >= 3 && keycode == Input.Keys.NUM_4) {
            setShape(3);
        }

        if (_powerLevel >= 4 && keycode == Input.Keys.NUM_5) {
            setShape(4);
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

        if (keycode == Input.Keys.SPACE) {
            _jumpPressed = false;
        }

        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.E || keycode == Input.Keys.F ||
                keycode == Input.Keys.SHIFT_RIGHT || keycode == Input.Keys.SHIFT_LEFT ||
                keycode == Input.Keys.ALT_LEFT || keycode == Input.Keys.ALT_RIGHT) {
            _actionPressed = false;
        }

        if (keycode == Input.Keys.NUM_1 || keycode == Input.Keys.NUM_2 || keycode == Input.Keys.NUM_3 ||
                keycode == Input.Keys.NUM_4 || keycode == Input.Keys.NUM_5) {
            if (CollisionHelper.isColliding(this)) {
                _jumpPressed = false;
                _jumpForceModifier = 0;
                setShape(_previousShape);
            }
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
