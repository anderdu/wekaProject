package params;

public class BaggingParams {
	private String classifier;
	private Integer bagSizePercent;
	private String batchSize;
	private Boolean calcOutOfBag;
	private Boolean debug;
	private Boolean doNotCheckCapabilities;
	private Integer numDecimalPlaces;
	private Integer numExecutionSlots;
	private Integer numIterations;
	private Double holdOutValue;
	private Double kFOldValue;
	private Double rebValue;
	
	public BaggingParams(String classifier, Integer bagSizePercent, String batchSize, Boolean calcOutOfBag, Boolean debug,
			Boolean doNotCheckCapabilities, Integer numDecimalPlaces, Integer numExecutionSlots, Integer numIterations,
			Double holdOutValue, Double kFOldValue, Double rebValue) {
		this.classifier = classifier;
		this.bagSizePercent = bagSizePercent;
		this.batchSize = batchSize;
		this.calcOutOfBag = calcOutOfBag;
		this.debug = debug;
		this.doNotCheckCapabilities = doNotCheckCapabilities;
		this.numDecimalPlaces = numDecimalPlaces;
		this.numExecutionSlots = numExecutionSlots;
		this.numIterations = numIterations;
		this.holdOutValue = holdOutValue;
		this.kFOldValue = kFOldValue;
		this.rebValue = rebValue;
	}
	
	public Double getHoldOutValue() {
		return holdOutValue;
	}


	public Double getkFOldValue() {
		return kFOldValue;
	}


	public Double getRebValue() {
		return rebValue;
	}


	public void headPrint() {
	System.out.format("%30s %30s %30s %30s %30s %30s %30s %30s %30s %30s %30s %30s",
			"classifier","bagSizePercent","batchSize","calcOutOfBag","debug",
			"doNotCheckCapabilities","numDecimalPlaces","numExecutionSlots","numIterations","holdOutValue",
			"kFOldValue","rebValue\n\n");
	}
	public void tPrint() {
		System.out.format("%30s %30s %30s %30s %30s %30s %30s %30s %30s %30s %30s %30s",
				classifier,bagSizePercent,batchSize,calcOutOfBag,debug,
				doNotCheckCapabilities,numDecimalPlaces,numExecutionSlots,numIterations,holdOutValue,
				kFOldValue,rebValue);
		System.out.println();
	}
	
	public String getHead() {
	return "classifier"+"\t"+"bagSizePercent"+"\t"+"batchSize"+"\t"+"calcOutOfBag"+"\t"+"debug"+"\t"+
			"doNotCheckCapabilities"+"\t"+"numDecimalPlaces"+"\t"+"numExecutionSlots"+"\t"+"numIterations"+"\t"+"holdOutValue"+"\t"+
			"kFOldValue"+"\t"+"rebValue";
	}
	
	public String toString() {
		return classifier+"\t"+bagSizePercent+"\t"+batchSize+"\t"+calcOutOfBag+"\t"+debug+"\t"+
		doNotCheckCapabilities+"\t"+numDecimalPlaces+"\t"+numExecutionSlots+"\t"+numIterations+"\t"+holdOutValue+"\t"+
		kFOldValue+"\t"+rebValue;
		
	}
}
