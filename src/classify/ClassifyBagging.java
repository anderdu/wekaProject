package classify;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import params.BaggingParams;
import weka.classifiers.Classifier;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.J48;

public class ClassifyBagging {
	
	
	public static void parametroEkorketa(File parameters,File train) throws Exception {
		
		// parametroen balioak sartzeko araylistak
		ArrayList<Classifier> classifiers = new ArrayList<Classifier>();
		ArrayList<Integer> bagSizePercent = new ArrayList<Integer>();
		ArrayList<String> batchSize = new ArrayList<String>();
		ArrayList<Boolean> calcOutOfBag = new ArrayList<Boolean>();
		ArrayList<Boolean> debug = new ArrayList<Boolean>();
		ArrayList<Boolean> doNotCheckCapabilities = new ArrayList<Boolean>();
		ArrayList<Integer> numDecimalPlaces = new ArrayList<Integer>();
		ArrayList<Integer> numExecutionSlots = new ArrayList<Integer>();
		ArrayList<Integer> numIterations = new ArrayList<Integer>();
		String pvisualTracking = "non";
		String model = "TF-IDF";
		
		// arraylist-ak bete
	
		
		
		
		//klasifikadorea sortu 
		Bagging bag = new Bagging();
		J48 j = new J48();
		bag.setClassifier(j);
		
		//Informazioa erakusteko
		PrintStream terminal = new PrintStream(new FileOutputStream(FileDescriptor.out));
		PrintStream BaggingParams = new PrintStream(train.getParent()+File.separatorChar+"BaggingParams.txt");//todos los parametros que se hayan probado
		PrintStream BaggingConf = new PrintStream(train.getParent()+File.separatorChar+"Bagging.conf"); //la mejor conbinacion de parametros para despues usarlos para clasificar
		System.setOut(new PrintStream(train.getParent()+File.separatorChar+"Bagging.log"));
		long startTime = System.nanoTime();

		
		//parametro ekorketa
		for (Classifier classifier:classifiers) {
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
										//Double holdOutValue = Evaluators.holOut(train, bag, 5, 70,pvisualTracking,model);
										//Double kFOldValue = Evaluators.kFold(train, bag, 10,pvisualTracking,model);
										//Double rebValue = Evaluators.resubstitution(train, bag,pvisualTracking,model);
										Double holdOutValue=0.0;
										Double kFOldValue=0.0;
										Double rebValue=0.0;
										BaggingParams val = new BaggingParams("j48", bagS, batS, calOB, deb, doNCap, numDec, 
												numEx, numIt, holdOutValue, kFOldValue, rebValue);
										
										//hold out
										Double holOutVal = Evaluators.holOut(train, bag, 5, 70,pvisualTracking,model);
										long holOutTime = (System.nanoTime()-startTime)/1000000;
										terminal.println("holOut time milliseconds  :" + holOutTime);
										terminal.println(holOutVal);
										
										//kfold
										startTime = System.nanoTime();
										Double kFoldVal = Evaluators.kFold(train, bag, 10,pvisualTracking,model);
										long kFoldTime = (System.nanoTime()-startTime)/1000000;
										terminal.println("kFold time milliseconds  :" + kFoldTime);
										terminal.println(kFoldVal);
										
										//resubstitution
										startTime = System.nanoTime();
										Double resubstitutionVal = Evaluators.resubstitution(train, bag,pvisualTracking,model);
										long rebTime = (System.nanoTime()-startTime)/1000000;
										terminal.println("resubstitutionVal time milliseconds  :" + rebTime);
										terminal.println(resubstitutionVal);
										
									}
								}
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		}
		terminal.close();
		BaggingParams.close();
		BaggingConf.close();
		
		
	}

}
