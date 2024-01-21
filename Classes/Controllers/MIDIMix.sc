ControllerValue {

  /*
  * A ControllerValue represents a MIDI Controller value.
  * It has a minimum and maximum value, and a curve. This
  * is used to convert from the MIDI value to a value that
  * is considered usable by the user.
  *
  * The curve is similar to the one used by the Env object.
  */

  var <>min = 0;
  var <>max = 1;
  var <>curve = 0;
  var <>currentValue;
  var <>bipolar = false;

  *new {
    arg min, max, curve;
    ^super.new.init()
  }

  init {
    this.min = min;
    this.max = max;
    this.curve = curve;
    this.currentValue = Bus.control;
    this.bipolar = false;
  }

  set {
    arg value;
    // If bipolar is true, then the value must go from -1 to 1
    var conversion = value.lincurve(
      inMin: 0,
      inMax: 127,
      outMin: this.min.neg,
      outMax: this.max,
      curve: this.curve
    );
    this.currentValue.set(conversion);
    ^this.currentValue;
  }

}


MIDIControl {

  /*
  * This is my personal MIDI controller interface. I am using a
  * MIDIMix. It has 8 faders, 24 knobs, and 16 buttons. I am only
  * using the knobs and faders. Two buttons are used to change "bank"
  * (increments the CC number value).
  */

  var <>currentBank = 0;
  var <>values;

  *new {
      ^super.new.init()
  }

  init {
    this.values = IdentityDictionary.new();
    this.connect(); this.installCallbacks();
  }

  getInit {
    arg number;
    if (this.values[number] == nil) {
      this.values[number] = ControllerValue.new(
        min: 0, max: 127, curve: 0
      );
      ^this.values[number]
    } {
      ^this.values[number]
    }
  }

  setCurve {
    arg number, curve;
    this.getInit(number).curve = curve;
  }

  setBounds {
    arg number, min, max;
    var controller = this.getInit(number);
    controller.min = min;
    controller.max = max;
  }

  at {
    arg number;
    var control = this.getInit(number);
    var choices = (
      value: this.getInit(number).currentValue.getSynchronous,
      bus: this.getInit(number).currentValue,
      map: this.getInit(number).currentValue.asMap,
      kr: In.kr(this.getInit(number).currentValue),
    );
    ^choices
  }

  connect {
    MIDIClient.init;
    MIDIIn.connectAll(verbose: true);
  }

  installCallbacks {
    MIDIIn.addFuncTo(\control, {
      arg src, chan, num, val;
      ("CONTROL:" + (num + (this.currentBank * 24)) + "=>" + val).postln;
      this.getInit(num + (this.currentBank * 24)).set(val);
    });
    MIDIIn.addFuncTo(\noteOn, {
      arg src, chan, num, val;
      "Changing bank".postln;
      if (chan == 8 && num == 22) {
        if (this.currentBank > 0) {
          this.currentBank = this.currentBank - 1;
        };
        this.currentBank.postln;
      };
      if (chan == 8 && num == 24) {
        if (this.currentBank < 3) {
          this.currentBank = this.currentBank + 1;
        };
        this.currentBank.postln;
      };
    });
  }
}


