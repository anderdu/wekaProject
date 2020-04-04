package classify;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import procesing.MakeCompatible;
import procesing.TransformRaw;
import util.AppUtils;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.Bagging;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

public class Evaluators {
	
	public static Double kFold(File train,Classifier classificador, Integer partitions,String pvisualTracking,String model) throws Exception {
		/*
		 * in: 
		 *    train.arff
		 *    erafiliko dugun clasifikadorea parametroak definituta daudelarik
		 *    partiketa kopurua
		 * out:
		 *    klase minoritarioren fmeasure
		 */
		System.out.println("============  using evaluator kFold   ==============");
		File[] results = new File[2];
		results = TransformRaw.transformRaw(train, pvisualTracking, model);
		File trainBOWall = results[1];
		File trainBOW = FeatureSubSel.apply(trainBOWall, "0");
		Instances data = null;
		DataSource source = new DataSource(trainBOW.getAbsolutePath());
		data = source.getDataSet();
		data.setClassIndex(data.numAttributes()-1);
		int minIndex = AppUtils.minIndex(data);
		
		Evaluation eval = new Evaluation(data);
		classificador.buildClassifier(data);
		
		System.out.println("Datu gehigarriak");
		eval.crossValidateModel(classificador, data, partitions, new Random(1));
		System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		System.out.println(eval.toClassDetailsString());
		System.out.println(eval.toMatrixString());
		
		
		return eval.fMeasure(minIndex);
	}
	public static Double holOut(File fTrain,Classifier classificador, Integer iterations, Integer percentage, String pvisualTracking, String model) throws Exception {
		System.out.println("============  using evaluator holOut   ==============");

		ArrayList<Double> fMeasureValues = new ArrayList<Double>();
		
		for (int i = 0; i < iterations; i++) { //cantidad de veces que hacemos hold out
			Instances data = AppUtils.file2instances(fTrain.getAbsolutePath(), "0");//arff fitxategi baten instantziak lortzen ditu 
			Instances[] trainANDtest= datuakZatitu(data,percentage);//datu partiketa metodo baten konprimituta

			File fileTrain = AppUtils.ordenagailuanGorde(trainANDtest[0],new File(fTrain.getParent()+File.separatorChar+"train.arff"));
			File fileDev = AppUtils.ordenagailuanGorde(trainANDtest[1],new File(fTrain.getParent()+File.separatorChar+"dev.arff"));
			
			File[] dictANDbow = new File[2];
			dictANDbow = TransformRaw.transformRaw(fileTrain, pvisualTracking, model);
			File fileTrainBOW = dictANDbow[1];
			
			Instances insTrainBOW = AppUtils.file2instances(fileTrainBOW.getAbsolutePath(), "0");
			
			Instances trainBOWsmall = FeatureSubSel.apply(insTrainBOW);
			File fTrainBOWsmall = AppUtils.ordenagailuanGorde(trainBOWsmall,fileTrainBOW);
			File smallDict = AppUtils.bow2dict(fileTrainBOW.getAbsolutePath());
			File fileDevCOMP = MakeCompatible.makeCompatible(fileDev, smallDict);
			Instances devCOMP = AppUtils.file2instances(fileDevCOMP.getAbsolutePath(), "0");
			
			devCOMP.setClassIndex(devCOMP.numAttributes()-1);
			trainBOWsmall.setClassIndex(trainBOWsmall.numAttributes()-1);
			
			int minIndex = AppUtils.minIndex(trainBOWsmall);
			
			Evaluation eval = new Evaluation(devCOMP);
			classificador.buildClassifier(trainBOWsmall);
			System.out.println(" - para evaluar modelo:");
			System.out.println("\t test:  "+fileDevCOMP.getAbsolutePath());
			System.out.println("\t train: "+fTrainBOWsmall.getAbsolutePath());
			eval.evaluateModel(classificador,devCOMP);
			
			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toMatrixString());
			
			System.out.println("------->    fMeasure:    "+eval.fMeasure(minIndex));
			System.out.println(eval.fMeasure(0));
			System.out.println(eval.fMeasure(1));
			System.out.println(eval.fMeasure(2));
			System.out.println("     "+eval.recall(2));
			System.out.println("     "+eval.precision(2));
			System.out.println(eval.fMeasure(3));
			System.out.println(eval.fMeasure(4));
			System.out.println(eval.fMeasure(5));
			fMeasureValues.add(eval.fMeasure(minIndex));
			
			//System.exit(0);
		}
		return AppUtils.getMean(fMeasureValues);
	}
	
	public static void holdOutPrueba(String train, String test) {
		/*
		 * clase para porbart hold out de forma distinta a la de programa
		 * 
		 * 
		 */
		Instances insTrain = null;
		Instances insTest = null;
		try {
			insTrain = AppUtils.file2instances(train, "0");
			insTest = AppUtils.file2instances(test, "0");
		} catch (Exception e) {
			System.out.println("error leyendo archivos");
		}
		
		try {
			Bagging classificador = new Bagging();
			Evaluation eval = new Evaluation(insTest);
			classificador.buildClassifier(insTrain);
			eval.evaluateModel(classificador,insTest);
		} catch (Exception e) {
			System.out.println("problema evaluar");
		}
		

	}
	
	public static Double resubstitution(File train,Classifier classificador,String pvisualTracking,String model) throws Exception {
		System.out.println("============  using evaluator resubstitution   ==============");

		
		File[] results = new File[2];
		results = TransformRaw.transformRaw(train, pvisualTracking, model);
		File fileTrainBOW = results[1];
		
		Instances trainBOWall = AppUtils.file2instances(fileTrainBOW.getAbsolutePath(), "0");
		Instances trainBOW = FeatureSubSel.apply(trainBOWall);

		Evaluation eval = new Evaluation(trainBOW);
		classificador.buildClassifier(trainBOW);
		eval.evaluateModel(classificador,trainBOW);
		int minIndex = AppUtils.minIndex(trainBOW);

		System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		System.out.println(eval.toClassDetailsString());
		System.out.println(eval.toMatrixString());
		
		return eval.fMeasure(minIndex);
		
	}
	
	private static Instances[] datuakZatitu(Instances data, Integer percentage) throws Exception {
		Instances[] emaitza = new Instances[2];
		Instances train;
		Instances test;
		data.randomize(new Random(1));
		RemovePercentage zatiFiltro = new RemovePercentage();
		
		zatiFiltro.setInputFormat(data);
		zatiFiltro.setPercentage(percentage);
		zatiFiltro.setInvertSelection(false);
		train = Filter.useFilter(data, zatiFiltro);
		
		zatiFiltro.setInputFormat(data);
		zatiFiltro.setPercentage(percentage);
		zatiFiltro.setInvertSelection(true);
		test = Filter.useFilter(data, zatiFiltro);
		
		emaitza[0]=train;
		emaitza[1]=test;
		
		return emaitza;
	}

}
