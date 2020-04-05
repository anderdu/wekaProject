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
		//File f = new File("C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\procesedFiles\\trainFull.arff");
		File f = new File(args[0]);
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
		/*
		classifiers.add(new DecisionStump());
		classifiers.add(new LMT());
		classifiers.add(new M5P());
		classifiers.add(new RandomForest());
		classifiers.add(new RandomTree());
		classifiers.add(new REPTree());//ts
		*/
		
		//bagSizePercent.add(100);//st
		//bagSizePercent.add(150);
		bagSizePercent.add(50);
		bagSizePercent.add(40);
		bagSizePercent.add(30);
		bagSizePercent.add(20);
		bagSizePercent.add(10);
		
		batchSize.add("100");//st
		//batchSize.add("150");
		batchSize.add("50");
		
		numDecimalPlaces.add(2);//st
		numDecimalPlaces.add(5);
		numDecimalPlaces.add(7);
		numDecimalPlaces.add(10);
		
		numExecutionSlots.add(1);//st
		numExecutionSlots.add(10);
		numExecutionSlots.add(20);
		numExecutionSlots.add(30);
		numExecutionSlots.add(40);
		
		numIterations.add(10);//st
		numIterations.add(20);
		numIterations.add(30);
		numIterations.add(40);
		
		seed.add(1);//st
		seed.add(0);
		seed.add(2);
		seed.add(5);
		seed.add(7);
		
		//Informazioa erakusteko
		PrintStream terminal = new PrintStream(new FileOutputStream(FileDescriptor.out));
		PrintStream error = new PrintStream(train.getParent()+File.separatorChar+"params error.txt");
		PrintStream baggingParams = new PrintStream(train.getParent()+File.separatorChar+"BaggingParams.txt");//todos los parametros que se hayan probado
		//PrintStream BaggingConf = new PrintStream(train.getParent()+File.separatorChar+"Bagging.conf"); //la mejor conbinacion de parametros para despues usarlos para clasificar
		System.setOut(new PrintStream(train.getParent()+File.separatorChar+"Bagging_parametro_ekorketa.log"));
		long startTime = System.nanoTime();

		ArrayList<Double> emaitzak = new ArrayList<Double>();
		baggingParams.println(BaggingParams.getHead());
		terminal.println(BaggingParams.getHead());
		int exceptCounter = 0;
		int cont = 0;
		Double[] emait = new Double[2];
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
								Double tpr=0.0;
								System.out.println();
								//hold out
								String classifieName = classifier.getClass().getName().split("\\.")[3];
								if(classifieName.length()<4) classifieName= classifieName+"\t\t";
								if(classifieName.length()<8) classifieName= classifieName+"\t";
								BaggingParams val = new BaggingParams(classifieName, bagS, batS, numDec, numEx, numIt, holOutVal,tpr,se);
								try {
									System.out.println(cont);
									cont++;
									System.out.println("     **** parametros de la prueba   : "+val.toString());
									emait = Evaluators.holOut2(train, bag, 1, 70,pvisualTracking,model);
									holOutVal = emait[0];
									tpr = emait[1];
								} catch (Exception e) {
									exceptCounter++;
									error.println();
								}
								long holOutTime = (System.nanoTime()-startTime)/1000000000;
 								//emaitzak.add(holOutVal);
								val = new BaggingParams(classifieName, bagS, batS, numDec, numEx, numIt, holOutVal,tpr,se);
								baggingParams.print(val.toString());
								baggingParams.println("\t\t    execution time seconds  :" + holOutTime);
								terminal.print(val.toString());
								terminal.println("\t\t    execution time seconds  :" + holOutTime);
								emaitzak.add(holOutVal);
							}}}}}}}
		System.out.println("errors count  :   "+exceptCounter);
		terminal.close();
		baggingParams.close();
		
		
	}

}
	
