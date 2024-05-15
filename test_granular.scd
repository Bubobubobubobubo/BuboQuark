Boot(
  samplePath: "/Users/bubo/.config/livecoding/samples"
)

(
~test +=> [
  sp: "casio", nb: [0, 2, 4].pseq(inf), amp: 1,
  grain: {SinOsc.ar(1/4).range(1,20)},
  pos: {LFNoise2.kr(1/8).range(0, 0.25)},
  rate: Pwhite(1, 2, inf)
];
~test.play;
~test.fx(100, 0.5, {
  arg in; MiVerb.ar(in, time: 0.5);
})
)

~test ++> [];


/*
* Granular Sampler
*/
(
  z = SynthDef('grainSampler', {
    arg out, buf;
    var sound = GrainBuf.ar(
      numChannels: buf.numChannels,
      trigger: Impulse.kr(\grain.kr(4)),
      dur: 1, sndbuf: buf,
      rate: \rate.kr(4),
      pos: \pos.kr(0.0),
      interp: 2,
      pan: \pan.kr(0.0),
      envbufnum: \env.kr(-1),
    );
    sound = sound * Env.perc(
      \attack.kr(0.1), \release.kr(0.5)
    ).kr(2);
    sound = sound * \amp.kr(-6).dbamp;
    OffsetOut.ar(out, sound);
  }).add;
  d.grainPlayer = z;
)
