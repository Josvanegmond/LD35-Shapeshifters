package nl.joozey.shapeshifter.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import nl.joozey.shapeshifter.main.Constants;
import nl.joozey.shapeshifter.util.CountTimer;
import nl.joozey.shapeshifter.util.HSL;

/**
 * Created by mint on 16-4-16.
 */
public class CorruptObject extends GameObject {

    private ShapeRenderer _shapeRenderer;
    private HSL _color;
    private float _angle;
    private float spacing = 25;
    private boolean _dissolved;
    private boolean _dissolving;

    List<Rectangle> _corruptionList;

    public CorruptObject(float x, float y, float w, float h) {
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);
        _color = new HSL(new Color(Constants.GREEN_ENV_COLOR));
        _corruptionList = new ArrayList<Rectangle>();

        setPosition(x, y);
        setSize(w, h);

        for (int i = 0; i < h / spacing; i++) {
            for (int j = 0; j < w / spacing; j++) {
                _corruptionList.add(new Rectangle(
                        i * spacing + (float) Math.random() * spacing - spacing * .5f,
                        j * spacing + (float) Math.random() * spacing - spacing * .5f,
                        (float) Math.random() * 360f,
                        (int) (Math.random()) == 0 ?
                                -1 * ((float) Math.random() * 3f + 1f) :
                                1 * ((float) Math.random() * 3f + 1f)));
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        if (!_dissolved) {
            Vector2 pos = getPosition();

            _shapeRenderer.setColor(Color.BLACK);
            _shapeRenderer.begin();

            for (Rectangle corruptPos : _corruptionList) {
                _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                _shapeRenderer.rect(
                        pos.x + corruptPos.x + (float) Math.cos(Math.toRadians(corruptPos.height * _angle + corruptPos.width)) * spacing,
                        pos.y + corruptPos.y + (float) Math.sin(Math.toRadians(corruptPos.height * _angle + corruptPos.width)) * spacing,
                        3, 3);
            }

            _shapeRenderer.end();
        }
    }

    @Override
    public void act() {
        if (!_dissolved) {
            _angle += Gdx.graphics.getDeltaTime() * 200;
            if (_angle > 360) {
                _angle -= 360;
            }

            if (_angle < 0) {
                _angle += 360;
            }
        }
    }

    @Override
    public boolean isGrabbableBy(GameObject gameObject) {
        if (!_dissolved && !_dissolving && gameObject instanceof Jeff) {
            Jeff jeff = (Jeff) gameObject;
            if (jeff.getShape() == 0 && jeff.isActionActive()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void hitObject(GameObject gameObject) {
        if (!_dissolved && !_dissolving && gameObject instanceof Jeff) {
            Jeff jeff = (Jeff) gameObject;
            if (jeff.getShape() == 4 | (jeff.getShape() == 0 && jeff.isActionActive())) {
                _dissolve();
            } else {
                jeff.redbow(100);
                jeff.paralyze();
            }
        }
    }

    @Override
    public void unhitObject(GameObject gameObject) {
        if (gameObject instanceof Jeff) {
            Jeff jeff = (Jeff) gameObject;
            jeff.freeze(false);
        }
    }

    private void _dissolve() {
        if(!_dissolved && !_dissolving) {
            _dissolving = true;
            CountTimer timer = new CountTimer(new CountTimer.Task() {
                @Override
                public void count(float amount) {
                    spacing += 100 * Gdx.graphics.getDeltaTime();
                }

                @Override
                public void finish() {
                    _dissolved = true;
                    setPosition(-100, 0);
                    setSize(0, 0);
                }
            }, 200, 0, 0.01f);
        }
    }
}
