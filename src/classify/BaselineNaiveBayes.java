package classify;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

import util.AppUtils;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.Bagging;

public class BaselineNaiveBayes {
	public static void main(String[] args) throws Exception {
		//C:\Users\andur\Programas\wekaData\Proiektua_text_mining\procesedFiles\train.arff
		AppUtils.disableWarning();
			File train = new File(args[0]);
			//File train = new File("C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\procesedFiles\\trainFull.arff");
			classify(train);
			//SerializationHelper.write(args[1], clasificador);     .model
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
		
		//train = FeatureSubSel.apply(train,"0");//no filtrar este hacerlo con los bow
		
		Integer iterations = 2;
		Integer percentage = 70;
		Integer partitions = 5;
		String pvisualTracking = "non";
		String model = "BOW";
		
		
		//klasifikadorea definitu
		NaiveBayes classificador = new NaiveBayes();
		//Bagging classificador = new Bagging();

		//pruebas block print
		//
		PrintStream terminal = new PrintStream(new FileOutputStream(FileDescriptor.out)); // hace sout en terminal
		//System.setOut(new PrintStream(train.getParent()+File.separatorChar+"NaiveBayes.log"));//desvia sout a log
		long startTime;
		
		//kfold
		System.out.println("##### kFold #####");
		startTime = System.nanoTime();
		Double kFoldVal = Evaluators.kFold(train, classificador, partitions,pvisualTracking,model);
		long kFoldTime = (System.nanoTime()-startTime)/1000000000;
		terminal.println("kFold time seconds  :" + kFoldTime);
		terminal.println(kFoldVal);
		

		
		//hold out
		System.out.println("##### holOut #####");
		startTime = System.nanoTime();
		Double holOutVal = Evaluators.holOut(train, classificador, iterations, percentage,pvisualTracking,model);
		long holOutTime = (System.nanoTime()-startTime)/1000000000;
		terminal.println("holOut time seconds  :" + holOutTime);
		terminal.println(holOutVal);
		
		//resubstitution
		System.out.println("##### resubstitution #####");
		startTime = System.nanoTime();
		Double resubstitutionVal = Evaluators.resubstitution(train, classificador,pvisualTracking,model);
		long rebTime = (System.nanoTime()-startTime)/1000000000;
		terminal.println("resubstitutionVal time seconds  :" + rebTime);
		terminal.println(resubstitutionVal);
		
		
		
		terminal.close();
		
	}
}
