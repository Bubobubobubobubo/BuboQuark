+ String {

  /*
  * Interface with Bank.sc to return a sample faster
  */
  sample {
    arg sampleNumber = 2;
    ^Bank(this)[sampleNumber];
  }
}
