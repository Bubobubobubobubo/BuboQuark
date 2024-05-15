(
Boot(samplePath: "/Users/bubo/.config/livecoding/samples");
)

// TEST: [X] ça passe
(
~basic => [pat: "kick snare"];
~basic.play;

~hat => [pat: "hat:2 hat:4!3"];
~hat.play;
)

// TEST: [X] ça passe
(
~basic => [
  sp: "kick",
  nb: [0, 2, 3].pseq(inf),
];
~basic.play;
)

(
~hat => [sp: "hat", dur: 1/2, nb: [0, 2, 3].pseq(inf)];
~hat.play;
)

