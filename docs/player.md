# Player shortcuts

##### Rationale

JITLib is allowing you to update musical patterns on the fly. If you want to do
pattern-based _live coding_, this is the technique you will be using all the
time. Take a look at the following example:

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
~melody.play(fadeTime: 4);
)

~melody.clear(fadeTime: 2);
```

This does the job but it is a bit verbose. I have added a few shortcuts to make
it easier to edit patterns on the fly. Here is the same example using the
convenience and hacks brought by BuboQuark. I am skipping the synth definition:

```supercollider
["pattern", i: 'test',
 note:, [0, 2, 4, 5, ...].pseq,
 dur: 1/4, leg: 0.1].pat.play;

```
This is just saving you a few keystrokes here. There are other advantages
brought by this syntax that we will see later on.

##### Declaring patterns

BuboQuark is adding multiple methods to facilitate the creation of `Pbinds`:

- `.pat(quant=4, fade=0.05)`: create a `Pbind` from an array. **The first element of the list is always the
name of the NodeProxy you want to use**.
  - `quant`: pattern quantization (clock)
  - `fade`: fading time between evaluations


- `.p`: simple conversion from an array to a `Pbind`. This is useful when you want to use the `NodeProxy` roles like `\set` and `\xset`. It doesn't do more than that.

##### Usage with base syntax

BuboQuark does not break the compatibility with the base syntax. You can still control your NodeProxies using the regular syntax. For example, if you want to change the `\amp` of the previous example, you can do it like this:
```supercollider
// Playing a kick drum using the abbreviated syntax
["drumming", i: "s", sp: "kick", nb: 0, amp: 0.2].pat.play;

~drumming.xset(\amp, 0.5); // Changing the amp to 0.5

~drumming.stop(fadeTime: 4); // Stopping the pattern

~drumming.clear;
```
NodeProxies are still the base object used when improvising. I am just bringing syntax sugar and varnish.
