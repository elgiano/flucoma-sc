FluidKMeans : FluidManipulationClient {

	var <>k;

	*new {|server|
		var uid = UniqueID.next;
		^super.new(server,uid)!?{|inst|inst.init(uid);inst}
	}

	init {|uid|
		id = uid;
	}

	fit{|dataset,k, maxIter = 100, action|
		this.k = k;
		this.prSendMsg(\fit,
			[dataset.asSymbol, k,maxIter], action,
			[numbers(FluidMessageResponse,_,k,_)]
		);
	}

	fitPredict{|dataset, labelset, k, maxIter = 100, action|
		this.k = k;
		this.prSendMsg(\fitPredict,
			[dataset.asSymbol,labelset.asSymbol, k,maxIter],
			action,[numbers(FluidMessageResponse,_,k,_)]
		);
	}

	predict{ |dataset, labelset,action|
		this.prSendMsg(\predict,
			[dataset.asSymbol, labelset.asSymbol], action,
			[numbers(FluidMessageResponse,_,this.k,_)]
		);
	}

	predictPoint { |buffer, action|
		this.prSendMsg(\predictPoint,
			[buffer.asUGenInput], action,
			[number(FluidMessageResponse,_,_)]
		);
	}
}
