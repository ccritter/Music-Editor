package cs3500.music.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Class that represents a model of the Music interface, which should allow us to add Sounds,
 * Edit Sounds, Remove Sounds and display the sounds.
 */
public class MusicModel implements IMusicModel<HashMap<Integer, ArrayList<Sound>>, Note> {
  private HashMap<Integer, ArrayList<Sound>> beatMap;
  private int endBeat;
  private TreeMap<Note, Sound> treeMinMax;
  private int tempo;

  /**
   * Constructs a new empty music model.
   */
  public MusicModel() {
    this.beatMap = new HashMap<Integer, ArrayList<Sound>>();
    this.endBeat = 0;
    this.treeMinMax = new TreeMap<Note, Sound>();
  }

  //Changed: Changed parameters to allow parsing of txt files
  @Override
  public void addNote(int start, int end, int instrument, int intPitch, int volume) {
    this.checkValidSound(start, end, instrument, intPitch, volume);
    PitchType pitch = this.parsePitch(intPitch);
    OctaveType octave = this.parseOctave(intPitch);
    Note noteToBeAdded = new Note(pitch, octave);
    Sound soundToBeAdded = new Sound(noteToBeAdded, start, end - start + 1, instrument,
            volume);
    int beats = (end - start);
    for (int i = 0; i < beats; i += 1) {
      int curIdx = i + start;
      if ((curIdx) > this.endBeat) {
        this.endBeat = curIdx;
      }
      if (!this.beatMap.containsKey(curIdx)) {
        this.beatMap.put(curIdx, new ArrayList<Sound>());
      }

      this.beatMap.get(curIdx).add(soundToBeAdded);
    }

    this.treeMinMax.put(noteToBeAdded, soundToBeAdded);
  }

  //Changed: Changed parameters to allow parsing of txt files
  @Override
  public void removeNote(int start, int end, int instrument, int intPitch, int volume) {
    this.checkValidSound(start, end, instrument, intPitch, volume);
    PitchType pitch = this.parsePitch(intPitch);
    OctaveType octave = this.parseOctave(intPitch);
    Note remNote = new Note(pitch, octave);
    Sound remSound = new Sound(remNote, start, end - start + 1, instrument,
            volume);

    if (!this.beatMap.get(start).contains(remSound)) {
      throw new IllegalArgumentException("Sound not present in track.");
    }

    for (int i = start; i < end; i += 1) {
      this.beatMap.get(i).remove(remSound);
    }

    this.endBeat = Collections.max(this.beatMap.keySet());

    //Added condition to remove if last and only last note in beatMap
    if (!this.containsOtherNote(remNote)) {
      this.treeMinMax.remove(remNote);
    }
  }

  /**
   * Checks if the note is not the last note in the entire beatMap.
   *
   * @param remNote the note to be removed
   * @return whether last note or not
   */
  private boolean containsOtherNote(Note remNote) {
    for (int i = 0; i <= this.endBeat; i += 1) {
      if (this.beatMap.containsKey(i)) {
        List<Sound> tempSounds = this.beatMap.get(i);
        for (Sound tempSound : tempSounds) {
          if (tempSound.getNote().equals(remNote)) {
            return true;
          }
        }
      }
    }
    return false;
  }
  // Added: to allow for better integration of both of our models (for TreeMap)

  @Override
  public void editNote(int startSource, int endSource, int instrumentSource, int pitchSource, int
          volumeSource, int startDest, int endDest, int instrumentDest, int pitchDest, int
                               volumeDest) {
    this.checkValidSound(startSource, endSource, instrumentSource, pitchSource, volumeSource);
    this.checkValidSound(startDest, endDest, instrumentDest, pitchDest, volumeDest);
    this.removeNote(startSource, endSource, instrumentSource, pitchSource, volumeSource);
    this.addNote(startDest, endDest, instrumentDest, pitchDest, volumeDest);
  }

  @Override
  public IMusicModel combineMusic(CombineType combineType, IMusicModel that) {
    int thatLength = that.getEndBeat() + 1;
    switch (combineType) {
      //One after the other
      case CONSECUTIVE:
        for (int i = 0; i <= this.endBeat; i += 1) {
          List<Sound> tempSound = this.beatMap.get(i);
          for (Sound temp : tempSound) {
            that.addNote(temp.getStartBeat() + thatLength,
                    temp.getStartBeat() + temp.getDuration() + thatLength,
                    temp.getInstrument(), this.convertNoteToMIDIInt(temp.getNote().getPitch(),
                            temp.getNote().getOctave()), temp.getVolume());
          }
        }
        break;
      //Both together
      case SIMULTANEOUS:
        for (int i = 0; i <= this.endBeat; i += 1) {
          List<Sound> tempSound = this.beatMap.get(i);
          for (Sound temp : tempSound) {
            that.addNote(temp.getStartBeat(), temp.getStartBeat() + temp.getDuration(),
                    temp.getInstrument(), this.convertNoteToMIDIInt(temp.getNote().getPitch(), temp
                            .getNote().getOctave()), temp.getVolume());
          }
        }
        break;
      default:
        throw new IllegalArgumentException("Not a valid CombineType.");
    }
    return that;
  }

