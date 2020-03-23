package classify;

import java.io.File;

import util.AppUtils;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.Bagging;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ClassifyBagging {
	
	
	public static void classify(File train, File test,File parameters) throws Exception {
		
		Instances dataTr = null;
		Instances dataTs = null;
		try {
			DataSource sourceTr = new DataSource(train.getAbsolutePath());
			dataTr = sourceTr.getDataSet();
			dataTr.setClassIndex(0);
			
			DataSource sourceTs = new DataSource(test.getAbsolutePath());
			dataTs = sourceTs.getDataSet();
			dataTs.setClassIndex(0);
		} catch (Exception e) {
			System.out.println("error geting data");
		}
		
		int minIndex = AppUtils.minIndex(dataTr);
		
		//klasifikadorea definitu
		Bagging classificador = new Bagging();
		//evaluadorea definitu
		Evaluation eval = new Evaluation(dataTs);
		//probak egiteko parametroak prestatu
		
	}

}
