# Oscilloscope and meters

SuperCollider comes with a default oscilloscope and a default frequency scope. They are accessible through the `s.scope` and `s.freqscope` methods. They are great but for some reason, they are being hidden behind other windows when you switch to another window. I want to see them all the time. I have added a few shortcuts to make that happen:

- `Scope()`: a scope that always stays on top!
- `FScope()`: a frequency scope that always stay on top!
- `Gui()`: a server GUI window that always stay on top!
- `Meter()`: a server meter window that always stay on top!

There are other widget windows but I don't use them that much. One that is worth
noting is `s.plotTree` that allows you to see what is currently alive on the
audio server (stuck synths?).


## Ndef GUI

When you live code on SuperCollider, you tend to use a lot of `Ndef`/`Nodeproxy` objects. One feature of `NodeProxy` that is often overlooked is that they can be displayed in a graphical interface. You can monitor them this way. Check the following example:

```supercollider
(
~test = {
  var sequence = Demand.ar(
    Impulse.ar(c.dur * 8),
    Impulse.ar(c.dur),
    Dseq([100, 150, 200, 400, 800], inf)).varlag(c.dur / \woof.kr(12));
  var sig = LPF.ar(Pulse.ar(sequence), LFNoise1.kr(c.dur * 2).range(500, 2000)) ! 2 * 0.5;
  JPverb.ar(sig, size: 10, t60: 2)
};
~test.play(fadeTime: 8);
~test.mold(2);
)

~test.gui; // Calling a GUI for that specific definition!
~test.gui2; // Modern replacement for the gui method
```

The `.gui` method uses the default [NdefGui](https://doc.sccode.org/Classes/NdefGui.html). The `.gui2` method uses Mads Kjeldgaard's [NdefGui2](https://github.com/madskjeldgaard/nodeproxygui2). It is a modern rewrite that fixes some of the issues of the old one.

Since the `\woof` parameter is declared as a control, it will be available in the GUI! The GUI was created by assuming some random values as the set of default values for the `\woof` control but you can define the default values yourself:

```supercollider
Spec.add(\woof, ControlSpec(0.01, 4.0, \exp) );
// Then, re-evaluate the code above
```

Now the `\woof` parameter will adhere to the defaults you have set using the
[Spec](https://doc.sccode.org/Classes/Spec.html) class. This is a quick and easy
way to create a GUI when you need one :)

