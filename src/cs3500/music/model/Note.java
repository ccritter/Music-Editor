package cs3500.music.model;

import java.util.Objects;

/**
 * Represents one particular Note. It consists of a pitch and an Octave(0-10).
 */
public final class Note implements Comparable<Note> {
  private PitchType pitch;
  private OctaveType octave;

  /**
   * Constructs an object of the note. Checks for valid arguments and throws
   * IllegalArgumentExceptions if they are invalid.
   *
   * @param pitch  the pitch of the note
   * @param octave the octave of the note
   */
  public Note(PitchType pitch, OctaveType octave) throws IllegalArgumentException {
    this.validPitch(pitch);
    this.validOctave(octave);
    this.pitch = pitch;
    this.octave = octave;
  }

  /**
   * Copy constructor for constructing an identical Note.
   *
   * @param other the Note to copy.
   */
  public Note(Note other) {
    this.pitch = other.pitch;
    this.octave = other.octave;
  }

  /**
   * Checks whether the pitch entered by the user is a valid PitchType and throws an
   * IllegalArgumentException if it is invalid.
   *
   * @param pitch The pitch of the note
   */
  private void validPitch(PitchType pitch) {
    Objects.requireNonNull(pitch);
    switch (pitch) {
      case C:
      case C_SHARP:
      case D:
      case D_SHARP:
      case E:
      case F:
      case F_SHARP:
      case G:
      case G_SHARP:
      case A:
      case A_SHARP:
      case B:
        break;
      default:
        throw new IllegalArgumentException("PitchType invalid.");
    }
  }

  /**
   * Checks whether the octave entered by the user is a valid OctaveType and throws an
   * IllegalArgumentException if it is invalid.
   *
   * @param octave The octave of the note
   */
  //Added : Case zero after adding it to the enumeration.
  private void validOctave(OctaveType octave) {
    Objects.requireNonNull(octave);
    switch (octave) {
      case ZERO:
      case ONE:
      case TWO:
      case THREE:
      case FOUR:
      case FIVE:
      case SIX:
      case SEVEN:
      case EIGHT:
      case NINE:
      case TEN:
        break;
      default:
        throw new IllegalArgumentException("OctaveType invalid.");
    }
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Note)) {
      return false;
    }

    Note that = (Note) other;
    return this.pitch.equals(that.pitch) && this.octave.equals(that.octave);
  }

  @Override
  public int hashCode() {
    return (this.octave.ordinal() * 12) + this.pitch.ordinal();
  }
  // Changed hashcode to MIDI's interpretation of a note (0 to 127)

  @Override
  public int compareTo(Note other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot compare to Null.");
    }

    if (this.octave != other.octave) {
      return this.octave.ordinal() - other.octave.ordinal();
    } else if (this.pitch != other.pitch) {
      return this.pitch.ordinal() - other.pitch.ordinal();
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    String out = this.pitch.toString() + this.octave.toString();
    if (out.length() == 2) {
      out = "  " + out + " ";
    } else if (out.length() == 3) {
      out = " " + out + " ";
    } else {
      out = " " + out;
    }
    return out;
  }

  /**
   * Gets a copy the OctaveType of the current Note.
   *
   * @return copy of OctaveType of the note
   */
  public OctaveType getOctave() {
    OctaveType temp = this.octave;
    return temp;
  }

  /**
   * Gets a copy of the PitchType of the current Note.
   *
   * @return copy of the PitchType of the note
   */
  public PitchType getPitch() {
    PitchType temp = this.pitch;
    return temp;
  }
}