  /**
   * Returns an integer representation of a note (0 to 127) based on PitchType and OctaveType.
   *
   * @param p the note's pitch
   * @param o the note's octave
   * @return an integer representation of the given note data from 0 to 127
   */
  private int convertNoteToMIDIInt(PitchType p, OctaveType o) {
    this.validPitch(p);
    this.validOctave(o);
    Note tempNote = new Note(p, o);
    return tempNote.hashCode();
  }
  // Added for conversion of our interpretation of note info into abstracted integer data.

  @Override
  public String display() {
    if (this.beatMap.isEmpty()) {
      return "";
    }
    StringBuilder out = new StringBuilder();
    List<Note> notesToDisplay = getNotesToDisplay();

    //Column length for beats
    int beatDigits = String.valueOf(this.endBeat).length();
    out.append(this.getSpacesAsString(beatDigits));

    //Note Row
    for (Note tempNote : notesToDisplay) {
      out.append(tempNote.toString());
    }
    out.append("\n");

    //Each beat
    for (int i = 0; i <= this.endBeat; i += 1) {
      out.append(this.getBeatDisplay(i, beatDigits));
      if (this.beatMap.containsKey(i)) {
        List<Sound> retrieve = this.beatMap.get(i);

        for (Note tempNote : notesToDisplay) {
          if (this.containsNote(retrieve, tempNote)) {
            Sound curSound = this.getSound(retrieve, tempNote);

            if (curSound == null) {
              throw new IllegalArgumentException("Note is not in HashMap.");
            }

            if (curSound.getStartBeat() == i) {
              out.append("  X  ");
            } else {
              out.append("  |  ");
            }
          } else {
            out.append("     ");
          }
        }
        out.append("\n");
      } else {
        out.append(this.getSpacesAsString(notesToDisplay.size() * 5));
        out.append("\n");
      }
    }

    return out.toString();
  }

  /**
   * Checks if a note is present in a list of sounds.
   *
   * @param sounds      the list of sounds to be checked from
   * @param noteToCheck the note to be checked
   * @return whether the note was found in the list of sounds or not
   */
  private boolean containsNote(List<Sound> sounds, Note noteToCheck) {
    for (Sound tempSounds : sounds) {
      if (tempSounds.getNote().equals(noteToCheck)) {
        return true;
      }
    }
    return false;
  }

  //Added : Implemented functions using getters instead of protected fields.

  /**
   * Returns the sound found with the particular note being looked for.
   *
   * @param sounds      the list of sounds to be checked from
   * @param noteToCheck the note to be looked for
   * @return the sound which consists of the note being looked for
   */
  private Sound getSound(List<Sound> sounds, Note noteToCheck) {
    for (Sound tempSounds : sounds) {
      if (tempSounds.getNote().equals(noteToCheck)) {
        return tempSounds;
      }
    }
    return null;
  }

  /**
   * Returns a String consisting entirely of spaces whose length equals the parameter entered
   * by the user.
   *
   * @param numOfSpaces The number of spaces required in the string
   * @return the string of spaces with the number of spaces being the parameter entered
   */
  private String getSpacesAsString(int numOfSpaces) {
    String temp = "";
    for (int i = 0; i < numOfSpaces; i += 1) {
      temp = temp + " ";
    }
    return temp;
  }

  /**
   * Returns an indented String of spaces with the beat number in correct format.
   *
   * @param beat   the beat number to be converted to a string
   * @param digits the maximum number of digits in the music track
   * @return the string value of the correctly indented beat number
   */
  private String getBeatDisplay(int beat, int digits) {
    int beatDigit = String.valueOf(beat).length();
    int leadingSpaces = digits - beatDigit;
    String temp = this.getSpacesAsString(leadingSpaces);
    temp = temp + String.valueOf(beat);
    return temp;
  }

