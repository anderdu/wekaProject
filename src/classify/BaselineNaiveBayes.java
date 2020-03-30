package classify;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

import util.AppUtils;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class BaselineNaiveBayes {
	public static void main(String[] args) throws Exception {
		//C:\Users\andur\Programas\wekaData\Proiektua_text_mining\procesedFiles\train.arff
		AppUtils.disableWarning();
			//File train = new File(args[0]);
			File train = new File("C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\procesedFiles\\trainFull.arff");
			
			classify(train);
	}

	public static void classify(File train) throws Exception {
		/*
		 * holOut, kFold eta resubstitution-en klase minoritarioen fmeasureak bueltazen ditu
		 * in:
		 *     train_raw arff
		 * out:
		 *     holOutVal
		 *     kFoldVal
		 *     resubstitutionVal
		 */
		
		Integer iterations = 5;
		Integer percentage = 70;
		Integer partitions = 10;
		String pvisualTracking = "non";
		String model = "TF-IDF";
		
		
		//klasifikadorea definitu
		NaiveBayes classificador = new NaiveBayes();

		//pruebas block print
		//
		PrintStream terminal = new PrintStream(new FileOutputStream(FileDescriptor.out)); // hace sout en terminal
		System.setOut(new PrintStream(train.getParent()+File.separatorChar+"NaiveBayes.log"));//desvia sout a log
		
		//kfold
		long startTime = System.nanoTime();
		Double kFoldVal = Evaluators.kFold(train, classificador, partitions,pvisualTracking,model);
		long kFoldTime = (System.nanoTime()-startTime)/1000000;
		terminal.println("kFold time milliseconds  :" + kFoldTime);
		terminal.println(kFoldVal);
		
		//hold out
		startTime = System.nanoTime();
		Double holOutVal = Evaluators.holOut(train, classificador, iterations, percentage,pvisualTracking,model);
		long holOutTime = (System.nanoTime()-startTime)/1000000;
		terminal.println("holOut time milliseconds  :" + holOutTime);
		terminal.println(holOutVal);
		
		//resubstitution
		startTime = System.nanoTime();
		Double resubstitutionVal = Evaluators.resubstitution(train, classificador,pvisualTracking,model);
		long rebTime = (System.nanoTime()-startTime)/1000000;
		terminal.println("resubstitutionVal time milliseconds  :" + rebTime);
		terminal.println(resubstitutionVal);
		terminal.close();
	}
}
