// Setting up MIDI for hardware performances
"Initialising MIDI...".postln;
MIDIClient.init;
p = currentEnvironment;
c = currentEnvironment.clock;

// Space for loading custom SynthDefs
"Loading SynthDefs...".postln;
"Synthdefs.scd".loadRelative;

// Debug during development
// OSCFunc.trace(true);
