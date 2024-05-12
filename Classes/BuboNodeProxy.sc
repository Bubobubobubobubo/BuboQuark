
+ NodeProxy {

    /* Simple FX chain management */

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
      var quant = this.getQuantFromPattern(pattern);
      var fade = this.getFadeFromPattern(pattern);
      pattern = EventShortener.process(
        pattern,
        this.key,
        [type: 'midi']
      );
      this[0] = Pbind(*pattern);
      this.prepareToPlay(this, quant, fade);
      ^this
    }

    /* Player syntax sugar */
    => {
      arg pattern;
      var quant = this.getQuantFromPattern(pattern);
      var fade = this.getFadeFromPattern(pattern);
      pattern = EventShortener.process(
        pattern,
        this.key,
        [\type, \buboEvent]
      );
      this[0] = Pbind(*pattern);
      this.prepareToPlay(this, quant, fade);
      ^this
    }

    /* Audio Looper (sample playback) */
    == {
      // TODO: fix this terrible mess
      arg pattern;
      var quant = this.getQuantFromPattern(pattern);
      var fade = this.getFadeFromPattern(pattern);
      var nbSlices = this.getValueFromPattern(pattern, 'slices', 1);
      var time = (Pkey(\dur) / Pfunc { currentEnvironment.clock.tempo }) / nbSlices;
      pattern = EventShortener.process(
        pattern,
        this.key,
        [
          \type, \buboLoopEvent,
          \legato, 1,
          \time, time
        ]
      );
      this[0] = Pmono(*pattern);
      this.prepareToPlay(this, quant, fade);
      ^this
    }

    /* Pmono player */
    -> {
      arg pattern;
      var quant = this.getQuantFromPattern(pattern);
      var fade = this.getFadeFromPattern(pattern);
      pattern = EventShortener.processPmono(
        pattern,
        this.key
      );
      this[0] = Pmono(*pattern);
      this.prepareToPlay(this; quant, fade);
      ^this
    }

    f {
      arg value;
      this.fadeTime = value;
      ^this
    }

    p {
      arg quant, fade;
      this.quant = quant;
      this.fadeTime = fade;
      this.play(fadeTime: fade);
      ^this
    }

    s {
      arg duration;
      this.stop(fadeTime: duration)
      ^this
    }

    / {
      arg pattern;
      this.stop(1);
      ^this
    }

    getValueFromPattern {
      arg pattern, key, default;
      var keyIndex = pattern.indexOf(key);
      if (keyIndex.notNil) {
        ^pattern[keyIndex + 1]
      } {
        ^default
      }
    }

    getQuantFromPattern {
      arg pattern;
      ^this.getValueFromPattern(pattern, 'quant', 4)
    }

    getFadeFromPattern {
      arg pattern;
      ^this.getValueFromPattern(pattern, 'fade', 0.01)
    }


}
