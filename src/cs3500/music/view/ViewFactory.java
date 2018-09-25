package cs3500.music.view;

import java.util.Objects;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import cs3500.music.model.IMusicModel;

/**
 * Factory class for creating MusicModels. It will give you a view based on a model and a string
 * type of view.
 */
public class ViewFactory {

  private final IMusicModel model;

  /**
   * Constructs a new ViewFactory using the given model.
   *
   * @param model the model to base the generated Views from
   */
  public ViewFactory(IMusicModel model) {
    Objects.requireNonNull(model);
    this.model = model;
  }

  /**
   * Gets a specific kind of view based on a String representing the type of View to construct.
   * "console" will return a TextualView, "visual" will return a GuiViewFrame, and "midi" will
   * return a MidiViewImpl.
   *
   * @param type which type of View to retrieve. Must be "console", "visual", "midi", or "composite"
   * @return the view based on this class's model
   * @throws IllegalArgumentException if the given String doesn't match any of the types above
   */
  public IMusicView getView(String type) throws InvalidMidiDataException, MidiUnavailableException {
    switch (type.toLowerCase()) {
      case "console":
        return new TextualView(this.model, System.out);
      case "visual":
        return new GuiViewFrame(this.model);
      case "midi":
        return new MidiViewImpl(this.model);
      case "composite":
        // Added: now allows for the composite view.
        return new MidiGuiView(new MidiViewImpl(this.model), new GuiViewFrame(this.model));
      default:
        throw new IllegalArgumentException("Invalid view type.");
    }
  }
}
