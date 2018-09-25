package cs3500.music.model;

/**
 * Represents an Octave of a particular note. Humans hear typically 10 octaves.
 * We assume our octaves are greater than 0 and less than 11.<br>
 * An octave is one of:<br>
 * 1<br>
 * 2<br>
 * 3<br>
 * 4<br>
 * 5<br>
 * 6<br>
 * 7<br>
 * 8<br>
 * 9<br>
 * 10<br>
 */
public enum OctaveType {
  ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN;
  // Changed: added zero for better compatibility.

  @Override
  public String toString() {
    return String.valueOf(this.ordinal());
  }

  // Removed: method which got the integer value of the octave, which was redundant with ordinal().
}
