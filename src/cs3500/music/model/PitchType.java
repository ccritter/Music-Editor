package cs3500.music.model;

/**
 * Enumeration representing a specified tone, from the 12 tone Western music system.
 * Keeps track of each tone's letter value as well as whether or not it is sharp. Unlike regular
 * musical notation with enharmonics, there are no flats, only sharped notes.
 */
public enum PitchType {
  C('C', false),
  C_SHARP('C', true),
  D('D', false),
  D_SHARP('D', true),
  E('E', false),
  F('F', false),
  F_SHARP('F', true),
  G('G', false),
  G_SHARP('G', true),
  A('A', false),
  A_SHARP('A', true),
  B('B', false);

  /**
   * Constructs a Tone, given a character value A-G and a sharp modifier (true or false).
   *
   * @param value the character value of the Tone (Must be between A and G alphabetically)
   * @param sharp whether or not the note is raised a half step (sharped)
   */
  PitchType(char value, boolean sharp) {
    this.value = value;
    this.sharp = sharp;
  }

  private char value;
  private boolean sharp;

  @Override
  public String toString() {
    if (this.sharp) {
      return this.value + "#";
    } else {
      return "" + this.value;
    }
  }

}
