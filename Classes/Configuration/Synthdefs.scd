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

(
  z = SynthDef.new(\s, {
  	arg buf, out=0, freq=440, rate=1, amp=1, begin=0, end=1, pan=0, attack=0.01, release=1, loop=0;
  	var sig;
    var env = EnvGen.ar(Env.perc(attack, release, doneAction: 2));
    var startPos = begin * BufFrames.kr(buf);
    var endPos = end * BufFrames.kr(buf); // TODO: unused
    sig = PlayBuf.ar(
      numChannels: 1,
      bufnum: buf,
      rate:BufRateScale.kr(buf) * (rate * freq / 60.midicps),
      trigger: 1, startPos:startPos,
      loop:loop, doneAction: 2);
  	sig = sig * amp;
  	sig = Pan2.ar(sig * env, pan);
    OffsetOut.ar(out, sig)
  }).add;
  d.s = z;
);

(
  z = SynthDef(\sinfb, {
  	arg freq = 440, atk = 0.01, sus = 0, rel = 1, fb = 0, amp = 0.3, out = 0, pan=0;
  	var sig, env;
  	env = EnvGen.ar(Env.linen(atk,sus,rel),1,1,0,1,2);
  	sig = SinOscFB.ar(freq,fb,1);
  	sig = sig*env;
  	Out.ar(out,Pan2.ar(sig,pan,amp));
  }).add;
  d.sinfb = z;
);

(
  z = SynthDef(\kick2, {
      var snd;
      snd = DC.ar(0);
      snd = snd + (HPF.ar(Hasher.ar(Sweep.ar), 1320) * Env.perc(0.003, 0.03).ar * 0.5);
      snd = snd + (SinOsc.ar(XLine.ar(750, 161, 0.02)) * Env.perc(0.0005, 0.02).ar);
      snd = snd + (SinOsc.ar(XLine.ar(167, 52, 0.04)) * Env.perc(0.0005, 0.3).ar(2));
      snd = snd.tanh;
      Out.ar(\out.kr(0), Pan2.ar(snd, \pan.kr(0), \amp.kr(0.9)));
  }).add;
  d.kick2 = z;
);

(
  z = SynthDef(\kick3, {
      var snd;
      snd = DC.ar(0);
      snd = snd + (SinOsc.ar(XLine.ar(1500, 800, 0.01)) * Env.perc(0.0005, 0.01, curve: \lin).ar);
      snd = snd + (BPF.ar(Impulse.ar(0) * SampleRate.ir / 48000, 6100, 1.0) * 3.dbamp);
      snd = snd + (BPF.ar(Hasher.ar(Sweep.ar), 300, 0.9) * Env.perc(0.001, 0.02).ar);
      snd = snd + (SinOsc.ar(XLine.ar(472, 60, 0.045)) * Env.perc(0.0001, 0.3, curve: \lin).delay(0.005).ar(2));
      snd = snd.tanh;
      Out.ar(\out.kr(0), Pan2.ar(snd, \pan.kr(0), \amp.kr(0.1)));
  }).add;
  d.kick3 = z;
);


(
  z = SynthDef("snare", {arg amp = 0.1, freq = 100, att = 0.01, rel = 0.2, ffreq = 2000, pan = 0;
  	var env, snd1, snd2, sum;
  	env = Env.perc(att, rel, amp).kr;
  	snd1 = HPF.ar(
  		in: WhiteNoise.ar,
  		freq: ffreq
  	);
  	snd2 = SinOsc.ar(freq: freq);
  	sum = snd1 + snd2;
  	sum = sum * env;
  	Out.ar(0, Pan2.ar(sum, pan));
  	DetectSilence.ar(sum, doneAction: 2);
  }).add;
  d.snare = z;
);


