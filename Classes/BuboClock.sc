+ Clock {

  dur {
    ^this.beatDur
  }

  mod {
    arg modulo;
    ^this.beats % modulo
  }

  modbar {
    arg modulo;
    ^this.bar % modulo
  }

}
