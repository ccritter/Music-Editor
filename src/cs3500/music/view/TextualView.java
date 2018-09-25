package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Objects;

import cs3500.music.model.IMusicModel;

/**
 * A String based representation of a View. The x-axis is all of the Pitches within the pitch range
 * of this piece, and the y-axis is every beat within the piece. Every note played on a pitch at a
 * beat will be represented either by an 'X' (if that is the beat the note starts on) or an '|'
 * (if it is continuing to play on another beat).
 */
public class TextualView implements IMusicView {
  IMusicModel model;
  Appendable out;

  /**
   * Construct a TextualView based on the given model.
   *
   * @param model the model who's piece will be outputted as text
   */
  public TextualView(IMusicModel model, Appendable out) {
    Objects.requireNonNull(model);
    this.model = model;
    this.out = out;
  }

  @Override
  public void initialize() {
    try {
      out.append(this.model.display());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void togglePlay() {
    //Does nothing
  }

  @Override
  public void moveLeft() {
    //Does nothing
  }

  @Override
  public void moveRight() {
    //Does nothing
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    //Does nothing
  }

  @Override
  public boolean isPlaying() {
    return false;
  }

  @Override
  public void setBeat(int beat) {
    //Does nothing
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    //Does nothing
  }

  @Override
  public Integer getNoteAtLocation(int x, int y) {
    return null;
  }

  @Override
  public int getCurrentBeat() {
    return 0;
  }

  @Override
  public void reset() {
    //Does nothing
  }
}
