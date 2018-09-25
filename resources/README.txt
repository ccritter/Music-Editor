PitchType : This enumeration consists of the 12 Distinct pitches in western music. C, C#, D, D#, E,
F, F#, G, G#, A , A# and B. These Pitches are present in each octave.

OctaveType : This enumeration consists of the 11 average Octaves any human can hear.

Note : This class is used to represent one particular note. It consists of a PitchType and
OctaveType.

Sound : This class is used to represent one piece of sound in the Music Editor. It consists of one
Note, the duration of the sound in beats and the start beat location of the sound.
Added: instrument and volume.

CombineType : This enumeration consists of the two types of combinations of two Music Pieces. It
can either be Simultaneous or Consecutive.

IMusicModel : This is the interface which defines all the functionality any Music Model implementing
 this interface should consist of. It should be able to add sounds, remove sounds, edit sounds,
  combine music pieces, display the sound in a way understood by the user and returns the end beat
  of the music beat. Added functionality to set tempo, get tempo, get all the beats from one
  location to another and get the list of notes to be displayed.

MusicModel : This class implements the IMusicModel which consists of a HashMap whose key is the beat
 number and value is a list of Sounds at that beat. It consists of a stack of lowest Notes and
 highest Notes so that no unnecessary columns are displayed if a note has been removed. It also
 consists of the parametrized constructor which allows you to set the minimum note to display by
 default and the maximum note to display by default. Added functionality to set tempo, get tempo,
 get all the beats from one location to another and get the list of notes to be displayed.

CompositionBuilder : It is the interface which defines what functions any builder class should
 have for a music model to read files. It can set the tempo of a mode, add notes to a model and
 get the model with the notes added in it.

ModelBuilder : This class implements all the functions defined in CompositionBuilder and build a
  MusicModel by parsing notes from a text file or any source of input.

MusicReader : This class reads music from any Readable object and Creates a Composition builder of
that input which allows us to create an IMusicModel of the added notes in it.

GuiViewFrame : This class is used to display the Graphical interpretation of the Music Model
created by displaying the notes being played at each beat and allowing us to move through the
music piece.

IMusicView : This interface defines the functions that should be contained in the different views
 that implement this. They should initialize themselves and output information from the model.
 Several methods were added since last assignment to accommodate the new spec features.

MidiViewImpl : This class is used to output the sound through playing each note at each beat
number in the Music Model through java's inbuilt Midi implementation. Added the ability to play
sequence beginning at any point within the song.

NotePanel : It creates the JPanel which contains the grid of each note and its respective beat
number with a slider for the GuiView class.

PianoPanel : It creates the JPanel which contains the piano which lights up the keys playing at
each beat at the location of the slider. Reworked how keys are drawn and made them represented
by an embedded class.

TextualView : This class displays the information contained in the model into the console using a
 grid of beat number and the note playing at each beat.

ViewFactory : This class constructs a View according to a parameter of its getView function
hereby allowing us to choose between multiple views. Added support for the composite view.

MusicEditor : This class is used to run a particular instance of a view with a single model after
 reading any Readable object. Now works with the controller.

IMusicController : This is the interface which gets implemented by all music controllers. Any
class implementing this class should be allowed to interact with the model and view.

KeyboardListener : This class creates a mapping of the keys on the keyboard, the types of presses
 and the function each key press should do using runnable objects.

MouseKeyListener : This class maps the clicks on a mouse to it's respective runnable object which
 allows to execute a specific function in the model and the view.

MusicController : This class implements the IMusicController and allows us to interact between
the model and the view using the keyboard and mouse.