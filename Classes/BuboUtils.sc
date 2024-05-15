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
    if (string == nil || string == "", { ^false });
    string = string.asList.collect({arg char; char.ascii});
    string = (48..57).includesAll(string);
    ^string
  }

  *cleanSampleIndex {
    arg number;
    if (number.isKindOf(Number), { ^number }, { ^0 });
  }

  *banner {
    var banner = "┳┓  ┓   ┏┓      ┓    ┓   ┓   ┓\n"
                 "┣┫┓┏┣┓┏┓┃┃┓┏┏┓┏┓┃┏  •┃  •┃  •┃\n"
                 "┻┛┗┻┗┛┗┛┗┻┗┻┗┻┛ ┛┗  •┛  •┛  •┛\n"
                 "This is my beloved SuperCollider setup\n"
                 "Enjoy, have fun: [ raphaelforment.fr ] \n";
    ^banner
 }

  *ready {
    var ready = "┓•          ┓             ╻\n"
                "┃┓┓┏┏┓  ┏┏┓┏┫┏┓  ┏┓┏┓┓┏┏  ┃\n"
                "┗┗┗┛┗   ┗┗┛┗┻┗   ┛┗┗┛┗┻┛  •\n";
    ^ready
  }

  *stop {
    var stop="    ╻╻  ┏┓    •   ╻╻\n"
             "    ┃┃  ┃┃┏┓┏┓┓┏  ┃┃\n"
             "    ••  ┣┛┗┻┛┗┗┗  ••\n";
    ^stop
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

  *getValueFromPattern {
    arg pattern, key, default;
    var keyIndex;
    try {
      if (pattern == nil, {
        ^default
      });
      keyIndex = pattern.indexOf(key);
      if (keyIndex.notNil) {
        ^pattern[keyIndex + 1]
      } {
        ^default
      }
    } {
      ^default
    }
  }

  *getQuantFromPattern {
    arg pattern;
   ^this.getValueFromPattern(pattern, 'quant', 4)
  }

  *getFadeFromPattern {
    arg pattern;
    ^this.getValueFromPattern(pattern, 'fade', 0.01)
  }



}
