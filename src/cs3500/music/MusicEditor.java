package cs3500.music;

import cs3500.music.controller.IMusicController;
import cs3500.music.controller.MusicController;
import cs3500.music.model.IMusicModel;
import cs3500.music.util.ModelBuilder;
import cs3500.music.util.MusicReader;
import cs3500.music.view.IMusicView;
import cs3500.music.view.ViewFactory;

import java.io.FileReader;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 * Main method for running the View classes with particular arguments.
 */
public class MusicEditor {

  /**
   * Main method for the entire project.
   *
   * @param args   The array of arguments to give. The first should be the file name, second should
   *               be the view type ("console", "visual", "midi", "composite")
   * @throws IOException   If the requested file cannot be found
   * @throws InvalidMidiDataException   if MIDI data is invalid
   * @throws MidiUnavailableException   if MIDI is unavailable
   */
  public static void main(String[] args)
          throws IOException, InvalidMidiDataException, MidiUnavailableException {
    ModelBuilder builder = new ModelBuilder();
    FileReader fr = new FileReader(args[0]);
    ViewFactory factory = new ViewFactory(MusicReader.parseFile(fr, builder));

    IMusicModel model = builder.build();

    IMusicView view = factory.getView(args[1]);

    IMusicController ctrl = new MusicController(model, view);

    ctrl.display();
  }
  // Changed: allows for using the controller now.
}
