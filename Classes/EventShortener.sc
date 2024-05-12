EventShortener {

  *process {
    arg pattern, key, other_keys;
    if (pattern.includes('pat'), {
      pattern = this.patternize(pattern);
    });
    pattern = this.findShortcuts(pattern);
    pattern = this.functionsToNdef(pattern, key);
    pattern = pattern ++ other_keys ;
    ^pattern
  }

  *patternize {
    arg pattern;
    var delta_index = nil;
    var new_pattern = List();
    pattern.doAdjacentPairs({
      arg a, b, index;
      if (index % 2 == 0, {
        if (a === 'pat', {
          var temp = Pmini(b);
          temp.pattern.postln;
          new_pattern = new_pattern ++ [
            [\trig, \delta, \dur, \str, \num],
            temp
          ];
          new_pattern = new_pattern ++ [
            degree: Pfunc({ |e|
              if (e.trig > 0, {
                e.str.asInteger
              }, {
                \rest
              }
            )});
          ];
          if (pattern.includes('i') || pattern.includes('instrument') == false, {
            new_pattern = new_pattern ++ [
              sp: Pkey(\str),
              nb: Pkey(\num),
              fast: 1,
            ];
          });
        }, {
          new_pattern.add(a);
          new_pattern.add(b);
        });
      })
    });
    ^new_pattern
  }

  *processPmono {
    arg pattern, key;
    pattern = this.findShortcuts(pattern);
    pattern = this.functionsToNdef(pattern, key);
    ^pattern
  }

  *functionsToNdef {
    arg pattern, key;
    var new_pattern = List.new();
    pattern.do({
      | element, i |
      if (element.isKindOf(Function), {
        new_pattern.add(Ndef((key ++ pattern[i - 1]).asSymbol, element))
      }, {
        new_pattern.add(element)
      });
    })
    ^new_pattern
  }

	*findShortcuts {
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

		pattern.do({| element, i |
			if (short.includesKey(element),
				{correctedPattern.add(short[element])},
				{correctedPattern.add(element)}
			);
		});

		^correctedPattern;
	}
}
