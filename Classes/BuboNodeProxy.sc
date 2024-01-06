+ NodeProxy {

    fx {
        arg number=1, wet=1, function = {|in| in};
        this[number] = \filter -> function;
        if (wet > 1, {wet = 1});
        this.set(("wet" ++ number).asSymbol, wet);
        ^this;
    }

    fx1 { arg wet, function; this.fx(100, wet, function); }
    fx2 { arg wet, function; this.fx(200, wet, function); }
    fx3 { arg wet, function; this.fx(300, wet, function); }
    fx4 { arg wet, function; this.fx(400, wet, function); }
    fx5 { arg wet, function; this.fx(500, wet, function); }
    fx6 { arg wet, function; this.fx(600, wet, function); }
    fx7 { arg wet, function; this.fx(700, wet, function); }
    fx8 { arg wet, function; this.fx(800, wet, function); }
    fx9 { arg wet, function; this.fx(900, wet, function); }

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
