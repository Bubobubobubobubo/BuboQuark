Boot {

  classvar <>clock;
  classvar <>localPath;
  classvar <>samplePath;
  classvar <>serverOptions;

  *new {
    arg configPath, samplePath, serverOptions;
    var p; var c; var t; var s;
    BuboUtils.fancyPrint(BuboUtils.banner, 40);

    if (serverOptions == nil,
    {
      "-> Booting using default server configuration".postln;
      s = Server.default;
      s.options.numBuffers = 2048 * 512;
      s.options.memSize = 8192 * 64;
	    s.options.numWireBufs = 2048;
	    s.options.maxNodes = 1024 * 32;
	    s.options.numOutputBusChannels = 16;
	    s.options.numInputBusChannels = 16;
      s.options.outDevice = "BlackHole 16ch";
    },
    {
      "-> Booting using user server configuration".postln;
      s = Server.default;
      s.options = serverOptions;
    },
    );

    // Using Ableton Link Clock for automatic synchronisation with other peers
    this.clock = LinkClock(130 / 60).latency_(Server.default.latency).permanent_(true);
    TempoClock.default = this.clock;
    c = this.clock;
    t = this.clock.tempo;

    // Defining the local path as default for configuration files if not configPath
    this.localPath = this.class.filenameSymbol.asString.dirname +/+ "Configuration";

    p = ProxySpace.push(s.boot, clock: this.clock);
    this.samplePath = samplePath ? "/Users/bubo/.config/livecoding/samples";

    // Setting up the audio samples/buffers manager
    Bank.lazyLoading = false;
    Bank.root = this.samplePath;

    // Post actions: installing behavior after server boot
    Server.default.waitForBoot({
      "-> Loading config from: %".format(configPath ? (this.localPath +/+ "Startup.scd")).postln;
      (configPath ? (this.localPath +/+ "Startup.scd")).load;
      Safety.all;
      Safety(s).defName = \safeLimit;
      Safety.setLimit(1);
      BuboUtils.fancyPrint(BuboUtils.ready, 40);
      this.installServerTreeBehavior();
      this.clock.enableMeterSync();
    });
    }

    *installServerTreeBehavior {
      CmdPeriod.add({
        BuboUtils.fancyPrint("\nBubo SuperCollider Session\nTempo: % | Peers: %\nCPU: %     | Peak: %\n".format(
         this.clock.tempo * 60, this.clock.numPeers,
         Server.default.avgCPU.round(2),
         Server.default.peakCPU.round(2)), 40)
      }, Server.default);
      Event.addEventType(\buboEvent, {
      // This is a custom event that makes it easier to play samples
        arg server;
        if (~sp.notNil && ~nb.notNil, {
          ~sp = ~sp ?? 'default';
          ~nb = ~nb ?? 0;
          ~buf = Bank(~sp)[~nb % Bank(~sp).paths.size];
          if (~buf.numChannels == 1) {
              ~instrument = \player;
          } {
              ~instrument = \splayer;
          };
        });
        ~type = \note;
        currentEnvironment.play;
      });
    }
}
