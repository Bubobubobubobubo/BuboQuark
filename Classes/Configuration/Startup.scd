MIDIClient.init;
p = currentEnvironment;
c = currentEnvironment.clock;
"Loading SynthDefs".postln;
"Synthdefs.scd".loadRelative;
m = MIDIControl();
