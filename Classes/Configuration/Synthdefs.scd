d = (); // This is a storage area for synthesizers
f = (); // This is a storage for various FX functions

(
d.params = { arg obj, name; obj[name].allControlNames.do({arg i; i.postln;}); };
d.list = { arg obj; obj.keys.do({arg i; i.postln}); };
);

f.vardel = {
  arg in;
  HPF.ar(CombC.ar(in, 2, c.beatDur / [1, 2], 2),
    SinOsc.ar(c.beatDur * 4).range(500, 2000));
};

/*
* This is the sampler used for 90% of sampling duties.
*/
(
  z = SynthDef.new(\player, {
  	arg buf;
  	var sig, env = EnvGen.ar(
    Env.perc(
      \attack.kr(0.01),
      \release.kr(1),
      doneAction: 2)
    );
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
    OffsetOut.ar(\out.kr(0), sig)
  }).add;
  d.player = z;
);


/*
* Stereo variant
*/
(
  z = SynthDef.new(\splayer, {
  	arg buf;
  	var sig, env = EnvGen.ar(
    Env.perc(
      \attack.kr(0.01),
      \release.kr(1),
      doneAction: 2)
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
    OffsetOut.ar(\out.kr(0), sig)
  }).add;
  d.splayer = z;
);

(
  z = SynthDef(\sinfb, {
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
  	OffsetOut.ar(\out.kr(0), Pan2.ar(sig, \pan.kr(0), \amp.kr(-6.dbamp)));
  }).add;
  d.sinfb = z;
);

(
  z = SynthDef(\omi, {
    var envLength = \sustain.kr(1) * (\end.kr(1) - \begin.kr(0)) / \speed.kr(1);
    var line = Line.ar(\begin.kr, \end.kr, envLength, doneAction: Done.freeSelf);
    var env = Env.asr;
    var volume = IEnvGen.ar(env, line) * \amp.kr(-6.dbamp);
    var sig;
    sig = MiOmi.ar(pit: \freq.kr(400).cpsmidi);
    OffsetOut.ar(\out.kr(0), Pan2.ar(sig * volume, \pan.kr(0)));
  }).add;
  d.omi = z;
);


(
  [
    'Panalog', 'Pwaveshape', 'Pfm', 'Pgrain',
    'Padditive', 'Pwavetable', 'Pchord', 'Pspeech',
    'Pswarm', 'Pnoise', 'Pparticle', 'Pstring',
    'Pmodal', 'Pbass', 'Psnare', 'Phat'
  ].do({arg name, index;
      var synth;
      synth = SynthDef(name, {
        var env = EnvGen.ar(
          Env.perc(
            \attack.kr(0.01),
            \release.kr(1)
          ), doneAction: Done.freeSelf
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
        OffsetOut.ar(\out.kr(0), sig);
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
      OffsetOut.ar(\out.kr(0), Pan2.ar(sig * env * \amp.kr(-6.dbamp), \pan.kr(0)));
    }).add;
    d.put(name, synth);
  });
);

(
  z = SynthDef(\tides, {
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
    OffsetOut.ar(\out.kr(0), Pan2.ar(sig * env * \amp.kr(-6.dbamp), \pan.kr(0)));
  }).add;
  d.tides = z;
);

z = nil; // We don't need that variable anymore
