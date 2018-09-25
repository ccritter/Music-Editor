package cs3500.music.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.BoxLayout;

import cs3500.music.model.IMusicModel;

/**
 * The GUI View with a note pane and a piano pane. The user can see the notes playing at eat beat
 * and the notes on the piano, and scroll through the beats with arrow keys.
 */
public class GuiViewFrame extends javax.swing.JFrame implements IMusicView {

  private int currentBeat;
  private final PianoPanel pianoPanel;

  // Changed: made a few things fields for easier access
  private NotePanel notePanel;
  //private int lastBeat;
  private JScrollPane scrollPane;
  private IMusicModel model;

  // Added: so that we can slide the slider while playing
  private Timer timer;

  // Added: to allow for toggling of playing and pausing
  private boolean isPlaying;

  /**
   * Constructor for the GUI view.
   *
   * @param model the model whose view will be constructed
   */
  public GuiViewFrame(IMusicModel model) {
    // Changed: this constructor was slightly reworked, got rid some KeyBindings and operated on
    // fields instead of localized variables.
    Objects.requireNonNull(model);
    this.setResizable(false); // Changed: disabled resizing
    this.currentBeat = 0;
    this.isPlaying = false;
    this.model = model;
    this.pianoPanel = new PianoPanel(model);
    this.notePanel = new NotePanel(model);
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    this.scrollPane = new JScrollPane(notePanel);
    this.scrollPane.setPreferredSize(new Dimension(1500, 250));
    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    panel.add(this.scrollPane);
    panel.add(this.pianoPanel);
    //this.lastBeat = model.getEndBeat();

    //Added: to allow for timing of advancing playback.
    ActionListener looper = actionEvent -> {
      if (this.currentBeat == this.model.getEndBeat()) {
        this.isPlaying = false;
        timer.stop();
      } else {
        moveRight();
      }
    };

    this.timer = new Timer(this.model.getTempo() / 1000, looper);

    this.getContentPane().add(panel);
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void initialize() {
    this.setVisible(true);
  }

  @Override
  public void togglePlay() {
    if (!isPlaying) {
      this.timer.start();
    } else {
      this.timer.stop();
    }

    this.isPlaying = !this.isPlaying;
  }

  /**
   * Scroll the scroll pane to the appropriate spot so that the slider is in view.
   */
  private void scroll() {
    JScrollBar hor = this.scrollPane.getHorizontalScrollBar();
    int min = hor.getValue();
    int max = hor.getVisibleAmount() + min - 80;
    int cur = this.currentBeat * 15;
    if (cur < min) {
      hor.setValue(min - 1380);
    }
    if (cur >= max) {
      hor.setValue(min + 1380);
    }
  }
  // Added: allow for automatic scrolling

  @Override
  public void moveLeft() {
    if (currentBeat > 0) {
      currentBeat = currentBeat - 1;
      pianoPanel.setBeat(currentBeat);
      notePanel.setBeat(currentBeat);
      this.scroll();
      repaint();
    }
  }

  @Override
  public void moveRight() {
    if (currentBeat < this.model.getEndBeat()) {
      currentBeat = currentBeat + 1;
      pianoPanel.setBeat(currentBeat);
      notePanel.setBeat(currentBeat);
      this.timer.setDelay(this.model.getTempo() / 1000);
      this.scroll();
      repaint();
    }
  }

  @Override
  public boolean isPlaying() {
    return this.isPlaying;
  }

  @Override
  public void setBeat(int beat) {
    if (beat >= 0 && beat <= this.model.getEndBeat()) {
      this.currentBeat = beat;
      pianoPanel.setBeat(beat);
      notePanel.setBeat(beat);
      if (beat == 0) {
        this.scrollHome();
      }
      if (beat == this.model.getEndBeat()) {
        this.scrollEnd();
      }
    }
    repaint();
  }

  @Override
  public Integer getNoteAtLocation(int x, int y) {
    return this.pianoPanel.getNoteAtLoc(x, y);
  }

  @Override
  public int getCurrentBeat() {
    return this.currentBeat;
  }

  @Override
  public void reset() {
    NotePanel newPanel = new NotePanel(this.model);
    this.remove(this.notePanel);
    this.add(newPanel);
    this.notePanel = newPanel;
    this.scrollPane.setViewportView(this.notePanel);
    repaint();
  }

  /**
   * Scroll to the very beginning of the piece.
   */
  //Added to allow the user to scroll to the start upon pressing the home button
  private void scrollHome() {
    JScrollBar hor = this.scrollPane.getHorizontalScrollBar();
    hor.setValue(0);
  }

  /**
   * Scroll to the very end of the piece.
   */
  //Added to allow the user to scroll to the end upon pressing the end key
  private void scrollEnd() {
    JScrollBar hor = this.scrollPane.getHorizontalScrollBar();
    hor.setValue(hor.getMaximum() - hor.getVisibleAmount() + 80);
  }
  // Added: as per the spec of the assignment to allow for scrolling to beginning and end.

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(1500, 500);
  }

}
