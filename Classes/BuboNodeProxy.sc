+ ProxySpace {

  solo {
    arg name, fadeTime=4;
    var names = name.isArray.if({ name }, { [name] });
    this.do({ |proxy|
      if (names.includes(proxy.key).not, {
        proxy.stop(fadeTime: fadeTime);
      });
    });
  }

  silence {|fadeTime=0|
    this.stop(fadeTime: fadeTime)
  }

}

+ NodeProxy {

    fx {
        arg number=1, wet=1, function = {|in| in};
        this[number] = \filter -> function;
        if (wet > 1, {wet = 1});
        this.set(("wet" ++ number).asSymbol, wet);
        ^this;
    }

    fx1 { arg wet=0.5, function; this.fx(100, wet, function); }
    fx2 { arg wet=0.5, function; this.fx(200, wet, function); }
    fx3 { arg wet=0.5, function; this.fx(300, wet, function); }
    fx4 { arg wet=0.5, function; this.fx(400, wet, function); }
    fx5 { arg wet=0.5, function; this.fx(500, wet, function); }
    fx6 { arg wet=0.5, function; this.fx(600, wet, function); }
    fx7 { arg wet=0.5, function; this.fx(700, wet, function); }
    fx8 { arg wet=0.5, function; this.fx(800, wet, function); }
    fx9 { arg wet=0.5, function; this.fx(900, wet, function); }

    wet { arg number=1, wet=1;
        this.set(("wet" ++ number).asSymbol, wet);
        ^this;
    }

    xwet { arg number=1, wet=1;
        this.xset(("wet" ++ number).asSymbol, wet);
        ^this;
    }

    fxin {
        arg number=1, wet=1, function = {|in| in};
        this[number] = \filterIn -> function;
        this.set(("wet" ++ number).asSymbol, wet);
        ^this;
    }

    prepareToPlay {
      | proxy, quant, fade |
      proxy.quant = quant;
      proxy.fadeTime = fade;
      // proxy.play;
    }

    /* Syntax for sending MIDI messages */
    >> {
      arg pattern;
      var quant = BuboUtils.getQuantFromPattern(pattern);
      var fade = BuboUtils.getFadeFromPattern(pattern);
      pattern = EventShortener.process(
        pattern, this.key, \midi, 0
      );
      this[0] = Pbind(*pattern);
      this.prepareToPlay(this, quant, fade);
      ^this
    }

    /* MIDI CC Operator */
    >>+ {
      arg pattern;
      var quant = BuboUtils.getQuantFromPattern(pattern);
      var fade = BuboUtils.getFadeFromPattern(pattern);
      pattern = EventShortener.process(
        pattern, this.key, 'midicc', 0
      );
      this[0] = Pbind(*pattern);
      this.prepareToPlay(this, quant, fade);
      ^this
    }

    /* Player syntax sugar */
    => {
      arg pattern;
      var quant = BuboUtils.getQuantFromPattern(pattern);
      var fade = BuboUtils.getFadeFromPattern(pattern);
      pattern = EventShortener.process(pattern, this.key, 'buboEvent', 1);
      pattern = EffectChain.process(pattern, this.key);
      this[0] = Pbind(*pattern);
      this.prepareToPlay(this, quant, fade);
      ^this
    }

    /* Granular Sampler */
    +=> {
      arg pattern;
      var quant = BuboUtils.getQuantFromPattern(pattern);
      var fade = BuboUtils.getFadeFromPattern(pattern);
      pattern = EventShortener.process(pattern, this.key, 'granular', 1);
      pattern = EffectChain.process(pattern, this.key);
      this[0] = Pbind(*pattern);
      this.prepareToPlay(this, quant, fade);
      ^this
    }

    /* Pmono player */
    -> {
      arg pattern;
      var quant = BuboUtils.getQuantFromPattern(pattern);
      var fade = BuboUtils.getFadeFromPattern(pattern);
      pattern = EventShortener.process(pattern, this.key, 'pmono', 1);
      this[0] = Pmono(*pattern);
      this.prepareToPlay(this, quant, fade);
      ^this
    }

    /* Audio Looper (sample playback) */
    ==> {
      // TODO: fix this terrible mess
      arg pattern;
      var quant = BuboUtils.getQuantFromPattern(pattern);
      var fade = BuboUtils.getFadeFromPattern(pattern);
      var nbSlices = this.getValueFromPattern(pattern, 'slices', 1);
      var time = (Pkey(\dur) / Pfunc { currentEnvironment.clock.tempo }) / nbSlices;
      pattern = EventShortener.process(
        pattern, this.key, \looper, time
      );
      this[0] = Pmono(*pattern);
      this.prepareToPlay(this, quant, fade);
      ^this
    }
}
