package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import nl.joozey.shapeshifter.gameobject.GameObject;

/**
 * Created by mint on 16-4-16.
 */
public class Level {

    private LevelManager _levelManager;
    private Texture _greenBackground;
    private Texture _grayBackground;

    public Level() {
        _levelManager = LevelManager.getInstance();

        _greenBackground = new Texture("greenscene.png");
        _grayBackground = new Texture("grayscene.png");

        //floor
        _levelManager.createWall(this, 0, 0, Gdx.graphics.getWidth(), 20);

        for(int i = 0; i < 10; i++) {
            float x = (float)Math.random() * Gdx.graphics.getWidth();
            float y = (float)Math.random() * Gdx.graphics.getHeight();
            _levelManager.createBlimp(this, x, y);
        }

        for(int i = 0; i < 10; i++) {
            float w = (float)Math.random() * 100 + 20;
            float h = (float)Math.random() * 100 + 20;
            float x = (float)Math.random() * Gdx.graphics.getWidth() - w;
            float y = (float)Math.random() * Gdx.graphics.getHeight() - h;

            _levelManager.createWall(this, x, y, w, h);
        }

        //player
        _levelManager.createJeff(this, 20, 150);
    }

    public void act() {
        for(GameObject gameObject : _levelManager.getAllGameObjects()) {
            gameObject.act();
        }
    }

    public void draw(Batch batch) {
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float takenBlimps = LevelManager.getInstance().getTakenBlimpCount();
        float totalBlimps = LevelManager.getInstance().getBlimpCount();

        float alpha = 1;
        if(totalBlimps > 0) {
            alpha = 1f - takenBlimps / totalBlimps;
        }

        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(_grayBackground, 0, 0);

        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(_greenBackground, 0, 0);


        batch.end();

        batch.begin();
        for(GameObject gameObject : _levelManager.getAllGameObjects()) {
            gameObject.draw(batch);
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
