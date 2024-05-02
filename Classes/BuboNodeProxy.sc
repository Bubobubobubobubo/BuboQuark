+ NodeProxy {

    /* Simple FX chain management */

    fx {
        arg number=1, wet=1, function = {|in| in};
        this[number] = \filter -> function;
        if (wet > 1, {wet = 1});
        this.set(("wet" ++ number).asSymbol, wet);
        ^this;
    }

    fx1 { arg wet, function; this.fx(100, wet, function); }
    fx2 { arg wet, function; this.fx(200, wet, function); }
    fx3 { arg wet, function; this.fx(300, wet, function); }
    fx4 { arg wet, function; this.fx(400, wet, function); }
    fx5 { arg wet, function; this.fx(500, wet, function); }
    fx6 { arg wet, function; this.fx(600, wet, function); }
    fx7 { arg wet, function; this.fx(700, wet, function); }
    fx8 { arg wet, function; this.fx(800, wet, function); }
    fx9 { arg wet, function; this.fx(900, wet, function); }

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

    /* Syntax for sending MIDI messages */
    >> {
      arg pattern;
      var quant = this.getQuantFromPattern(pattern);
      var fade = this.getFadeFromPattern(pattern);
      pattern = EventShortener.findShortcuts(pattern);
      pattern = pattern ++ [type: 'midi'];
      this[0] = Pbind(*pattern);
      this.quant = quant;
      this.fadeTime = fade;
      this.play;
      ^this
    }

    /* Player syntax sugar */
    => {
      arg pattern;
      var quant = this.getQuantFromPattern(pattern);
      var fade = this.getFadeFromPattern(pattern);
      pattern = EventShortener.findShortcuts(pattern);
      pattern = pattern ++ [\type, \buboEvent];
      this[0] = Pbind(*pattern);
      this.quant = quant;
      this.fadeTime = fade;
      this.play;
      ^this
    }

    /* Audio Looper (sample playback) */
    == {
      arg pattern;
      var quant = this.getQuantFromPattern(pattern);
      var fade = this.getFadeFromPattern(pattern);
      pattern = EventShortener.findShortcuts(pattern);
      pattern = pattern ++ [\type, \buboLoopEvent];
      pattern = pattern ++ [\legato, 1];
      pattern = pattern ++ [
        \time, Pkey(\dur) / Pfunc { currentEnvironment.clock.tempo }
      ];
      this[0] = Pbind(*pattern);
      this.quant = quant;
      this.fadeTime = fade;
      this.play;
      ^this
    }

    /* FIX: Rewrite this part, slightly broken */
    -> {
      arg pattern;
      var quant = this.getQuantFromPattern(pattern);
      var fade = this.getFadeFromPattern(pattern);
      pattern = EventShortener.findShortcuts(pattern);
      this[0] = Pmono(*pattern);
      this.quant = quant;
      this.fadeTime = fade;
      this.play;
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

    getQuantFromPattern {
      arg pattern; var quant;
      var quantIndex = pattern.indexOf('quant');
      if (quantIndex.notNil) {
        ^pattern[quantIndex + 1]
      } {
        ^0
      }
    }

    getFadeFromPattern {
      arg pattern; var fade;
      var fadeIndex = pattern.indexOf('fade');
      if (fadeIndex.notNil) {
        ^pattern[fadeIndex + 1]
      } {
        ^0.01
      }
    }

}