(
  z = SynthDef("hihat", {arg amp = 0.5, att = 0.01, rel = 0.2, ffreq = 6000, pan = 0;
  	var env, snd;
  	env = Env.perc(
  		attackTime: att,
  		releaseTime: rel,
  		level: amp*1.2
  	).kr;
  	snd = WhiteNoise.ar;
  	snd = HPF.ar(in: snd, freq: ffreq);
  	snd = snd * env;
  	Out.ar(0, Pan2.ar(snd, pan));
  	DetectSilence.ar(snd, doneAction: 2);
  }).add;
  d.hihat = z;
);

(
  z = SynthDef(\kick1, {
      var snd;
      snd = DC.ar(0);
      snd = snd + (SinOsc.ar(XLine.ar(800, 400, 0.01)) * Env.perc(0.0005, 0.01).ar);
      snd = snd + (BPF.ar(Hasher.ar(Sweep.ar), XLine.ar(800, 100, 0.01), 0.6) * Env.perc(0.001, 0.02).delay(0.001).ar);
      snd = snd + (SinOsc.ar(XLine.ar(172, 50, 0.01)) * Env.perc(0.0001, 0.3, 1, \lin).delay(0.005).ar(2));
      snd = snd.tanh;
      Out.ar(\out.kr(0), Pan2.ar(snd, \pan.kr(0), \amp.kr(0.1)));
  }).add;
  d.kick1 = z;
);

(
  z = SynthDef(\braids, {|out=0,freq=440,amp=0.5,sustain=1,pan=0,begin=0,end=1,speed=1,accelerate=0,timbre=0.5,color=0.5,model=0|
    var envLength = sustain*(end-begin)/speed;
    var line = Line.ar(begin, end, envLength, doneAction: Done.freeSelf);
    var env = Env.asr;
    var volume = IEnvGen.ar(env, line) * amp;
    var sig;

    freq = max(0, freq * speed * (1 + (accelerate * line)));
    sig = MiBraids.ar(pitch: freq.cpsmidi, timbre: timbre, color: color, model: model);
    Out.ar(out, Pan2.ar(sig * volume, pan));
  }).add;
  d.braids = z;
);

(
  z = SynthDef(\omi, {|out=0,freq=440,amp=0.5,sustain=1,pan=0,begin=0,end=1,speed=1,accelerate=0|
    var envLength = sustain*(end-begin)/speed;
    var line = Line.ar(begin, end, envLength, doneAction: Done.freeSelf);
    var env = Env.asr;
    var volume = IEnvGen.ar(env, line) * amp;
    var sig;

    freq = max(0, freq * speed * (1 + (accelerate * line)));
    sig = MiOmi.ar(pit: freq.cpsmidi);
    Out.ar(out, Pan2.ar(sig * volume, pan));
  }).add;
  d.omi = z;
);


(
  [
    'analog', 'waveshape',
    'fm', 'grain',
    'additive', 'wavetable',
    'chord', 'speech',
    'swarm', 'noise',
    'particle', 'string',
    'modal', 'bass',
    'snare', 'hat'
  ].do({arg name, index;
      var synth;
      synth = SynthDef(name, {|out=0,freq=440,sustain=1,pan=0,begin=0,end=1,speed=1,accelerate=0,
          amp=0.5,timbre=0.5,harm=0.5,morph=0.5,level=1,lpgdecay=0,lpgcolour=0,mode=0|
        var envLength = sustain*(end-begin)/speed;
        var line = Line.ar(begin, end, envLength, doneAction: Done.freeSelf);
        var env = Env.asr;
        var volume = IEnvGen.ar(env, line) * amp;
        var sig;
        freq = max(0, freq * speed * (1 + (accelerate * line)));
        sig = MiPlaits.ar(
          pitch: freq.cpsmidi,
          timbre: timbre,
          harm: harm,
          engine: index,
          morph: morph,
          level: level,
          decay: lpgdecay,
          lpg_colour: lpgcolour,
        );
        sig = Select.ar(mode, sig);
        Out.ar(out, Pan2.ar(sig * volume, pan));
      }).add;
      d.put(name, synth);
  });
);

