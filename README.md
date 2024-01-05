# BuboQuark: Simple, hassle-free live coding

This repository is a collection of methods and hacks that I found to make live
coding easier on **SuperCollider**. A secondary goal is to make that setup easy
to install on other computers. This Quark is not bringing a lot of new features
and it twists the language in a way that is highly personal. Internally, it
relies a lot on **JITLib**, **Patterns** and **NodeProxies**. It truly feels
like a collection of tips and hacks found on the internet.

## Installation

To install the **BuboQuark**, simply run the following command in your favorite **SC** editor:
```bash
Quarks.install("https://github.com/Bubobubobubobubo/BuboQuark")
```
To make use of the existing synth definitions, you will have to install the
[mi-Ugens](https://github.com/v7b1/mi-UGens), a collection of **SuperCollider**
UGens taken from Mutable Instruments module designs. Note that it is also
preferable to install [sc3-plugins](https://github.com/supercollider/sc3-plugins), the official **UGen** extension suite for **SuperCollider**. All the other dependencies will be installed automatically when installing the
**Quark**.

## How to use BuboQuark?

The main goal of **BuboQuark** is to provide everything you need to live code right out of the box. It is a balance between staying close to the initial language features while offering convenient shortcuts when possible:

- **boot** the server with a suitable configuration for live coding
- **facililate** the use of patterns and blending with audio functions
- **simplify** the use of `NodeProxy` roles for FX and mixing
- **synchonize** the clock with other applications
- **share** the audio signals easily with other applications

**SuperCollider** already possesses everything you need to do so. It is just not pre-arranged by default. 
It is a programming language after all, you have to do some work to get it right.

### Booting the server

There is a `Boot()` pseudo-class that acts as a configuration file. This configuration file is rather classic:

- raises the conservative options of `Server.default` to allow more connexions,
  more buffers, etc.
- set the ProxySpace clock to use `LinkClock` for syncing with other
applications
- make the default environment a **JITLib** `ProxySpace`
- Set custom default paths for sample and SynthDefs loading
- Install a `StageLimiter` not to blow up the speakers

The `Boot()` constructor takes three arguments:

- `configPath`: path to a `.scd` configuration file that will be automatically
loaded
- `samplePath`: path to a folder containing your audio samples (in sub-folders)
- `soundDevice`: name of the sound device to use

All of these arguments are optional. However, they will default to my
configuration if not set. If you want to set one option but not the others, use
keywords arguments or `nil` values: `Boot(soundDevice: "BlackHole 16ch")`.

### Controlling the clock

The `LinkClock` is accessible through the `c` global variable. Be careful not to
override it. It behaves like a regular `TempoClock` with the usual methods. 
There are a few useful methods to control it and to use it efficiently: 

- `c.tempo` : set or get the current tempo (will change other peers tempo)
- `c.beatDur` : duration of a beat

I use these methods very frequently when writing delay lines and time-based
effects.

### Events

I am using some Events as pseudo-classes to store some things I want to keep track on during the session (FX templates, SynthDef reference, etc). I am using :

- `d`: **D**efinitions (`SynthDefs`)
  - `d.list` : list all the available SynthDefs
  - `d.params('synth_name')` : list the parameters of a SynthDef

- `f`: **F**X templates (DSP functions accessible through a simple name)

To use one of the effects, you can use the following syntax:

```supercollider
~my_ndef.fx(100, 0.5, f[\vardel]);
```

These are not really hard-coded. They are in my `.scd` configuration files. You
can ignore this section entirely if you do things differently!

### Simplified Server/Gui Control

I like when SC panels stay on top of other applications by default:

- `Panic()`: shortcut for `CmdPeriod.run`.
- `Scope()`: a scope that always stays on top!
- `FScope()`: a frequency scope that always stay on top!
- `Gui()`: a server GUI window that always stay on top!

### Pattern tweaks

Patterns are powerful but writing them is long and can lead to a lot of typing errors. Moreover, they are often centered around list manipulation. **BuboQuark** defines a few helpers to transform a regular `Array` into various patterns:

```supercollider
[1, 2, 3, 4].pseq
```

Consider the source as a documentation. You will find all the additional methods
in `BuboString` or `BuboArray`. I am not entirely convinced by shortening the
most complex Pattern types because they are complex after all. Consider blending
the regular syntax with shortcuts when necessary.

### Pbind

I don't like using keys because of the backslash (`\`), a symbol that is really hard to type on **AZERTY** keyboards. For that reason, I much prefer the `["my_pattern", instrument: 'plaits', dur: 2]` syntax. I added a `.pat` method to convert an array into a `Pbind`. There are optional arguments to specify the `fadeTime` and `quant` for that pattern. Demo:

```supercollider
(
  [
    "name_of_pattern",
    instrument: 'sinfb',
    rel: Pbrown(0.1, 0.5, 0.125, inf),
    note: Place([Pxrand([0, 3, 7, 10], 12), 0, 3, 5, 0, 12, 0, 7, 5, [5, 10, 7].pwhite(1)], inf),
    octave: [Pxrand([5, 6, 4], 4)].pxrand(inf), dur: Pbjorklund2(6, 8, inf) / 2,
    legato: 0.1
  ].pat.play;
)
```
`.pat` take a few optional arguments:

- `quant` (defaults to `4`): pattern quantization (**LinkClock**)
- `fade` (defaults to `0.05`): fading time (**NodeProxy**)

There is also the `.p` function that will just turn the array into a `Pbind` without any additional behavior. This is useful when dealing with classic `NodeProxy Roles` like `\set` and `\xset`.

### NodeProxy

The `NodeProxy` roles are sometimes a bit verbose to my taste. I have tried to make the syntax easier on the eye by creating the `fx`, `wet` and `infx` methods. Here is a demo of how I use it:

```supercollider
(
  // Adding a tiny bit of reverb on slot 10 with a wet of 0.2
  ~test.fx(10, 0.2, {
    arg in; GVerb.ar(in)
  });
)

~test.wet(10, 0.5) // bring the reverb up with the wet method
```

There is also `.fxin` and `.wet` functions, shortcuts for the `\filter` and
`\filterIn` NodeProxy mechanisms. I have also added some rather shady functions
that automatically pick up a slot for a specific fx: `fx1`, `fx2`, up to `fx9`.


## Managing audio samples

I use a lot of samples in my live coding sessions. I have created a few helpers
to deal with audio samples without having to think about allocating buffers and
cleaning up. Everything relies on [SAMP](https://gist.github.com/scztt/73a2ae402d9765294ae8f72979d1720e), a mechanism that I borrowed to scztt (Scott Carver).

If your audio sample bank path is set right, you will have access to your bank
with lazy loading on by default not to eat all your RAM:

```supercollider
Bank.list // List of all the available sample folders
Bank('a/*') // List of all the samples in the 'a' folder
Bank('a/*')[0].play; // Playing the first sample in the folder
```

This is great. I also added some mechanisms to automatically feed a sample when
using patterns. That way, you don't have to type the `Bank` part all the time
and can stay focused on your improvisation:

```supercollider 
[
  "using_samples",
  i: "s" // s is the default sampler
  sp: "kick", // Give a string or symbol (pattern or not)
  nb: 0 // Give a number (can be pattern too)
].pat.play
```

Note that there is no optional argument here. You need `sp` and `nb` for it to
work. You can use these arguments when using the abbreviated syntax for `Pbind`
but not for regular `Pbind`. In that case, you will need to use the good old
`buf: Bank('a/*')[0]` syntax.
