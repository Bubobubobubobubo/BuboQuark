d = (); // This is a storage area for synthesizers
f = (); // This is a storage for various FX functions

(
d.params = { arg obj, name; obj[name].allControlNames.do({arg i; i.postln;}); };
d.list = { arg obj; obj.keys.do({arg i; i.postln}); };
);

(
  z = SynthDef.new(\player, {
  	arg buf, out;
  	var sig, env = EnvGen.ar(Env.perc(\attack.kr(0.01), \release.kr(1)), doneAction: 2);
    var startPos = \begin.kr(0) * BufFrames.kr(buf);
    var endPos = \end.kr(1) * BufFrames.kr(buf); // TODO: unused
    sig = PlayBuf.ar(
      numChannels: 1,
      bufnum: buf,
      rate:BufRateScale.kr(buf) * (\rate.kr(1) * \freq.kr(400) / 60.midicps),
      trigger: 1, startPos:startPos,
      loop:\loop.kr(0), doneAction: 2);
  	sig = sig * \amp.kr(-6.dbamp);
  	sig = Pan2.ar(sig * env, \pan.kr(0));
    OffsetOut.ar(out, sig)
  }).add;
  d.player = z;
);


/*
* Stereo variant
*/
(
  z = SynthDef.new(\splayer, {
  	arg buf, out;
  	var sig, env = EnvGen.ar(
      Env.perc(
        \attack.kr(0.01),
        \release.kr(1)),
        doneAction: 2
    );
    var startPos = \begin.kr(0) * BufFrames.kr(buf);
    var endPos = \end.kr(1) * BufFrames.kr(buf); // TODO: unused
    sig = PlayBuf.ar(
      numChannels: 2,
      bufnum: buf,
      rate:BufRateScale.kr(buf) * (\rate.kr(1) * \freq.kr(400) / 60.midicps),
      trigger: 1, startPos:startPos,
      loop:\loop.kr(0), doneAction: 2);
  	sig = sig * \amp.kr(-6.dbamp);
  	sig = Pan2.ar(sig * env, \pan.kr(0));
    OffsetOut.ar(out, sig)
  }).add;
  d.splayer = z;
);


(
  /* Crafted with some help from Bruno Gola */
  z = SynthDef(\looperMono,
      {
        arg out;
        var sig, env, index;
        index = Select.kr(\direction.kr(1) > 0, [\index.kr(0) + 1, \index.kr]);
        sig = PlayBuf.ar(
          1,
          \buf.kr(0),
          (BufRateScale.kr(\buf.kr) * (BufSamples.kr(\buf.kr)
          / \slices.kr(1) / BufSampleRate.kr(\buf.kr)) / \time.kr * \direction.kr) / 2,
          1, BufSamples.kr(\buf.kr) * (index / \slices.kr), doneAction: 0
        );
        env = EnvGen.ar(
          Env.asr(0.01, 1, 0.01), \gate.kr(1), doneAction: 2
        );
        sig = sig * env;
        sig = sig * \amp.kr(-6.dbamp);
        OffsetOut.ar(out,Pan2.ar(sig,\pan.kr(0)));
  }).add;
  d.looperMono = z;
);

(
  /* Crafted with some help from Bruno Gola */
  z = SynthDef(\looperStereo,
      {
        arg out;
        var sig, env, index;
        index = Select.kr(\direction.kr(1) > 0, [\index.kr(0) + 1, \index.kr]);
        sig = PlayBuf.ar(
          2,
          \buf.kr(0),
          (BufRateScale.kr(\buf.kr) * (BufSamples.kr(\buf.kr)
          / \slices.kr(1) / BufSampleRate.kr(\buf.kr)) / \time.kr * \direction.kr) / 2,
          1, BufSamples.kr(\buf.kr) * (index / \slices.kr), doneAction: 0
        );
        env = EnvGen.ar(
          Env.asr(0.01, 1, 0.01), \gate.kr(1), doneAction: 2
        );
        sig = sig * env;
        sig = sig * \amp.kr(-6.dbamp);
        OffsetOut.ar(out,Pan2.ar(sig,\pan.kr(0)));
  }).add;
  d.looperStereo = z;
);

