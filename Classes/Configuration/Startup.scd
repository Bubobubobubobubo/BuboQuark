// Setting up MIDI for hardware performances
"Initialising MIDI...".postln;
p = currentEnvironment;
c = currentEnvironment.clock;

// Space for loading custom SynthDefs
"Loading SynthDefs...".postln;
"Synthdefs.scd".loadRelative;

// Debug during development
// OSCFunc.trace(true);

a = Archive.global
