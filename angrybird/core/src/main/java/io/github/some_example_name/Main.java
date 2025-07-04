package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch batch;
    private Music backgroundMusic;


    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new HomePage(this));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("02. Ab Xmas 2016.mp3"));
        backgroundMusic.setLooping(true); // Loop the music
        backgroundMusic.play(); // Play music

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (backgroundMusic != null) {
            backgroundMusic.dispose(); // Dispose of music only when game closes
        }

    }
}
