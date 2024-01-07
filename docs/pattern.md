# Pattern shortcuts

SuperCollider has a very large and powerful collection of Pattern classes. They allow you to create pretty much every musical pattern you can imagine but it comes with a cost: it is cumbersome to write and read. This is a known problem
since a long time ago. Many live coding frameworks have been created just to
break free of the rigid pattern syntax used by SuperCollider. BuboQuark is not meant to replace the Pattern classes entirely. This would be a bit presumptuous but also counter-productive: there is so much you can do with it already!

I just bring a few shortcuts to make it easier to write and read patterns whenever possible. Once again, there is a strong reliance on arrays to do so. This is not the state of the art approach but it is a good compromise between simplicity and flexibility. Once again, I do not break compatibility with the base language. 

##### Pdv to write melodies/rhythms

There is a little rather unknown Quark called [Pdv](https://github.com/dmorgan-github/Pdv). This Quark brings a new method to parse a string into a pattern, inspired by the [TidalCycles](https://tidalcycles.org) syntax. There are other Quarks doing just the same thing like [Bacalao](https://github.com/totalgee/bacalao) but they are a little bit more involved. They expect you to adhere to the way the framework works, departing from the base syntax.

**Pdv** is super transparent. It just adds a new method to use on the `deg:` key of your patterns. `Pdv` will take care of turning that list into the `dur` and `degree` keys, facilitating the writing of rhythms and melodies. I have assocciated this method with the `.p` method of the string class. It means that you can use the following syntax to write patterns:

```supercollider
(
[
  "fmintro", i: "fm",
  // Look at the following line!
  deg: "0^4 ~^4 {0 5 7}^4 ~ -5^4 ~^4 {0 5 8 10}^4 ~".p,
  o: [4, 5], db: -20, attack: [0.1, 0.3].pwhite,
].pat.play;
)

```

Note that using `pdv` will override the `degree`/`note` and `deg` keys. You can use it or ignore it depending on what you have planned but it is a good thing to have it ready to be used :) You could write the whole documentation of `Pdv` on a napkin:

```shell
" "    - empty space separates beats/values
~      - rest
[]     - beat sub division or group
<>     - alternating values
{}     - chord values
^n     - stretch duration - where n is a float
!n     - repeat value - where n is an integer
$      - shuffle group of values
?n     - chance of value or rest - optional probability is specified with n as an integer 0-9
#(nnn) - choose one value from preceeding group of values, optional weights are specified within parens where n is an integer 0-9
|      - can be used as visual separator to help readability
,      - can be used as visual separator to help readability
```

##### Existing array shortcuts

These shortcuts are syntax sugar for turning a list into a pattern (_e.g._ `[1, 2, 3].pseq`). They work just like the regular Pattern type they correspond to.

  - `pseq(repeats=inf, offset=0)`: shortcut for `Pseq`
  - `pshuf(repeats=1)`: shortcut for `Pshuf`
  - `prand(repeats=inf)`: shortcut for `Prand`
  - `pxrand(repeats=inf)`: shortcut for `Pxrand`
  - `pwrand(weights, repeats=1)`: shortcut for `Pwrand`
  - `pwhite(repeats=inf)`: shortcut for `Pwhite`
  - `pseries(repeats=inf)`: shortcut for `Pseries`

##### Euclidian rhythms

There is a Quark named Pborklund that brings euclidian rhythms to the SuperCollider pattern library. Do you see yourself typing `Pbjorklund2(5, 8, inf)` everytime you want to use it? I don't. I have created a shortcut for it: `Eu(5, 8, inf)`. That's it. I don't have anything more to say about it.

