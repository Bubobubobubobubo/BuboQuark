EventShortener {

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

		// shortcuts are turned into regular keys;
		pattern.do({| element |
			if (short.includesKey(element),
				{correctedPattern.add(short[element])},
				{correctedPattern.add(element)}
			);
		});
		^correctedPattern;
	}
}
