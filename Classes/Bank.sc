/*
* This file is taken from: https://gist.github.com/scztt/73a2ae402d9765294ae8f72979d1720e
* I have added a method to list the samples in the bank.
*/

Bank : Singleton {
	classvar <>root, <>extensions, <>lazyLoading=true;
	var <paths, buffers, <channels, <foundRoot, <foundRootModTime, markersCache, atCache;

	*initClass {
		root = "/Users/bubo/.config/livecoding/samples";
		extensions = ["wav", "aiff", "aif", "flac", "mp3"];
	}

  *list {
    PathName(Bank.root).entries.do({
    arg item; item.folderName.postln;
    })
  }


	*new {
		|path, channels|
		^super.new(path, channels);
	}

	printOn {
		|stream|
		super.printOn(stream);
		stream << "[%]".format(paths.size)
	}

	init {
		buffers = [];
		ServerQuit.add(this);
		ServerBoot.add(this);
	}

	lazyLoading_{
		|lazy|
		if (lazyLoading != lazy) {
			lazyLoading = lazy;
			this.prUpdateBuffers();
		}
	}

	buffers {
		^paths.size.collect {
			|i|
			this.bufferAt(i)
		}
	}

	gui {
		var view, button, name;

		this.lazyLoading = false;

		view = View().layout_(GridLayout.rows());
		paths.do {
			|path, i|
			name = PathName(path).fileNameWithoutExtension;
			view.layout.add(
				DragSource()
					.object_("%(%)[%]".format(
						this.class.name,
						channels !? {
							"'%', %".format(this.name, channels)
						} ?? {
							"'%'".format(this.name)
						},
						name.quote
					))
					.string_(name)
					.canFocus_(true)
					.font_(Font("M+ 2c", 10, false))
					.minWidth_(100)
					.mouseDownAction_({ |v| if (v.focus) { this.bufferAt(i).play } })
					.keyDownAction_({ this.bufferAt(i).play })
					.focusGainedAction_({ this.bufferAt(i).play }),
				(i / 4).floor,
				i % 4
			);
		};
		ScrollView(bounds:500@600).canvas_(view).front;
	}

	set {
		|inChannels|
		var currentRoot, currentExtensions, foundPaths=[], attempts = List();

		if (channels != inChannels) {
			channels = inChannels;
			this.clear();
		};

		if (foundRootModTime.notNil) {
			if (File.mtime(foundRoot) == foundRootModTime) {
				^this; // no changes, so early return!
			}
		};

		currentExtensions = this.class.extensions;
		currentRoot = thisProcess.nowExecutingPath;

		if (currentRoot.notNil) {
			currentRoot = PathName(currentRoot).parentPath;
			foundPaths = Require.resolvePaths(name.asString, currentRoot, currentExtensions, attempts);
		};

		if (currentRoot.notNil) {
			currentRoot = currentRoot +/+ name.asString;
			foundPaths = Require.resolvePaths("*", currentRoot, currentExtensions, attempts);
		};

		if (foundPaths.isEmpty) {
			currentRoot = this.class.root;
			foundPaths = Require.resolvePaths(name.asString, currentRoot, currentExtensions, attempts);
		};

		if (foundPaths.isEmpty) {
			currentRoot = currentRoot +/+ name.asString;
			foundPaths = Require.resolvePaths("*", currentRoot, currentExtensions, attempts);
		};

		if (foundPaths.isEmpty) {
			foundRoot = nil;
				foundRootModTime = nil;
			"No samples found, attempted paths: ".warn;
			attempts.do {
				|a|
				"\t%.{%}".format(a, currentExtensions.join(",")).warn
			};
		} {
			foundRoot = currentRoot;
			foundRootModTime = File.mtime(foundRoot) ?? {0};
		};

		foundPaths = foundPaths.sort({
			|a, b|
			var pair;
			#a, b = [a, b].collect {
				|p|
				p = p.toLower;
				p = p.split($.).first;
				p = p.split($/).reverse;
			};
			pair = [a, b].flop.detect({
				|pair|
				pair[0] != pair[1]
			});
			pair !? {
				pair[0] < pair[1]
			} ?? { false }
		});

		if (paths != foundPaths) {
			paths = foundPaths;
			atCache = ();
			this.prUpdateBuffers();
		}
	}

	clear {
		paths = [];
		atCache = ();
		this.prUpdateBuffers()
	}

	bufferAt {
		|index|
		^buffers !? {
			buffers[index] ?? {
				if (Server.default.serverRunning) {
					if (channels.isNil) {
						buffers[index] = Buffer.read(Server.default, paths[index])
					} {
						buffers[index] = Buffer.readChannel(Server.default, paths[index], channels:Array.series(channels));
					};
				};

				buffers[index];
			}
		}
	}

	at {
		|key|
		var index;

		if (key.isArray && { key.isString.not }) {
			^key.collect(this.at(_))
		};

		if (key.isInteger) {
			index = key
		} {
			index = atCache[key.asSymbol];
			if (index.isNil) {
				index = paths.detectIndex({
					|path|
					key.asString.toLower.replace("*", ".*").matchRegexp(
						path.asString.toLower
					);
				});
				atCache[key.asSymbol] = index;
			}
		};

		^this.bufferAt(index);
	}

	markers {
		^markersCache ?? {
			markersCache = paths.collect({
				|path|
				SoundFile(path).extractMarkers
			})
		}
	}

	wrapAt {
		|index|
		if (index.isInteger) {
			index = index % buffers.size;
		};
		^this.at(index);
	}

	do 			{ |...args| buffers.size.collect(this.bufferAt(_)).do(*args) }
	collect 	{ |...args| ^buffers.size.collect(this.bufferAt(_)).collect(*args) }

	prUpdateBuffers {
		if (Server.default.serverBooting or: {
			Server.default.hasBooted && Server.default.serverRunning.not
		}) {
			Server.default.doWhenBooted {
				this.prUpdateBuffers();
			};
			^this;
		};

		if (Server.default.serverRunning.not) {
			buffers = [];
		} {
			if (paths.size > buffers.size) { buffers = buffers.extend(paths.size) };

			paths.do {
				|path, i|
				var buffer;

				buffer = buffers[i];

				if (path.notNil) {
					if (lazyLoading.not) {
						this.bufferAt(i)
					}
				} {
					if (buffer.notNil) {
						buffer.free;
						buffers[i] = buffer = nil;
					}
				}
			};

			buffers.extend(paths.size);
		}
	}

	doOnServerBoot {
		if (paths.size > 0) {
			buffers = [];
			this.prUpdateBuffers();
			"***Loaded samples for %***".format(this.asString).postln;
		}
	}

	doOnServerQuit {
		buffers = [];
	}

	pat {
		|keyPat|
		^Pindex(Pseq([this], inf), keyPat)
	}

	// Single buffer support
	asBuffer 		{ 				^this.singleSampleWrap(nil) }
	asControlInput  { |...args| 	^this.prSingleSampleWrap(\asControlInput, *args) }
	play 			{ |...args| 	^this.prSingleSampleWrap(\play, *args) }

	prSingleSampleWrap {
		|method ...args|
		var buffer;
		if (buffers.size == 1) {
			buffer = this.bufferAt(0);

			if (method.isNil) {
				^buffer
			} {
				if (buffer.numFrames.isNil) {
					fork {
						Server.default.sync;
						buffer.performList(method, args)
					};
					^nil;
				} {
					^buffer.performList(method, args)
				}
			}
		} {
			Error("Trying to % a bank with multiple buffers".format(method)).throw;
		}
	}
}
