package cs3500.music.tests;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 * Fake Receiver class used to emulate a MIDI receiver, used for testing/logging purposes.
 */
public class MockReceiver implements Receiver {

  private StringBuilder log;

  /**
   * Constructs a MockReceiver with a StringBuilder used to log outputs.
   *
   * @param sb the StringBuilder to output sent information.
   */
  public MockReceiver(StringBuilder sb) {
    this.log = sb;
  }

  @Override
  public void send(MidiMessage midiMessage, long l) {
    ShortMessage m = (ShortMessage) midiMessage;
    if (m.getCommand() == ShortMessage.NOTE_OFF || m.getCommand() == ShortMessage.NOTE_ON) {
      this.log.append("Sending " + parseCommand(m.getCommand()) + " for note " + m.getData1()
              + " of volume " + m.getData2() + " on channel " + m.getChannel() + " and tick " + l
              + "\n");
    } else if (m.getCommand() == ShortMessage.PROGRAM_CHANGE) {
      this.log.append("Changing instrument for channel " + m.getChannel() + " to " + m.getData1()
              + ".\n");
    } else {
      this.log.append("Other command " + m.getCommand() + " sent to channel " + m.getChannel()
              + " with data1 " + m.getData1() + " and data2 " + m.getData2() + " at tick " + l
              + ".\n");
    }
  }

  /**
   * Turn the integer version of a MIDI command into a String representation.
   *
   * @param command command to parse
   * @return a string representation of that command
   */
  private String parseCommand(int command) {
    switch (command) {
      case ShortMessage.NOTE_OFF:
        return "NOTE_OFF";
      case ShortMessage.NOTE_ON:
        return "NOTE_ON";
      case ShortMessage.POLY_PRESSURE:
        return "POLY_PRESSURE";
      case ShortMessage.CONTROL_CHANGE:
        return "CONTROL_CHANGE";
      case ShortMessage.PROGRAM_CHANGE:
        return "PROGRAM_CHANGE";
      case ShortMessage.CHANNEL_PRESSURE:
        return "CHANNEL_PRESSURE";
      case ShortMessage.PITCH_BEND:
        return "PITCH_BEND";
      default:
        return null;
    }
  }

  @Override
  public void close() {
    this.log.append("Receiver closed.");
  }

  @Override
  public String toString() {
    return this.log.toString();
  }

}
