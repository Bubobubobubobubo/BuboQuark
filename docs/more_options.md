# More options

BuboQuark is fine-tuned to work well on my system but you might want to set
different options for the server. SuperCollider already comes with the
[ServerOptions](https://doc.sccode.org/Classes/ServerOptions.html) class to help
with that. It is meant to be used like a set of options you can pass to your
server when you boot it.

```supercollider
o = Server.default.options; // Gathering default options
o.outDevice = "BlackHole 16ch"; // Changing the output
Boot(serverOptions: o); // Booting with the new options
```

With this simple argument, you can have different boot options for each setup
you might have. Just store your configuration somewhere and pass it to the boot
method. You are not stuck with my particular defaults :)
