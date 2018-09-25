package cs3500.music.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * A listener to listen for mouse clicks while using the GUI view.
 */
public class MouseKeyListener extends MouseAdapter {

  private Map<Integer, Runnable> buttonClickedMap;
  private Map<Integer, Runnable> buttonPressedMap;
  private Map<Integer, Runnable> buttonReleasedMap;

  @Override
  public void mouseClicked(MouseEvent e) {
    if (this.buttonClickedMap.containsKey(e.getButton())) {
      buttonClickedMap.get(e.getButton()).run();
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      MusicController.StartAddNote action =
              (MusicController.StartAddNote) buttonPressedMap.get(e.getButton());
      action.setX(e.getX());
      action.setY(e.getY());
      action.run();
    } else if (this.buttonPressedMap.containsKey(e.getButton())) {
      buttonPressedMap.get(e.getButton()).run();
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    /*if (e.getButton() == MouseEvent.BUTTON1) {
      MusicController.StopAddNote action =
              (MusicController.StopAddNote) buttonClickedMap.get(e.getButton());
      action.run();
    } else */if (this.buttonReleasedMap.containsKey(e.getButton())) {
      buttonReleasedMap.get(e.getButton()).run();
    }
  }

  /**
   * Takes in a map of the unique click identifier (Left, Right or Center) and sets its
   * corresponding runnable objects to allow the View to run it.
   * @param map map of the unique click identifier and its corresponding actions
   */
  public void setButtonClickedMap(Map<Integer, Runnable> map) {
    this.buttonClickedMap = map;
  }

  public void setButtonPressedMap(Map<Integer, Runnable> map) {
    this.buttonPressedMap = map;
  }

  public void setButtonReleasedMap(Map<Integer, Runnable> map) {
    this.buttonReleasedMap = map;
  }


}
