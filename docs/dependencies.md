# Dependencies

BuboQuark is assuming that you have a standard installation of SuperCollider ready:

- [SuperCollider](https://supercollider.github.io/): the main software,
available on most platforms, including niche ones like Raspberry Pi, and other
tiny computers. Pick the latest version, always.
- [sc3-plugins](https://github.com/supercollider/sc3-plugins): official plugin
collection for SuperCollider including many audio effects and useful synthesis objects. They are often considered standard and are available on most platforms.

BuboQuark also uses the following external libraries / plugins:

- [mi-Ugens](https://github.com/v7b1/mi-UGens): a collection of UGens taken from
  the open-source code of Mutable Instruments Eurorack modules. This is
basically free ear candy.
- [Ported Plugins](https://github.com/madskjeldgaard/portedplugins): another
collection of UGens compiled by Mads Kjeldgaard. It includes new objects taken
from other libraries or DSP research papers.

Everything else will be automatically installed by the Quarks system. Let's
install it now by running this command in the IDE:

```supercollider
Quarks.install("https://github.com/bubobubobubobubo/Buboquark");
```

Press `Ctrl/Cmd+Enter` to evaluate that line and the installation will promptly
start. When the install process is over, you will have to recompile the library by
pressing `Ctrl/Cmd+Shift+L`. That's it, you are ready to go!
