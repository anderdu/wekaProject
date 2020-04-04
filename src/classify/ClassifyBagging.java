package classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;

import params.BaggingParams;
import util.AppUtils;
import util.ParamReader;
import weka.classifiers.Classifier;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.HoeffdingTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;

public class ClassifyBagging {
	public static void main(String[] args) throws Exception {
		AppUtils.disableWarning();
		File f = new File("C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\procesedFiles\\trainFull.arff");
		ClassifyBagging.parametroEkorketa(f);
	}
	
	
	public static void parametroEkorketa(File train) throws Exception {
		
		//klasifikadorea sortu 
		Bagging bag = new Bagging();
		
		
		
		// parametroen balioak sartzeko araylistak
		ArrayList<Classifier> classifiers = new ArrayList<>();
		ArrayList<Integer> bagSizePercent = new ArrayList<>();
		ArrayList<String> batchSize = new ArrayList<>();
		//ArrayList<Boolean> doNotCheckCapabilities = new ArrayList<>();
		ArrayList<Integer> numDecimalPlaces = new ArrayList<>();
		ArrayList<Integer> numExecutionSlots = new ArrayList<>();
		ArrayList<Integer> numIterations = new ArrayList<>();
		ArrayList<Integer> seed = new ArrayList<>();
		String pvisualTracking = "non";
		String model = "TF-IDF";
		
		classifiers.add(new J48());
		classifiers.add(new HoeffdingTree());
		classifiers.add(new DecisionStump());
		classifiers.add(new LMT());
		classifiers.add(new M5P());
		classifiers.add(new RandomForest());
		classifiers.add(new RandomTree());
		
		classifiers.add(new REPTree());//ts
		
		bagSizePercent.add(100);//st
		bagSizePercent.add(150);
		bagSizePercent.add(50);
		
		batchSize.add("100");//st
		batchSize.add("150");
		batchSize.add("50");
		
		numDecimalPlaces.add(2);//st
		numDecimalPlaces.add(5);
		
		numExecutionSlots.add(1);//st
		
		numIterations.add(10);//st
		
		seed.add(1);//st
		
		//Informazioa erakusteko
		PrintStream terminal = new PrintStream(new FileOutputStream(FileDescriptor.out));
		PrintStream baggingParams = new PrintStream(train.getParent()+File.separatorChar+"BaggingParams.txt");//todos los parametros que se hayan probado
		//PrintStream BaggingConf = new PrintStream(train.getParent()+File.separatorChar+"Bagging.conf"); //la mejor conbinacion de parametros para despues usarlos para clasificar
		System.setOut(new PrintStream(train.getParent()+File.separatorChar+"Bagging_parametro_ekorketa.log"));
		long startTime = System.nanoTime();

		ArrayList<Double> emaitzak = new ArrayList<Double>();
		terminal.println(BaggingParams.getHead());
		int exceptCounter = 0;
		//parametro ekorketa
		for (Classifier classifier:classifiers) {
		for (Integer bagS : bagSizePercent) {
			for (String batS : batchSize) {
				for (Integer se : seed) {
					for (Integer numDec : numDecimalPlaces) {
						for (Integer numEx : numExecutionSlots) {
							for (Integer numIt : numIterations) {
								bag.setClassifier(classifier);
								bag.setBagSizePercent(bagS);
								bag.setBatchSize(batS);
								bag.setSeed(se);
								bag.setNumDecimalPlaces(numDec);
								bag.setNumExecutionSlots(numEx);
								bag.setNumIterations(numIt);
								Double holOutVal=0.0;
								System.out.println();
								//hold out
								try {
									holOutVal = Evaluators.holOut(train, bag, 1, 70,pvisualTracking,model);
								} catch (Exception e) {
									exceptCounter++;
								}
								long holOutTime = (System.nanoTime()-startTime)/1000000000;
 								//emaitzak.add(holOutVal);
								
								String classifieName = classifier.getClass().getName().split("\\.")[3];
								if(classifieName.length()<8) classifieName= classifieName+"\t";
								BaggingParams val = new BaggingParams(classifieName, bagS, batS, numDec, numEx, numIt, holOutVal,se);
								baggingParams.println(val.toString());
								terminal.print(val.toString());
								terminal.println("\t\t    execution time seconds  :" + holOutTime);
								emaitzak.add(holOutVal);
							}}}}}}}
		terminal.close();
		baggingParams.close();
		
		
	}

}
	
