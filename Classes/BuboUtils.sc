BuboUtils {

  *banner {
    var banner = "┳┓  ┓     ┳┓\n"
                 "┣┫┓┏┣┓┏┓  ┣┫┏┓┏┓╋\n"
                 "┻┛┗┻┗┛┗┛  ┻┛┗┛┗┛┗";
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

