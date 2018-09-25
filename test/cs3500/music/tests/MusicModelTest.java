package cs3500.music.tests;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cs3500.music.model.CombineType;
import cs3500.music.model.IMusicModel;
import cs3500.music.model.MusicModel;
import cs3500.music.model.Note;
import cs3500.music.model.Sound;

import static junit.framework.TestCase.assertEquals;

/**
 * Class which tests the music model.
 */
public class MusicModelTest {

  //Test add note with start beat less than zero
  @Test(expected = IllegalArgumentException.class)
  public void testAddNote() {
    IMusicModel obj = new MusicModel();
    obj.addNote(-2, 3, 123, 60, 23);
  }

  //Test add note with end beat less than zero
  @Test(expected = IllegalArgumentException.class)
  public void testAddNote1() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, -2, 123, 60, 23);
  }

  //Test add note with end beat less than start beat
  @Test(expected = IllegalArgumentException.class)
  public void testAddNote2() {
    IMusicModel obj = new MusicModel();
    obj.addNote(3, 2, 123, 60, 23);
  }

  //Test add note with pitch number less than zero
  @Test(expected = IllegalArgumentException.class)
  public void testAddNote3() {
    IMusicModel obj = new MusicModel();
    obj.addNote(1, 2, 123, -2, 23);
  }

  //Test add note with pitch number greater than 127
  @Test(expected = IllegalArgumentException.class)
  public void testAddNote4() {
    IMusicModel obj = new MusicModel();
    obj.addNote(1, 2, 123, 129, 23);
  }

  //Test add note with volume less than zero
  @Test(expected = IllegalArgumentException.class)
  public void testAddNote5() {
    IMusicModel obj = new MusicModel();
    obj.addNote(1, 2, 123, 60, -4);
  }

  //Test add note with volume greater than 127
  @Test(expected = IllegalArgumentException.class)
  public void testAddNote6() {
    IMusicModel obj = new MusicModel();
    obj.addNote(1, 2, 123, 60, 130);
  }

  //Test add note
  @Test
  public void testAddNote7() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    assertEquals("   C5 \n" +
            "0  X  \n" +
            "1  |  \n" +
            "2  |  \n", obj.display());
  }

  //Test add note
  @Test
  public void testAddNote8() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    obj.addNote(3, 9, 123, 65, 10);
    assertEquals("   F5   E5  D#5   D5  C#5   C5 \n" +
            "0                           X  \n" +
            "1                           |  \n" +
            "2                           |  \n" +
            "3  X                           \n" +
            "4  |                           \n" +
            "5  |                           \n" +
            "6  |                           \n" +
            "7  |                           \n" +
            "8  |                           \n", obj.display());
  }

  //Test add note
  @Test
  public void testAddNote9() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    obj.addNote(3, 9, 123, 65, 10);
    obj.addNote(3, 7, 123, 57, 10);
    assertEquals("   F5   E5  D#5   D5  C#5   C5   B4  A#4   A4 \n" +
            "0                           X                 \n" +
            "1                           |                 \n" +
            "2                           |                 \n" +
            "3  X                                       X  \n" +
            "4  |                                       |  \n" +
            "5  |                                       |  \n" +
            "6  |                                       |  \n" +
            "7  |                                          \n" +
            "8  |                                          \n", obj.display());
  }

  //Test add note
  @Test
  public void testAddNote10() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    obj.addNote(3, 9, 123, 65, 10);
    obj.addNote(3, 7, 123, 57, 10);
    obj.addNote(5, 15, 123, 65, 10);
    assertEquals("    F5   E5  D#5   D5  C#5   C5   B4  A#4   A4 \n" +
            " 0                           X                 \n" +
            " 1                           |                 \n" +
            " 2                           |                 \n" +
            " 3  X                                       X  \n" +
            " 4  |                                       |  \n" +
            " 5  |                                       |  \n" +
            " 6  |                                       |  \n" +
            " 7  |                                          \n" +
            " 8  |                                          \n" +
            " 9  |                                          \n" +
            "10  |                                          \n" +
            "11  |                                          \n" +
            "12  |                                          \n" +
            "13  |                                          \n" +
            "14  |                                          \n", obj.display());
  }

  //Test remove note
  @Test
  public void testRemoveNote() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    obj.addNote(3, 9, 123, 65, 10);
    obj.addNote(3, 7, 123, 57, 10);
    obj.removeNote(3, 9, 123, 65, 10);
    assertEquals("   C5   B4  A#4   A4 \n" +
            "0  X                 \n" +
            "1  |                 \n" +
            "2  |                 \n" +
            "3                 X  \n" +
            "4                 |  \n" +
            "5                 |  \n" +
            "6                 |  \n" +
            "7                    \n" +
            "8                    \n", obj.display());
  }

  //Test remove note without the note present
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNote1() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    obj.addNote(3, 9, 123, 65, 10);
    obj.addNote(3, 7, 123, 57, 10);
    obj.removeNote(2, 9, 123, 65, 10);
  }

  //Test remove note without any note added to the track
  @Test(expected = NullPointerException.class)
  public void testRemoveNote2() {
    IMusicModel obj = new MusicModel();
    obj.removeNote(2, 9, 123, 65, 10);
  }

  //Test edit note by adding beats
  @Test
  public void testEditNote() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    obj.addNote(3, 9, 123, 65, 10);
    obj.addNote(3, 7, 123, 57, 10);
    obj.editNote(3, 9, 123, 65,
            10, 3, 13, 123, 65,
            10);
    assertEquals("    F5   E5  D#5   D5  C#5   C5   B4  A#4   A4 \n" +
            " 0                           X                 \n" +
            " 1                           |                 \n" +
            " 2                           |                 \n" +
            " 3  X                                       X  \n" +
            " 4  |                                       |  \n" +
            " 5  |                                       |  \n" +
            " 6  |                                       |  \n" +
            " 7  |                                          \n" +
            " 8  |                                          \n" +
            " 9  |                                          \n" +
            "10  |                                          \n" +
            "11  |                                          \n" +
            "12  |                                          \n", obj.display());
  }

  //Test edit note by changing pitch
  @Test
  public void testEditNote1() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    obj.addNote(3, 9, 123, 65, 10);
    obj.addNote(3, 7, 123, 57, 10);
    obj.editNote(3, 9, 123, 65,
            10, 3, 9, 123, 66,
            10);
    assertEquals("  F#5   F5   E5  D#5   D5  C#5   C5   B4  A#4   A4 \n" +
            "0                                X                 \n" +
            "1                                |                 \n" +
            "2                                |                 \n" +
            "3  X                                            X  \n" +
            "4  |                                            |  \n" +
            "5  |                                            |  \n" +
            "6  |                                            |  \n" +
            "7  |                                               \n" +
            "8  |                                               \n", obj.display());
  }

  //Test Combine Music with Illegal arguments
  @Test(expected = NullPointerException.class)
  public void testCombineMusic() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    obj.addNote(3, 9, 123, 65, 10);
    obj.addNote(3, 7, 123, 57, 10);
    IMusicModel obj2 = new MusicModel();
    obj.addNote(0, 5, 123, 49, 34);
    obj.addNote(2, 9, 123, 52, 34);
    obj.addNote(6, 8, 123, 57, 34);
    obj2.combineMusic(null, obj);
  }

  //Test Combine Music with simultaneous
  @Test
  public void testCombineMusic1() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    obj.addNote(3, 9, 123, 65, 10);
    obj.addNote(3, 7, 123, 57, 10);
    IMusicModel obj2 = new MusicModel();
    obj2.addNote(0, 5, 123, 49, 34);
    obj2.addNote(2, 9, 123, 52, 34);
    obj2.addNote(6, 8, 123, 57, 34);
    obj2.combineMusic(CombineType.SIMULTANEOUS, obj);
    assertEquals(
            "   F5   E5  D#5   D5  C#5   C5   B4  A#4" +
                    "   A4  G#4   G4  F#4   F4   E4  D#4   D4  C#4 \n" +
            "0                           X                     " +
                    "                                 X  \n" +
            "1                           |                      " +
                    "                                |  \n" +
            "2                           |                      " +
                    "                 X              |  \n" +
            "3  X                                       X       " +
                    "                 |              |  \n" +
            "4  |                                       |       " +
                    "                 |              |  \n" +
            "5  |                                       |       " +
                    "                 |              |  \n" +
            "6  |                                       |       " +
                    "                 |                 \n" +
            "7  |                                       |       " +
                    "                 |                 \n" +
            "8  |                                       |       " +
                    "                 |                 \n" +
            "9                                                  " +
                    "                 |                 \n", obj.display());
  }

  //Test Combine Music with consecutive
  @Test
  public void testCombineMusic2() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    obj.addNote(3, 9, 123, 65, 10);
    obj.addNote(3, 7, 123, 57, 10);
    IMusicModel obj2 = new MusicModel();
    obj2.addNote(0, 5, 123, 49, 34);
    obj2.addNote(2, 9, 123, 52, 34);
    obj2.addNote(6, 8, 123, 57, 34);
    obj2.combineMusic(CombineType.CONSECUTIVE, obj);
    assertEquals("    F5   E5  D#5   D5  C#5   C5   B4  A#4  " +
            " A4  G#4   G4  F#4   F4   E4  D#4   D4  C#4 \n" +
            " 0                           X                           " +
            "                              \n" +
            " 1                           |                           " +
            "                              \n" +
            " 2                           |                            " +
            "                             \n" +
            " 3  X                                       X             " +
            "                             \n" +
            " 4  |                                       |             " +
            "                             \n" +
            " 5  |                                       |              " +
            "                            \n" +
            " 6  |                                       |              " +
            "                            \n" +
            " 7  |                                                     " +
            "                             \n" +
            " 8  |                                                     " +
            "                             \n" +
            " 9                                                        " +
            "                             \n" +
            "10                                                        " +
            "                          X  \n" +
            "11                                                         " +
            "                         |  \n" +
            "12                                                         " +
            "          X              |  \n" +
            "13                                                         " +
            "          |              |  \n" +
            "14                                                         " +
            "          |              |  \n" +
            "15                                                         " +
            "          |              |  \n" +
            "16                                          X               " +
            "         |                 \n" +
            "17                                          |               " +
            "         |                 \n" +
            "18                                          |                " +
            "        |                 \n" +
            "19                                                           " +
            "        |                 \n", obj.display());
  }

  //Test display empty music model
  @Test
  public void testDisplay() {
    IMusicModel obj = new MusicModel();
    assertEquals("", obj.display());
  }

  //Test end beat
  @Test
  public void testEndBeat() {
    IMusicModel obj = new MusicModel();
    obj.addNote(0, 3, 123, 60, 10);
    obj.addNote(3, 9, 123, 65, 10);
    obj.addNote(3, 7, 123, 57, 10);
    assertEquals(9, obj.getEndBeat());
  }

  //Test end beat with empty piece
  @Test
  public void testEndBeat1() {
    IMusicModel obj = new MusicModel();
    assertEquals(1, obj.getEndBeat());
  }

  //Test get beats with empty music model
  @Test
  public void testGetBeats() {
    IMusicModel obj = new MusicModel();
    assertEquals(new HashMap<Integer, List<Sound>>(), obj.getBeats(0, 3));
  }

  //Test notes to display with empty music model
  @Test
  public void testNotesToDisplay() {
    IMusicModel obj = new MusicModel();
    assertEquals(new ArrayList<Note>(), obj.notesToDisplay());
  }

  //Test set tempo with negative input
  @Test(expected = IllegalArgumentException.class)
  public void testSetTempo() {
    IMusicModel obj = new MusicModel();
    obj.setTempo(-4);
  }

  //Test get tempo with negative input
  @Test
  public void testGetTempo() {
    IMusicModel obj = new MusicModel();
    obj.setTempo(200);
    assertEquals(200, obj.getTempo());
  }
}