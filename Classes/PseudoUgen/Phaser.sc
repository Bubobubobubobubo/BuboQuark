BuboPhaser {
  * ar {
    arg signal, speed=2, skew=0, feedback=0.25, mod=0.5;
    ^AnalogPhaser.ar(
      input: signal,
      lfoinput: LFNoise2.ar(speed).range(-1, 1),
      skew: skew.clip2(1),
      feedback: feedback.clip2(1),
      modulation: mod.clip2(1)
    )
  }
}
