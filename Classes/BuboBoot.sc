Boot {

  classvar <>clock;
  classvar <>localPath;
  classvar <>samplePath;
  classvar <>serverOptions;

  *new {
    arg configPath, samplePath, serverOptions;
    var p; var c; var t; var s; var d; var e; var b;
    BuboUtils.banner().postln;
    Server.killAll;

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
      BuboUtils.ready.postln;
      this.installServerTreeBehavior();
      this.clock.enableMeterSync();

      // Installing Safety
      Safety.all;
      Safety(s).defName = \safeLimit;
      Safety.setLimit(1);
      MIDIClient.init;
    });

    }

    *installServerTreeBehavior {

      CmdPeriod.add({
        BuboUtils.stop().postln;
        // Printing session state when the session is stopped
        "\nBubo SuperCollider Session\nTempo: % | Peers: %\nCPU: %     | Peak: %\n".format(
         this.clock.tempo * 60,
         this.clock.numPeers,
         Server.default.avgCPU.round(2),
         Server.default.peakCPU.round(2)).postln;
      }, Server.default);

      Event.addEventType(\buboLoopEvent, {
        arg server;
        if (BuboUtils.stringIsNumber(~sp), {}, {
          ~sp = BuboUtils.cleanSampleName(~sp);
          ~nb = BuboUtils.cleanSampleIndex(~nb);
          if (~sp.notNil && ~nb.notNil, {
            if (~sp !== "", {
              ~buf = Bank(~sp)[~nb % Bank(~sp).paths.size];
              if (Bank(~sp).metadata[~nb % Bank(~sp).size][\numChannels] == 1) {
                  ~instrument = \looperMono;
              } {
                  ~instrument = \looperStereo;
              };
            })
          });
        });
        ~type = \note;
        currentEnvironment.play;
      });

      Event.addEventType(\buboEvent, {
        arg server;
        if (~sp.notNil, {
          if (BuboUtils.stringIsNumber(~sp), {}, {
            ~sp = BuboUtils.cleanSampleName(~sp);
            ~nb = BuboUtils.cleanSampleIndex(~nb);
            if (~sp !== "", {
              ~buf = Bank(~sp)[~nb % Bank(~sp).paths.size];
              if (Bank(~sp).metadata[~nb % Bank(~sp).size][\numChannels] == 1) {
                  ~instrument = \player;
              } {
                  ~instrument = \splayer;
              };
            });
          });
        });
        ~type = \note;
        currentEnvironment.play;
      });

      Event.addEventType(\buboGranular, {
        arg server;
        if (~sp.notNil, {
          if (BuboUtils.stringIsNumber(~sp), {}, {
            ~sp = BuboUtils.cleanSampleName(~sp);
            ~nb = BuboUtils.cleanSampleIndex(~nb);
            if (~sp !== "", {
              ~buf = Bank(~sp)[~nb % Bank(~sp).paths.size];
              ~instrument = 'grainSampler';
            });
          });
        });
        ~type = \note;
        currentEnvironment.play;
      });
    }

}
