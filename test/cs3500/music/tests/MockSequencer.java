package cs3500.music.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

/**
 * Fake Sequencer class used for testing purposes. Every method used in the View class is
 * implemented and will log the actions to a StringBuilder.
 */
public class MockSequencer implements Sequencer {

  StringBuilder log;
  Sequence sequence;
  MockReceiver receiver;

  boolean isPlaying;
  private long tickPos;

  /**
   * Construct a new MockSequencer with an empty StringBuilder and MockReceiver.
   */
  MockSequencer() {
    this.log = new StringBuilder();
    this.receiver = new MockReceiver(this.log);
    this.isPlaying = false;
    this.tickPos = 0;
  }

  @Override
  public void setSequence(Sequence sequence) throws InvalidMidiDataException {
    this.log.append("Setting sequence...\n");
    this.sequence = sequence;
  }

  @Override
  public void setSequence(InputStream inputStream) throws IOException, InvalidMidiDataException {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public Sequence getSequence() {
    return this.sequence;
  }

  @Override
  public void start() {
    this.log.append("Playing...\n");
    Track t = this.sequence.getTracks()[0];
    for (int i = 0; i < t.size() - 1; i++) {
      this.receiver.send(t.get(i).getMessage(), t.get(i).getTick());
    }

    this.isPlaying = true;
  }

  @Override
  public void stop() {
    this.log.append("Pausing...\n");
    this.isPlaying = false;
  }

  @Override
  public boolean isRunning() {
    return this.isPlaying;
  }

  @Override
  public void startRecording() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void stopRecording() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public boolean isRecording() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void recordEnable(Track track, int i) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void recordDisable(Track track) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public float getTempoInBPM() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void setTempoInBPM(float v) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public float getTempoInMPQ() {
    return 0; // stays at 0 so we don't have to wait for tests to run.
  }

  @Override
  public void setTempoInMPQ(float v) {
    this.log.append("Tempo set to " + v + "\n");
    // Do nothing, because for our purposes tempo should remain at 0.
  }

  @Override
  public void setTempoFactor(float v) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public float getTempoFactor() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public long getTickLength() {
    return 0; // Same as tempo, should be 0 so test do not have to wait for notes to play.
  }

  @Override
  public long getTickPosition() {
    return this.tickPos;
  }

  @Override
  public void setTickPosition(long l) {
    log.append("Setting tick position to " + l + "...\n");
    this.tickPos = l;
  }

  @Override
  public long getMicrosecondLength() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public Info getDeviceInfo() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void open() throws MidiUnavailableException {
    this.log.append("Sequencer opened.\n");
  }

  @Override
  public void close() {
    this.log.append("Sequencer closed.\n");
  }

  @Override
  public boolean isOpen() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public long getMicrosecondPosition() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public int getMaxReceivers() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public int getMaxTransmitters() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public Receiver getReceiver() throws MidiUnavailableException {
    return this.receiver;
  }

  @Override
  public List<Receiver> getReceivers() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public Transmitter getTransmitter() throws MidiUnavailableException {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public List<Transmitter> getTransmitters() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void setMicrosecondPosition(long l) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void setMasterSyncMode(SyncMode syncMode) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public SyncMode getMasterSyncMode() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public SyncMode[] getMasterSyncModes() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void setSlaveSyncMode(SyncMode syncMode) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public SyncMode getSlaveSyncMode() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public SyncMode[] getSlaveSyncModes() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void setTrackMute(int i, boolean b) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public boolean getTrackMute(int i) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void setTrackSolo(int i, boolean b) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public boolean getTrackSolo(int i) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public boolean addMetaEventListener(MetaEventListener metaEventListener) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void removeMetaEventListener(MetaEventListener metaEventListener) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public int[] addControllerEventListener(ControllerEventListener controllerEventListener,
                                          int[] ints) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public int[] removeControllerEventListener(ControllerEventListener controllerEventListener,
                                             int[] ints) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void setLoopStartPoint(long l) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public long getLoopStartPoint() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void setLoopEndPoint(long l) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public long getLoopEndPoint() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public void setLoopCount(int i) {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public int getLoopCount() {
    throw new RuntimeException("Method not supported for MockReceivers.");
  }

  @Override
  public String toString() {
    return this.log.toString();
  }
}
