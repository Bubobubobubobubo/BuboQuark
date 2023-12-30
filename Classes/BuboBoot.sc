Boot {

  classvar <>clock;
  classvar <>localPath;
  classvar <>samplePath;


  *new {
    arg configPath, samplePath;
    var s = Server.default;
    var p; var c;
    var banner = "┳┓  ┓     ┳┓\n"
                 "┣┫┓┏┣┓┏┓  ┣┫┏┓┏┓╋\n"
                 "┻┛┗┻┗┛┗┛  ┻┛┗┛┗┛┗";
    var ready = "┓ ┳┓┏┏┓  ┏┓┏┓┳┓┏┓  ┳┓┏┓┏┓┳┓┓┏\n"
                "┃ ┃┃┃┣   ┃ ┃┃┃┃┣   ┣┫┣ ┣┫┃┃┗┫\n"
                "┗┛┻┗┛┗┛  ┗┛┗┛┻┛┗┛  ┛┗┗┛┛┗┻┛┗┛";
    this.fancyPrint(banner, 40);

    // Using Ableton Link Clock for automatic synchronisation with other peers
    this.clock = LinkClock(130 / 60).latency_(Server.default.latency).permanent_(true);
    c = this.clock;

    // Defining the local path as default for configuration files if not configPath
    this.localPath = this.class.filenameSymbol.asString.dirname +/+ "Configuration";

    // Customizing server options: less conservative than SuperCollider defaults
    s.options.numBuffers = 1024 * 128;
    s.options.memSize = 8192 * 64;
	  s.options.numWireBufs = 2048;
	  s.options.maxNodes = 1024 * 32;
	  s.options.device = "BlackHole 16ch";
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
      StageLimiter.activate;
      this.fancyPrint(ready, 40);
      // this.installServerTreeBehavior();
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
          "Dummy text".postln;
         // var text = "Bubo SuperCollider Session\n Tempo: % =-= CPU: %".format(
         //   this.clock.tempo * 60, Server.default.avgCPU.round(2)
         // );
      }, Server.default);
    }

}
