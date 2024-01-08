# BuboQuark: Simple, hassle-free live coding

This repository is a collection of methods and hacks that I found to make live
coding easier on **SuperCollider**. A secondary goal is to make that setup easy
to install on other computers. This Quark is not bringing a lot of new features
and it twists the language in a way that is highly personal. Internally, it
relies a lot on **JITLib**, **Patterns** and **NodeProxies**. It truly feels
like a collection of tips and hacks found on the internet.

### Gated control-rate modulations

Ever wished to use a control-rate modulation for any of your `Pbind` parameters?
I have added a `Pdyn` pseudo-class that allows you to do that fairly easily.
Take a look at the following example :

```supercollider
(
[
"test2", i: "modulo",
  scale: Scale.minor,
  octave:3,
  // This is the line you need to look at
  ffreq: Pdyn({XLine.ar(5000, 200, c.beatDur * 2)}), 
  deg: "[0 7 0 5]^2.0 [0 3 0 5]^2.0 [~ ~ ~ ~]".p,
].pat.play;
~test2.fx(100, 0.35, {arg in; JPverb.ar(in, size: 10)});
)
```

`Pdyn` will wrap a `Ndef` and repeat it _every time_ for every event. This is
fairly hacky but it works rather well!

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
