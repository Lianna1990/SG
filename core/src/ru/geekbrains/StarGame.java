package ru.geekbrains;

import com.badlogic.gdx.Game;

import ru.geekbrains.screen.MenuScreen;

public class StarGame extends Game {



   // public GameOverScreen gameOverScreen;
    public MenuScreen menuScreen;

    @Override
    public void create() {
        setScreen(new MenuScreen(this));

        menuScreen=new MenuScreen(this);

      //  gameOverScreen=new GameOverScreen(this);
        setScreen(menuScreen);
    }

}
