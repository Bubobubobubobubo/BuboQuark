+ Pbind {
	fastest { ^this.set(\dur, 1/8) }
	faster { ^this.set(\dur, 1/4) }
	fast { ^this.set(\dur, 1/2) }
	slow { ^this.set(\dur, 2) }
	slower { ^this.set(\dur, 4) }
	slowest { ^this.set(\dur, 8) }

	lowest { ^this.set(\octave, 2) }
	lower { ^this.set(\octave, 3) }
	low { ^this.set(\octave, 4) }
	high { ^this.set(\octave, 6) }
	higher { ^this.set(\octave, 7) }
	highest { ^this.set(\octave, 8) }

	fff { ^this.set(\amp, 2) }
	ff { ^this.set(\amp, 1) }
	f { ^this.set(\amp, 0.5) }
	p { ^this.set(\amp, 0.3) }
	pp { ^this.set(\amp, 0.2) }
	ppp { ^this.set(\amp, 0.1) }
}

