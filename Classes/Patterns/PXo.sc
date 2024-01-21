Pxo {
  *new {
    arg expression, divisor=1, repeats=inf;
    var xoExp = Array.new(maxSize: expression.size);
    expression.do({
      arg char, i;
      if (char == $x, {xoExp.add(1, i)}, {xoExp.add(Rest(), i)})
    });
    xoExp = xoExp.collect({arg item; item / divisor});
    ^Pseq.new(xoExp, repeats)
  }
}


Ptx {
  *new {
    arg expression, repeats=inf;
    var xoExp = Array.new(maxSize: expression.size);
    expression.do({
      arg char, i;
      switch(char)
        {$@} {xoExp.add(2, i)}
        {$O} {xoExp.add(1, i)}
        {$o} {xoExp.add(0.5, i)}
        {$°} {xoExp.add(0.25, i)}
        {} {xoExp.add(Rest(), i)};
    });
    ^Pseq.new(xoExp, repeats)
  }
}
