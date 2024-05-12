Wah {
  *ar { arg signal, speed;
    ^(signal * SinOsc.ar(speed).range(0.1, 1))
  }
}