(
  ['csaw', 'morph', 'saw_square', 'sine_triangle',
  'buzz', 'square_sub', 'saw_sub', 'square_sync',
  'saw_sync', 'triple_saw', 'triple_square',
  'triple_triangle', 'triple_sine', 'triple_ring_mod',
  'saw_swarm', 'saw_comb', 'toy', 'filter_lp',
  'filter_pk', 'filter_bp', 'filter_hp', 'vosim',
  'vowel', 'vowel_fof', 'harmonics', 'bfm', 'feedback_fm',
  'chaotic_feedback_fm', 'plucked', 'bowed', 'blown', 'fluted',
  'struck_bell', 'struck_drum', 'bkick', 'cymbal', 'bsnare',
  'bwavetable', 'wave_map', 'wave_line', 'wave_paraphonic',
  'filtered_noise', 'twin_peaks_noise', 'clocked_noise',
  'granular_cloud', 'particle_noise', 'digital_modulation',
  'question_mark'].do({
    arg name, index;
    var synth;
    synth = SynthDef(name, {|out=0,freq=440,amp=0.5,sustain=1,pan=0,begin=0,end=1,speed=1,accelerate=0,timbre=0.5,color=0.5,ws=0,bits=0, resamp=0, decim=32|
      var envLength = sustain*(end-begin)/speed;
      var line = Line.ar(begin, end, envLength, doneAction: Done.freeSelf);
      var env = Env.asr;
      var volume = IEnvGen.ar(env, line) * amp;
      var sig;

      freq = max(0, freq * speed * (1 + (accelerate * line)));
      sig = MiBraids.ar(pitch: freq.cpsmidi, timbre: timbre, resamp: 0, decim: decim, color: color, model: index, ws: ws, bits:bits);
      Out.ar(out, Pan2.ar(sig * volume, pan));
    }).add;
    d.put(name, synth);
  });
);

(
  z = SynthDef(\plaits, {|out=0,freq=440,sustain=1,pan=0,begin=0,end=1,speed=1,accelerate=0,
      amp=0.5,timbre=0.5,engine=0,harm=0.5,morph=0.5,level=1,lpgdecay=0,lpgcolour=0,mode=0|
    var envLength = sustain*(end-begin)/speed;
    var line = Line.ar(begin, end, envLength, doneAction: Done.freeSelf);
    var env = Env.asr;
    var volume = IEnvGen.ar(env, line) * amp;
    var sig;
    freq = max(0, freq * speed * (1 + (accelerate * line)));
    sig = MiPlaits.ar(
      pitch: freq.cpsmidi,
      timbre: timbre,
      harm: harm,
      engine: engine,
      morph: morph,
      level: level,
      decay: lpgdecay,
      lpg_colour: lpgcolour,
    );
    sig = Select.ar(mode, sig);
    Out.ar(out, Pan2.ar(sig * volume, pan));
  }).add;
  d.plaits = z;
);

(
  z = SynthDef(\tides, {|out=0,freq=440,amp=0.5,sustain=1,pan=0,begin=0,end=1,speed=1,accelerate=0,tidesshape=0.5,slope=0.5,tidessmooth=0.5,shift=0.5,mode=2|
    var envLength = sustain*(end-begin)/speed;
    var line = Line.ar(begin, end, envLength, doneAction: Done.freeSelf);
    var env = Env.asr;
    var volume = IEnvGen.ar(env, line) * amp;
    var sig;

    freq = max(0, freq * speed * (1 + (accelerate * line)));
    sig = MiTides.ar(
      freq: freq,
      shape: tidesshape,
      slope: slope,
      smooth: tidessmooth,
      shift: shift,
      output_mode: mode,
      ramp_mode: 1,
      rate: 1
    );
    Out.ar(out, Pan2.ar(sig * volume, pan));
  }).add;
  d.tides = z;
);

z = nil; // We don't need that variable anymore
