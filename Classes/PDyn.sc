Pdyn {
  *new {
    arg function, hash;
    if (hash.isNil) { hash = 10.collect({
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".choose })
      .join("").asSymbol;
    }
    ^Pfunc({Ndef(hash, function).asMap})
  }
}