(
  z = SynthDef(\sinfb, {
    arg out;
  	var sig, env;
  	env = EnvGen.ar(
      Env.linen(
        \attack.kr(0.01),
        \sustain.kr(0),
        \release.kr(1)
    ),1,1,0,1,2);
  	sig = SinOscFB.ar(
      \freq.kr(400),
      \fb.kr(0),
      1
    );
  	sig = sig * env;
  	OffsetOut.ar(out, Pan2.ar(sig, \pan.kr(0), \amp.kr(-6.dbamp)));
  }).add;
  d.sinfb = z;
);

(
  z = SynthDef(\omi, {
    arg out;
    var envLength = \sustain.kr(1) * (\end.kr(1) - \begin.kr(0)) / \speed.kr(1);
    var line = Line.ar(\begin.kr, \end.kr, envLength, doneAction: Done.freeSelf);
    var env = Env.asr;
    var volume = IEnvGen.ar(env, line) * \amp.kr(-6.dbamp);
    var sig;
    sig = MiOmi.ar(pit: \freq.kr(400).cpsmidi);
    OffsetOut.ar(out, Pan2.ar(sig * volume, \pan.kr(0)));
  }).add;
  d.omi = z;
);


(
  [
    'Panalog', 'Pwaveshape', 'Pfm', 'Pgrain',
    'Padditive', 'Pwavetable', 'Pchord', 'Pspeech',
    'Pswarm', 'Pnoise', 'Pparticle', 'Pstring',
    'Pmodal', 'Pbass', 'Psnare', 'Phat'
  ].do({
      arg name, index;
      var synth;
      synth = SynthDef(name, {
        arg out;
        var env = EnvGen.ar(
          Env.perc(
            \attack.kr(0.01),
            \release.kr(1)
          ),
          doneAction: Done.freeSelf
        );
        var sig;
        sig = MiPlaits.ar(
          pitch: \freq.kr(400).cpsmidi,
          timbre: \timbre.kr(0),
          harm: \harm.kr(0),
          engine: index,
          morph: \morph.kr(0),
          level: \level.kr(1),
          decay: \lpgdecay.kr(0),
          lpg_colour: \lpgcolour.kr(0),
        );
        sig = Pan2.ar(sig[0], \pan.kr(0));
        sig = sig * env * \amp.kr(-6.dbamp);
        OffsetOut.ar(out, sig);
      }).add;
      d.put(name, synth);
  });
);


(
  [
    'Bcsaw',
    'Bmorph',
    'Bsaw_square',
    'Bsine_triangle',
    'Bbuzz',
    'Bsquare_sub',
    'Bsaw_sub',
    'Bsquare_sync',
    'Bsaw_sync',
    'Bt_saw',
    'Bt_square',
    'Bt_triangle',
    'Bt_sine',
    'Bt_ring_mod',
    'Bsaw_swarm',
    'Bsaw_comb',
    'Btoy',
    'Bfilter_lp',
    'Bfilter_pk',
    'Bfilter_bp',
    'Bfilter_hp',
    'Bvosim',
    'Bvowel',
    'Bvowel_fof',
    'Bharmonics',
    'Bbfm',
    'Bfeedback_fm',
    'Bchaotic_feedback_fm',
    'Bplucked',
    'Bbowed',
    'Bblown',
    'Bfluted',
    'Bstruck_bell',
    'Bstruck_drum',
    'Bbkick',
    'Bcymbal',
    'Bbsnare',
    'Bbwavetable',
    'Bwave_map',
    'Bwave_line',
    'Bwave_paraphonic',
    'Bfiltered_noise',
    'Btwin_peaks_noise',
    'Bclocked_noise',
    'Bgranular_cloud',
    'Bparticle_noise',
    'Bdigital_modulation',
    'Bquestion_mark'].do({
    arg name, index;
    var synth;
    synth = SynthDef(name, {
      arg out;
      var sig;
      var env = EnvGen.ar(Env.perc(\attack.kr(0.01), \release.kr(1)), doneAction: Done.freeSelf);
      var freq = \freq.kr;
      sig = MiBraids.ar(
        pitch: freq.cpsmidi,
        timbre: \timbre.kr(0),
        color: \color.kr(0),
        resamp: 0,
        decim: \decim.kr(32),
        model: index,
        ws: \ws.kr(0),
        bits: \bits.kr(0)
      );
      OffsetOut.ar(out, Pan2.ar(sig * env * \amp.kr(-6.dbamp), \pan.kr(0)));
    }).add;
    d.put(name, synth);
  });
);

