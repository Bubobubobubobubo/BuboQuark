# Dynamic Control

The problem of using patterns and fixed values too much is that your patterns
can start to sound a bit rigid. They will lack the kind of life and movement
that you might strive for. One of my favorite features from SuperCollider is that you can modulate every key of your `Pbind` very freely at control rate. This blends the limit between writing patterns and writing DSP algorithms. You often feel like you are jumping very smoothly between working at the signal level and working at the control level.

I typically think of patterns as having three different types of controls:

- **language-based values:** using patterns, fixed values, discrete values, etc.
- **continuous modulations:** low-frequency modulations (LFOs), random signals, etc.
- **gated modulations:** envelopes, triggers, etc.

This is a very shallow typology but it is enough to start thinking about how to combine these three types of controls. I will now go through each of them and show you how BuboQuark manages them.

## Continuous modulations

There is nothing to do here. You can use the regular SuperCollider syntax to modulate your patterns. For example, if you want to modulate the `\freq` of a synth, you can do it like this:

```supercollider
["modulating_frequency", i: "default", amp: 0.5,
  freq: Ndef(\example, {SinOsc.ar(c.dur * 2).range(500, 2000)})].pat.play;
```

This example sounds horrible but it demonstrates that you can use regular `Ndef`
or any `NodeProxy` to get that type of modulation. We could also write it this
way:

```supercollider
~example = {SinOsc.ar(c.dur * 2).range(500, 2000)};
["modulating_frequency", i: "default", amp: 0.5, freq: ~example].pat.play;
```

This is up to you to decide which one you prefer.

## Gated modulations

It was a bit more difficult to find a good way to handle gated modulations. I
don't think I have found the perfect solution yet but it is good enough for now. I have created another pseudo-object called `Pdyn`. Despite its name, it is not really a pattern but it is meant to be used as one. `Pdyn` will retrigger for every event inside your pattern (by default).

```supercollider
["pdyn_demo", i: "default", freq: Pdyn({XLine.ar(2000, 100, c.dur)})].pat.play;
```

I don't think that it holds very well performance wise so be careful and don't expect the best precision ever. I will likely improve it whenever possible.
