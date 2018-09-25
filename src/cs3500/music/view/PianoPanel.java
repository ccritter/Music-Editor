package cs3500.music.view;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cs3500.music.model.IMusicModel;
import cs3500.music.model.Note;
import cs3500.music.model.Sound;

/**
 * The Piano Panel which displays the notes on the piano which have to be played.
 */
public class PianoPanel extends JPanel {
  private int left;
  private int keyWidth;
  private int keyHeight;
  private IMusicModel<HashMap<Integer, List<Sound>>, Note> model;
  private int currentBeat;

  // Added: allow for easy iteration for click location checking. All the keys on the panel.
  private List<Key> blackKeys;
  private List<Key> whiteKeys;

  /**
   * The constructor for the piano pane.
   *
   * @param model the model whose pane has to be drawn
   */
  //Modified to allow us to check for mouse clicks in an easier way
  public PianoPanel(IMusicModel model) {
    this.setLayout(null);
    this.setBackground(Color.gray);
    this.left = 30;
    this.keyWidth = 20;
    this.keyHeight = keyWidth * 10;
    this.model = model;
    this.currentBeat = 0;
    // Added: all below added for clicking on the piano keys, and absolute positioning.
    this.setMaximumSize(new Dimension(Integer.MAX_VALUE,
            (int) this.getPreferredSize().getHeight()));
    this.blackKeys = new ArrayList<>();
    this.whiteKeys = new ArrayList<>();
    this.initKeys();

    Insets insets = this.getInsets();
    Dimension size;

    for (Key k : this.whiteKeys) {
      this.add(k);
      size = k.getPreferredSize();
      k.setBounds(k.getX() + insets.left, k.getY() + insets.top, size.width, size.height);
    }

    for (Key k : this.blackKeys) {
      this.add(k);
      size = k.getPreferredSize();
      k.setBounds(k.getX() + insets.left, k.getY() + insets.top, size.width, size.height);
    }

    this.setVisible(true);
  }

  /**
   * Setter method for the current beat.
   *
   * @param beat the beat to be set as the current beat
   */
  public void setBeat(int beat) {
    if (beat < 0 || beat > this.model.getEndBeat()) {
      throw new IllegalArgumentException("Cannot set beat, must be between 0 and the " +
              "Model's last beat");
    }
    this.currentBeat = beat;
  }

  //Changed since last submission to incorporate a better way of drawing keys.
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (Key k : this.whiteKeys) {
      k.paintComponent(g);
    }
    for (Key k : this.blackKeys) {
      k.paintComponent(g);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(1500, 250);
  }

  /**
   * Initilaize the keys of the piano to allow it to be draw later.
   */
  //Added to allow us to check for mouse clicks in an easier way
  private void initKeys() {
    for (int i = 0; i < 120; i += 1) {
      int pitch = i % 12;
      int oct = i / 12;
      int leftMost = (left + (oct * keyWidth * 7));
      if (pitch == 1 || pitch == 3 || pitch == 6 || pitch == 8 || pitch == 10) {
        int x = 0;
        switch (pitch) {
          case 1:
            x = 1;
            break;
          case 3:
            x = 2;
            break;
          case 6:
            x = 4;
            break;
          case 8:
            x = 5;
            break;
          case 10:
            x = 6;
            break;
          default:
            throw new RuntimeException("Invalid pitch value");
        }

        Key newKey = new Key(i, leftMost + (x * keyWidth) - (int) (keyWidth * 0.25), true);
        this.blackKeys.add(newKey);

      } else {
        int x = 0;
        switch (pitch) {
          case 0:
            x = 0;
            break;
          case 2:
            x = 1;
            break;
          case 4:
            x = 2;
            break;
          case 5:
            x = 3;
            break;
          case 7:
            x = 4;
            break;
          case 9:
            x = 5;
            break;
          case 11:
            x = 6;
            break;
          default:
            throw new RuntimeException("Invalid pitch value");
        }

        Key newKey = new Key(i, leftMost + (x * keyWidth), false);
        this.whiteKeys.add(newKey);

      }
    }
  }

  // Removed: Methods for initializing and drawing black and white keys separately

  /**
   * On piano roll of this panel, check to see what note is present at the given (x, y) coordinates.
   * If the location is outside of the piano roll, then it returns null.
   *
   * @param x   the x coordinate to check
   * @param y   the y coordinate to check
   * @return    the integer value of the note found at the location.
   */
  protected Integer getNoteAtLoc(int x, int y) {
    for (Key k : this.blackKeys) {
      if (k.contains(x, y)) {
        return k.note;
      }
    }

    for (Key k : this.whiteKeys) {
      if (k.contains(x, y)) {
        return k.note;
      }
    }

    return null;
  }

  /**
   * Method which checks if the note entered as a parameter is the note being played at this
   * current beat.
   * @param key the note to be checked if it is being played
   * @return whether the note entered is being played right now or not
   */
  //Added to allow us to check for mouse clicks in a better way
  private boolean isNotePlaying(int key) {
    HashMap<Integer, List<Sound>> beats = this.model.getBeats(0, this.model.getEndBeat());
    if (beats.containsKey(this.currentBeat)) {
      List<Sound> playingNow = beats.get(this.currentBeat);
      for (Sound tempSound : playingNow) {
        if (tempSound.getNote().hashCode() == key) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Represents a black or white key on a piano roll.
   */
  //Added to check for mouse clicks in a much easier way
  private class Key extends JComponent {
    private int note;
    private int x;
    private boolean black;

    /**
     * Constructs a new key component with the given note value, x location on the panel, and a
     * boolean which is true if the key is black and false if it is white.
     *
     * @param note   the integer value of the note the key represents.
     * @param x      the absolute x location of the key on the panel
     * @param black  is this key black
     */
    Key(int note, int x, boolean black) {
      this.note = note;
      this.x = x;
      this.black = black;
      this.setLocation(this.x, 250);
      if (this.black) {
        this.setPreferredSize(new Dimension((int) (keyWidth * .75), keyHeight / 2));
      } else {
        this.setPreferredSize(new Dimension(keyWidth, keyHeight));
      }
      this.setVisible(true);
    }

    @Override
    public boolean contains(int x, int y) {
      y = y - 12;
      x = x - 2; // TODO
      if (y >= 250 && y <= (250 + this.getHeight())) {
        if (x >= (this.x) && x <= (this.x + this.getWidth())) {
          return true;
        }
      }
      return false;
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);

      if (this.black) {
        g.setColor(Color.BLACK);
      } else {
        g.setColor(Color.WHITE);
      }

      if (isNotePlaying(this.note)) {
        g.setColor(Color.MAGENTA);
      }

      g.fillRect(x, 0, this.getWidth(), this.getHeight());
      g.setColor(Color.BLACK);
      g.drawRect(x, 0, this.getWidth(), this.getHeight());
    }

  }

}
