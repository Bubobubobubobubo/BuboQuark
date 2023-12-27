# BuboQuark: Simple, hassle-free live coding


This repository is a collection of tools and methods that I use to make live coding easier on **SuperCollider**. My aim is to make that setup easy to install on my other computers. This repository is not bringing anything new or interesting. This is merely a default configuration for my **SuperCollider** install. It's just a bunch of scripts that I use to make my life easier and I am no expert. I rely heavily on **JITLib** and other Quarks that I find useful.

## Installation

To install the **BuboQuark**, simply run the following command in your favorite **SC** editor:
```bash
Quarks.install("https://github.com/Bubobubobubobubo/BuboQuark")
```

## Usage

This quark brings syntax shortcuts and minor improvements (_highly personal matter_) to make **SuperCollider** easier to handle on stage.

### Boot method

There is a `Boot()` class that acts as a configuration file for my setup. This configuration file is rather classic:

- raises the conservative options of `Server.default`
- set the ProxySpace clock to use `LinkClock` for syncing with other
applications
- pushes everything into a `ProxySpace`
- set paths for samples and synthdefs
- install a `StageLimiter` not to blow up my speakers

The `LinkClock` is accessible through the `c` global variable.

**Note:** I put my configuration into `./config/livecoding/` and there should be a folder named `samples/` and a file called `Synthdefs.scd`.

### Events

I am using some Events as classes to store some of the things I want to load with each session (FX templates, SynthDef reference, etc). I am using :

- `d`: **D**efinitions (`SynthDefs`)
- `f`: **F**X templates (DSP functions with a simple name)

To use one of the effects, you can use the following syntax:

```supercollider
~my_ndef.fx(100, 0.5, f[\vardel]);
```

### Simplified useful commands

- `Panic()`: shortcut for `CmdPeriod.run`.
- `Boot(path)`: boot my config (**hardcoded** path or user specified path)
- `Scope()`: a scope that always stays on top!
- `FScope()`: a frequency scope that always stay on top!
- `Gui()`: a server GUI window that always stay on top!

### Patterns

Patterns are powerful but writing them is long and can lead to a lot of typing errors. Moreover, they are often centered around list manipulation. **BuboQuark** defines a few helpers to transform a regular `Array` into various patterns:

```supercollider
[1, 2, 3, 4].pseq
```
Consider the source as a documentation.

### Patterns

I don't like using keys because of the backslash (`\`), a symbol that is really hard to type on AZERTY keyboards. For that reason, I much prefer the `[instrument: 'plaits', dur: 2]` syntax. I added a `.pat` method to convert an array into a `Pbind`. There are optional arguments to specify the `fadeTime` and `quant` for that pattern. Demo:

```supercollider
(
  [
    instrument: 'sinfb',
    rel: Pbrown(0.1, 0.5, 0.125, inf),
    note: Place([Pxrand([0, 3, 7, 10], 12), 0, 3, 5, 0, 12, 0, 7, 5, [5, 10, 7].pwhite(1)], inf),
    octave: [Pxrand([5, 6, 4], 4)].pxrand(inf), dur: Pbjorklund2(6, 8, inf) / 2,
    legato: 0.1
  ].pat(~test).play;
)
```

### NodeProxy

The `NodeProxy` roles are somewhat verbose. I have tried to make the syntax easier on the eye by creating the `fx`, `wet` and `infx` methods. Here is a demo of how I use it:


```supercollider
(
  // Adding a tiny bit of reverb on slot 10 with a wet of 0.2
  ~test.fx(10, 0.2, {
    arg in; GVerb.ar(in)
  });
)

~test.wet(10, 0.5) // bring the reverb up with the wet method
```
