Boot {

  classvar <>clock;
  classvar <>localPath;
  classvar <>samplePath;
  classvar <>serverOptions;

  *new {
    arg configPath, samplePath, serverOptions;
    var p; var c; var t; var s; var d; var e; var b;

    BuboUtils.fancyPrint(BuboUtils.banner, 40);
    Server.killAll;
    MIDIClient.init;

    if (serverOptions == nil, {
      "-> Booting using default server configuration".postln;
      s = Server.default;
      s.options.numBuffers = (2048 * 2048) * 2;
      s.options.maxLogins = 1;
      s.options.memSize = 8192 * 64;
	    s.options.numWireBufs = 2048;
	    s.options.maxNodes = 1024 * 32;
	    s.options.numOutputBusChannels = 24;
	    s.options.numInputBusChannels = 16;
    }, {
      "-> Booting using custom server configuration".postln;
      s = Server.default;
      // Imposing a very high number of buffers!
      serverOptions.numBuffers = (2048 * 512) * 2;
      s.options = serverOptions;
    });

    // Using Ableton Link Clock to sync with other peers
    this.clock = LinkClock(130 / 60).latency_(Server.default.latency).permanent_(true);
    TempoClock.default = this.clock;
    c = this.clock;
    t = this.clock.tempo;

    // Defaut local path for configuration files if not configPath
    this.localPath = this.class.filenameSymbol.asString.dirname +/+ "Configuration";
    this.samplePath = samplePath;

    p = ProxySpace.push(s.boot, clock: this.clock);
    p.quant = 4;
    p.fadeTime = 0.01;

    // Setting up the audio samples/buffers manager
    Bank.lazyLoading = true;
    Bank.root = this.samplePath;

    // Post actions: installing behavior after server boot
    Server.default.waitForBoot({
	    s.latency = 0.3;
      // Resume normal boot sequence
      "-> Loading config from: %".format(configPath ? (this.localPath +/+ "Startup.scd")).postln;
      (configPath ? (this.localPath +/+ "Startup.scd")).load;
      BuboUtils.fancyPrint(BuboUtils.ready, 40);
      this.installServerTreeBehavior();
      this.clock.enableMeterSync();

      // Installing Safety for Sound
      Safety.all;
      Safety(s).defName = \safeLimit;
      Safety.setLimit(1);
      e = currentEnvironment;
    });

    }

    *installServerTreeBehavior {
      CmdPeriod.add({
        BuboUtils.fancyPrint("\nBubo SuperCollider Session\nTempo: % | Peers: %\nCPU: %     | Peak: %\n".format(
         this.clock.tempo * 60, this.clock.numPeers, Server.default.avgCPU.round(2), Server.default.peakCPU.round(2)), 40);

         // This Routine prints the current server state
         Tdef(\log, {
         	loop {
         		"TP: %/% CPU: %".format(
              TempoClock.default.bar,
              TempoClock.default.beats,
              Server.default.avgCPU
            ).postln;
         		1.0.wait;
         	}
         }).play;

      }, Server.default);

      Event.addEventType(\buboLoopEvent, {
        arg server;
        if (~sp.notNil && ~nb.notNil, {
          ~sp = ~sp ?? 'default';
          ~nb = ~nb ?? 0;
          ~buf = Bank(~sp)[~nb % Bank(~sp).paths.size];
          if (Bank(~sp).metadata[~nb % Bank(~sp).size][\numChannels] == 1) {
              ~instrument = \looperMono;
          } {
              ~instrument = \looperStereo;
          };
        });
        ~type = \note;
        currentEnvironment.play;
      });

      Event.addEventType(\buboEvent, {
        arg server;
        if (~sp.notNil && ~nb.notNil, {
          ~sp = ~sp ?? 'default';
          ~nb = ~nb ?? 0;
          ~buf = Bank(~sp)[~nb % Bank(~sp).paths.size];
          if (Bank(~sp).metadata[~nb % Bank(~sp).size][\numChannels] == 1) {
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
