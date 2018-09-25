package cs3500.music.model;

import java.util.Objects;

/**
 * Represents a Sound piece which consists of a Note, startBeat, beatLength, instrument number
 * for MIDI and volume of the sound.
 */
public class Sound {
  private Note note;
  private int startBeat;
  private int beatLength;

  //Added: These fields are new, added for functionality with View. To be interpreted by MIDI
  private int instrument;
  private int volume;

  /**
   * Constructs a Sound piece.
   *
   * @param note       The note of the current sound piece
   * @param startBeat  the start beat location of the current sound piece (inclusive)
   * @param beatLength the end beat of the location of the current sound piece (exclusive)
   * @param instrument the instrument number of the sound piece to be interpreted by midi
   * @param volume     the volume of the current sound piece to be interpreted by midi
   */
  Sound(Note note, int startBeat, int beatLength, int instrument, int volume) {
    Objects.requireNonNull(note);
    this.validStartBeat(startBeat);
    this.validBeatLength(beatLength);
    this.validMIDIValue(instrument);
    this.validMIDIValue(volume);
    this.note = note;
    this.startBeat = startBeat;
    this.beatLength = beatLength;
    this.instrument = instrument;
    this.volume = volume;
    // Added checks and assignments for the new instrument and volume fields.
  }

  /**
   * Checks if the beat entered in a sound is greater than or equal to zero. If it is less
   * than zero it throws an IllegalArgumentException.
   *
   * @param startBeat the beat where the sound starts
   * @throws IllegalArgumentException when the starting beat is less than or equal to zero
   */
  private void validStartBeat(int startBeat) throws IllegalArgumentException {
    if (startBeat < 0) {
      throw new IllegalArgumentException("Start beat cannot be negative.");
    }
  }

  /**
   * Checks if the beat length of the sound is valid or not. The beat length cannot be
   * zero since that would be equivalent to no sound.
   *
   * @param beatLength The beat length of the sound
   * @throws IllegalArgumentException when the beat length is lesser than zero
   */
  private void validBeatLength(int beatLength) throws IllegalArgumentException {
    if (beatLength <= 0) {
      throw new IllegalArgumentException("Beat length must be greater than 0.");
    }
  }

  /**
   * Checks if the parameter value is within [0, 127].
   *
   * @param num the value to be checked
   */
  private void validMIDIValue(int num) {
    if (num < 0 || num > 127) {
      throw new IllegalArgumentException("MIDI value outside of range (0 to 127 inclusive).");
    }
  }
  // Method added since last assignment to work with the new MIDI fields.

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Sound)) {
      return false;
    }
    Sound that = (Sound) other;
    return this.note.equals(that.note) && this.beatLength == that.beatLength
            && this.startBeat == that.startBeat
            && this.instrument == that.instrument
            && this.volume == that.volume;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.note, this.beatLength, this.startBeat,
            this.instrument, this.volume);
  }

  /**
   * Getter method for the note field of the sound piece.
   *
   * @return a copy of the note of the current sound piece
   */
  public Note getNote() {
    return new Note(this.note);
  }

  /**
   * Getter method for the duration field of the sound.
   *
   * @return this note's duration.
   */
  public int getDuration() {
    return beatLength;
  }

  /**
   * Getter method for the volume field of the sound.
   *
   * @return this note's volume.
   */
  public int getVolume() {
    return volume;
  }

  /**
   * Getter method for the duration field of the sound.
   *
   * @return this note's duration.
   */
  public int getStartBeat() {
    return startBeat;
  }

  /**
   * Getter method for the instrument field of the sound.
   *
   * @return this note's instrument.
   */
  public int getInstrument() {
    return instrument;
  }
  //Added: two new getters for the instrument and volume fields that were added.
}