+ String {

  /*
  * Interface with Bank.sc to return a sample faster
  */
  sp {
    arg sampleNumber = 0, repeats=inf;
    ^Pindex(Bank(this), sampleNumber, repeats);
  }

}
