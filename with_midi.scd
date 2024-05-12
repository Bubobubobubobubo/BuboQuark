// NOTE: ajouter un truc pour tuer tout le MIDI quand j'appuie sur F12



// NOTE: Pattern rythmique de base
(
~test => [ sp: "kick", nb: 0 ];
~test.play;
)

// NOTE: Pattern de base, explicite (sans clés magiques)
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


// NOTE: Sans pattern Pmini
(
m = MIDIOut.newByName("MIDI", "Bus 1");
~test >> [ degree: [0, 2, 3, 4].pseq(inf), midiout: m ];
~test.play;
)

// NOTE: Avec Pmini + type explicite
(
m = MIDIOut.newByName("MIDI", "Bus 1");
~test >> [type: 'midi', pat: "0 1 2 3", midiout: m];
~test.play;
)

// NOTE: Avec Pmini, sans type explicite
(
m = MIDIOut.newByName("MIDI", "Bus 1");
~test >> [pat: "0 1 2 3", midiout: m];
~test.play;
)

// NOTE: : Pattern de démonstration
(
~baba = Pbind(
  \type, \midi,
  [\trig, \delta, \dur, \str, \num], Pmini("[1 ~ 2 3 4]/2").trace,
  \degree, Pfunc({ |e| if(e.trig > 0) { e.str.asInteger } { \rest } }),
  \midiout, m
);
~baba.play;
)
