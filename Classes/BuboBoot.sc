Boot {
  *new {
    arg samplePath = "/Users/bubo/.config/livecoding/samples";
    var banner = "┳┓  ┓     ┳┓\n"
                 "┣┫┓┏┣┓┏┓  ┣┫┏┓┏┓╋\n"
                 "┻┛┗┻┗┛┗┛  ┻┛┗┛┗┛┗";
    var ready = "┓ ┳┓┏┏┓  ┏┓┏┓┳┓┏┓  ┳┓┏┓┏┓┳┓┓┏\n"
                "┃ ┃┃┃┣   ┃ ┃┃┃┃┣   ┣┫┣ ┣┫┃┃┗┫\n"
                "┗┛┻┗┛┗┛  ┗┛┗┛┻┛┗┛  ┛┗┗┛┛┗┻┛┗┛";
    var p; var c; var m;
    var s = Server.default;
    var clock = LinkClock(130 / 60).latency_(Server.default.latency).permanent_(true);
    var localPath = this.class.filenameSymbol.asString.dirname +/+ "Configuration";
    "=-=-=-=-=-=-=-=-=-=-=".postln;
    banner.postln;
    "=-=-=-=-=-=-=-=-=-=-=".postln;
    s.options.numBuffers = 1024 * 128;    // Nombre de buffers disponibles pour stocker des samples
    s.options.memSize = 8192 * 64;        // Mémoire disponible pour le serveur
	  s.options.numWireBufs = 2048;         // Augmenter ce nombre si "exceeded number of interconnect buffers"
	  s.options.maxNodes = 1024 * 32;       // Changer cette valeur si le son saute avec le message "too many nodes"
	  s.options.device = "BlackHole 16ch";  // Choix de l'interface audio à utiliser
	  s.options.numOutputBusChannels = 16;  // Indiquer le nombre de sorties de son interface audio
	  s.options.numInputBusChannels = 16;   // Indiquer le nombre d'entrées de son interface audio
    p = ProxySpace.push(Server.default.boot, clock: clock);
    c = clock;
    Bank.root = samplePath;               // Chemin vers les samples
    Bank.lazyLoading = true;              // Lazy loading des samples
    Server.default.waitForBoot({
      (localPath +/+ "Synthdefs.scd").load;               // Chargement des synthétiseurs
      (localPath+/+ "Startup.scd").load;                 // Chargement post-configuration
      StageLimiter.activate;              // StageLimiter pour les oreilles
      "=-=-=-=-=-=-=-=-=-=-=".postln;
      ready.postln;
      "=-=-=-=-=-=-=-=-=-=-=".postln;
    });
    }
}
