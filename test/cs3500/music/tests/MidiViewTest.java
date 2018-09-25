package cs3500.music.tests;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import cs3500.music.model.MusicModel;
import cs3500.music.util.ModelBuilder;
import cs3500.music.util.MusicReader;
import cs3500.music.view.MidiViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * Test class for testing the MidiViewImpl class methods/operations.
 */
public class MidiViewTest {

  // This is the string that should be outputted before every song. It consists of some
  // initialization logs and then all of the channels getting set to their respective instruments.
  String initString = "Sequencer opened.\n" +
          "Setting sequence...\n" +
          "Setting tick position to 0...\n" +
          "Playing...\n" +
          "Changing instrument for channel 0 to 1.\n" +
          "Changing instrument for channel 1 to 2.\n" +
          "Changing instrument for channel 2 to 3.\n" +
          "Changing instrument for channel 3 to 4.\n" +
          "Changing instrument for channel 4 to 5.\n" +
          "Changing instrument for channel 5 to 6.\n" +
          "Changing instrument for channel 6 to 7.\n" +
          "Changing instrument for channel 7 to 8.\n" +
          "Changing instrument for channel 8 to 9.\n" +
          "Changing instrument for channel 9 to 10.\n";

  interface Adder {
    void add(ModelBuilder in);
  }

  static Adder add(int start, int end, int instrument, int pitch, int volume) {
    return (input) -> {
      input.addNote(start, end, instrument, pitch, volume);
    };
  }

  private String testRun(int tempo, Adder... additions) {
    ModelBuilder builder = new ModelBuilder();
    builder.setTempo(tempo);
    for (Adder a : additions) {
      a.add(builder);
    }
    MusicModel model = builder.build();
    Sequencer fakeSeq = new MockSequencer();
    try {
      MidiViewImpl view = new MidiViewImpl(model, fakeSeq);
      view.initialize();
      return fakeSeq.toString();
    } catch (MidiUnavailableException | InvalidMidiDataException e) {
      return null; // Should never be returned.
    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTempo() {
    testRun(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStartBeat() {
    testRun(100, add(-1, 2, 1, 60, 64));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEndBeat() {
    testRun(100, add(0, -1, 1, 60, 64));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEndBeforeStart() {
    testRun(100, add(4, 2, 1, 60, 64));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidPitchUnder() {
    testRun(100, add(0, 2, 1, -1, 64));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidPitchOver() {
    testRun(100, add(0, 2, 1, 200, 64));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidVolumeUnder() {
    testRun(100, add(0, 2, 1, 60, -1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidVolumeOver() {
    testRun(100, add(0, 2, 1, 60, 200));
  }

  @Test
  public void testEmptyModel() {
    // All that should happen here is instruments getting set to specific channels.
    assertEquals(initString + "Tempo set to 100.0\n", testRun(100));
  }

  @Test
  public void testAddNoteWith0Length() {
    assertEquals(initString + "Tempo set to 100.0\n",
            testRun(100, add(0, 0, 1, 60, 64)));
  }

  @Test
  public void testAddNote() {
    assertEquals(initString
                    + "Sending NOTE_ON for note 60 of volume 64 on channel 0 and tick 0\n"
                    + "Sending NOTE_OFF for note 60 of volume 64 on channel 0 and tick 2\n"
                    + "Tempo set to 100.0\n",
            testRun(100,
                    add(0, 2, 1, 60, 64)));
  }

  @Test
  public void testAddSameNoteDifferentInstrument() {
    assertEquals(initString
                    + "Sending NOTE_ON for note 60 of volume 64 on channel 0 and tick 0\n"
                    + "Sending NOTE_ON for note 60 of volume 64 on channel 1 and tick 0\n"
                    + "Sending NOTE_OFF for note 60 of volume 64 on channel 0 and tick 2\n"
                    + "Sending NOTE_OFF for note 60 of volume 64 on channel 1 and tick 2\n"
                    + "Tempo set to 101.0\n",
            testRun(101,
                    add(0, 2, 1, 60, 64),
                    add(0, 2, 2, 60, 64)));
  }

  @Test
  public void testConsecutiveNotes() {
    assertEquals(initString
                    + "Sending NOTE_ON for note 60 of volume 64 on channel 0 and tick 0\n"
                    + "Sending NOTE_OFF for note 60 of volume 64 on channel 0 and tick 2\n"
                    + "Sending NOTE_ON for note 60 of volume 64 on channel 1 and tick 2\n"
                    + "Sending NOTE_OFF for note 60 of volume 64 on channel 1 and tick 4\n"
                    + "Tempo set to 1000.0\n",
            testRun(1000,
                    add(0, 2, 1, 60, 64),
                    add(2, 4, 2, 60, 64)));
  }

  @Test
  public void testNoteTiming() {
    assertEquals(initString
                    + "Sending NOTE_ON for note 60 of volume 64 on channel 0 and tick 0\n"
                    + "Sending NOTE_ON for note 60 of volume 64 on channel 1 and tick 1\n"
                    + "Sending NOTE_OFF for note 60 of volume 64 on channel 0 and tick 2\n"
                    + "Sending NOTE_ON for note 60 of volume 64 on channel 2 and tick 2\n"
                    + "Sending NOTE_OFF for note 60 of volume 64 on channel 1 and tick 3\n"
                    + "Sending NOTE_OFF for note 60 of volume 64 on channel 2 and tick 4\n"
                    + "Tempo set to 100.0\n",
            testRun(100,
                    add(0, 2, 1, 60, 64),
                    add(1, 3, 2, 60, 64),
                    add(2, 4, 3, 60, 64)));
  }

  @Test
  public void testMaryHasCorrectNumberOfNotes()
          throws FileNotFoundException, InvalidMidiDataException, MidiUnavailableException {
    ModelBuilder builder = new ModelBuilder();
    FileReader fr = new FileReader("mary-little-lamb.txt");
    Sequencer fakeSeq = new MockSequencer();
    MidiViewImpl tv = new MidiViewImpl(MusicReader.parseFile(fr, builder), fakeSeq);
    tv.initialize();
    Scanner scanner = new Scanner(fakeSeq.toString());
    int noteOnCalls = 0;
    int noteOffCalls = 0;
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.contains("NOTE_ON")) {
        noteOnCalls++;
      } else if (line.contains("NOTE_OFF")) {
        noteOffCalls++;
      }
    }
    scanner.close();

    assertEquals(34 * 2, noteOnCalls + noteOffCalls);

  }

  // Removed a test that checked for timing/wait logic, since we now use a Timer.
  // Added a few more tests for some new functionality
  @Test
  public void testTogglePlay()
          throws FileNotFoundException, InvalidMidiDataException, MidiUnavailableException {
    Sequencer fakeSeq = new MockSequencer();
    MidiViewImpl tv = new MidiViewImpl(new MusicModel(), fakeSeq);
    tv.togglePlay();
    tv.togglePlay();
    assertEquals(initString + "Tempo set to 0.0\n" +
            "Pausing...\n", fakeSeq.toString());
  }
}
