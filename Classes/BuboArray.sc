+ Array {

  sp {
    arg repeats=inf;
    var pat;
    if (this[0].isString,
      { pat = Pseq([this[0]], inf).collect({|i| Bank(i)}); },
      { pat = this[0].collect({|i| Bank(i)})}
    );
    ^Pindex(pat, this[1], repeats)
  }

  pat {
    arg quant=4, fade=0.05;
    var proxyName = this[0];
    var newArray = this[1..] ++ [\type, \buboEvent];
    currentEnvironment.at(proxyName.asSymbol).quant_(quant);
    currentEnvironment.at(proxyName.asSymbol).fadeTime = fade;
    currentEnvironment.at(proxyName.asSymbol)[0] = Pbind(*(EventShortener.findShortcuts(newArray)));
    ^currentEnvironment.at(proxyName.asSymbol);
  }

  pwrap {
    arg maxIndex, startIndex=0, repeats=inf;
    ^PwrapSeq(this, maxIndex, startIndex, repeats)
  }


	p {
		^Pbind(*(Eventshortener.findShortcuts(this)))
	}

  eu {
    arg repeats=inf;
    var divisor = 1;
    if (this[3] != nil, { divisor = this[3] });
    ^Pbjorklund2(this[0], this[1], repeats, this[2]) / divisor;
  }

  pseq {
    arg repeats=inf;
    ^Pseq(this, repeats);
  }

  pshuf {
    arg repeats=1;
    ^Pshuf(this, repeats);
  }

  prand {
    arg repeats=inf;
    ^Prand(this, repeats);
  }

  pxrand {
    arg repeats=inf;
    ^Pxrand(this, repeats);
  }

  pwrand {
    arg weights, repeats=1;
    ^Pwrand(this, weights.normalizeSum, repeats);
  }

  pwhite {
    arg repeats=inf;
    ^Pwhite(this[0], this[1], repeats);
  }

  pseries {
    arg repeats=inf;
    ^Pseries(this[0], this[1], repeats);
  }

  pbrown {
    arg repeats=inf;
    ^Pbrown(this[0], this[1], this[2], repeats);
  }
}
