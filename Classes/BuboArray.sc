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
    arg node_proxy, quant=4, fade=0.05;
    var newArray = this ++ [\type, \buboEvent];
    node_proxy.quant_(quant);
    node_proxy.fadeTime = fade;
    node_proxy[0] = Pbind(*newArray);
    ^node_proxy;
  }

	pbind {
		^Pbind(*this)
	}

  euclid {
    arg repeats=inf;
    ^Pbjorklund2(this[0], this[1], repeats);
  }

  pseq { arg repeats=inf, offset=0;
    ^Pseq(this, repeats, offset);
  }

  pshuf { arg repeats=1;
    ^Pshuf(this, repeats);
  }

  prand { arg repeats=inf;
    ^Prand(this, repeats);
  }

  pxrand { arg repeats=inf;
    ^Pxrand(this, repeats);
  }

  pwrand { arg weights, repeats=1;
    ^Pwrand(this, weights, repeats);
  }

  pwhite { arg repeats=inf;
    ^Pwhite(this[0], this[1], repeats);
  }
}
