package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label; // Import Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen1 implements Screen {
    private Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private TextButton pause;
    private TextButton back;
    private Main dam;
    private Label scoreLabel; // Label for displaying score
    private int score = 0; // Variable to hold score

    public GameScreen1(Main dam) {
        this.dam = dam;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("level.ppm");
        stage = new Stage(); // Initialize the stage
        Gdx.input.setInputProcessor(stage); // Set input processor
        skin = new Skin(Gdx.files.internal("freezing/skin/freezing-ui.json"));

        // Create buttons
        pause = new TextButton("Pause", skin);
        back = new TextButton("Back", skin);

        // Initialize the score label
        scoreLabel = new Label("Score: " + score, skin);
        scoreLabel.setFontScale(2); // Adjust the font scale for better visibility

        // Add click listener for pause button
        pause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Implement pause functionality here
                dam.setScreen(new LevelEndScreen(dam));
            }
        });

        // Add click listener for back button
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dam.setScreen(new LevelScreen(dam)); // Go back to the level selection screen
            }
        });

        // Set sizes for the buttons
        pause.setSize(100, 50);
        back.setSize(100, 50);

        // Position the buttons
        updateButtonPositions();
        stage.addActor(pause);
        stage.addActor(back);

        // Add score label to the stage
        stage.addActor(scoreLabel);
        updateScoreLabelPosition(); // Position the score label
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f); // Clear the screen

        // Begin drawing
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update stage viewport
        updateButtonPositions(); // Update button positions on resize
        updateScoreLabelPosition(); // Update score label position on resize
    }

    private void updateButtonPositions() {
        // Set positions for buttons
        pause.setPosition(Gdx.graphics.getWidth() - pause.getWidth() - 10, Gdx.graphics.getHeight() - pause.getHeight() - 10); // Top right
        back.setPosition(10, Gdx.graphics.getHeight() - back.getHeight() - 10); // Top left
    }

    private void updateScoreLabelPosition() {
        // Set position for score label at the top center
        scoreLabel.setPosition((Gdx.graphics.getWidth() - scoreLabel.getWidth()) / 2, Gdx.graphics.getHeight() - scoreLabel.getHeight() - 10);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        stage.dispose();
        skin.dispose();
    }
}
