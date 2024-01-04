Boot {

  classvar <>clock;
  classvar <>localPath;
  classvar <>samplePath;

  *new {
    arg configPath, samplePath, soundDevice;
    var s = Server.default;
    var p; var c; var t;
    var banner = "┳┓  ┓     ┳┓\n"
                 "┣┫┓┏┣┓┏┓  ┣┫┏┓┏┓╋\n"
                 "┻┛┗┻┗┛┗┛  ┻┛┗┛┗┛┗";
    var ready = "┓ ┳┓┏┏┓  ┏┓┏┓┳┓┏┓  ┳┓┏┓┏┓┳┓┓┏\n"
                "┃ ┃┃┃┣   ┃ ┃┃┃┃┣   ┣┫┣ ┣┫┃┃┗┫\n"
                "┗┛┻┗┛┗┛  ┗┛┗┛┻┛┗┛  ┛┗┗┛┛┗┻┛┗┛";
    this.fancyPrint(banner, 40);

    // Using Ableton Link Clock for automatic synchronisation with other peers
    this.clock = LinkClock(130 / 60).latency_(Server.default.latency).permanent_(true);
    TempoClock.default = this.clock;
    c = this.clock;
    t = this.clock.tempo;

    // Defining the local path as default for configuration files if not configPath
    this.localPath = this.class.filenameSymbol.asString.dirname +/+ "Configuration";

    // Customizing server options: less conservative than SuperCollider defaults
    s.options.numBuffers = 1024 * 128;
    s.options.memSize = 8192 * 64;
	  s.options.numWireBufs = 2048;
	  s.options.maxNodes = 1024 * 32;
    soundDevice ? s.options.device = soundDevice;
	  s.options.numOutputBusChannels = 16;
	  s.options.numInputBusChannels = 16;

    p = ProxySpace.push(Server.default.boot, clock: this.clock);
    this.samplePath = samplePath ? "/Users/bubo/.config/livecoding/samples";

    // Setting up the audio samples/buffers manager
    Bank.root = this.samplePath;
    Bank.lazyLoading = true;

    // Post actions: installing behavior after server boot
    Server.default.waitForBoot({
      "-> Loading config from: %".format(configPath ? (this.localPath +/+ "Startup.scd")).postln;
      (configPath ? (this.localPath +/+ "Startup.scd")).load;
      Safety.setLimit(0.8);
      this.fancyPrint(ready, 40);
      this.installServerTreeBehavior();
      this.clock.enableMeterSync();
    });
    }

    /*
    * Convenience method for printing a message with a fancy separator.
    */
    *fancyPrint {
      arg message, length;
      var separator= length.collect({
        arg index;
        if (index % 2 == 0, "=", "-")
      });
      separator = separator.join("");
      [separator, message, separator].do({
        arg each;
        each.postln;
      });
    }

    *installServerTreeBehavior {
      CmdPeriod.add({
        this.fancyPrint("\nBubo SuperCollider Session\nTempo: % | Peers: %\nCPU: %     | Peak: %\n".format(
         this.clock.tempo * 60, this.clock.numPeers,
         Server.default.avgCPU.round(2),
         Server.default.peakCPU.round(2)), 40)
      }, Server.default);
      Event.addEventType(\buboEvent, {
         arg server;
         if (~sp.notNil && ~nb.notNil,
            { ~buf = Bank(~sp)[~nb % Bank(~sp).buffers.size]; }
         );
         if (~nb == nil) {~nb = 0};
         ~type = \note; // back to note
         currentEnvironment.play;
      });
    }
}
