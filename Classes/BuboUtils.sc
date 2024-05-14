BuboUtils {

  *timer {
    var time = Main.elapsedTime;
    var hours = (time / 3600).asInteger;
    var minutes = ((time % 3600) / 60).asInteger;
    var secs = (time % 60).asInteger;
    ^("%:%:%".format(
      hours.asString.padLeft(2, "0"),
      minutes.asString.padLeft(2, "0"),
      secs.asString.padLeft(2, "0"))
    )
  }

  *banner {
    var banner = "┳┓  ┓   ┳┓  ┓    ┳┓\n"
                 "┣┫┓┏┣┓┏┓┣┫┓┏┣┓┏┓ ┣┫┏┓┏┓╋\n"
                 "┻┛┗┻┗┛┗┛┻┛┗┻┗┛┗┛ ┻┛┗┛┗┛┗";
    ^banner
  }

  *ready {
    var ready = "┓ ┳┓┏┏┓  ┏┓┏┓┳┓┏┓  ┳┓┏┓┏┓┳┓┓┏\n"
                "┃ ┃┃┃┣   ┃ ┃┃┃┃┣   ┣┫┣ ┣┫┃┃┗┫\n"
                "┗┛┻┗┛┗┛  ┗┛┗┛┻┛┗┛  ┛┗┗┛┛┗┻┛┗┛";
    ^ready
  }

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

