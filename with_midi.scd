MIDIClient.destinations;

m

(
~test => [ sp: "kick", nb: 0 ];
~test.play;
)

(
~test = Pbind(
  \instrument, 'splayer',
  \sp, Bank("kick")[0],
);
~test.play;
)

(
~test >> [
  pat: "0 2 3 4",
  midiout: m
];
~test.play;
)

(
m = MIDIOut.newByName("MIDI", "Bus 1");
~test >> [ pat: "0 2 3 4", midiout: m ];
~test.play;
)

(
~baba = Pbind(
  \type, \midi,
  [\trig, \delta, \dur, \str, \num], Pmini("[1 2 3 4]/2"),
  \degree, Pfunc({ |e| if(e.trig > 0) { e.str.asInteger } { \rest } }),
  \midiout, m
);
~baba.play;
)
