package classify;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;


import procesing.MakeCompatible;
import procesing.TransformRaw;
import util.AppUtils;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

public class Evaluators {
	
	public static Double kFold(File train,Classifier classificador, Integer partitions) throws Exception {
		/*
		 * in: 
		 *    train.arff
		 *    erafiliko dugun clasifikadorea parametroak definituta daudelarik
		 *    partiketa kopurua
		 * out:
		 *    klase minoritarioren fmeasure
		 */
		
		String pvisualTracking = "nonsparse";
		String model = "BOW";
		
		File[] results = new File[2];
		results = TransformRaw.transformRaw(train, pvisualTracking, model);
		File trainBOW = results[0];
		
		Instances data = null;
		DataSource source = new DataSource(trainBOW.getAbsolutePath());
		data = source.getDataSet();
		data.setClassIndex(0);
		
		int minIndex = AppUtils.minIndex(data);
		
		Evaluation eval = new Evaluation(data);
		classificador.buildClassifier(data);
		
		eval.crossValidateModel(classificador, data, partitions, new Random(1));
		
		return eval.fMeasure(minIndex);
	}
	public static Double holOut(File fTrain,Classifier classificador, Integer iterations, Integer percentage) throws Exception {
		ArrayList<Double> fMeasureValues = new ArrayList<Double>();
		
		for (int i = 0; i < iterations; i++) {
			
			String pvisualTracking = "nonsparse";
			String model = "BOW";
			
			Instances data = AppUtils.file2instances(fTrain.getAbsolutePath(), "0");//arff fitxategi baten instantziak lortzen ditu 
			
			Instances[] trainANDtest= datuakZatitu(data,percentage);//datu partiketa metodo baten konprimituta
			
			File fileTrain = AppUtils.ordenagailuanGorde(trainANDtest[0],new File(fTrain.getParent()+File.separatorChar+"trainBOW.arff"));
			File fileDev = AppUtils.ordenagailuanGorde(trainANDtest[1],new File(fTrain.getParent()+File.separatorChar+"devBOW.arff"));
			
			File[] results = new File[2];
			results = TransformRaw.transformRaw(fileTrain, pvisualTracking, model);
			File fileTrainBOW = results[0];
			File dictionary = results[1];
			File fileDevCOMP = MakeCompatible.makeCompatible(fileTrainBOW, fileDev, dictionary);
			
			Instances trainBOW = AppUtils.file2instances(fileTrainBOW.getAbsolutePath(), "0");
			Instances DevCOMP = AppUtils.file2instances(fileDevCOMP.getAbsolutePath(), "0");
			
			int minIndex = AppUtils.minIndex(trainBOW);
			
			Evaluation eval = new Evaluation(DevCOMP);
			classificador.buildClassifier(trainBOW);
			eval.evaluateModel(classificador,DevCOMP);
			fMeasureValues.add(eval.fMeasure(minIndex));
		}
		return AppUtils.getMean(fMeasureValues);
		
	}
	public static Double resubstitution(File train,Classifier classificador) throws Exception {
		String pvisualTracking = "nonsparse";
		String model = "BOW";
		File[] results = new File[2];
		results = TransformRaw.transformRaw(train, pvisualTracking, model);
		File fileTrainBOW = results[0];
		
		Instances trainBOW = AppUtils.file2instances(fileTrainBOW.getAbsolutePath(), "0");

		Evaluation eval = new Evaluation(trainBOW);
		classificador.buildClassifier(trainBOW);
		eval.evaluateModel(classificador,trainBOW);
		int minIndex = AppUtils.minIndex(trainBOW);

		return eval.fMeasure(minIndex);
		
	}
	
	private static Instances[] datuakZatitu(Instances data, Integer percentage) throws Exception {
		Instances[] emaitza = new Instances[2];
		Instances train;
		Instances test;
		data.randomize(new Random(1));
		RemovePercentage zatiFiltro = new RemovePercentage();
		
		zatiFiltro.setInputFormat(data);
		zatiFiltro.setPercentage(70);
		zatiFiltro.setInvertSelection(false);
		train = Filter.useFilter(data, zatiFiltro);
		
		zatiFiltro.setInputFormat(data);
		zatiFiltro.setPercentage(70);
		zatiFiltro.setInvertSelection(true);
		test = Filter.useFilter(data, zatiFiltro);
		
		emaitza[0]=train;
		emaitza[1]=test;
		
		return emaitza;
	}

}