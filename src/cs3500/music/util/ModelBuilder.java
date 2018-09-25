package cs3500.music.util;

import cs3500.music.model.MusicModel;

/**
 * Class implemented for building our specific MusicModel by adding notes.
 */
public class ModelBuilder implements CompositionBuilder<MusicModel> {

  private MusicModel model;

  /**
   * Constructs a new ModelBuilder with an empty MusicModel.
   */
  public ModelBuilder() {
    this.model = new MusicModel();
  }

  @Override
  public MusicModel build() {
    return this.model;
  }

  @Override
  public CompositionBuilder<MusicModel> setTempo(int tempo) {
    this.model.setTempo(tempo);
    return this;
  }

  @Override
  public CompositionBuilder<MusicModel> addNote(int start, int end, int instrument,
                                                int pitch, int volume) {
    this.model.addNote(start, end, instrument, pitch, volume);
    return this;
  }
}
