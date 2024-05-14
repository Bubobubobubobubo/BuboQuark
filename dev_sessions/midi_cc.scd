(
m = MIDIOut.newByName("MIDI", "Bus 1");
~cc >>+ [num: 20, pat: "[10 50 100 [120 100]]/2", midiout: m ];
~cc.play;
)

(
m = MIDIOut.newByName("MIDI", "Bus 1");
~test >> [
  midiout: m,
  degree: [0, 2, 4, 5].pxrand(inf),
  chan: 1,
  dur: [5,8,0].eu / 4, octave: 5,
  amp: 1/2,
];
~test.play;
)

(
m = MIDIOut.newByName("MIDI", "Bus 1");
~cc >>+ [
  num: 20, dur: 1/8, midiout: m,
  pat: "10 20 30 40 50 70 100 120",
];
~cc.play;
)


// TEST: Avec des samples
(
~test => [pat: "[kick hat snare hat:5]/2", test: Pfunc { |e| e.postln; e }];
~hat => [pat: "[hat!3 hat:5]", test: Pfunc { |e| e.postln; e }, speed: 2, release: 1/16];
~test.play;
~hat.play;
)

// TEST: Avec un synthétiseur
(
~test => [pat: "0 3 5 <7 ~>", i: "Pwaveshape", octave: 6, harm: 0.25];
~test.play;
)

// TEST: une mélodie MIDI
(
m = MIDIOut.newByName("MIDI", "Bus 1");
~test >> [
  pat: "[0 3 5 3, [0,<3 5>,<7 10>]]/2",
  midiout: m,
  amp: [0.0, 1.0].pwhite(inf),
  octave: 4,
  chan: [0, 1].pxrand(inf),
  release: 0.125/4,
  legato: 0.1,
];
~test.play;
)

// NOTE: Je vais jouer avec jusqu'à ce qu'il casse
(
m = MIDIOut.newByName("MIDI", "Bus 1");
~a = Pbind(
  \type, \midi,
  \midicmd, \control,
  \ctlNum, [20,25].pseq(inf),
  // chan est optionnel
  \midiout, m,
  \dur, [1,1/2].pseq(inf),
  \val, Pseq([50, 100], inf),
  \test, Pfunc {
    |e| e.postln; e
  }
);
~a.play;
)

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
~acid -> ['acid', dur: (1/2), amp: 0.5, pat: "[0 2 4 5]/2"];
~acid.play;
)

~acid.source

(
~a = Pmono(
  'acid', \freq, [100, 150, 200, 400].pxrand(inf),
  \dur, 1/2,
);
~a.play;
)

(
~a = Pmono(*[
  'acid', freq: [100, 150, 200, 400].pxrand(inf),
  dur: 1/2,
]);
~a.play;
)

(
a = Pmono(
  'acid', \note, [0, 2].pseq(inf),
  \dur, [1, 1/2].pxrand(inf),
  \release, 0.12
);
a.play;
)
