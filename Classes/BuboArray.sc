+ Array {

  sp {
    arg repeats=inf;
    var pat;
    if (this[0].isString,
      { pat = Pseq([this[0]], inf).collect({|i| Bank(i)}); },
      { pat = this[0].collect({|i| Bank(i)})}
    );
    ^Pindex(pat, this[1], repeats)
  }

	findShortcuts {
    arg pattern;
    var short, correctedPattern;
    correctedPattern = List.new();
    short = Dictionary.newFrom([
      // Instrument
      \i, \instrument,
      // Notes
      \n, \note,
      \mn, \midinote,
      \vel, \velocity,
      \deg, \degree,
      \off, \timingOffset,
      \o, \octave,
      \f, \freq,
      \det, \detune,
      // Durations
      \d, \dur,
      \l, \legato,
      // Amplitude
      \p, \pan,
      // Envelope
      \a, \attack,
      \d, \decay,
      \s, \sustain,
      \r, \release,
      // Filter control
      \r, \resonance,
      \ff, \ffreq,
      // Modulation
      \m, \mod,
      \mo, \midout,
      \c, \midichan,
      \st, \stretch,
      \rt, \root,
      \scl, \scale,
  	]);

		// shortcuts are turned into regular keys;
		pattern.do({| element |
			if (short.includesKey(element),
				{correctedPattern.add(short[element])},
				{correctedPattern.add(element)}
			);
		});
		^correctedPattern;
	}

  pat {
    arg quant=4, fade=0.05;
    var proxyName = this[0];
    var newArray = this[1..] ++ [\type, \buboEvent];
    currentEnvironment.at(proxyName.asSymbol).quant_(quant);
    currentEnvironment.at(proxyName.asSymbol).fadeTime = fade;
    currentEnvironment.at(proxyName.asSymbol)[0] = Pbind(*(this.findShortcuts(newArray)));
    ^currentEnvironment.at(proxyName.asSymbol);
  }

	p {
		^Pbind(*(this.findShortcuts(this)))
	}

  euclid {
    arg repeats=inf;
    ^Pbjorklund2(this[0], this[1], repeats);
  }

  pseq { arg repeats=inf, offset=0;
    ^Pseq(this, repeats, offset);
  }

  pshuf { arg repeats=1;
    ^Pshuf(this, repeats);
  }

  prand { arg repeats=inf;
    ^Prand(this, repeats);
  }

  pxrand { arg repeats=inf;
    ^Pxrand(this, repeats);
  }

  pwrand { arg weights, repeats=1;
    ^Pwrand(this, weights.normalizeSum, repeats);
  }

  pwhite { arg repeats=inf;
    ^Pwhite(this[0], this[1], repeats);
  }

  pseries { arg repeats=inf;
    ^Pseries(this[0], this[1], repeats);
  }
}
