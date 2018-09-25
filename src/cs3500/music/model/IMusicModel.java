package cs3500.music.model;

import java.util.List;

/**
 * An interface for a model of the music editor, which should allow us to interact with the
 * music by allowing us to add notes, remove notes, edit notes and combine sounds.
 *
 * @param <S> type of data/note storage method
 * @param <N> type of Note representation
 */
public interface IMusicModel<S, N> {
  // Changed: we now have a parameter for the way notes are stored and the Note representation.

  /**
   * Adds a new note to the piece.
   *
   * @param start      The start time of the note, in beats
   * @param end        The end time of the note, in beats
   * @param instrument The instrument number (to be interpreted by MIDI)
   * @param pitch      The pitch (in the range [0, 127], where 60 represents C4, the middle-C on a
   *                   piano)
   * @param volume     The volume (in the range [0, 127])
   */
  void addNote(int start, int end, int instrument, int pitch, int volume);
  // Changed: readjusted method arguments to allow for instruments and volume. Also changed types
  // of start, end, and pitch to hide information as well as work with MIDI better.

  /**
   * Removes a note from the piece.
   *
   * @param start      The start time of the note, in beats
   * @param end        The end time of the note, in beats
   * @param instrument The instrument number (to be interpreted by MIDI)
   * @param pitch      The pitch (in the range [0, 127], where 60 represents C4, the middle-C on a
   *                   piano)
   * @param volume     The volume (in the range [0, 127])
   */
  void removeNote(int start, int end, int instrument, int pitch, int volume);
  // Changed: readjusted method arguments for information hiding as well as new requirements for
  // identifying a Sound (instrument, volume).

  /**
   * It edits the note entered by the user by looking up the current note int the music,
   * removing it, and adding a new note with the specified parameters of the edited note.
   * If a note is edited and tried to place over another note, it overwrites the previous note.
   *
   * @param startSource      The start time of the source note, in beats
   * @param endSource        The end time of the source note, in beats
   * @param instrumentSource The Source instrument number (to be interpreted by MIDI)
   * @param pitchSource      The source pitch (in the range [0, 127], where 60 represents C4, the
   *                         middle-C on a piano)
   * @param volumeSource     The source volume (in the range [0, 127])
   * @param startDest        The start time of the destination note, in beats
   * @param endDest          The end time of the destination note, in beats
   * @param instrumentDest   The destination instrument number (to be interpreted by MIDI)
   * @param pitchDest        The destination pitch (in the range [0, 127], where 60 represents C4,
   *                         the middle-C on a piano)
   * @param volumeDest       The destination volume (in the range [0, 127])
   */
  void editNote(int startSource, int endSource, int instrumentSource, int pitchSource, int
          volumeSource, int startDest, int endDest, int instrumentDest, int pitchDest, int
                        volumeDest);
  // Changed: all parameters were adjusted to ints for information hiding, and instrument and
  // volume were added.

  /**
   * It combines "this" music with the music entered as a parameter, according to the CombineType.
   * If the CombineType is SIMULTANEOUS then the music entered as a parameter is superimposed over
   * the current music overwriting the previous musics notes in common to each other.
   * If the CombineType is CONSECUTIVE, it adds the music entered as a parameter at the end of this
   * music.
   *
   * @param combineType The CombineType of the combine we want to do : SIMULTANEOUS OR CONSECUTIVE
   * @param that        The music to be combined to the current music
   */
  IMusicModel combineMusic(CombineType combineType, IMusicModel that);

  /**
   * It displays the current music track in a visual representation as a String, within the range
   * of the lowest note in the music track and the highest note in the music track with the beat
   * starting from 0.
   */
  String display();

  /**
   * Returns the last beat number of the current music.
   *
   * @return the length of the current sound track
   */
  int getEndBeat();

  /**
   * Return the data structure which stores all sounds for each beat in it.
   *
   * @param start start beat
   * @param end   end beat
   * @return data structure storing beats and sounds
   */
  S getBeats(int start, int end);
  // Added: we needed a way to access the data of the model, so we added this method for this

  /**
   * Returns the list of notes present in the music composition.
   *
   * @return List of Notes present in the music composition
   */
  List<N> notesToDisplay();
  // Added: we needed a way to display the list of notes in the view

  /**
   * Getter method for this Model's tempo. If not set, it will return 0.
   *
   * @return this model's tempo
   */
  int getTempo();
  // Added to allow models to store tempos.

  /**
   * Setter method for this model's tempo. Tempo should not be negative.
   *
   * @param tempo the tempo to set this model to.
   * @throws IllegalArgumentException if the given tempo is negative.
   */
  void setTempo(int tempo);
  // Added to allow for tempo setting.
}
