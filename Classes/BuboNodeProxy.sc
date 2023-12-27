+ NodeProxy {

    fx {
        arg number=1, wet=1, function = {|in| in};
        this[number] = \filter -> function;
        this.set(("wet" ++ number).asSymbol, wet);
        ^this;
    }

    fx1 { arg wet, function; this.fx(\wet100, wet, function); }
    fx2 { arg wet, function; this.fx(\wet200, wet, function); }
    fx3 { arg wet, function; this.fx(\wet300, wet, function); }
    fx4 { arg wet, function; this.fx(\wet400, wet, function); }
    fx5 { arg wet, function; this.fx(\wet500, wet, function); }
    fx6 { arg wet, function; this.fx(\wet600, wet, function); }
    fx7 { arg wet, function; this.fx(\wet700, wet, function); }
    fx8 { arg wet, function; this.fx(\wet800, wet, function); }
    fx9 { arg wet, function; this.fx(\wet900, wet, function); }

    wet { arg number=1, wet=1;
        this.set(("wet" ++ number).asSymbol, wet);
        ^this;
    }

    xwet { arg number=1, wet=1;
        this.xset(("wet" ++ number).asSymbol, wet);
        ^this;
    }

    fxin {
        arg number=1, wet=1, function = {|in| in};
        this[number] = \filterIn -> function;
        this.set(("wet" ++ number).asSymbol, wet);
        ^this;
    }
}
