package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Objects;

/**
 * View that combines both the MIDI implementation of a view as well as the GUI implementation of
 * a view. It will keep the two views in sync and make sure that they operate in tandem. Delegates
 * work to the views it contains.
 */
public class MidiGuiView implements IMusicView {

  private MidiViewImpl midi;
  private GuiViewFrame gui;

  /**
   * Constructor to allow us to construct one consolidated view containing the GUI view as well
   * as the MIDI view.
   * @param midi The midi view to be added
   * @param gui the gui view to be added
   */
  public MidiGuiView(MidiViewImpl midi, GuiViewFrame gui) {
    Objects.requireNonNull(midi);
    Objects.requireNonNull(gui);
    this.midi = midi;
    this.gui = gui;
  }

  @Override
  public void togglePlay() {
    this.midi.togglePlay();
    this.gui.togglePlay();
  }

  @Override
  public void initialize() {
    this.gui.initialize();
  }

  @Override
  public void moveLeft() {
    this.midi.moveLeft();
    this.gui.moveLeft();
  }

  @Override
  public void moveRight() {
    this.midi.moveRight();
    this.gui.moveRight();
  }

  @Override
  public void setBeat(int beat) {
    this.midi.setBeat(beat);
    this.gui.setBeat(beat);
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    this.gui.addMouseListener(listener);
    this.midi.addMouseListener(listener);
  }

  @Override
  public Integer getNoteAtLocation(int x, int y) {
    return this.gui.getNoteAtLocation(x, y);
  }

  @Override
  public int getCurrentBeat() {
    return this.gui.getCurrentBeat();
  }

  @Override
  public void reset() {
    this.gui.reset();
    this.midi.reset();
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    this.gui.addKeyListener(listener);
    this.midi.addKeyListener(listener);
  }

  @Override
  public boolean isPlaying() {
    return this.midi.isPlaying() && this.gui.isPlaying();
  }
}
