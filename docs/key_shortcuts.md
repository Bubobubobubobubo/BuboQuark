# Key shortcuts

We can take this shortening job one step further by shortening the key names used in `Pbinds`. I am not covering all of them but only the most common. Note that you can still use the regular keys. The shortcuts are just replaced with their long version when the code is being evaluated.

#### Instrument selection

This is the key that you have to use to select the synthesizer that your pattern
will be controlling. This key can also be patterned, something that people often
forget about.

| Key Name    | Shortcut    | Purpose       |
| ----------- | ----------- | ------------- |
| \instrument | i           | Synthdef name |

#### Amplitude envelope

I have covered the basic ADSR shortcuts but not much more.

| Key Name    | Shortcut    | Purpose                   |
| ----------- | ----------- | ------------------------- |
| \attack     | a           | Attack time (in seconds)  |
| \decay      | d           | Decay time (in seconds)   |
| \sustain    | s           | Sustain level (relative)  |
| \release    | r           | Release time (in seconds) |

#### Pitch

The pitch model used by the default [Event](https://doc.sccode.org/Classes/Event.html) type is pretty complex and I will not try to sum it up here. It is better to just read about it. To print the list of available scales, use hte `Scale.directory` command.

| Key Name    | Shortcut    | Purpose                   |
| ----------- | ----------- | ------------------------- |
| \root       | rt          | Root note                 |
| \midinote   | mn          | MIDI Note number          |
| \note       | n           | Note number (O to `inf`)  |
| \degree     | deg         | Degree of the scale       |
| \octave     | o           | Octave number             |
| \freq       | f           | Frequency                 |
| \detune     | det         | Detune amount             |
| \scale      | scl         | Scale selection           |

#### Duration

| Key Name    | Shortcut    | Purpose                   |
| ----------- | ----------- | ------------------------- |
| \dur        | d           | Event duration (in beats) |
| \stretch    | st          | Event time stretching (multiple)    |
| \legato     | l           | Time gap between events             |
| \timingOffset     | off           | Offset in beats             |


#### Amplitude

| Key Name    | Shortcut    | Purpose                   |
| ----------- | ----------- | ------------------------- |
| \velocity        | vel           | MIDI Velocity      |

#### MIDI Related keys

| Key Name    | Shortcut    | Purpose                   |
| ----------- | ----------- | ------------------------- |
| \midichan   | c           | MIDI Channel (0 to 15)    |
| \midiout    | mo          | MIDI Output               |


#### Other keys

| Key Name    | Shortcut    | Purpose                   |
| ----------- | ----------- | ------------------------- |
| \pan        | p           | Stereo Panning (-1 to 1)  |

I will probably add some more keys in the future. They are to be found in the
`BuboArray.sc` file if you ever get curious about this small mechanism. It is
just replacing values in the list we use to generate our `Pbinds`.
