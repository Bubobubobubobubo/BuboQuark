StereoWah {
  *ar {
    arg signal, leftSpeed, rightSpeed;
    ^[
      signal[0] * SinOsc.ar(leftSpeed).range(0.1, 1),
      signal[1] * SinOsc.ar(rightSpeed).range(0.1, 1),
    ]
  }
}
