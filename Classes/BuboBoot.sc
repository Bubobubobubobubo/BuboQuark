Boot {

  classvar <>clock;
  classvar <>localPath;
  classvar <>samplePath;
  classvar <>serverOptions;

  *new {
    arg configPath, samplePath, serverOptions;
    var p; var c; var t; var s; var d; var e;
    BuboUtils.fancyPrint(BuboUtils.banner, 40);
    MIDIClient.init;

    if (serverOptions == nil,
    {
      "-> Booting using default server configuration".postln;
      s = Server.default;
      s.options.numBuffers = (2048 * 2048) * 2;
      s.options.maxLogins = 1;
      s.options.memSize = 8192 * 64;
	    s.options.numWireBufs = 2048;
      s.options.outDevice = "BlackHole 64ch";
	    s.options.maxNodes = 1024 * 32;
	    s.options.numOutputBusChannels = 24;
	    s.options.numInputBusChannels = 16;
    },
    {
      "-> Booting using custom server configuration".postln;
      s = Server.default;
      // Imposing a very high number of buffers!
      serverOptions.numBuffers = (2048 * 512) * 2;
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
    Bank.lazyLoading = true;
    Bank.root = this.samplePath;

    // Post actions: installing behavior after server boot
    Server.default.waitForBoot({
      // d = ();
      // // Exceptional Dual Sardine Boot
      // d.dirt = SuperDirt(2, s);
		  // d.dirt.fileExtensions = ["wav","aif","aiff","aifc","mp3"];
		  // d.dirt.loadSoundFiles("/Users/bubo/Library/Application\ Support/Sardine/SON/*");
      // d.dirt.loadSoundFiles("/Users/bubo/.config/livecoding/samples/*");
	    // d.dirt.doNotReadYet = true;
   	  // d.dirt.start(57120, [ 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22]);
		  // (
		  // 	d.d1 = d.dirt.orbits[0]; d.d2 = d.dirt.orbits[1]; d.d3 = d.dirt.orbits[2];
		  // 	d.d4 = d.dirt.orbits[3]; d.d5 = d.dirt.orbits[4]; d.d6 = d.dirt.orbits[5];
		  // 	d.d7 = d.dirt.orbits[6]; d.d8 = d.dirt.orbits[7]; d.d9 = d.dirt.orbits[8];
		  // 	d.d10 = d.dirt.orbits[9]; d.d11 = d.dirt.orbits[10]; d.d12 = d.dirt.orbits[11];
		  // );
      // d.dirt.soundLibrary.addMIDI(\midi, MIDIOut.newByName("MIDI", "Bus 1"));
      // d.dirt.soundLibrary.addMIDI(\midi2, MIDIOut.newByName("MIDI", "Bus 2"));
	    s.latency = 0.3;

      // Resume normal boot sequence
      "-> Loading config from: %".format(configPath ? (this.localPath +/+ "Startup.scd")).postln;
      (configPath ? (this.localPath +/+ "Startup.scd")).load;
      BuboUtils.fancyPrint(BuboUtils.ready, 40);
      this.installServerTreeBehavior();
      this.clock.enableMeterSync();
      Safety.all;
      Safety(s).defName = \safeLimit;
      Safety.setLimit(1);
      e = currentEnvironment;


    });

    }

    *installServerTreeBehavior {
      CmdPeriod.add({
        BuboUtils.fancyPrint("\nBubo SuperCollider Session\nTempo: % | Peers: %\nCPU: %     | Peak: %\n".format(
         this.clock.tempo * 60, this.clock.numPeers,
         Server.default.avgCPU.round(2),
         Server.default.peakCPU.round(2)), 40)
      }, Server.default);

      // This custom event is used for audio looping
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

      // This custom event makes it easier to play samples
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
