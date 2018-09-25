package cs3500.music.tests;

import org.junit.Test;

import java.awt.Button;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import cs3500.music.controller.MouseKeyListener;

import static org.junit.Assert.assertEquals;

/**
 * Test class for methods/operations of the MouseKeyListener class.
 */
public class MouseKeyListenerTest {

  static Runnable leftClick(StringBuilder sb) {
    return () -> {
      sb.append("Left click.");
    };
  }

  static Runnable rightClick(StringBuilder sb) {
    return () -> {
      sb.append("Left click.");
    };
  }

  @Test
  public void testAddButton() {
    MouseKeyListener ml = new MouseKeyListener();
    HashMap<Integer, Runnable> map = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    map.put(MouseEvent.BUTTON2, rightClick(sb));
    //ml.setButtonClickedMap(map);
    ml.mouseClicked(new MouseEvent(new Button(), MouseEvent.BUTTON2, System
            .currentTimeMillis(), 0,  0, 0, 1, false));
    assertEquals("", sb.toString());
  }
}

