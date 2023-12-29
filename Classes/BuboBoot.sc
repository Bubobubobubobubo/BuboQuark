Boot {
  *new {
    arg samplePath;
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
    this.fancyPrint(banner, 40);
    s.options.numBuffers = 1024 * 128;
    s.options.memSize = 8192 * 64;
	  s.options.numWireBufs = 2048;
	  s.options.maxNodes = 1024 * 32;
	  s.options.device = "BlackHole 16ch";
	  s.options.numOutputBusChannels = 16;
	  s.options.numInputBusChannels = 16;
    p = ProxySpace.push(Server.default.boot, clock: clock);
    c = clock;
    Bank.root = samplePath ? "/Users/bubo/.config/livecoding/samples";
    Bank.lazyLoading = true;
    Server.default.waitForBoot({
      "-> Loading config from: %".format(localPath +/+ "Startup.scd").postln;
      (localPath+/+ "Startup.scd").load;
      StageLimiter.activate;
      this.fancyPrint(ready, 40);
    });
    }

    /*
     * Routine that prints the current state of the session every bar
    */
    *monitorMessage {
      CmdPeriod.add({})
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
}
