FluidAmpFeature : FluidRTMultiOutUGen {

	const <features=#[\diff, \fast, \slow];
	classvar featuresLookup;

	*initClass {
		featuresLookup = Dictionary.with(*this.features.collect{|x,i| x->(1<<i)});
	}

	*prWarnUnrecognised {|sym| ("WARNING: FluidAmpFeature -" + sym + "is not a recognised option").postln}

	*prProcessSelect {|a|
		var bits;
		a.asBag.countsDo{|item,count,i|
			if(count > 1) { ("Option '" ++ item ++ "' is repeated").warn};
		};
		bits = a.collect{ |sym|
			(featuresLookup[sym.asSymbol] !? {|x| x} ?? {this.prWarnUnrecognised(sym); 0})
		}.reduce{|x,y| x | y};
		^bits
	}

	*ar { arg in = 0, select, fastRampUp = 1, fastRampDown = 1, slowRampUp = 100, slowRampDown = 100, floor = -144, highPassFreq = 85;

		var selectbits  =  select !? {this.prProcessSelect(select)} ?? {this.prProcessSelect(this.features)};
		^this.multiNew('audio', in.asAudioRateInput(this), selectbits, fastRampUp, fastRampDown, slowRampUp, slowRampDown, floor, highPassFreq)
	}

	init {arg ...theInputs;
		var numChannels;
		inputs = theInputs;
		numChannels = inputs.at(1).asBinaryDigits.sum;
		^this.initOutputs(numChannels,rate);
	}

	checkInputs {
		^this.checkValidInputs;
	}
}
