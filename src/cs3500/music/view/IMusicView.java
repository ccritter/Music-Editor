package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * Interface for all View Classes to display and output sounds.
 */
public interface IMusicView {
  /**
   * Initializes the view and outputs the sound piece.
   */
  void initialize();

  // Added: methods below were added to accommodate the new assignment spec
  /**
   * If the view is not "playing" (whatever that may mean for the implemented class), start playing.
   * If it is already "playing", then stop it.
   */
  void togglePlay();

  /**
   * Decrement this view's current beat by one. This operation should never happen when the view is
   * currently playing, but can manually be called if it is not.
   */
  void moveLeft();

  /**
   * Increment this view's current beat by one. This operation should happen automatically in time
   * with its model's tempo, but can only be manually called if it is not playing.
   */
  void moveRight();

  /**
   * Add a key listener to this class to allow for interpreting of keypresses. Only really needed
   * for visual implementations of a view.
   *
   * @param listener a KeyListener that will listen for key events
   */
  void addKeyListener(KeyListener listener);

  /**
   * Is this view currently playing (whatever the implementation's interpretation of playing is).
   *
   * @return true if the view is playing
   */
  boolean isPlaying();

  /**
   * Set the view's current beat to the given beat.
   *
   * @param beat the non-negative beat to set the view to.
   */
  void setBeat(int beat);

  /**
   * Add a mouse listener to this class to allow for interpreting mouse presses. Only really needed
   * for visual interpretations of a view.
   *
   * @param listener a MouseListener that will listen for mouse events
   */
  //Added to listen to allow the controller to listen to mouse click events
  void addMouseListener(MouseListener listener);

  /**
   * Only needs to be implemented in visual representations of a view. For a specific pixel location
   * (x, y), return the note that can be found there (whether it be on a piano roll, or something
   * similar). Null if no note is found there.
   *
   * @param x the x coordinate of the desired location
   * @param y the y coordinate of the desired location
   * @return the integer value of the note found at those coordinates.
   */
  //Added to allow us to know which note the user is clicking on
  Integer getNoteAtLocation(int x, int y);

  /**
   * Getter method for the beat at which the slider is currently placed.
   * @return beat number at which the slider is currently located
   */
  //Added a getter method for the current beat to allow us to sync up th model and the view in
  //the controller
  int getCurrentBeat();

  /**
   * Resets the view to the new and modified map and reinitializes it fields.
   */
  //Added to allow us to redraw the panel once a note is added outside the current pitch range
  void reset();
}
