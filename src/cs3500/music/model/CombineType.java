package cs3500.music.model;

/**
 * Represents the type of combine function we want to perform.<br>
 * It can be one of:<br>
 * SIMULTANEOUS - Play both sounds together<br>
 * CONSECUTIVE - Play the second sound after the first one ends<br>
 */
public enum CombineType {
  SIMULTANEOUS, CONSECUTIVE
}
