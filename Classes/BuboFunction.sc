+ Function {

  f {
    arg repeats=inf;
    ^Pfunc(this, inf)
  }

  fx {
      arg nodeProxyName, number, wet=1;
      if (wet > 1, {wet = 1});
      nodeProxyName[number] = \filter -> this;
      nodeProxyName.set(("wet" ++ number).asSymbol, wet);
    ^nodeProxyName;
  }

  fx1 {arg n, w=1; this.fx(n, 100, w) }
  fx2 {arg n, w=1; this.fx(n, 200, w) }
  fx3 {arg n, w=1; this.fx(n, 300, w) }
  fx4 {arg n, w=1; this.fx(n, 400, w) }
  fx5 {arg n, w=1; this.fx(n, 500, w) }
  fx6 {arg n, w=1; this.fx(n, 600, w) }
  fx7 {arg n, w=1; this.fx(n, 700, w) }
  fx8 {arg n, w=1; this.fx(n, 800, w) }
  fx9 {arg n, w=1; this.fx(n, 900, w) }

}
