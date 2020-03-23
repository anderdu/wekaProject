package procesing;

import util.AppUtils;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.Remove;

public class MakeCompatible {
	private static String procesedFilesPath;

	public static void main(String[] args) {
		//C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\procesedFiles\\trainBOW.arff
		AppUtils.disableWarning();
		try {
			File train = new File(args[0]);
			File test = new File(args[1]);
			makeCompatible(train, test);
		} catch (Exception e) {
			System.out.println("");
		}
	}

	public static void makeCompatible(File trainBOW, File testBOW) throws Exception {
		/*
		 * Bi liburutegien konpatibilitatea bermatzeko atributuen posizioak berdindu
		 * in: 
		 *     trainBOW.arff
		 *     testBOW.arff
		 * out:
		 *     trainCOMP.arff
		 *     testCOMP.arff
		 *     
		 * PROIEKTU
		 * raw2TFIDF.jar trainBow.arff testBOW.arff
		 */
		if(trainBOW.getParentFile().getName().equals("procesedFiles")) procesedFilesPath = trainBOW.getParent();
		else procesedFilesPath = trainBOW.getParent()+File.separator+"procesedFiles";

		Instances dataTr = null;
		Instances dataTs = null;

		try {
			DataSource sourceTr = new DataSource(trainBOW.getAbsolutePath());
			dataTr = sourceTr.getDataSet();

			DataSource sourceTs = new DataSource(testBOW.getAbsolutePath());
			dataTs = sourceTs.getDataSet();
		} catch (Exception e) {
			System.out.println("error: TransformRaw.nonSparseToS --> get data source");
		}
		
		// Fixed aplikatu test-ari train-aren berdina izateko
		FixedDictionaryStringToWordVector fixedDictionary = new FixedDictionaryStringToWordVector();
		fixedDictionary.setDictionaryFile(trainBOW);
		fixedDictionary.setInputFormat(dataTs);
		Instances newtest = Filter.useFilter(dataTs, fixedDictionary);
		String name = testBOW.getName().split(File.separator+".")[0]; //fitxeroaren izena lortzen du, parent barruan
		name = name.replace("BOW", "");
		String newFileName = procesedFilesPath+File.separator+name+"COMP.arff"; //Sortuko dugun arff berriaren izena definitu
		File newfitx = new File(newFileName);
		AppUtils.ordenagailuanGorde(newtest, newfitx);
		System.out.println("compatible file done: "+ newFileName);
	}
}
