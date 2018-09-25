package cs3500.music.controller;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import javax.swing.Timer;

import cs3500.music.model.IMusicModel;
import cs3500.music.view.IMusicView;

/**
 * Class that allows us to take in a music model and a particular music model view and allows us
 * to control the view while manipulating the model.
 */
public class MusicController implements IMusicController {
  private IMusicModel model;
  private IMusicView view;
  private Timer timer;
  private int start;
  private int end;
  private Integer noteClicked;

  /**
   * Constructor for MusicController which takes in a music model and a chosen view to display.
   * @param model the model whose notes we are working on
   * @param view the music view we have to chosen to display the model through
   */
  public MusicController(IMusicModel model, IMusicView view) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(view);
    this.model = model;
    this.view = view;
    this.configureKeyBoardListener();
    this.configureMouseListener();

    ActionListener looper = actionEvent -> {
      view.moveRight();
      end += 1;
    };

    this.timer = new Timer(this.model.getTempo() / 1000, looper);
  }

  /**
   * Creates multiple maps for each key press type and their corresponding runnable actions. Then
   * adds these maps to the key listener object.
   */
  private void configureKeyBoardListener() {
    Map<Character, Runnable> keyTypes = new HashMap<>();
    Map<Integer, Runnable> keyPresses = new HashMap<>();
    Map<Integer, Runnable> keyReleases = new HashMap<>();

    keyPresses.put(KeyEvent.VK_LEFT, new MoveLeft());
    keyPresses.put(KeyEvent.VK_RIGHT, new MoveRight());
    keyPresses.put(KeyEvent.VK_HOME, new MoveHome());
    keyPresses.put(KeyEvent.VK_END, new MoveEnd());
    keyPresses.put(KeyEvent.VK_COMMA, new DecreaseTempo());
    keyPresses.put(KeyEvent.VK_PERIOD, new IncreaseTempo());
    keyTypes.put(' ', new PlayOrPause());

    KeyboardListener kbd = new KeyboardListener();
    kbd.setKeyTypedMap(keyTypes);
    kbd.setKeyPressedMap(keyPresses);
    kbd.setKeyReleasedMap(keyReleases);

    view.addKeyListener(kbd);
  }

  /**
   * Creates multiple maps for each mouse click type and their corresponding runnable
   * actions. Then adds these maps to the key listener object.
   */
  private void configureMouseListener() {
    Map<Integer, Runnable> buttonPresses = new HashMap<>();
    Map<Integer, Runnable> buttonReleases = new HashMap<>();
    Map<Integer, Runnable> buttonClicked  = new HashMap<>();

    buttonPresses.put(MouseEvent.BUTTON1, new StartAddNote());
    buttonReleases.put(MouseEvent.BUTTON1, new StopAddNote());
    MouseKeyListener mse = new MouseKeyListener();
    mse.setButtonPressedMap(buttonPresses);
    mse.setButtonReleasedMap(buttonReleases);
    mse.setButtonClickedMap(buttonClicked);

    view.addMouseListener(mse);
  }

  /**
   * Runnable object to add a note to the music model and display it in the music view.
   */
  class StartAddNote implements Runnable {
    private int x;
    private int y;

    /**
     * Sets the x coordinate to the given value.
     * @param x the value to set the x value to
     */
    public void setX(int x) {
      this.x = x;
    }

    /**
     * Sets the y coordinate to the given value.
     * @param y the value to set the y value to
     */
    public void setY(int y) {
      this.y = y;
    }

    @Override
    public void run() {
      Integer note = view.getNoteAtLocation(this.x, this.y);
      if (!view.isPlaying() && note != null) {
        timer.setDelay(model.getTempo() / 1000);
        timer.start();
        noteClicked = note;
        start = view.getCurrentBeat();
        end = start + 1;
      }
    }
  }

  class StopAddNote implements Runnable {
    @Override
    public void run() {
      timer.stop();

      if (noteClicked != null) {
        model.addNote(start, end, 1, noteClicked, 100);
        view.reset();
        view.moveRight();
      }
    }
  }

  /**
   * Runnable object to Move the slider to the right by one beat.
   */
  class MoveRight implements Runnable {
    @Override
    public void run() {
      if (!view.isPlaying() || timer.isRunning()) {
        view.moveRight();
      }
    }
  }

  /**
   * Runnable object to Move the slider to the start beat.
   */
  class MoveHome implements Runnable {
    @Override
    public void run() {
      if (!view.isPlaying()) {
        view.setBeat(0);
      }
    }
  }

  /**
   * Runnable object to Move the slider to the end beat.
   */
  class MoveEnd implements Runnable {
    @Override
    public void run() {
      if (!view.isPlaying()) {
        view.setBeat(model.getEndBeat());
      }
    }
  }

  /**
   * Runnable object to Move the slider to the left by one beat.
   */
  class MoveLeft implements Runnable {
    @Override
    public void run() {
      if (!view.isPlaying()) {
        view.moveLeft();
      }
    }
  }

  /**
   * Runnable object to stop or play the music piece if it is not being played.
   */
  class PlayOrPause implements Runnable {
    @Override
    public void run() {
      if (!timer.isRunning()) {
        view.togglePlay();
      }
    }
  }

  /**
   * Runnable object to increase the tempo of the model.
   */
  class IncreaseTempo implements Runnable {
    @Override
    public void run() {
      int decAmount = 1000;
      if (model.getTempo() - decAmount < 0) {
        model.setTempo(1);
      } else {
        model.setTempo(model.getTempo() -  decAmount);
      }

    }
  }

  /**
   * Runnable object to increase the tempo of the model.
   */
  class DecreaseTempo implements Runnable {
    @Override
    public void run() {
      model.setTempo(model.getTempo() + 1000);
    }
  }

  @Override
  public void display() {
    this.view.initialize();
  }
}
