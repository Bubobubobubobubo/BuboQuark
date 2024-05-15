# BuboQuark: A Live Coding Companion

A collection of methods, hacks and tips I found to make my live coding workflow easier  on SuperCollider. Wraps around the base behavior of [JITLib](https://doc.sccode.org/Overviews/JITLib.html), makes writing patterns easier and faster. BuboQuark is not a replacement for the SCLang syntax. On the contrary, BuboQuark's goal is to make it easier to navigate the possibilities offered by the language. 

## What is BuboQuark?

BuboQuark is an experimental and use-at-your-own-risk Quark for SuperCollider 3.13+. It adds a few methods, objects and operators to make live coding easier on stage. It is inspired by a long tradition of high-level frameworks such as FoxDot, TidalCycles, [Sardine](https://sardine.raphaelforment.fr) and so on. It is also inspired by the trove of techniques that people shared for nearly twenty years on forums, chats, emails, etc. The code itself contains very few new or never-seen-before features. I think of it as a creative assemblage of software fragments that were lying around the internet for quite some time.

### Features

#### Boot command

BuboQuark is a manager for improvised live coding sessions. To start a session, use the `Boot(configPath, samplePath, serverOptions)` command:
- `configPath`: a personal `.scd` file if you want to evaluate stuff at the beginning.
- `samplePath`: path to a folder containing audio samples (`/folder/sample/__x.wav`).
    - use a good old regular path (_e.g_ `/Users/bubo/.config/livecoding/samples`)
- `serverOptions`: a `ServerOptions` object to configure the audio server 
    - if you don't provide anything, a suitable configuration will be assigned :) 

After booting for the first time, and if everything booted correctly, you should be greeted with the following banner:

```bash
┳┓  ┓   ┏┓      ┓    ┓   ┓   ┓
┣┫┓┏┣┓┏┓┃┃┓┏┏┓┏┓┃┏  •┃  •┃  •┃
┻┛┗┻┗┛┗┛┗┻┗┻┗┻┛ ┛┗  •┛  •┛  •┛
This is my beloved SuperCollider setup
Enjoy, have fun: [ raphaelforment.fr ] 
-> Booting using default server configuration

[...]

┓•          ┓             ╻
┃┓┓┏┏┓  ┏┏┓┏┫┏┓  ┏┓┏┓┓┏┏  ┃
┗┗┗┛┗   ┗┗┛┗┻┗   ┛┗┗┛┗┻┛  •
```

You can now start live coding! Note that BuboQuark is pushing you into a [ProxySpace](https://doc.sccode.org/Classes/ProxySpace.html). Every global variable (`~hello`) is now a proxy. Letters from `a` to `z` remain as global variables. Some of them are already used by BuboQuark:
- `c`: Clock, a `LinkClock` for synchronising with other peers using the [Ableton Link](https://www.ableton.com/en/link/) protocol
- `p`: ProxySpace, the scope/environment you are currrently coding in!
    - `p.clear(fadeTime)`: clean everything, kill all active/existing nodes.
    - `p.gui`: a graphical window popup displaying the current session state.
- `s`: the beloved audio server, still using its default variable name :)
- `d`: an object holding all your currently defined SynthDefs (in config file)
    - `d.params(synth)`: prints all the available controls for a given `synth`
    - `d.list`: lists all the available synthesizers in the session

If you don't like what you are hearing, simply type and evaluate `Panic()`.

#### Clock

The BuboQuark session uses `LinkClock` to link/synchronize with other peers on the local network. This is convenient if you want to jam with your friends. Note that true synchronisation is hard! You will have to account for audio latency, MIDI latency, etc.. The `c` variable gives you access to the current clock:

- `c.tempo`: use it to define the tempo (_e.g._ `c.tempo = 120 / 60`)
- `c.dur`: the duration of a beat (super useful for audio sequencing)

#### Sample Library

Your sample library is loaded automatically. It is accessible through  the `Bank` class (using [SAMP](https://gist.github.com/scztt/73a2ae402d9765294ae8f72979d1720e/revisions)):
- Audio samples are lazily loaded. They do not clutter your RAM! Expect some missed events.
- Audio samples are automatically handled/dispatched in patterns using a key/value combo: 
    - `sp`: sample folder name, usually something like `kick` or `casio`
    - `nb`: sample number, from `0` to `n` (wraps around)
- No need to specify a sampler in your patterns, they will be automatically assigned:
    - mono sampler or stereo sampler depending on the selected file
    - basic control over playback speed, envelope, amplitude, etc

Use the `Bank.list` command to see the list of possible samples if you forgot what you have currently loaded. Note that you can use `Bank(<sample_folder>)[<sample_number>]` everywhere where SuperCollider expects a `Buffer`. You can load a ton of creative stuff!

#### Pattern Syntax

I personally dislike the `Pbind(\qdklsj)` or `Ndef(\qkljsdf)` syntax. The `\` symbol is hard to reach on some keyboards. What was expected to be a shortcut for QWERTY users is now a pain for everyone else. I have modified the pattern syntax every so slightly to make it convenient for fast-typing (a hardly personal topic!). Patterns are now using `Arrays` (`[]`). There is a bunch of new operators for the `NodeProxy` class:

- Operators for creating SuperCollider patterns on-the-fly:
    -  `=>` (Pbind): basic musical pattern
    -  `->` (Pmono): monophonic expression pattern
    -  `==` (Looper): looper/sampler (**WIP**, currently broken)
    - `>>` (Note): MIDI Note Pattern
    - `>>+` (CC): MIDI CC Pattern

The syntax for creating patterns is as follows:

```cpp
// A kick pattern using different samples
~pattern => [sp: "kick", nb: [0, 2, 4].pseq(inf)];
~pattern.play;

// A snare/casio pattern using shortcuts
~comp => [pat: "[snare casio]/2"];
~comp.play;
```

Under the hood, everything is a `NodeProxy`, ensuring total compatibilty with the wonderful JITLIB library. There are some other shortcuts to add effects to a pattern:

```cpp
// Our beloved snare casio pattern
~comp => [pat: "[snare casio]/2"];
~comp.fx(100, 0.5, {
    arg in; MiVerb.ar(in, time: 0.5)
});
~comp.play;
```

There are also the `fx1`... `fx9` functions that take only two arguments: `~comp.fx1(wet, func)`.

You will have to adapt the operator (_e.g_ `=>`) to the type of pattern you want to make use of. Internally, each operator will perform custom logic for a specific type of event. For instance, you can modify the `fadeTime` and `quant` of a pattern directly from the pattern itself:

```cpp
~comp => [pat: "[snare casio]/2", fade: 4, quant:8];
```

You **cannot escape** learning the SuperCollider syntax and/or JITLib. The management of pattern is done using the regular syntax:

```cpp
~comp => [pat: "[snare casio]/2", fade: 4, quant:8];

~comp.play(fadeTime: 8); // Fade in over 8 beats

~comp.fadeTime = 4; // Changing the global fadeTime

~comp.stop(fadeTime: 4) // Fade out over 4 beats

~comp.clear; // Clearing the proxy
```

#### Patterning

It is sometimes hard to write patterns on-the-fly using SuperCollider. The pattern library is powerful and infinitely rewarding but the syntax is.. rather verbose. I am kicking open doors here, everybody knows about this, there is just no real incentive to change, SuperCollider is not only for live coders but also for composers and other creative coders that don't need to alter stuff on-the-fly. Let's accept it as it is and move forward by making sure that we stay compatible with the base library. Please, [read the tutorial](https://doc.sccode.org/Tutorials/A-Practical-Guide/PG_01_Introduction.html) that comes included with SuperCollider. It will teach you everything there is to known about SuperCollider Events and patterns. BuboQuark is including a few batteries to deal with patterns:
- [Pmini](https://github.com/j0py/Pmini): TidalCycles mini-notation look-alike
- [Pdv](https://github.com/dmorgan-github/Pdv): same thing, but a different flavour
- various pattern shortcuts using Arrays (_e.g_, `[1, 2].pseq(inf)`)

The `pat` key/value you can use in patterns makes use of [Pmini](https://github.com/j0py/Pmini), a Quark that helps creating [TidalCycles](https://tidalcycles.org/) like patterns without relying on a third-party tool. This is not the same thing of course, but it is already super fun to use and better integrated with the base SuperCollider. This Quark is a tradeoff. You will loose patterning capabilities, but you will remain close to the audio engine. There are things you can do with Tidal but not with Pmini. Using BuboQuark, you can replace some creative patterning tricks by leveraging audio manipulations and modulations, by harnessing the raw power of the SuperCollider audio engine. Here are some examples:

```cpp
// Generative melodies using Pmini
(
    ~test => [
        instrument: "Panalog", octave: 6,
        scale: Scale.chromatic,
        pat: "[0 3 [5 7 10 12]",
    ];
    ~test.play;
)

// Sample based pattern
(
    ~test => [pat: "[kick/2, hat!4, snare/2]"];
    ~test.play;
)

// Sample based pattern (with sample numbers)
(
    ~test => [pat: "[kick:4/2, hat:2!4, snare:8/2]"];
    ~test.play;
)

// Pitched/rhythmic sample playback
(
    ~test => [sp: "casio", pat: "0 ~ 3 5 7"];
    ~test.play;
)
```

You will need to learn the Tidal mini-notation syntax to be proficient with this mini-notation syntax. This is something that can be learned quite easily just by experimenting with samples and synthesizers. Note that Pmini sometimes behave a bit differently compared to the base Tidal. It is not a 100% accurate software port. For my use-case, this is more than enough!

## A small tour of possible pattern types

This is a very small tour of the syntax and how I expect people to play with BuboQuark.  For the rest of the demonstration, I will assume that you are able to replace sample names and synthesizers by your own:

```cpp
// Boot using your personal sample path
Boot(samplePath: "/Users/bubo/.config/livecoding/samples");
```

### Sampling capabilities

Let's start with something simple, a four-on-the-floor with high hats:

```cpp
(
    ~basic => [pat: "kick snare"];
    ~basic.play;
    ~hat => [pat: "hat:2 hat:4!3"];
    ~hat.play;
)
```

This is pitched sample playback. Note that you can also decompose patterns with `sp` and `nb` to carefully pick your samples:

```cpp
(
~basic => [
  sp: "kick",
  nb: [0, 2, 3].pseq(inf),
];
~basic.play;
~hat => [sp: ["hat", "casio"].pseq(inf), dur: 1/2, nb: [0, 2, 3].pseq(inf)];
~hat.play;
)
```

All the remaining keys in patterns are behaving just like regular SuperCollider patterns.

### Controlling synthesizers

Simply pick a synthesiser using the `instrument` or `i` key and go with the flow:

```cpp
(
~instrument => [
  instrument: 'Panalog', octave: 6,
  degree: [0, 4].pseq(inf)
];
~instrument.play;
)
```

Again, you can use the `pat` key to create melodies using the mini-notation:

```cpp
(
~instrument => [
  instrument: 'Panalog',
  pat: "0 2 3 4 5",
  octave: 6,
];
~instrument.play;
)
```

Here, I am pulling another trick. I am using `Pdv`, another Tidal-like mini-notation that behaves a bit differently. Please be creative and alter the system to your liking.

```cpp
(
~instrument => [
  instrument: 'Panalog',
  deg: "0 2 3 4".p,
  octave: 6,
];
~instrument.play;
)
```

Let's move on to `Pmono`, an event-type that allows you to keep a synth alive for as long as you want instead of creating a new synthesizer for each note you play. There is the `->` operator for it. Please note that you should feed the synth name as the first argument and without a `key/value` pair. This oddity comes from SuperCollider:

```cpp
(
SynthDef('acid', {
  var freq = \freq.kr(100).varlag(\glide.kr(0.05));
  var signal = PulseDPW.ar([freq / 2, freq / 1.99])
    + SawDPW.ar([freq, freq / 1.99]);
  var env = Env.perc(
    \attack.kr(0.1),
    \release.kr(0.125)).ar(0);
  var synth = signal * env;
  synth = RLPF.ar(signal,
    \ffreq.kr(1500).lag(\glide.kr),
    \res.kr(0.2).lag(\glide.kr));
  synth = Pan2.ar(synth, \pan.kr(0));
  OffsetOut.ar(\out.kr(0), synth * \amp.kr(-24.dbamp));
}).add;
)

(
~acid -> ['acid', dur: (1/2), amp: 0.5, pat: "[0 2 4 5]/2"];
~acid.play;
)

(
~acid -> ['acid', dur: (1/2), amp: 0.5, degree: [0, 4].pseq(inf)];
~acid.play;
)
```

### Sequencing external MIDI synthesizers

I often like to blend audio generated by my computer with audio generated by external synthesizers. There are a few shortcuts for this too:

```cpp
// Please open a MIDI output device like so
(
MIDIClient.init;
m = MIDIOut.newByName("MIDI", "Bus 1");
)

// Now you need to feed it to your patterns so it knows on 
// which external device the pattern should be playing
(
~notes >> [
  midiout: m, chan: 1,
  degree: [0, 4, 7, 10].pseq(inf),
  dur: [1, 1/2, 1/4].pxrand(inf),
];
~notes.play;
)

(
~notes >> [
  midiout: m, chan: 1, pat: "0 4 0 3 0 5",
  octave: [3, 4, 5, 6].pxrand(inf),
  legato: [0.125, 0.5].pwhite(inf),
];
~notes.play;
)
```

There is also a special operator (`>>+`) for MIDI Control Changes. This allows you to add parametric modulations to your external gear using the same syntax:

```cpp
(
~control >>+ [
  midiout: m, chan: 1, num: 20, val: [0, 127].pwhite(inf),
  dur: [1, 1/2, 1/4].pxrand(inf),
];
~control.play;
)

(
~control >>+ [
  midiout: m, chan: 1, num: 20, pat: "0 10 20 30 40 100 120",
];
~control.play;
)
```

This is a very shallow tour but I don't expect BuboQuark to become a big thing. It is simply a super fun way to play with SuperCollider! Please let me know if you want more info about the system!

## Contributions

Contributions are welcome! If you have a technique/method that you think is worth sharing, please open a pull request. If you have any questions, feel free to open an issue. There might be some things that I missed, or that could be improved. Please, let me know!

## About the website

There is a [small companion website](https://bubobubobubobubo.github.io/BuboQuark/#/) that initially came with the repo but it is not really important. It will be used to host a few tutorials when everything will take shape in the future.