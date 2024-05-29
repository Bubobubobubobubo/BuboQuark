+ NodeProxy {

    fx1 { arg wet=0.5, function; this.fx(100, wet, function); }
    fx2 { arg wet=0.5, function; this.fx(200, wet, function); }
    fx3 { arg wet=0.5, function; this.fx(300, wet, function); }
    fx4 { arg wet=0.5, function; this.fx(400, wet, function); }
    fx5 { arg wet=0.5, function; this.fx(500, wet, function); }
    fx6 { arg wet=0.5, function; this.fx(600, wet, function); }
    fx7 { arg wet=0.5, function; this.fx(700, wet, function); }
    fx8 { arg wet=0.5, function; this.fx(800, wet, function); }
    fx9 { arg wet=0.5, function; this.fx(900, wet, function); }

    fx {
        arg number=1, wet=1, function = {|in| in};
        this[number] = \filter -> function;
        if (wet > 1, {
          wet = 1
        });
        this.set(("wet" ++ number).asSymbol, wet);
        ^this;
    }

    limiter {
      arg level=1.0, pos=950, wet=1;
      this.fx(pos, wet, { |in|
        Limiter.ar(in: in, level: level, dur: 0.01)
      })
    }

    compressor {
      arg sidechainIn=false, sidechain=0, ratio=4, threshold=40.neg,
      attack=0.1, release=100.01, makeup=0.5, automakeup=1, pos=950, wet=1;
      var sideChainValue;
      if (sidechainIn.not, {
        var sideChainValue = 0;
      }, {
        var sideChainValue = sidechainIn;
      });
      this.fx(pos, wet, { |in|
        DCompressor.ar(in,
          sidechainIn: sideChainValue,
          sidechain: sidechain,
          ratio: ratio,
          threshold: threshold,
          attack: attack,
          release: release,
          makeup: makeup,
          automakeup: automakeup
        )
      })
    }


    flanger {
      /* FIX: Make it better than it currently is */
      arg modSpeed=0.1, modDepth=0.01, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        var lfo = SinOsc.kr(modSpeed, 3pi / 2).range(0.001, modDepth);
        var delay = DelayL.ar(in, 0.01, lfo);
        in + delay
      })
    }

    phaser {
      /* TODO: implement */
      arg pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        in
      })
    }

    grain {
      /* Experimental live audio granular effect */
      arg grains=32, dur= 0.1, pos=950, wet=1;
      this.fx(pos, wet, { |in|
        var signal = DelayN.ar(in, 0.2, 0.2) * 0.7;
        var modulatedSignal = GrainIn.ar(
          numChannels: 2,
          trigger: Dust.kr(32),
          dur: 0.1,
          in: signal
        );
        modulatedSignal
      });
    }

    rings {
      /* TODO: adapt with audio rate pattern capabilities */
      arg pitch=60, trig=0, struct=0.25, bright=0.5, 
      damp=0.5, position=0.25, model=0, position=950, wet=1;
      this.fx(pos, wet, { |in|
        MiRings.ar(
          in: in,
          trig: trig,
          pit: pitch.cpsmidi,
          struct: struct,
          bright: bright,
          damp: damp,
          pos: position,
          model: model,
          poly: 1,
        )
      })
    }

    distort {
      arg cutoff=600, gain=0.5, harmonics=0,
      lowgain=0.1, highgain=0.1, pos=950,
      wet=1;
      this.fx(pos, wet, {
        arg in;
        AnalogVintageDistortion.ar(
          in,
          drivegain: gain,
          bias: harmonics.linlin(0, 1, 0, 2.5),
          lowgain: lowgain.linlin(0, 1, 0.0001, 3),
          highgain: highgain.linlin(0, 1, 0.0001, 3),
          shelvingfreq: cutoff,
          oversample: 0
        ) * -6.dbamp
      });
    }

    crush {
      arg rate=Server.default.sampleRate, bits=24, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        Decimator.ar(
          in,
          rate: rate,
          bits: bits
        ) 

      });
    }

    shift {
      arg ratio=1, dispersion= 0.0, time=0.0, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        PitchShift.ar(
          in: in,
          windowSize: 0.2,
          pitchRatio: ratio,
          pitchDispersion: dispersion,
          timeDispersion: time
        )
      });
    }

    verb {
      arg time=0.5, damp=0.5, freeze=0, pos=950, wet=1;
      this.fx(pos, 1, {
        arg in;
        MiVerb.ar(
          inputArray: in,
          time: time,
          drywet: wet,
          damp: damp,
          freeze: freeze
        )
      });
    }

    delay {
      arg time=2, slice=0.125, decay=1, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
          CombC.ar(
            in: in,
            maxdelaytime: time,
            delaytime: slice,
            decaytime: decay
          )
      });
    }

    lpf {
      arg cutoff=10000, resonance=0.5, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        RLPF.ar(in, cutoff, resonance);
      })
    }

    mooglpf {
      arg cutoff=10000, resonance=0.5, saturation=0.95, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        BMoog.ar(
          in: in,
          freq: cutoff,
          q: resonance,
          mode: 0.0,
          saturation: saturation,
        );
      })
    }

    vlpf2 {
      arg cutoff=10000, resonance=0.5, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        VadimFilter.ar(
          in,
          freq: cutoff,
          resonance: resonance, 
          type: 0
        )
      })
    }

    vlpf4 {
      arg cutoff=10000, resonance=0.5, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        VadimFilter.ar(
          in,
          freq: cutoff,
          resonance: resonance, 
          type: 1
        )
      })
    }

    mooghpf {
      arg cutoff=10000, resonance=0.5, saturation=0.95, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        BMoog.ar(
          in: in,
          freq: cutoff,
          q: resonance,
          mode: 1.0,
          saturation: saturation,
        );
      })
    }

    moogbpf {
      arg cutoff=10000, resonance=0.5, saturation=0.95, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        BMoog.ar(
          in: in,
          freq: cutoff,
          q: resonance,
          mode: 2.0,
          saturation: saturation,
        );
      })
    }


    hpf {
      arg cutoff=10000, resonance=0.5, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        RHPF.ar(in, cutoff, resonance);
      })
    }

    vhpf2 {
      arg cutoff=10000, resonance=0.5, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        VadimFilter.ar(
          in,
          freq: cutoff,
          resonance: resonance, 
          type: 4
        )
      })
    }

    vhpf4 {
      arg cutoff=10000, resonance=0.5, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        VadimFilter.ar(
          in,
          freq: cutoff,
          resonance: resonance, 
          type: 5
        )
      })
    }

    bpf {
      arg cutoff=10000, resonance=0.5, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        BPF.ar(in, cutoff, resonance);
      })
    }

    vbpf2 {
      arg cutoff=10000, resonance=0.5, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        VadimFilter.ar(
          in,
          freq: cutoff,
          resonance: resonance, 
          type: 2
        )
      })
    }

    vbpf4 {
      arg cutoff=10000, resonance=0.5, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        VadimFilter.ar(
          in,
          freq: cutoff,
          resonance: resonance, 
          type: 3
        )
      })
    }

    brf {
      arg cutoff=10000, resonance=0.5, pos=950, wet=1;
      this.fx(pos, wet, {
        arg in;
        BRF.ar(in, cutoff, resonance);
      })
    }
}
