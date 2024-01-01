+ String {
  sp {
    arg sampleNumber = 0, repeats=inf;
    ^Pindex(Bank(this), sampleNumber, repeats);
  }

  p {
    ^Pdv.parse(this)
  }
}
