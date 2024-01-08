# Audio sample library

The [Boot()](boot.md) method adds a mechanism to load audio samples automatically. The samples are lazily loaded in memory. You can load a vast library of samples without using too much memory. The mechanism is written by Scott Carver and can be found [here](https://gist.github.com/scztt/73a2ae402d9765294ae8f72979d1720e). It was originally named **SAMP**. I renamed it to **Bank** not to break out too much with the naming conventions. 


### Browsing the sample bank

If your audio sample bank path is set right, you will have access to your bank
using **Bank**:
```supercollider
Bank.list // List of all the available sample folders
Bank('a/*') // List of all the samples in the 'a' folder
Bank('a/*')[0].play; // Playing the first sample in the folder
```

Note that you can also be more picky about the samples you want to load:
```supercollider
Bank('a/*')['clap'].play; // Play the first sample with 'clap' in its name
```

### Using samples in patterns

I have added a mechanism to automatically feed a sample to the default sampler when using patterns. That way, you don't have to type the `Bank` part all the time and can stay focused on your improvisation:

```supercollider 
[
  "using_samples",
  i: "s" // s is the default sampler
  sp: "kick", // Give a string or symbol (pattern or not)
  nb: 0 // Give a number (can be pattern too)
].pat.play
```


Note that there is no optional argument here. You need `sp` and `nb` for it to
work. You can use these arguments when using the abbreviated syntax for `Pbind`
but not for regular `Pbind`. In that case, you will need to use the good old
`buf: Bank('a/*')[0]` syntax.

Be careful. If you forgot one of the two arguments, it is likely that the last
selected sample will play instead. This is probably easy to fix but it is what
it is.
