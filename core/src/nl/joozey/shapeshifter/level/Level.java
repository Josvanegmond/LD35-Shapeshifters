package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Objects;

import nl.joozey.shapeshifter.gameobject.GameObject;

/**
 * Created by mint on 16-4-16.
 */
public class Level {

    protected BitmapFont _font;
    protected ShapeRenderer _shapeRenderer;
    protected LevelManager _levelManager;
    protected Texture _greenBackground;
    protected Texture _grayBackground;
    protected String _message = "";

    protected float _floorLevel = 70;

    public Level() {}

    public void load(int dir) {
        _font = new BitmapFont(Gdx.files.internal("forced_square.fnt"), Gdx.files.internal("forced_square.png"), false);
        _font.setColor(Color.WHITE);
        _shapeRenderer = new ShapeRenderer();
        _shapeRenderer.setAutoShapeType(true);

        _levelManager = LevelManager.getInstance();

        _greenBackground = new Texture("greenscene.png");
        _grayBackground = new Texture("grayscene.png");
    }

    public void run() {
        for (GameObject gameObject : _levelManager.getAllGameObjects()) {
            gameObject.run();
        }
    }

    public void drawScene(Batch batch) {

    }

    public final void draw(Batch batch) {
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float alpha = LevelManager.getInstance().getDimFactor();

        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(_grayBackground, 0, 0);

        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(_greenBackground, 0, 0);

        batch.end();
        batch.begin();

        _shapeRenderer.begin();

        //game scene
        drawScene(batch);

        //dialog box
        _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.setColor(Color.BLACK);
        _shapeRenderer.rect(40, 20, 720, _floorLevel - 40);
        _shapeRenderer.end();

        batch.end();
        batch.begin();

        if(!Objects.equals(_message, "")) {
            _font.draw(batch, _message, 60, 38);
        }
    }

    public void setMessage(String message) {
        _message = message;
    }

    public Level getLeft() { return null; }

    public Level getRight() { return null; }

    public void onTouched(GameObject gameObject) {

    }
}
