# BuboQuark: A Live Coding Companion

A collection of methods, hacks and tips I found to make my live coding workflow easier  on SuperCollider. Wraps around the base behavior of [JITLib](https://doc.sccode.org/Overviews/JITLib.html), makes writing patterns easier and faster. BuboQuark is not a replacement for the SCLang syntax. On the contrary, BuboQuark's goal is to make it easier to navigate the possibilities offered by the language. 

## What is BuboQuark?

BuboQuark is an experimental and use-at-your-own-risk Quark for SuperCollider 3.13+. It adds a few methods, objects and operators to make live coding easier on stage. It is inspired by a long tradition of high-level frameworks such as FoxDot, TidalCycles, [Sardine](https://sardine.raphaelforment.fr) and so on. It is also inspired by the trove of techniques that people shared for nearly twenty years on forums, chats, emails, etc. The code itself contains very few new or never-seen-before features. I think of it as a creative assemblage of software fragments that were lying around the internet for quite some time.

### Features

#### Boot command

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

#### Pattern Syntax

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


## Contributions

Contributions are welcome! If you have a technique/method that you think is worth sharing, please open a pull request. If you have any questions, feel free to open an issue. There might be some things that I missed, or that could be improved. Please, let me know!

## About the website

There is a [small companion website](https://bubobubobubobubo.github.io/BuboQuark/#/) that initially came with the repo but I never updated it properly :). It will host a tutorial when things will settle in the distant future.
