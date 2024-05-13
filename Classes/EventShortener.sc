EventShortener {

  *process {
    arg pattern, key, type, time;
    var new_pattern;
    var additionalKeys = Dictionary.newFrom([
      \midi, [
        type: \midi,
        midiCmd: \noteOn,
      ],
      \buboEvent, [
        type: \buboEvent,
      ],
      \looper, [
          type: \buboLoopEvent,
          legato: 1, time: time
      ],
      \pmono, [],
    ]);
    new_pattern = this.findShortcuts(pattern);
    new_pattern = this.functionsToNdef(new_pattern, key);
    new_pattern = new_pattern ++ additionalKeys[type];
    if (pattern.includes('pat'), {
      new_pattern = this.patternize(new_pattern, type);
    });
    ^new_pattern
  }

  *patternize {
    arg pattern, type;
    var new_pattern = List();
    pattern.doAdjacentPairs({
      arg a, b, index;
      if (index % 2 == 0, {
        if (a === 'pat', {
          var temp = Pmini(b);
          var additionalKeys;
          if (type == 'midi', {
            additionalKeys = [\trig, \delta, \dur, \str];
            new_pattern = new_pattern ++ [\type, 'midi'];
          }, {
            additionalKeys = [\trig, \delta, \dur, \str, \num];
          });
          new_pattern = new_pattern ++ [additionalKeys, temp];
          new_pattern = new_pattern ++ [
            degree: Pfunc({ |e|
              if (e.trig > 0, {
                e.str.asInteger
              }, {
                \rest
              }
            )});
          ];
          if (type !== 'midi', {
            if (pattern.includes('i') || pattern.includes('instrument') == false, {
              new_pattern = new_pattern ++ [
                sp: Pfunc { |e| e.str ? "kick" },
                nb: Pfunc { |e| e.num ? 0 },
                instrument: Pfunc { |e| e.str.isNil && e.num.isNil ? "default" },
                fast: 1,
              ];
            });
          })
        }, {
          new_pattern = new_pattern ++ [a, b];
        });
      })
    });
    ^new_pattern
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
