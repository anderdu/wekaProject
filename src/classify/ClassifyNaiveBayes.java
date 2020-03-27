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
	public static void main(String[] args) throws Exception {
		//C:\Users\andur\Programas\wekaData\Proiektua_text_mining\procesedFiles\train.arff
		AppUtils.disableWarning();
			//File train = new File(args[0]);
			File train = new File("C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\procesedFiles\\trainFull.arff");
			
			classify(train);
	}

	public static void classify(File train) throws Exception {
		
		Integer iterations = 5;
		Integer percentage = 70;
		Integer partitions = 10;
		
		//klasifikadorea definitu
		NaiveBayes classificador = new NaiveBayes();
		Double holOutVal = Evaluators.holOut(train, classificador, iterations, percentage);
		Double kFoldVal = Evaluators.kFold(train, classificador, partitions);
		Double resubstitutionVal = Evaluators.resubstitution(train, classificador);
		System.out.println(holOutVal);
		System.out.println(kFoldVal);
		System.out.println(resubstitutionVal);
	}
}
