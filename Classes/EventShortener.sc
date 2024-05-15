EventShortener {

  *process {
    arg pattern, key, type, time;
    var new_pattern;
    new_pattern = this.findShortcuts(pattern);
    new_pattern = this.functionsToNdef(new_pattern, key);
    new_pattern = switch(type,
      'pmono', this.patternPmono(new_pattern),
      'buboEvent', this.patternBuboEvent(new_pattern),
      'midi', this.patternMidi(new_pattern),
      'midicc', this.patternMidiCC(new_pattern),
      'looper', this.patternLooper(new_pattern),
      'granular', this.patternGranular(new_pattern),
    );
    ^new_pattern
  }

  *patternLooper {
    arg pattern;
    ^pattern
  }

  *patternPmono {
    arg pattern;
    var new_pattern = List();
    new_pattern = new_pattern ++ pattern[0];
    pattern = pattern[1..];
    pattern.doAdjacentPairs({ | a, b, index |
      if (index % 2 == 0, {
        if (a === 'pat', {
          var temp = Pmini(b);
          new_pattern = new_pattern ++ [
            [\trig, \delta, \dur, \str, \num], Pmini(b),
          ];
          new_pattern = new_pattern ++ [
            degree: Pfunc({ |e| e.str.asInteger });
          ];
        }, {
          new_pattern = new_pattern ++ [a, b];
        });
      });
    });
    ^new_pattern
  }

  *patternGranular {
    arg pattern;
    var new_pattern = List();
    var sp_position = nil;
    var nb_position = nil;
    // Check if the sp key already exists in the pattern
    if (pattern.includes('sp'), {
      pattern.do({ arg e, i;
        if (e == 'sp', {
          sp_position = i;
        })
      });
    });
    // Check if the nb key already exists in the pattern
    if (pattern.includes('nb'), {
      pattern.do({ arg e, i;
        if (e == 'nb', {
          nb_position = i;
        })
      });
    });
    new_pattern = new_pattern ++ [\type, 'buboGranular'];
    pattern.doAdjacentPairs({ | a, b, index |
      if (index % 2 == 0, {
        if (a === 'pat', {
          var temp = Pmini(b);
          new_pattern = new_pattern ++ [
            [\trig, \delta, \dur, \str, \num], Pmini(b),
          ];
          new_pattern = new_pattern ++ [
            degree: Pfunc({ |e|
              if (e.trig > 0, {
                e.str.asInteger
              }, {
                \rest
              });
            });
          ];
          if (sp_position.notNil, {
            new_pattern = new_pattern ++ [
              sp: pattern[sp_position + 1],
              nb: pattern[nb_position + 1],
            ];
          }, {
            new_pattern = new_pattern ++ [
              sp: Pfunc { |e| e.str ? "" },
              nb: Pfunc { |e| e.num ? 0 }
            ];
          });
        }, {
          new_pattern = new_pattern ++ [a, b];
        });
      });
    });
    new_pattern.postln;
    ^new_pattern
  }

  *patternBuboEvent {
    arg pattern;
    var new_pattern = List();
    var sp_position = nil;
    var nb_position = nil;
    // Check if the sp key already exists in the pattern
    if (pattern.includes('sp'), {
      pattern.do({ arg e, i;
        if (e == 'sp', {
          sp_position = i;
        })
      });
    });
    // Check if the nb key already exists in the pattern
    if (pattern.includes('nb'), {
      pattern.do({ arg e, i;
        if (e == 'nb', {
          nb_position = i;
        })
      });
    });
    new_pattern = new_pattern ++ [\type, 'buboEvent'];
    pattern.doAdjacentPairs({ | a, b, index |
      if (index % 2 == 0, {
        if (a === 'pat', {
          var temp = Pmini(b);
          new_pattern = new_pattern ++ [
            [\trig, \delta, \dur, \str, \num], Pmini(b),
          ];
          new_pattern = new_pattern ++ [
            degree: Pfunc({ |e|
              if (e.trig > 0, {
                e.str.asInteger
              }, {
                \rest
              });
            });
          ];
          if (sp_position.notNil, {
            new_pattern = new_pattern ++ [
              sp: pattern[sp_position + 1],
              nb: pattern[nb_position + 1],
            ];
          }, {
            new_pattern = new_pattern ++ [
              sp: Pfunc { |e| e.str ? "" },
              nb: Pfunc { |e| e.num ? 0 }
            ];
          });
        }, {
          new_pattern = new_pattern ++ [a, b];
        });
      });
    });
    new_pattern.postln;
    ^new_pattern
  }

  *patternMidi {
    arg pattern;
    var new_pattern = List();
    new_pattern = new_pattern ++ [\type, 'midi', \midicmd, \noteOn];
    pattern.doAdjacentPairs({ | a, b, index |
      if (index % 2 == 0, {
        if (a === 'pat', {
          var temp = Pmini(b);
          new_pattern = new_pattern ++ [ [\trig, \delta, \dur, \str, \num], Pmini(b) ];
          new_pattern = new_pattern ++ [
            degree: Pfunc({ |e|
              if (e.trig > 0, {
                e.str.asInteger
              }, {
                \rest
              });
            });
          ];
        }, {
          new_pattern = new_pattern ++ [a, b];
        });
      });
    });
    ^new_pattern
  }

  *patternMidiCC {
    arg pattern;
    var new_pattern = List();
    // The base requirement for a message to be considered CC
    new_pattern = new_pattern ++ [
      \type: 'midi',
      \midicmd: 'control'
    ];
    pattern.doAdjacentPairs({
      arg a, b, index;
      if (index % 2 == 0, {
        if (a === 'pat', {
          var temp = Pmini(b);
          new_pattern = new_pattern ++ [
            [\trig, \delta, \dur, \str], Pmini(b),
          ];
          new_pattern = new_pattern ++ [
            val: Pfunc({ |e|
              if (e.trig > 0, {
                e.str.asInteger
              }, {
                \rest
              });
            });
          ];
        }, {
          new_pattern = new_pattern ++ [a, b];
        });
      });
   });
   new_pattern = new_pattern ++ [
     'ctlNum': Pkey(\num),
     'control': Pfunc { |e| e.val.asInteger },
   ]
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
    });
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
      \n, \note, \mn, \midinote,
      \vel, \velocity, \deg, \degree,
      \off, \timingOffset, \o, \octave,
      \f, \freq, \det, \detune,
      // Durations
      \d, \dur, \l, \legato,
      // Amplitude
      \p, \pan,
      // Envelope
      \a, \attack, \d, \decay,
      \s, \sustain, \r, \release,
      // Filter control
      \r, \resonance, \ff, \ffreq,
      // Modulation
      \m, \mod, \mo, \midout,
      \c, \midichan, \st, \stretch,
      \rt, \root, \scl, \scale,
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
