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