  /**
   * Returns a list of Notes that starts with the lowest note in the sound and ends with the highest
   * note int the sound.
   *
   * @return List of notes present in the entire sound
   */
  private List<Note> getNotesToDisplay() {
    if (this.beatMap.isEmpty()) {
      return new ArrayList<Note>();
    }
    List<Note> notesToDisplay;
    ArrayList<Note> allNotes = new ArrayList<Note>();
    for (OctaveType tempOctave : OctaveType.values()) {
      for (PitchType tempPitch : PitchType.values()) {
        Note tempNote = new Note(tempPitch, tempOctave);
        allNotes.add(tempNote);
      }
    }
    int startIdx = allNotes.indexOf(this.treeMinMax.firstKey());
    int endIdx = allNotes.indexOf(this.treeMinMax.lastKey());
    notesToDisplay = allNotes.subList(startIdx, endIdx + 1);
    Collections.reverse(notesToDisplay);
    return notesToDisplay;
  }

  /**
   * Getter method for the list of notes to be displayed.
   *
   * @return a copy of the list of notes to be displayed
   */
  @Override
  public List<Note> notesToDisplay() {
    List<Note> temp = new ArrayList<Note>();
    temp.addAll(this.getNotesToDisplay());
    return temp;
  }

  @Override
  public int getEndBeat() {
    int end = this.endBeat + 1;
    return end;
  }

  /**
   * Checks the values of all parameters.
   *
   * @param start      The start time of the note, in beats
   * @param end        The end time of the note, in beats
   * @param instrument The instrument number (to be interpreted by MIDI)
   * @param pitch      The pitch (in the range [0, 127], where 60 represents C4, the middle-C on a
   *                   piano)
   * @param volume     The volume (in the range [0, 127])
   */
  private void checkValidSound(int start, int end, int instrument, int pitch, int volume)
          throws IllegalArgumentException {
    if (start < 0) {
      throw new IllegalArgumentException("Start beat cannot be negative.");
    }

    if (end < 0) {
      throw new IllegalArgumentException("End beat cannot be negative.");
    }

    if (end < start) {
      throw new IllegalArgumentException("End cannot be before start.");
    }

    if (pitch < 0 || pitch > 127) {
      throw new IllegalArgumentException("Pitch value cannot be outside range [0, 127].");
    }

    if (volume < 0 || volume > 127) {
      throw new IllegalArgumentException("Volume value cannot be outside range [0, 127].");
    }
  }

  /**
   * Checks if the PitchType is a valid PitchType and throws an InvalidArgumentException if it is
   * not.
   *
   * @param pitch The PitchType to be checked
   * @throws IllegalArgumentException if the PitchType is not Valid
   */
  private void validPitch(PitchType pitch) throws IllegalArgumentException {
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
        return;
      default:
        throw new IllegalArgumentException("Not a valid PitchType.");
    }
  }

  /**
   * Checks if the OctaveType entered is a valid OctaveType and throws an
   * InvalidArgumentException if it is not valid.
   *
   * @param octave The OctaveType to be checked
   */
  private void validOctave(OctaveType octave) {
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
        return;
      default:
        throw new IllegalArgumentException("Not a valid OctaveType.");
    }
  }

  @Override
  public HashMap<Integer, ArrayList<Sound>> getBeats(int start, int end) {
    if (end < start) {
      throw new IllegalArgumentException("Start value must be lesser than or equal to end beat.");
    }
    HashMap<Integer, ArrayList<Sound>> tempBeats = new HashMap<>();
    for (int i = start; i <= end; i += 1) {
      if (this.beatMap.containsKey(i)) {
        tempBeats.put(i, this.beatMap.get(i));
      }
    }
    return tempBeats;
  }

  /**
   * Parses an integer to an OctaveType.
   *
   * @param pitch the pitch number read by the txt files.
   * @return the OctaveType of the note
   * @throws IllegalArgumentException if the given pitch is not valid (not 0 to 127)
   */
  //Added : to read from the text files.
  private OctaveType parseOctave(int pitch) {
    if (pitch < 0 || pitch > 127) {
      throw new IllegalArgumentException("Unable to parse, pitch given out of range.");
    }
    pitch = pitch / 12;
    return OctaveType.values()[pitch];
  }

  /**
   * Parses an integer to an PitchType.
   *
   * @param pitch the pitch number read by the txt files.
   * @return the PitchType of the note
   * @throws IllegalArgumentException if the given pitch is not valid (not 0 to 127)
   */
  //Added : to read from the text files.
  private PitchType parsePitch(int pitch) {
    if (pitch < 0 || pitch > 127) {
      throw new IllegalArgumentException("Unable to parse, pitch given out of range.");
    }
    return PitchType.values()[pitch % 12];
  }

  //Added: to read sound from text files
  @Override
  public int getTempo() {
    return tempo;
  }

  //Added: to play sound from midi
  @Override
  public void setTempo(int tempo) {
    if (tempo < 0) {
      throw new IllegalArgumentException("Tempo cannot be negative.");
    }
    this.tempo = tempo;
  }
}
