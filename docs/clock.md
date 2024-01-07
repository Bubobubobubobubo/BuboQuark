# LinkClock

SuperCollider is using a clock to sequence events in time. When you boot the
interpreter, there is a clock created by default, accessible through the `TempoClock.default` command. This clock is local and specific to your server. However, thanks to the client / server architecture of SuperCollider, it is quite easy to implement networked clocks that can be shared between peers. This is what the `LinkClock` class is doing through the _Ableton Link_ protocol. You just have to swap the base clock with a `LinkClock` instance and you are ready to go. This is what BuboQuark is doing.

The `LinkClock` is accessible through the `c` global variable. Be careful not to
override it by mistake! It behaves like the regular default `TempoClock` with the typical methods from this class. The main difference is that this timing information is shared with other peers on the network. You can synchronize easily with your friends ... basically by doing nothing if their applications are also using the same protocol.

**Note:** The few additional methods are already very well documented. 

**Note 2:** For _live coding_, you will need to use the clock fairly often to synchronize your time-based effects with it or to write various routines.

##### Setting / Getting the tempo

Use `c.tempo` to set or get the current tempo. Note that it _will_ change the tempo of the other peers. Be careful if you are playing with other people as they will probably not like it very much. There is also a `.beats` and `.quantum` method that can be used for other time-based calculations but I almost never use them.

##### Beat duration

Use `c.beatDur` to get the duration of a beat. This is so useful that I have
created a shortcut for it: `c.dur`. It is a very common thing to use in audio
functions. Take a look at the following excerpt:

```supercollider
(
~wind = {
  var wind = PinkNoise.ar() * LFNoise1.kr(c.dur * 4).range(0.01, 0.3).varlag(0.1);
  wind = Pan2.ar(wind, LFNoise1.kr(c.dur * 2).range(-1, 1));
  wind = RLPF.ar(wind, LFNoise1.kr(c.dur * 2).range(200, 2000),
         LFNoise1.kr(1).range(0.2, 0.9));
  wind = [BPF.ar(wind, LFNoise1.kr(c.dur / 4).range(40, 2000)),
          BPF.ar(wind, LFNoise1.kr(c.dur / 2).range(40, 2000))];
  wind[1] = DelayC.ar(wind[1], 2, LFNoise1.kr(1).range(0.025, 0.3));
  wind = JPverb.ar(wind, size: 40, t60: 4 );
  wind[0] = wind[0] * LFNoise1.kr(c.dur * 4).range(0.01, 0.3).varlag(0.1);
  wind[1] = wind[1] * LFNoise1.kr(c.dur * 2).range(0.01, 0.3).varlag(0.1);
  wind ! 2 * 2
}
)
```
This is a wind sound texture that is also synchronised with the tempo thanks to
`c.dur`.
