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
  	sig = sig * \amp.kr(-6).dbamp;
  	sig = Pan2.ar(sig * env, \pan.kr(0.0));
    OffsetOut.ar(out, sig)
  }).add;
  d.player = z;
);

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
  d.grainSampler = z;
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
  	sig = sig * \amp.kr(-6).dbamp;
  	sig = Pan2.ar(sig * env, \pan.kr(0.0));
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
        sig = sig * \amp.kr(-6).dbamp;
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
        sig = sig * \amp.kr(-6).dbamp;
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
      \fb.kr(0.5),
      1
    );
  	sig = sig * env;
  	OffsetOut.ar(out, Pan2.ar(sig, \pan.kr(0), \amp.kr(-6).dbamp));
  }).add;
  d.sinfb = z;
);

(
  z = SynthDef(\omi, {
    arg out;
    var envLength = \sustain.kr(1) * (\end.kr(1) - \begin.kr(0)) / \speed.kr(1);
    var line = Line.ar(\begin.kr, \end.kr, envLength, doneAction: Done.freeSelf);
    var env = Env.asr;
    var volume = IEnvGen.ar(env, line) * \amp.kr(-6).dbamp;
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
          timbre: \timbre.kr(0.5),
          harm: \harm.kr(0.5),
          engine: index,
          morph: \morph.kr(0.5),
          level: \level.kr(1),
          decay: \lpgdecay.kr(0),
          lpg_colour: \lpgcolour.kr(0),
        );
        sig = Pan2.ar(sig[0], \pan.kr(0));
        sig = sig * env * \amp.kr(-6).dbamp;
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
        timbre: \timbre.kr(0.5),
        color: \color.kr(0.5),
        resamp: 0,
        decim: \decim.kr(32),
        model: index,
        ws: \ws.kr(0),
        bits: \bits.kr(0)
      );
      OffsetOut.ar(out, Pan2.ar(sig * env * \amp.kr(-6).dbamp, \pan.kr(0)));
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
      Pan2.ar(sig * env * \amp.kr(-6).dbamp,
      \pan.kr(0)
    ));
  }).add;
  d.tides = z;
);

(
  z = SynthDef('kick', {
      arg out;
      var p0, p1, p, freq0, freq1, freqEnv, sig;
      p0 = 0.006699687;
      p1 = 0.00001884606;
      p = (1-\vsweep.kr(0.5)) * p0 + (\vsweep.kr * p1);
      freq1 = \freq.kr(100);
      freq0 = freq1 * \mul.kr(20);
      freqEnv = EnvGen.ar(Env([0,1], [1.0], [0]));
      freqEnv = freq1 + ((freq0-freq1)/(1.0 + (freqEnv/p)));
      sig = SinOsc.ar(freqEnv);
      sig = sig * EnvGen.ar(Env([1,1,0], [\hold.kr(0.25), \release.kr(0.5)], [0,0]), doneAction: Done.freeSelf) * \amp.kr(-6).dbamp;
      sig = Pan2.ar(sig, \pan.kr(0.0));
      OffsetOut.ar(out, sig);
  }).add;
  d.kick = z;
);


(
  z = SynthDef('nnkick', {
    arg out;
    var sig, env;
    env = Env.perc(\attack.kr(0.001), \release.kr(0.8)).ar(Done.freeSelf);
    sig = SinOsc.ar(\freq.kr(50) * (1 + (8 * Env.perc(0, 0.13, curve:-8).ar)));
    sig = sig + (SinOsc.ar(XLine.ar(\sweep.kr(8000), 100, 0.01)) * Env.perc(0.0001,0.01).ar * -8.dbamp);
    sig = sig +(BPF.ar(WhiteNoise.ar, \cutoff.kr(10120), 0.5)*Env.perc(0.0001, 0.03).ar * -8.dbamp);
    sig = sig.tanh;
    sig = sig * env * \amp.kr(-10).dbamp;
    OffsetOut.ar(out, Pan2.ar(sig, \pan.kr(0.0)));
  }).add;
  d.nnkick = z;
);


(
  ["lpf", "bpf", "hpf"].do({
	  arg synth_type, index;
	  z = SynthDef(("chip" ++ synth_type).asSymbol , {
	    arg out=0;
	    var vibrato = SinOsc.ar(
        \vibspeed.kr(2)).range(
          \vibdepth.kr(0).neg,
          \vibdepth.kr
      ) * \vibrato.kr(1);
	  var pitchEnv = XLine.ar(1/4, 1, \glide.kr(0.1) / 10);
	  var filtEnv = Env.perc(\fattack.kr(0.01), \frelease.kr(1.0)).kr(doneAction: 0);
	  var detune = \detune.kr(0);
	  var basePitch = pitchEnv * \freq.kr(400);
	  var sound = AY.ar(
      tonea: AY.freqtotone(basePitch + vibrato + detune),
      toneb: AY.freqtotone(basePitch * \ratio.kr(1) + vibrato - detune),
      tonec: AY.freqtotone(basePitch / \ratio.kr + vibrato + (detune/2)),
      noise: \noise.kr(0).linlin(0, 1, 0, 31),
      control: \control.kr(0.125).linlin(0, 1, 0, 63),
      vola: \vola.kr(15),
      volb: \volb.kr(15),
      volc: \volc.kr(15),
      envfreq: \envfreq.kr(4),
      envstyle: \envstyle.kr(1),
      chiptype: \type.kr(0)
	  );
	  var env = Env.asr(
      \attack.kr(0.01),
		  \sustain.kr(0),
		  \release.kr(0.125/2)
	  );
	  env = EnvGen.ar(
      env,
      gate: \gate.kr(1.0),
      doneAction: 2
    );
	  sound = VadimFilter.ar(
		  sound,
		  freq: \cutoff.kr(10000) * filtEnv,
		  resonance: \resonance.kr(0.5),
		  type: index
	  );
	  sound = sound * env * \amp.kr(1.0);
	  Out.ar(out, Pan2.ar(sound ! 2, \pan.kr(0.0)));
  }).add;
  d[z.name] = z;
});
);

z = nil; // We don't need that variable anymore
