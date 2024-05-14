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

  *cleanSampleName {
    arg str;
    var good_string;
    if (str == nil, { ^"" });
	  good_string = str.asList.collect({
	  	|char|
	  	if (char.isAlphaNum, char, "")
	  }).join;
    ^good_string
  }

  *stringIsNumber {
    arg string;
    if (string == nil, { ^true });
    string = string.asList.collect({arg char; char.ascii});
    string = (48..57).includesAll(string);
    ^string
  }

  *cleanSampleIndex {
    arg number;
    if (number.isKindOf(Number), { ^number }, { ^0 });
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
