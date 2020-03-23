package procesing;

import util.AppUtils;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;

public class MakeCompatible {
	private static String procesedFilesPath;

	public static void main(String[] args) {
		//C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\procesedFiles\\trainBOW.arff
		AppUtils.disableWarning();
		try {
			File trainBOW = new File(args[0]);
			File test = new File(args[1]);
			File dictionary = new File(args[2]);
			makeCompatible(trainBOW, test, dictionary);
		} catch (Exception e) {
			System.out.println("");
		}
	}

	public static void makeCompatible(File trainBOW, File test, File dictionary) throws Exception {
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
		String name = test.getName().split(File.separator+".")[0];
		String newFileName = procesedFilesPath+File.separator+name+"COMP.arff";

		Instances dataTs = null;
		try {
			DataSource sourceTs = new DataSource(test.getAbsolutePath());
			dataTs = sourceTs.getDataSet();
		} catch (Exception e) {
			System.out.println("error: TransformRaw.nonSparseToS --> get data source");
		}
		
		// Fixed aplikatu test-ari train-aren berdina izateko
		FixedDictionaryStringToWordVector fixedDictionary = new FixedDictionaryStringToWordVector();
		fixedDictionary.setDictionaryFile(dictionary);
		fixedDictionary.setInputFormat(dataTs);
		dataTs = Filter.useFilter(dataTs, fixedDictionary);
		AppUtils.ordenagailuanGorde(dataTs,new File(newFileName));
	}
}
