Segment {
	*ar {
    arg start = 0, end = 1, time = 1, curve = \lin, trig = 1, doneAction = 0;
		^EnvGen.ar(
			Env([start, start, end], [0, time], curve),
			trig,
			doneAction: doneAction
		)
	}
	*kr {
    arg start = 0, end = 1, time = 1, curve = \lin, trig = 1, doneAction = 0;
		^EnvGen.kr(
			Env([start, start, end], [0, time], curve),
			trig,
			doneAction: doneAction
		)
	}
}

