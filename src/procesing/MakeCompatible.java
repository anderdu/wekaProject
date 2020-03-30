package procesing;

import util.AppUtils;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;

public class MakeCompatible {
	public static void main(String[] args) {
		//C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\procesedFiles\\trainBOW.arff
		AppUtils.disableWarning();
		try {
			File trainBOW = new File(args[0]);
			File test = new File(args[1]);
			File dictionary = new File(args[2]);
			makeCompatible(test, dictionary);
		} catch (Exception e) {
			System.out.println("");
		}
	}

	public static File makeCompatible(File test, File dictionary){
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
		String name = test.getName().split("\\.")[0];
		String newFileName = dictionary.getParent()+File.separator+name+"COMP.arff";
		File devCompatible = new File(newFileName);

		Instances dataTs = null;
		try {
			DataSource sourceTs = new DataSource(test.getAbsolutePath());
			dataTs = sourceTs.getDataSet();
		} catch (Exception e) {
			System.out.println("error: TransformRaw.nonSparseToS --> get data source");
		}
		
		// Fixed aplikatu test-ari train-aren berdina izateko
		try {
			FixedDictionaryStringToWordVector fixedDictionary = new FixedDictionaryStringToWordVector();
			fixedDictionary.setDictionaryFile(dictionary);
			fixedDictionary.setInputFormat(dataTs);
			dataTs = Filter.useFilter(dataTs, fixedDictionary);
		} catch (Exception e) {
			System.out.println("error: MakeCompatible  fixed diccionary ");
		}

		AppUtils.ordenagailuanGorde(dataTs,devCompatible);
		return devCompatible;
	}
}
