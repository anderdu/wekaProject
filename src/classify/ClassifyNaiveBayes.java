package classify;

import java.io.File;
import java.util.Random;

import util.AppUtils;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ClassifyNaiveBayes {
	public static void main(String[] args) {
		
		AppUtils.disableWarning();
		try {
			File parameters = null;
			File train = new File(args[1]);
			Integer partitions = Integer.parseInt(args[2]);
			String evalMode = args[3];
			Integer percentage = Integer.parseInt(args[4]);
			classify(parameters,train,partitions,evalMode,percentage);
		} catch (Exception e) {
			System.out.println("NaiveBayes error");
		}
	}

	public static void classify(File parameters, File train, Integer partitions , String evalMode, Integer percentage) throws Exception {
		
		//get data
		Instances data = null;
		try {
			if(evalMode.equals("kfold")) {
				Evaluators.kFold(train,partitions);
			}else if(evalMode.equals("holOut")) {
				
			}else if(evalMode.equals("reb")) {
				
			}
		} catch (Exception e) {
			System.out.println("error geting data");
		}
		
		//klasifikadorea definitu
		Classifier prueba = new NaiveBayes();
		NaiveBayes classificador = new NaiveBayes();
		//evaluadorea definitu
		Evaluation eval = new Evaluation(data);
		
		//probak aplikatu
		classificador.buildClassifier(data);
		if(test=="kfold") {
			eval.crossValidateModel(classificador, dataTr, 10, new Random(1));
		}
		else {
			eval.evaluateModel(classificador, dataTs);
		}
		System.out.println(eval.toSummaryString("=== Summary ===", false));
		System.out.println(eval.toClassDetailsString());
		System.out.println(eval.toMatrixString());		
	}

	public static void classify(File train, File parameters) {
		// TODO Auto-generated method stub
		
	}
}
