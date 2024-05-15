// Remplacer la PATH par un chemin valide (dossiers/samples)
(
Boot(samplePath: "/Users/bubo/.config/livecoding/samples");
)

// ================ SAMPLING (~SP et ~NB) ================

// TEST: [X] ça passe
(
~basic => [pat: "kick snare"];
~basic.play;
~hat => [pat: "hat:2 hat:4!3"];
~hat.play;
)

// TEST: [X] ça passe
(
~basic => [pat: "kick:4 snare:8"];
~basic.play;
~hat => [pat: "hat:2 hat:4!3"];
~hat.play;
)

// TEST: [X] ça passe
(
~basic => [
  sp: "kick",
  nb: [0, 2, 3].pseq(inf),
];
~basic.play;
~hat => [sp: ["hat", "casio"].pseq(inf), dur: 1/2, nb: [0, 2, 3].pseq(inf)];
~hat.play;
)


// TEST: [X] ça passe
(
~basic => [
  sp: ["kick", "synthi"].pseq(inf),
  nb: 10, // Change moi
  pat: "[0 7 0 7]/2",
  test: Pfunc {
    |e|
    e.postln;
    e
  }
];
~basic.play;
)

// ================ SYNTHETISEURS TYPE PBIND ================

// TEST: [X] ça passe
(
~instrument => [
  instrument: 'Panalog',
  octave: 6,
  degree: [0, 4].pseq(inf)
];
~instrument.play;
)


// TEST: [X] ça passe
(
~instrument => [
  instrument: 'Panalog',
  pat: "0 2 3 4 5",
  octave: 6,
];
~instrument.play;
)

// TEST: [X] ça passe
(
~instrument => [
  instrument: 'Panalog',
  deg: "0 2 3 4".p,
  octave: 6,
];
~instrument.play;
)

// ================ SYNTHETISEURS TYPE PMONO ================

// NOTE: SynthDef de test
(
SynthDef('acid', {
  var freq = \freq.kr(100).varlag(\glide.kr(0.05));
  var signal = PulseDPW.ar([freq / 2, freq / 1.99])
    + SawDPW.ar([freq, freq / 1.99]);
  var env = Env.perc(
    \attack.kr(0.1),
    \release.kr(0.125)).ar(0);
  var synth = signal * env;
  synth = RLPF.ar(signal,
    \ffreq.kr(1500).lag(\glide.kr),
    \res.kr(0.2).lag(\glide.kr));
  synth = Pan2.ar(synth, \pan.kr(0));
  OffsetOut.ar(\out.kr(0), synth * \amp.kr(-24.dbamp));
}).add;
)

// TEST: [X] ça passe !
(
~acid -> ['acid', dur: (1/2), amp: 0.5, pat: "[0 2 4 5]/2"];
~acid.play;
)

// TEST: [X] ça passe !
(
~acid -> ['acid', dur: (1/2), amp: 0.5, degree: [0, 4].pseq(inf)];
~acid.play;
)

// TEST: [X] ça passe !
(
~acid -> ['acid', dur: (1/2), amp: 0.5, degree: "0 2 3 4".p];
~acid.play;
)

// ================ COMMANDES MIDI ================

(
MIDIClient.init;
m = MIDIOut.newByName("MIDI", "Bus 1");
)

// TEST: ça passe (le canal 0 ne fonctionne pas !)
(
~notes >> [
  midiout: m, chan: 1,
  degree: [0, 4, 7, 10].pseq(inf),
  dur: [1, 1/2, 1/4].pxrand(inf),
];
~notes.play;
)


// TEST: ça passe (le canal 0 ne fonctionne pas !)
(
~notes >> [
  midiout: m, chan: 1, pat: "0 4 0 3 0 5",
  octave: [3, 4, 5, 6].pxrand(inf),
  legato: [0.125, 0.5].pwhite(inf),
];
~notes.play;
)

// TEST: ça passe
(
~control >>+ [
  midiout: m, chan: 1, num: 20, val: [0, 127].pwhite(inf),
  dur: [1, 1/2, 1/4].pxrand(inf),
];
~control.play;
)

// TEST: ça passe
(
~control >>+ [
  midiout: m, chan: 1, num: 20, pat: "0 10 20 30 40 100 120",
];
~control.play;
)
