package nl.joozey.shapeshifter.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by mint on 16-4-16.
 */
public class Constants {

    public static final Color JEFF_COLOR = new Color(0.05f, 0.3f, 0.78f, 1f);
    public static final Color RINN_COLOR = new Color(1f, 0.39f, 0f, 1f);
    public static final Color GREEN_ENV_COLOR = new Color(0.32f, 0.91f, 0f, 1f);

    public static final BitmapFont font = new BitmapFont(Gdx.files.internal("forced_square.fnt"), Gdx.files.internal("forced_square.png"), false);
    public static final Texture greenBackground = new Texture("greenscene.png");
    public static final Texture grayBackground = new Texture("grayscene.png");
}
