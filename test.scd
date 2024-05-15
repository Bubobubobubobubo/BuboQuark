(
p = ProxySpace.push(s.boot, p);
"abcde".do { |k| p[k.asSymbol].ar };
m = ProxyMixer(p, 8);
m.parent.alwaysOnTop_(true); // show mixer in front of IDE
)

(
p = ProxySpace.push(s.waitForBoot({
  ~a = {
    SinOsc.ar([199, 201, 199/1.75] * [1, 8, 2, 4].choose) * SinOsc.ar(
      LFNoise2.kr(1 /4).range(1,8)
    ).range(0.01, 0.125);
  };
  ~a.play;
}), p);
)

Boot()

(
~a = {
  SinOsc.ar([200, 400] ! 2) * 0.125
};
)

~a.play;


(
~b.fadeTime = 4;
~b => [
  i: "Panalog", pat: "0 2 3 4", octave: 6,
];
~b.play;
)

p.gui


