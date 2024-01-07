# Server shortcuts

I tend to control the audio server and the interface directly from code without
any additional interface. I don't have to memorize a lot of shortcuts because I
can just evaluate the command I need to run. I have added a few shortcuts to
some of the most common tasks I do when experimenting or playing live.

### Clear everything

The following command will clear the current ProxySpace and get rid of all the
definitions:

```supercollider
currentEnvironment.clear;
```

### Panic mode

When you are playing live, you want to be able to stop everything **immediately**:

- `Panic()`: shortcut for `CmdPeriod.run`.

It will also print out a message in the post window about the current state of
the session. **Note:** if you are using Neovim, you can also just press `F12`. It is the same as pressing `Ctrl+.` in the IDE. I just tend to be focused on my keyboard so writing `Panic()` is totally fine.
