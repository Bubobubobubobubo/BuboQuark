// TODO: broken
Compressor {
  *ar {
    arg signal, attack, release, threshold, ratio;
    var gainDb, amplitudeDb;
	  amplitudeDb = Amplitude.ar(signal, attack, release).ampdb;
	  gainDb = ((amplitudeDb - threshold) * (1 / ratio - 1)).min(0);
	  signal = signal * gainDb.dbamp;
    signal
  }
}
