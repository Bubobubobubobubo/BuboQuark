# Player/Pbind shortcuts

JITLib allows you to update musical patterns on the fly and to quantize that
change to a logical time division in the future. If you want to play some
pattern-based _live coded_ music, this is the technique you will be using a lot both for drums, melodies, etc. Take a look at the following example that could be taken from the JITLib tutorial:


<details>
<summary>Long SuperCollider Pattern Example</summary>

```supercollider
// This is a very simple synthesizer
(
SynthDef(\test, {
  var sig = SinOsc.ar(\freq.kr(440));
  var env = EnvGen.ar(Env.perc, doneAction:2);
  var synth = sig * env * \amp.kr(1);
  Out.ar(\out.kr(0), Pan2.ar(synth));
}).add;

// We use it to create a pattern
~melody = Pbind(
  \instrument, \test,
  \note, Pseq([0, 2, 4, 5, 7, 9, 11, 12], inf),
  \dur, 0.25,
  \legato, 0.1
);
// Fade-in over four beats
~melody.play(fadeTime: 4);
)

// We clear that melody after 2 beats (fade-out)
~melody.clear(fadeTime: 2);
```
</details>


This is doing the job but many people, including me, find it a bit verbose. I have added a few shortcuts to make
it easier to edit patterns on the fly. Here is the same example now using the
convenience and hacks brought by BuboQuark. I am skipping the synth definition
since we already added it to the server:

```supercollider
["pattern", i: 'test',
 note:, [0, 2, 4, 5, ...].pseq,
 dur: 1/4, leg: 0.1].pat.play;

```
This is just saving you a few keystrokes but will save you thousands in the long
run. There is multiple techniques used here to shorten the pattern. I will now
explain them one by one.

### Pattern declaration

BuboQuark is adding multiple methods to facilitate the creation of `Pbinds`:

- `.pat(quant=4, fade=0.05)`: create a `Pbind` from an array. **The first element of the list is always the
name of the NodeProxy you want to use**.
  - `quant`: pattern quantization (clock)
  - `fade`: fading time between evaluations

- `.p`: simple conversion from an array to a `Pbind`. This is useful when you want to use the `NodeProxy` roles like `\set` and `\xset`. It doesn't do more than that.

It replaces the verbose `Pbind(\key, value)` used by SuperCollider with an Array based syntax that is more concise to read and write: `[key: value]`.

### Blending with the base syntax

BuboQuark does not break the compatibility with the base syntax. You can still control your NodeProxies using the regular syntax. For example, if you want to change the `\amp` of the previous example, you can do it like this:
```supercollider
// Playing a kick drum using the abbreviated syntax
(
["drumming", instrument: "s", 
 sp: "kick", nb: 0,
 amp: 0.2
].pat.play;
)

~drumming.xset(\amp, 0.5); // Changing the amp to 0.5

~drumming.stop(fadeTime: 4); // Stopping the pattern

~drumming.clear;
```
`NodeProxy` is still the base object used here. I am just bringing syntax sugar and varnish. There is no incompatibility with the rest of the language.
