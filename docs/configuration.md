# Configuration

When you install the Quark, you will also inherit a folder called _Configuration_. This folder contains my personal configuration files. They are `.scd` files that will be interpreted during the boot sequence. You can use them to pre-declare synth definitions, add convenience methods, etc:

```bash
├── Configuration
│   ├── Startup.scd
│   └── Synthdefs.scd
```

You do as you wish with these. They are currently very minimal and only include
a method call to setup **MIDI** and a bunch of synth definitions I use. They
rely pretty heavily on [mi-UGens](dependencies.md).

### How to list synths and effects?

In my personal configuration files, I am using the `d` variable to hold a
reference to every synth definition I write. I also use the `f` variable to
pre-write some audio effects that I use often. This is not hard-coded, you can
get rid of it if you want. I am also attaching a few methods to these objects:

- `d.list`: print the list of available synth definitions.
- `d.params('synth')`: print the parameters of the synth definition named `'synth'`.
