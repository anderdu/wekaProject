package params;

public class BaggingParams {
	
	private String classifier;
	private Integer bagSizePercent;
	private String batchSize;
	private Integer numDecimalPlaces;
	private Integer numExecutionSlots;
	private Integer numIterations;
	private Double holdOutValue;
	private Integer seed;
	
	public BaggingParams(String classifier, Integer bagSizePercent, String batchSize, Integer numDecimalPlaces, Integer numExecutionSlots, Integer numIterations,
			Double holdOutValue, Integer seed) {
		this.classifier = classifier;
		this.bagSizePercent = bagSizePercent;
		this.batchSize = batchSize;
		this.numDecimalPlaces = numDecimalPlaces;
		this.numExecutionSlots = numExecutionSlots;
		this.numIterations = numIterations;
		this.holdOutValue = holdOutValue;
		this.seed = seed;
	}
	
	public Double getHoldOutValue() {
		return holdOutValue;
	}


	public static String getHead() {
	return "classifier"+"\t\t"+"bagSizePercent"+"\t"+"batchSize"+"\t"+"numDecPla"+"\t"+"numExSlot"+"\t"+"numIterations"+"\t"+"seed"+"\t"+
			"\t"+"holdOutValue";
	}
	
	public String toString() {
		return classifier+"\t\t"+bagSizePercent+"\t\t"+batchSize+"\t\t"+numDecimalPlaces+"\t\t"+numExecutionSlots+"\t\t"+numIterations+"\t\t"+seed+"\t\t"+
		holdOutValue;
	}
}
