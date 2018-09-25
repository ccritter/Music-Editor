package cs3500.music.view;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import cs3500.music.model.IMusicModel;
import cs3500.music.model.Note;
import cs3500.music.model.Sound;

import java.util.HashMap;
import java.util.List;

/**
 * A view which displays the notes playing on each beat.
 */
public class NotePanel extends JPanel {
  private IMusicModel<HashMap<Integer, List<Sound>>, Note> model;
  private int top;
  private int left;
  private int blockSize;
  private List<Note> notesToDisp;
  private int timeSignature;
  private int width;
  private int height;
  private int recX;
  private int recY;
  private int currentBeat;

  /**
   * Constructor for the NotePane class. Default time signature is 4 beats per measure.
   *
   * @param model the model whose notePanel is being created
   */
  NotePanel(IMusicModel model) {
    this.model = model;
    this.top = 25;
    this.left = 25;
    this.blockSize = 15;
    this.notesToDisp = this.model.notesToDisplay();
    this.timeSignature = 4;
    this.width = this.model.getEndBeat() + 1;
    this.height = this.notesToDisp.size();
    this.recX = left + blockSize;
    this.recY = top - blockSize;
    this.currentBeat = 0;
    this.setVisible(true);
  }

  /**
   * Constructs a NotePanel with a custom time signature.
   *
   * @param model   The model to be used to read notes from
   * @param timeSignature   The number of beats per measure
   */
  NotePanel(IMusicModel model, int timeSignature) {
    this(model);
    if (timeSignature < 1) {
      throw new IllegalArgumentException("Time signature must be greater than 0.");
    }
    this.timeSignature = timeSignature;
  }

  /**
   * Setter method to set the current beat of the NotePanel.
   *
   * @param beat the beat to set the current beat to
   */
  public void setBeat(int beat) {
    if (beat < 0 || beat > this.model.getEndBeat()) {
      throw new IllegalArgumentException("Cannot set beat, must be between 0 and the "
              + "Model's last beat");
    }
    this.currentBeat = beat;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    this.mutateSlider(g, currentBeat);
    this.displayBeats((Graphics2D) g, currentBeat);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.displayNotes(g);
    this.drawGrid((Graphics2D) g);
    this.displayBeats((Graphics2D) g, currentBeat);
  }

  /**
   * Display all the notes on the grid of notes and beat number.
   *
   * @param g       The graphic Object to draw on
   * @param curBeat the notes to be lit up on the current beat
   */
  private void displayBeats(Graphics2D g, int curBeat) {
    HashMap<Integer, List<Sound>> beatMap = this.model.getBeats(0, this.model.getEndBeat());
    for (int i = 0; i <= this.model.getEndBeat(); i += 1) {
      if (beatMap.containsKey(i)) {
        List<Sound> temp = beatMap.get(i);
        for (Sound tempSound : temp) {
          Note tempNote = tempSound.getNote();
          int locY = (this.notesToDisp.indexOf(tempNote) * blockSize) + top - blockSize;
          int locX = (i * blockSize) + left + blockSize;
          if (i == curBeat) {
            g.setColor(Color.MAGENTA);
            g.fillRect(locX, locY, blockSize, blockSize);
          } else if (tempSound.getStartBeat() == i) {
            g.setColor(Color.black);
            g.fillRect(locX, locY, blockSize, blockSize);
          } else {
            g.setColor(Color.green);
            g.fillRect(locX, locY, blockSize, blockSize);
          }
        }
      }
    }
  }

  /**
   * Display all the notes in the piece in the entire piece at the extreme left.
   *
   * @param g The graphics object to be drawn on
   */
  private void displayNotes(Graphics g) {
    int i = 0;
    for (Note tempNote : this.notesToDisp) {
      g.drawString(tempNote.toString(), left - blockSize, top + i);
      i = i + blockSize;
    }
  }

  /**
   * Draw the grid of notes to be played and its respective beat number.
   *
   * @param g the graphics object for the grid to be drawn on
   */
  private void drawGrid(Graphics2D g) {
    int leftBorder = left + blockSize;
    int topBorder = top - blockSize;
    int widthRec = width * blockSize;
    int heightRec = height * blockSize;
    g.drawRect(leftBorder, topBorder, widthRec, heightRec);

    for (int i = 0; i < notesToDisp.size(); i += 1) {
      int temp = i * blockSize;
      temp = temp + top;
      g.drawLine(leftBorder, temp, leftBorder + widthRec, temp);
    }

    for (int i = 0; i <= this.model.getEndBeat(); i += timeSignature) {
      int temp = i * blockSize;
      temp = temp + leftBorder;
      g.drawLine(temp, topBorder, temp, topBorder + heightRec);
      g.drawString(String.valueOf(i), temp, topBorder);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension((blockSize * width) + (blockSize * 3), (blockSize * height)
            + (blockSize * 2));
  }

  /**
   * To move the current beat slider according to the current beat.
   *
   * @param g       the graphics object to be drawn on
   * @param curBeat the beat on which the slider has to be drawn on
   */
  private void mutateSlider(Graphics g, int curBeat) {
    g.setColor(Color.RED);
    g.fillRect(recX + (curBeat * blockSize), recY, 3, this.model.notesToDisplay().size() * 15);
  }
}
