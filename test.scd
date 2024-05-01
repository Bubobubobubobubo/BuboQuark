Boot()

c.tempo = 1.5

// Gestion manuelle avec une sorte de console virtuelle

(
~kick => [i: "splayer", nb: 0, sp: "kick", db:0];
~snare => [i: "splayer", nb: 0, sp: "snare", db:0, dur: 2];
~hat => [i: "splayer",
  nb: 0,
  sp: "hat",
  release: 0.1, db:0,
  dur: [0.25, 0.25, 0.25, 0.125, 0.125].pseq(inf)
];
)

(
~master = {
  var snare = MiVerb.ar(CombC.ar(~snare.ar(2), 2, c.dur / 3, 1.5), time: 0.5);
  var hat = ~hat.ar(2);
  var kick = MiVerb.ar(~kick.ar(2), time: 0.2);
  [snare, kick, hat].sum
};
~master.fadeTime = 4
)
~master.play;

~master.clear(8)

// L'idée ici doit être de pouvoir jouer avec un step sequencer
// Le son d'une routine dans un autre proxy et traitement (comment ?)

// ... ProxySpace.push(s.boot)
(
~step = r {
  /*
  * How can I output these events on the NodeProxy Output?
  */
  loop {
    (instrument: "Pwaveshape",
     scale: Scale.minorPentatonic,
     release: 1, harm: 0.25,
     degree: 8.rand);
    (1/4).wait;
    (instrument: "Panalog",
      scale: Scale.minorPentatonic,
      release: 1, harm: 1.rand,
      degree: 8.rand + 12);
    (instrument: "Panalog",
      scale: Scale.minorPentatonic,
      release: 1, harm: 1.rand,
      degree: 8.rand - 24);
    (1/4).wait;
  }
};
)

(
~master = {
  /*
  * I would like to add reverb to ~step here.
  */
  MiVerb.ar(~step.ar(2), time: 0.9)
}
)

~master.play;


Tdef.help

Boot()


(
SynthDef(\lplay,
    {arg out, buf = 0, amp = 0.5, pan = 0, rel=15, dur = 8;
        var sig,env ;
        sig = Mix.ar(PlayBuf.ar(2,buf,BufRateScale.ir(buf) * ((BufFrames.ir(buf)/s.sampleRate)*c.tempo/dur),1,0,doneAction:2));
        env = EnvGen.ar(Env.linen(0.0,rel,0),doneAction:2);
        sig = sig * env;
        sig = sig * amp;
        Out.ar(out,Pan2.ar(sig,pan));
}).add;
)


(
~test2 => [
  i: "lplay", nb: [0,10].pwhite,
  sp: "amen1", dur: 1, rel: 32];
~test2.play;
)

(
~test => [i: "splayer",
  nb: 0,
  sp: "amen1",
  dur: 16];
~test.play;
)

// Debugging stuff

Boot()

(
~step = r {
  loop {
    (out: ~step.bus,
     instrument: "Panalog",
     release: 1, harm: [0, 1].pwhite,
     degree: 8.rand).play;
    (1/4).wait;
    (out: ~step.bus,
     instrument: "Panalog",
     harm: [0, 1.0].pwhite,
     release: 1, degree: 8.rand + 12).play;
    (1/4).wait;
  }
};
~step.mold(2);
)

~step.scope // WARNING: Can't scope unintitialized bus

"awake".help

// J'étudie comment fonctionne spawn et awake

Boot()

(
~bleep = {
  var sig = MiPlaits.ar(
    \freq.kr(300).cpsmidi,
    engine: LFNoise0.kr(c.dur * 2).range(0,8),
    harm: 0.5, morph: 0.5, timbre: 0.5
  ) * Env.perc(
    \attack.kr(0.01),
    \release.kr(0.5)
  ).kr(doneAction: 2);
  sig = RLPF.ar(sig, LFNoise2.kr(c.dur).range(200,1200), 0.3);
  Safe.ar(sig ! 2 * 0.5)
};
~bleep.awake_(false);
~bleep.play;
)

(
~bleepverb = {
  Limiter.ar(MiVerb.ar(~bleep.ar, time: 0.5))
};
~bleepverb.play;
)


~d = Tdef(\d, {
  loop {
    ~bleep.spawn([
      freq: [50, 100, 200, 400].choose / [2, 1].choose,
      fb: 1.rand, release: [1, 2, 0.25][c.beats % 4]]);
    [0.5, 1/4, 1/2].choose.wait;
    ~bleep.spawn([
      freq: 2 / [50, 100, 200, 400].choose / [2, 1].choose,
      fb: 1.rand, release: 2]);
    [0.5, 1/4, 1/2].choose.wait;
  }
});

