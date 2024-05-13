// == BACKLOG PERFORMANCE SAINT NAZAIRE ==
// TODO: ajouter un truc pour tuer tout le MIDI quand j'appuie sur F12
// TODO: simplifier l'émission de control change (c'est un type d'event)
// TODO: pourquoi le MIDI se comporte si bizarrement ? ('uid' not understood)

// BUG: Evalue moi
m = MIDIOut.newByName("MIDI", "Bus 1");

// NOTE: Pattern rythmique de base
(
~test => [ sp: "kick", nb: 0];
~test.play;
)

// NOTE: Pattern avec Pmini
(
~test => [ pat: "kick:4 snare:3" ];
~test.play;
)

// NOTE: Pattern sans numéro maintenant !
(
~test => [ pat: "[kick hat snare hat]/2" ];
~test.play;
)

Bank.list

Sweep.help

(
~rhythm => [
  pat: "[kick:11(3,8)/2, hat:4(5,8)/2, fsnare:2(1,8)/4]",
  rate: [1, 0.5], release: 0.5,
];
~rhythm.fx1(0.1, {
  arg in;
  var sound = CombC.ar(in, 2, c.dur / 4, LFNoise0.kr(c.dur * 2).range(0.01, 1));
  sound = MiVerb.ar(sound, time: 0.1);
  sound
});
~rhythm.play;
)

~basse => [
  pat: "0(5,8)/2",
  i: "kraut",
  pan: {SinOsc.ar(c.dur / 4).range(-0.5, 0.5)},
  harmonics: {
    Sweep.kr(
      Impulse.kr(c.dur*2),
      rate: 0.5
    ).linlin(0,1,0,2,\minmax)},
  tune: [1, 2], amp: -2, release: 1, octave: 3,
];
~basse.fx1(0.5, {
  arg in;
  MiVerb.ar(in, time:0.3)
});
~basse.play;
~plop => [
  pat: "[sound:4 4modular:5 sound:6 4modular:8]/4",
  pan: {SinOsc.ar(c.dur/4)},
  rate: {
    Sweep.ar(c.dur / 4, 1).linlin(0, 4, 0, 1, \minmax)
  }, release: [1, 2, 3, 1].pseq(inf),
];
~plop.mold(2);
~plop.fx1(0.5, {arg in; MiVerb.ar(in, 0.2)});
~plop.play;
)

// NOTE: changer amp de place cause une erreur (wtf?)
(
m = MIDIOut.newByName("MIDI", "Bus 1");
~test >> [
  pat: "[[0 2 3 7 0 3 5 7 0 5 8 12 0 1 2 7]/4, [-7 -0]]/2",
  chan: 0,
  velocity: 10,
  midiout: m
];
~test.play;
)

"uid".help

// NOTE: reproduction avec un pattern conventionnel
(
m = MIDIOut.newByName("MIDI", "Bus 1");
~test = Pbind(
  \type, 'midi',
  \degree, [0,2,3,4].pseq(inf),
  \amp, Pwhite(0.0, 1.0, inf),
  \midiout, m
);
)

// NOTE: MIDI très fancy !
(
m = MIDIOut.newByName("MIDI", "Bus 1");
~test >> [
  pat: "[0 ~ 2 3 4]/2", midiout: m,
  chan: 0, amp: [0.5, 0.7].pwhite(inf),
  bob: {SinOsc.ar(2).range(1,120)},
  control: 20,
  da: Pfunc {
    |e|
    m.control(0, e.control, e.bob.asInteger)
  }
];
~test.play;
)

~test.source.patternpairs


m.control(0, 20, 40)


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

~a = [

];
~a.play;


(
~ground => [pat: "[kick:2(5,16)/16, fgood:11/2]", release: 4];
~snare => [pat: "[~ snare]/16", release: 4];
~snare.fx1(0.7, {arg in; MiVerb.ar(in, time: 0.7)});
~hat => [pat: "[sound:2|hat:5] hat:2 hat:3!2", release: 1/32,
  amp: [0.0,-12.0].pwhite(inf)
];
~hat.play;
~snare.play;
~ground.play;
~vorb => [
  instrument: 'kraut',
  octave: 4,
  amp: -12, curve: -2, tune: [0, [2, 4, 8].pseq(inf)],
  release: {LFNoise0.kr(c.dur).range(0.1, 1)} * 4,
  scale: Scale.chromatic,
  modSpeed: 8, pat: "[0(5,8) 3(5,8)]/8",
];
~vorb.play;
~apply => [
  instrument: 'kraut',
  octave: [5, 6].pxrand(inf),
  amp: -12, curve: -2, tune: 2,
  release: {LFNoise0.kr(c.dur).range(0.1, 1)},
  scale: Scale.chromatic, pan: 0.0,
  modSpeed: 8, pat: "[[0 [3|~] 7 10]/2 [0 <[2 5 7 3] 3 [2 5] [3 12]> 7 10]/2]/2",
];
~apply.fx1(0.5, {
  arg in;
  MiVerb.ar(in, time: 0.8)
});
~apply.play;
)

nil.isNil

