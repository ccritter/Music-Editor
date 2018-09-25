package cs3500.music.tests;

import org.junit.Test;

import java.awt.Button;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import cs3500.music.controller.KeyboardListener;

import static org.junit.Assert.assertEquals;

/**
 * Test class for methods/operations of the KeyboardListener class.
 */
public class KeyboardListenerTest {

  static Runnable pressSpace(StringBuilder sb) {
    return () -> {
      sb.append("Space");
    };
  }

  static Runnable pressLeft(StringBuilder sb) {
    return () -> {
      sb.append("Left");
    };
  }

  static Runnable pressRight(StringBuilder sb) {
    return () -> {
      sb.append("Right");
    };
  }

  @Test
  public void testNothing() {
    KeyboardListener kbl = new KeyboardListener();
    HashMap<Character, Runnable> map = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    kbl.setKeyTypedMap(map);

    assertEquals("", sb.toString());
  }

  @Test
  public void testSpaceTyped() throws AWTException {
    KeyboardListener kbl = new KeyboardListener();
    HashMap<Character, Runnable> map = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    map.put(' ', pressSpace(sb));
    kbl.setKeyTypedMap(map);

    kbl.keyTyped(new KeyEvent(new Button(), KeyEvent.KEY_TYPED, System.currentTimeMillis(),
            0, KeyEvent.VK_UNDEFINED, ' '));

    assertEquals("Space", sb.toString());

  }

  @Test
  public void testRightPressed() {
    KeyboardListener kbl = new KeyboardListener();
    HashMap<Integer, Runnable> map = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    map.put(KeyEvent.VK_RIGHT, pressRight(sb));
    kbl.setKeyPressedMap(map);

    kbl.keyPressed(new KeyEvent(new Button(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
            0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED));

    assertEquals("Right", sb.toString());
  }

  @Test
  public void testLeftReleased() {
    KeyboardListener kbl = new KeyboardListener();
    HashMap<Integer, Runnable> map = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    map.put(KeyEvent.VK_LEFT, pressLeft(sb));
    kbl.setKeyReleasedMap(map);

    kbl.keyReleased(new KeyEvent(new Button(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(),
            0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED));

    assertEquals("Left", sb.toString());
  }

  @Test
  public void testKeyPressedNotPresent() {
    KeyboardListener kbl = new KeyboardListener();
    HashMap<Integer, Runnable> map = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    map.put(KeyEvent.VK_LEFT, pressLeft(sb));
    kbl.setKeyPressedMap(map);

    kbl.keyPressed(new KeyEvent(new Button(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(),
            0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED));

    assertEquals("", sb.toString());
  }

  @Test
  public void testKeyPressedWithSeveralKeys() {
    KeyboardListener kbl = new KeyboardListener();
    HashMap<Integer, Runnable> map = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    map.put(KeyEvent.VK_LEFT, pressLeft(sb));
    map.put(KeyEvent.VK_RIGHT, pressRight(sb));
    map.put(KeyEvent.VK_SPACE, pressSpace(sb));
    kbl.setKeyPressedMap(map);

    kbl.keyPressed(new KeyEvent(new Button(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(),
            0, KeyEvent.VK_SPACE, KeyEvent.CHAR_UNDEFINED));

    assertEquals("Space", sb.toString());
  }

  @Test
  public void testKeyPressedThenReleased() {
    KeyboardListener kbl = new KeyboardListener();
    HashMap<Integer, Runnable> pressedMap = new HashMap<>();
    HashMap<Integer, Runnable> releasedMap = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    pressedMap.put(KeyEvent.VK_LEFT, pressLeft(sb));
    releasedMap.put(KeyEvent.VK_LEFT, pressLeft(sb));
    kbl.setKeyPressedMap(pressedMap);
    kbl.setKeyReleasedMap(releasedMap);

    kbl.keyPressed(new KeyEvent(new Button(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
            0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED));
    kbl.keyReleased(new KeyEvent(new Button(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(),
            0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED));

    assertEquals("LeftLeft", sb.toString());
  }

  @Test
  public void testSeveralMappings() {
    KeyboardListener kbl = new KeyboardListener();
    HashMap<Character, Runnable> typedMap = new HashMap<>();
    HashMap<Integer, Runnable> releasedMap = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    typedMap.put(' ', pressSpace(sb));
    releasedMap.put(KeyEvent.VK_LEFT, pressLeft(sb));
    kbl.setKeyTypedMap(typedMap);
    kbl.setKeyReleasedMap(releasedMap);

    kbl.keyTyped(new KeyEvent(new Button(), KeyEvent.KEY_TYPED, System.currentTimeMillis(),
            0, KeyEvent.VK_UNDEFINED, ' '));
    kbl.keyReleased(new KeyEvent(new Button(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(),
            0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED));

    assertEquals("SpaceLeft", sb.toString());
  }

}
