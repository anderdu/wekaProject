package classify;

import java.io.File;
import java.util.ArrayList;

import params.BaggingParams;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.J48;

public class ClassifyBagging {
	
	
	public static void parametroEkorketa(File parameters,File train) throws Exception {
		
		// parametroen balioak sartzeko araylistak
		ArrayList<Integer> bagSizePercent = new ArrayList<Integer>();
		ArrayList<String> batchSize = new ArrayList<String>();
		ArrayList<Boolean> calcOutOfBag = new ArrayList<Boolean>();
		ArrayList<Boolean> debug = new ArrayList<Boolean>();
		ArrayList<Boolean> doNotCheckCapabilities = new ArrayList<Boolean>();
		ArrayList<Integer> numDecimalPlaces = new ArrayList<Integer>();
		ArrayList<Integer> numExecutionSlots = new ArrayList<Integer>();
		ArrayList<Integer> numIterations = new ArrayList<Integer>();
		
		// arraylist-ak bete
	
		//klasifikadorea sortu 
		Bagging bag = new Bagging();
		J48 j = new J48();
		bag.setClassifier(j);
		
		//parametro ekorketa
		for (Integer bagS : bagSizePercent) {
			for (String batS : batchSize) {
				for (Boolean calOB : calcOutOfBag) {
					for (Boolean deb : debug) {
						for (Boolean doNCap : doNotCheckCapabilities) {
							for (Integer numDec : numDecimalPlaces) {
								for (Integer numEx : numExecutionSlots) {
									for (Integer numIt : numIterations) {
										bag.setBagSizePercent(bagS);
										bag.setBatchSize(batS);
										bag.setCalcOutOfBag(calOB);
										bag.setDebug(deb);
										bag.setDoNotCheckCapabilities(doNCap);
										bag.setNumDecimalPlaces(numDec);
										bag.setNumExecutionSlots(numEx);
										bag.setNumIterations(numIt);
										Double holdOutValue = Evaluators.holOut(train, bag, 5, 70);
										Double kFOldValue = Evaluators.kFold(train, bag, 10);
										Double rebValue = Evaluators.resubstitution(train, bag);
										BaggingParams val = new BaggingParams("j48", bagS, batS, calOB, deb, doNCap, numDec, 
												numEx, numIt, holdOutValue, kFOldValue, rebValue);

									}
								}
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
		
	}

}
