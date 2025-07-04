
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

public class LevelEndScreen implements Screen {
    private Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private TextButton mainmenu;
    private TextButton nextlevel;
    private TextButton playagain;
    private Main dam;
    private Label ScoreLabel; // Label for current score
    private Label highestScoreLabel; // Label for highest score
    private int Score; // Current score to display
    private int highestScore; // Highest score to display

    public LevelEndScreen(Main dam) {
        this.dam = dam;
        this.Score = Score;
        this.highestScore = highestScore;
    }
    private int currentLevel = 1; // Variable to track the current level

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("pause.jpg");
        stage = new Stage(); // Initialize the stage
        Gdx.input.setInputProcessor(stage); // Set input processor
        skin = new Skin(Gdx.files.internal("freezing/skin/freezing-ui.json"));

        // Create buttons
        mainmenu = new TextButton("Main Menu", skin);
        playagain = new TextButton("Play Again", skin);
        nextlevel = new TextButton("Next Level", skin);

        // Add click listener for buttons
        mainmenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dam.setScreen(new HomePage(dam)); // Go to Main Menu
            }
        });

        playagain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (currentLevel) {
                    case 1:
                        dam.setScreen(new Level1Screen(dam)); // Restart Level 1
                        break;
                    case 2:
                        dam.setScreen(new Level2Screen(dam)); // Restart Level 2
                        break;
                    case 3:
                        dam.setScreen(new Level3Screen(dam)); // Restart Level 3
                        break;
                }
            }
        });

        nextlevel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentLevel < 3) {
                    currentLevel++; // Move to the next level
                    switch (currentLevel) {
                        case 2:
                            dam.setScreen(new Level2Screen(dam)); // Go to Level 2
                            break;
                        case 3:
                            dam.setScreen(new Level3Screen(dam)); // Go to Level 3
                            break;
                    }
                }
            }
        });

        // Set sizes for the buttons
        mainmenu.setSize(150, 50);
        playagain.setSize(150, 50);
        nextlevel.setSize(150, 50);

        // Position the buttons at the bottom center
        updateButtonPositions();

        // Initialize score labels
        ScoreLabel = new Label("Score: " + Score, skin);
        highestScoreLabel = new Label("Highest Score: " + highestScore, skin);

        // Set font scale for labels
        ScoreLabel.setFontScale(2);
        highestScoreLabel.setFontScale(2);
        // Set color to black
        ScoreLabel.setColor(0, 0, 0, 1); // RGBA for black
        highestScoreLabel.setColor(0, 0, 0, 1); // RGBA for black

        // Add buttons and labels to the stage
        stage.addActor(mainmenu);
        stage.addActor(playagain);
        stage.addActor(nextlevel);
        stage.addActor(ScoreLabel);
        stage.addActor(highestScoreLabel);

        // Position the score labels at the center
        updateScoreLabelPositions();
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
        updateScoreLabelPositions(); // Update score label positions on resize
    }

    private void updateButtonPositions() {
        // Position buttons at the bottom center
        float totalWidth = mainmenu.getWidth() + playagain.getWidth() + nextlevel.getWidth() + 40; // 40 for spacing
        mainmenu.setPosition((Gdx.graphics.getWidth() - totalWidth) / 2, 10);
        playagain.setPosition(mainmenu.getX() + mainmenu.getWidth() + 20, 10); // 20 for spacing
        nextlevel.setPosition(playagain.getX() + playagain.getWidth() + 20, 10); // 20 for spacing
    }

    private void updateScoreLabelPositions() {
        // Calculate centered positions for the labels
        ScoreLabel.setPosition((Gdx.graphics.getWidth() - ScoreLabel.getPrefWidth()) / 2,
            (Gdx.graphics.getHeight() + ScoreLabel.getPrefHeight()) / 2 + 20); // Offset slightly above center
        highestScoreLabel.setPosition((Gdx.graphics.getWidth() - highestScoreLabel.getPrefWidth()) / 2,
            (Gdx.graphics.getHeight() - highestScoreLabel.getPrefHeight()) / 2 - 10); // Offset slightly below center
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
