package nl.joozey.shapeshifter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by mint on 16-4-16.
 */
public class PlayerSquare extends GameObject implements InputProcessor {

    private ShapeRenderer _shapeRenderer;

    private boolean _moveRight;
    private boolean _moveLeft;
    private float _speed = 200f;
    private float _jumpForce;
    private float _gravity = 1000;
    private float _angle;

    private Timer _barrelRollTimer;
    private Timer.Task _barrelRollTask;

    public PlayerSquare() {
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);

        System.out.println("Adding inputprocessor");
        InputManager.getInstance().addProcessor(this);

        setPosition(20, 20);
        setSize(20, 50);

        _barrelRollTimer = new Timer();
        _barrelRollTask = new Timer.Task() {
            @Override
            public void run() {
                _angle ++;
                if(_angle > 360) {
                    _angle = 0;
                }
            }
        };
    }

    @Override
    public void act() {
        float factor = Gdx.graphics.getDeltaTime();
        Rectangle dimension = getDimension();

        //jump
        if (Math.abs(_jumpForce - _gravity) < 100) {
            synchronized (_barrelRollTask) {
                if (!_barrelRollTask.isScheduled()) {
                    _barrelRollTimer.scheduleTask(_barrelRollTask, 0, 0.001f, 360);
                }
            }
        }

        dimension.y += _jumpForce * factor;
        _jumpForce *= .99f;

        dimension.y -= _gravity * factor;
        if (dimension.y < 20) {
            dimension.y = 20;
        }

        if (_moveLeft) {
            dimension.x -= _speed * factor;
        }

        if (_moveRight) {
            dimension.x += _speed * factor;
        }

        dimension = CollisionHelper.check(this, dimension);

        setPosition(dimension.x, dimension.y);
    }

    @Override
    public void draw(Batch batch) {
        Vector2 pos = getPosition();
        Vector2 size = getSize();

        _shapeRenderer.setColor(Color.BLUE);
        _shapeRenderer.begin();
        _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.rect(pos.x, pos.y, size.x * .5f, size.y * .5f, size.x, size.y, 1, 1, _angle);
        _shapeRenderer.end();
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
            _jumpForce = 1500f;
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
