# Boot the server

There is a `Boot()` pseudo-class acting as a configuration file. Its behavior
is pretty classic if you are already accustomed to SuperCollider:

- it raises the conservative options of `Server.default` to allow more connexions,
  more buffers, etc. Will prevent you from running out of memory while
improvising.

- turns the the default environment into a **JITLib** `ProxySpace`, replacing
all global variables with `NodeProxy` instances. This is one of the classic
_ways_ to _live code_ using SuperCollider.


- set the `ProxySpace` to use `LinkClock` for syncing with other.
`LinkClock` is using the [Ableton Link protocol](https://ableton.github.io/link/), supported by most other apps.

- Set custom paths for loading audio sample banks and synth definitions. Write
your definitions once, reuse them all the time (if you want to!).

- Set up a sound limit to prevent you from blowing up your speakers/ears. This is an often overlooked step that can save you from a lot of trouble. BuboQuark will also automatically load the _SafetyNet_ Quark to protect you from loud / incorrect audio signals.

The `Boot()` constructor takes three arguments:

- `configPath`: path to a `.scd` configuration file that will be automatically
loaded
- `samplePath`: path to a folder containing your audio samples (in sub-folders)
- `serverOptions`: a set of [ServerOptions](https://doc.sccode.org/Classes/ServerOptions.html) to fine tune the server configuration

All of these arguments are optional. However, they will default to my
configuration if not set. If you want to set one option but not the others, use
keywords arguments or `nil` values. Here is an example of how you could boot the server: 

```supercollider
Boot(serverOptions: nil, // use default options
     configPath: "/some/config/path",
     samplePath: "/home/me/my_samples")
```

To make sure that you have booted correctly, you can go through the following
check list:

- [ ] do I see my samples when I write `Bank.list`?
- [ ] do I see my synth definitions when I write `d.list`?
- [ ] is this snippet producing sound: `{SinOsc.ar(200) ! 2 * 0.5}.play`?
