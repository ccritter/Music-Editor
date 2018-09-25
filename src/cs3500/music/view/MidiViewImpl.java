package cs3500.music.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.sound.midi.Sequencer;
import javax.sound.midi.Sequence;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Track;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.swing.Timer;

import cs3500.music.model.IMusicModel;
import cs3500.music.model.Note;
import cs3500.music.model.Sound;

/**
 * Audio based view for playing the notes of a MusicModel, using Java's built in MIDI classes.
 */
public class MidiViewImpl implements IMusicView {
  private final Sequencer sequencer;
  private final Sequence sequence;

  private final IMusicModel<HashMap<Integer, List<Sound>>, Note> model;

  // Added: to allow for starting in the middle of the piece.
  private int startingBeat;

  // Added: so that we can slide the slider while playing
  private Timer timer;

  /**
   * Construct a new MidiViewImpl, which will be used to play the notes of the given model.
   *
   * @param model the MusicModel which will be read to get the notes to be played.
   * @throws MidiUnavailableException thrown when MIDI is unavailable
   * @throws InvalidMidiDataException thrown when MIDI data is invalid
   */
  public MidiViewImpl(IMusicModel model) throws MidiUnavailableException, InvalidMidiDataException {
    // Added: a few extra initialization operations
    Objects.requireNonNull(model);
    this.model = model;
    this.sequencer = MidiSystem.getSequencer();
    this.sequence = new Sequence(Sequence.PPQ, 1);
    this.sequence.createTrack();
    this.sequencer.open();
    this.loadNotesIntoSequence();
    this.sequencer.setSequence(this.sequence);
    this.startingBeat = 0;

    //Added: to allow for timing of advancing playback.
    ActionListener looper = actionEvent -> {
      if (!this.isPlaying()) {
        this.timer.stop();
      } else {
        moveRight();
      }
    };

    this.timer = new Timer(this.model.getTempo() / 1000, looper);
  }

  /**
   * Convenience constructor used for testing this class with a given Sequencer. To be used with
   * a MockSequencer so that outputs can be tested.
   *
   * @param model  the MusicModel which will be read to get the notes to be played.
   * @param device the Sequencer which will be receiving and playing all the notes of the Model.
   * @throws MidiUnavailableException thrown when MIDI is unavailable
   * @throws InvalidMidiDataException thrown when MIDI data is invalid
   */
  public MidiViewImpl(IMusicModel model, Sequencer device)
          throws MidiUnavailableException, InvalidMidiDataException {
    Objects.requireNonNull(model);
    Objects.requireNonNull(device);
    this.model = model;
    this.sequencer = device;
    this.sequence = new Sequence(Sequence.PPQ, 1);
    this.sequence.createTrack();
    this.sequencer.open();
    this.loadNotesIntoSequence();
    this.sequencer.setSequence(this.sequence);
    this.startingBeat = 0;

    //Added: to allow for timing of advancing playback.
    ActionListener looper = actionEvent -> {
      if (!this.isPlaying()) {
        this.timer.stop();
      } else {
        moveRight();
      }
    };

    this.timer = new Timer(this.model.getTempo() / 1000, looper);
  }

  /**
   * Retrieve the notes from the model and load them all into a Sequence so that the Sequencer can
   * use them.
   *
   * @throws InvalidMidiDataException if any of the MIDI info is invalid
   */
  private void loadNotesIntoSequence() throws InvalidMidiDataException {
    HashMap<Integer, List<Sound>> notes = this.model.getBeats(0, this.model.getEndBeat());

    Track t = this.sequence.getTracks()[0];

    for (int i = 1; i <= 10; i++) {
      t.add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE, i - 1, i, 0), 0));
    }

    for (int i = 0; i < this.model.getEndBeat(); i++) {
      if (notes.containsKey(i)) {
        for (Sound s : notes.get(i)) {
          if (i == s.getStartBeat()) {
            t.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON, s.getInstrument() - 1,
                    s.getNote().hashCode(), s.getVolume()), s.getStartBeat()));
            t.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_OFF, s.getInstrument() - 1,
                    s.getNote().hashCode(), s.getVolume()),
                    s.getStartBeat() + s.getDuration() - 1));
          }
        }
      }
    }

  }

  @Override
  public void togglePlay() {
    if (!this.isPlaying()) {
      this.sequencer.setTickPosition(this.startingBeat);
      this.sequencer.start();
      this.sequencer.setTempoInMPQ(this.model.getTempo());

      this.timer.start();
    } else {
      this.startingBeat = (int) this.sequencer.getTickPosition();
      this.timer.stop();
      this.sequencer.stop();
    }
  }

  @Override
  public void initialize() {
    this.togglePlay();
  }

  @Override
  public void moveLeft() {
    if (this.startingBeat > 0) {
      this.startingBeat--;
    }
  }

  @Override
  public void moveRight() {
    if (this.startingBeat < this.model.getEndBeat()) {
      this.startingBeat++;
      this.timer.setDelay(this.model.getTempo() / 1000);
      this.sequencer.setTempoInMPQ(this.model.getTempo());
    }
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    //Does nothing
  }

  @Override
  public boolean isPlaying() {
    return this.sequencer.isRunning();
  }

  @Override
  public void setBeat(int beat) {
    if (beat >= 0 && beat <= this.model.getEndBeat()) {
      this.startingBeat = beat;
    }
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
    return this.startingBeat;
  }

  @Override
  public void reset() {
    Track t = this.sequence.getTracks()[0];

    for (int i = 0; i < t.size(); i++) {
      t.remove(t.get(i));
    }
    try {
      this.loadNotesIntoSequence();
    } catch (InvalidMidiDataException e) {
      //Does nothing
    }
  }
}
