package procesing;

import util.AppUtils;

import java.io.File;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.Reorder;

public class MakeCompatible {
	public static void main(String[] args) {
		//C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\procesedFiles\\trainBOW.arff
		AppUtils.disableWarning();
		try {
			File test = new File(args[0]);
			File dictionary = new File(args[1]);
			makeCompatible(test, dictionary);
		} catch (Exception e) {
			System.out.println("");
		}
	}

	public static File makeCompatible(String test, String dictionary){
		return makeCompatible(new File(test),new File(dictionary));
	}
	public static File makeCompatible(File test, File dictionary){
		/*
		 * Bi liburutegien konpatibilitatea bermatzeko atributuen posizioak berdindu
		 * 
		 * in: 
		 *     testBOW.arff (bow sin haber aplicado ffs)
		 * out:
		 *     testCOMP.arff
		 *     
		 * PROIEKTU
		 * raw2TFIDF.jar trainBow.arff testBOW.arff
		 */
		String name = test.getName().split("\\.")[0];
		String newFileName = dictionary.getParent()+File.separator+name+"Comp.arff";
		File devCompatible = new File(newFileName);

		Instances dataTs = null;
		Instances dataBerria = null;
		try {
			DataSource sourceTs = new DataSource(test.getAbsolutePath());
			dataTs = sourceTs.getDataSet();
			dataTs.setClassIndex(dataTs.numAttributes()-1);
		} catch (Exception e) {
			System.out.println("error: TransformRaw.nonSparseToS --> get data source");
		}
		
		// Fixed aplikatu test-ari train-aren berdina izateko
		try {
			FixedDictionaryStringToWordVector fixedDictionary = new FixedDictionaryStringToWordVector();
			fixedDictionary.setDictionaryFile(dictionary);
			fixedDictionary.setInputFormat(dataTs);
			dataBerria = Filter.useFilter(dataTs, fixedDictionary);
			System.out.println("make compatible");
			System.out.println("\t in1: file to make kompatible   "+test.getAbsolutePath());
			System.out.println("\t in2: dictionary used "+dictionary);
			System.out.println(dataBerria.numAttributes());
		} catch (Exception e) {
			System.out.println("error: MakeCompatible  fixed diccionary ");
			e.printStackTrace();
		}
		
		Instance label = null;
		Instance prue = null;
		try {
			Reorder reo = new Reorder();
			reo.setAttributeIndices("2-last,first");
			reo.setInputFormat(dataBerria);
			dataBerria = Filter.useFilter(dataBerria, reo);
		} catch (Exception e) {
			System.out.println("error eliminando");
		}

		//dataBerria.add(label);
		
		AppUtils.ordenagailuanGorde(dataBerria,devCompatible);
		System.out.println("Fitxategi conpatiblea: ");
		return devCompatible;
	}
}
