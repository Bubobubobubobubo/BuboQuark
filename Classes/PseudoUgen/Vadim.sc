VLPF2 {
  *ar {
    arg input, freq=500, resonance=1.0;
    ^VadimFilter.ar(input, freq: freq, resonance: resonance, type: 0)
  }
}

VLPF4 {
  *ar {
    arg input, freq=500, resonance=1.0;
    ^VadimFilter.ar(input, freq: freq, resonance: resonance, type: 1)
  }
}

VBPF2 {
  *ar {
    arg input, freq=500, resonance=1.0;
    ^VadimFilter.ar(input, freq: freq, resonance: resonance, type: 2)
  }
}

VBPF4 {
  *ar {
    arg input, freq=500, resonance=1.0;
    ^VadimFilter.ar(input, freq: freq, resonance: resonance, type: 3)
  }
}

VHPF2 {
  *ar {
    arg input, freq=500, resonance=1.0;
    ^VadimFilter.ar(input, freq: freq, resonance: resonance, type: 4)
  }
}

VHPF4 {
  *ar {
    arg input, freq=500, resonance=1.0;
    ^VadimFilter.ar(input, freq: freq, resonance: resonance, type: 5)
  }
}
