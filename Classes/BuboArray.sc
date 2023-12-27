+ Array {
  pat {
    arg node_proxy, quant=4, fade=0.05;
    node_proxy.quant_(quant);
    node_proxy.fadeTime = fade;
    node_proxy[0] = Pbind(*this);
    ^node_proxy;
  }

	pbind {
		^Pbind(*this)
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