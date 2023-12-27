Boot {
  *new {
    arg path = "/Users/bubo/.config/livecoding";
    var s = Server.default;
    var p;
    s.options.numBuffers = 1024 * 128;   // Nombre de buffers disponibles pour stocker des samples
    s.options.memSize = 8192 * 64;       // Mémoire disponible pour le serveur
	  s.options.numWireBufs = 2048;        // Augmenter ce nombre si "exceeded number of interconnect buffers"
	  s.options.maxNodes = 1024 * 32;      // Changer cette valeur si le son saute avec le message "too many nodes"
	  s.options.device = "BlackHole 16ch"; // Choix de l'interface audio à utiliser
	  s.options.numOutputBusChannels = 16; // Indiquer le nombre de sorties de son interface audio
	  s.options.numInputBusChannels = 16;  // Indiquer le nombre d'entrées de son interface audio
    p = ProxySpace.push(Server.default.boot);
    p.makeTempoClock;                    // Gestion du tempo
    p.clock.tempo = 120/60;
    Bank.root = path +/+ "samples"; // Chemin vers les samples
    Bank.lazyLoading = True; // Lazy loading des samples
    Server.default.waitForBoot({
      (path +/+ "Synthdefs.scd").load; // Chargement des synthétiseurs
      StageLimiter.activate;                               // StageLimiter pour les oreilles
      "== 💻 LIVE CODING PRÊT 💻 == ".postln;
    });

    }
}