(
  z = SynthDef(\tides, {
    arg out;
    var env = Env.perc(\attack.kr(0.01), \release.kr(1)).ar(2);
    var sig = MiTides.ar(
      freq: \freq.kr(400),
      shape: \shape.kr(0),
      slope: \slope.kr(0),
      smooth: \smooth.kr(0),
      shift: \shift.kr(0),
      output_mode: \mode.kr(2),
      ramp_mode: 1,
      rate: 1
    );
    OffsetOut.ar(out,
      Pan2.ar(sig * env * \amp.kr(-6.dbamp),
      \pan.kr(0)
    ));
  }).add;
  d.tides = z;
);

(
  z = SynthDef('pink', {
    arg out;
    var pink = PinkTrombone.ar(
      noiseSource: BPF.ar(WhiteNoise.ar(), \noiseFilter.kr(2000)),
      freq: \freq.kr(800),
      tenseness: \tenseness.kr(0.4),
      tongueIndex: \tongueIndex.kr(30),
      tongueDiameter: \tongueDiameter.kr(3.5),
      constrictionX: \constrictionX.kr(1.5),
      constrictionY: \constrictionY.kr(2.5),
      fricativeIntens: \fricativeIntens.kr(1.5)
    );
    var env = Env.perc(\attack.kr(0.01), releaseTime: \release.kr(2.0)).kr(doneAction: 2);
    var sound = pink * env;
    OffsetOut.ar(out, Pan2.ar(sound, pos: \pan.kr(0.0)))
  }).add;
  d.pink = z;
);


(
  z = SynthDef('kick', {
      arg out, freq, mul=512, vsweep=0.5, hold=0.25, release=0.25, amp=0.5, pan=0;
      var p0, p1, p, freq0, freq1, freqEnv, sig;
      p0 = 0.006699687;
      p1 = 0.00001884606;
      p = (1-vsweep)*p0 + (vsweep*p1);
      freq1 = freq;
      freq0 = freq1 * mul;
      freqEnv = EnvGen.ar(Env([0,1], [1.0], [0]));
      freqEnv = freq1 + ((freq0-freq1)/(1.0 + (freqEnv/p)));
      sig = SinOsc.ar(freqEnv);
      sig = sig * EnvGen.ar(Env([1,1,0], [hold,release], [0,0]), doneAction: Done.freeSelf) * amp;
      sig = Pan2.ar(sig, pan);
      OffsetOut.ar(out, sig);
  }).add;
  d.kick = z;
);




(
  z = SynthDef('kraut', {
    /*
    * time: frequency sweep time
    * tune: frequency division
    * harmonics: nb of harmonics
    * curve: distortion
    * modSpeed: frequency bias
    */
    arg out;
    var sweepFreq = XLine.ar(\freq.kr(400) * 1.99, \freq.kr, \attack.kr(0.05) / \time.kr(2));
    var frequencyBias = SinOsc.ar(\modSpeed.kr(1)).range(0.90, 1);
    var sin = SinOscFB.ar([
      sweepFreq * \tune.kr(1) * frequencyBias,
      sweepFreq / \tune.kr * frequencyBias],
      feedback: \harmonics.kr(0.25));
    var env = Env.perc(\attack.kr, \release.kr(0.5)).ar(doneAction: 2);
    var sound = Safe.ar(sin.lincurve(-1, 1, -1, 1, \curve.kr(4)));
    OffsetOut.ar(out, DirtPan.ar(sound, ~dirt.numChannels, \pan.kr(0), env))
  }).add;
  d.kraut = z;
);

z = nil; // We don't need that variable anymore
