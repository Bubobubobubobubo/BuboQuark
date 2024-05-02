~a => [sp: ["kick", \].pseq(inf), nb: 0, db: 0];

~a.play;

~a.clear;

Scope()

(
var sampleStr = "kick:2(1) snare:4(1/4) . hihat:1(1/8) .";
a = { |sampleStr, defaultRestDuration = 1.0|
    var pattern = "(\\w+):(\\d+)\\(([\\d/]+)\\)|\\.";
    var matches = sampleStr.findAllRegexp(pattern);
    matches.collect { |match|
        if(match.size == 1) {
            [ 'sp': 'rest', 'nb': 0, 'dur': defaultRestDuration ]
        } {
            var sampleName = match[0];
            var sampleNumber = match[1].asInteger;
            var duration = match[2];
            var durationValue;
            if(duration.contains("/")) {
                var nums = duration.split("/");
                durationValue = nums[0].asInteger / nums[1].asInteger;
            } {
                durationValue = duration.asFloat;
            };
            [ 'sp': sampleName, 'nb': sampleNumber, 'dur': durationValue ]
        }
    }
};
a.value(sampleStr);
)
