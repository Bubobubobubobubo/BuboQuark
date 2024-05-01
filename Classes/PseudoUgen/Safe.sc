Safe {
  /*
  * This pseudo-ugen is meant to be used on chaotic or unpredictible
  * signals. It can help taming them, making sure that you don't blow
  * your ears or speakers.
  */
  *ar { arg signal;
      ^LeakDC.ar(Limiter.ar(signal))
  }
}
