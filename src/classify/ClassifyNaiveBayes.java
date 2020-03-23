package classify;

import java.io.File;

import util.AppUtils;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ClassifyNaiveBayes {
	public static void main(String[] args) {
		
		AppUtils.disableWarning();
		try {
			File train = new File(args[0]);
			File test = new File(args[1]);
			File parameters = null;
			classify(train, test, parameters);
		} catch (Exception e) {
			System.out.println("NaiveBayes error");
		}
	}

	public static void classify(File train, File test,File parameters) throws Exception {
		//get data
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
		
		//klasifikadorea definitu
		NaiveBayes classificador = new NaiveBayes();
		//evaluadorea definitu
		Evaluation eval = new Evaluation(dataTs);
		//probak egiteko parametroak prestatu
		
		//probak aplikatu
		classificador.buildClassifier(dataTr);
		eval.evaluateModel(classificador, dataTs);
		System.out.println(eval.toSummaryString("=== Summary ===", false));
		System.out.println(eval.toClassDetailsString());
		System.out.println(eval.toMatrixString());		
	}
}
