+ Env {

  *rand {
    arg numSegs=8, dur=1, bipolar=true;
    var env, levels, times, curves, minLevel;
    levels = {rrand(-1.0, 1.0)}!numSegs+1;
    minLevel = bipolar.asInteger.neg;
    levels = levels.normalize(minLevel, 1);
    times = {exprand(1,10)}!numSegs;
    times = times.normalizeSum * dur;
    curves = {rrand(-4.0,4.0)}!numSegs;
    env = this.new(levels, times, curves);
    ^env;
  }

}
