/* oharrak:
 * Programaren erabilgarritasunagatik erabaki dugu erabiki dugu raw2bow eta raw2TFIDF ez dutela raw fitxategia artuko
 * orrela probak erabiltzaileari askoz obeto azaldu ahal diogulako.
 * 
 */

package procesing;

import util.AppUtils;


import java.io.File;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

public class TransformRaw {
	private static String procesedFilesPath;
	private static String visualTracking;
	private static File[] emaitza = new File[2];
	//private static String model;


	public static void main(String[] args) {
		AppUtils.disableWarning();
		try {
			File arff = new File(args[0]);
			transformRaw(arff,args[1],args[2]);
		} catch (Exception e) {
			System.out.println("error: TransformRaw");
		}
	}
	
	public static File[] transformRaw(File inputArff, String pvisualTracking, String model) {
		/*
		 * Aurrebaldintzak: arff-aren datuak ez dira nominalak izango, stringToNominal filtroarekin ragazita daude
		 * in: arff_RAW_fitxategia pvisualTracking(sparse  nonsparse)  model(BOW  TF-IDF)
		 * out: 
		 *     file1: trainBOW
		 *     file2: train_dictionary.txt
		 *     return f1,f2
		 *       f1:
		 *         - sparse     BOW
		 *         - sparse     TF-IDF
		 *         - nonsparse  BOW
		 *         - nonsparse  TF-IDF
		 * 
		 * PROIEKTU
		 * TransformRaw.jar 
		 * raw2BoW.jar   train.arff trainBOW.arff
		 * 
		 * Mas info:
		 * BOW: Bag Of Words
		 * Tf-idf: Term frequency – Inverse document frequency
		 */
		
		//  dataBOW_NonSparse.arff
		visualTracking=pvisualTracking;
		File nonSparseArff = null;
		if(!visualTracking.equals("non") && (!visualTracking.equals("sparse"))){ //parametroa txar
			System.out.println("error:  TransformRaw   bad text input: please fefine sparse or nonsparse");
			return null;
		}
		try {
			toNonSparse(inputArff,model);
			System.out.println("TransformRaw --> nonsparse done");
		} catch (Exception e) {
			System.out.println("TransformRaw.transformRaw.toSparse");
		}
		if(visualTracking.equals("non")) return emaitza;
		
		//  dataBOW_Sparse.arff
		try {
			nonSparseToS(nonSparseArff);
		} catch (Exception e) {
			System.out.println("error: TransformRaw.transformRaw.NonSparseToS");
			System.out.println("TransformRaw --> sparse done");
		}
		if(visualTracking.equals("sparse")) return emaitza;
		System.out.println("TransformRaw  errore ezezaguna");
		return null;
	}

	
	private static void toNonSparse(File originalArff, String model) {
		/* Lehen sortutako arff fitxategiaren textuak hitzetan banatzen du
		 * in:  
		 *    file1: originalArff
		 *    String1: model(BOW  TF-IDF)
		 * out: 
		 * 	  file1: train_dictionary.txt
		 *    file2: trainBOW
		 */
		
		//hasierako balioa derrigorrezkoa da
		boolean TFTransform = false;
		boolean IDFTransform= false;
		if(model.equals("BOW")) { //BOW denean parametroak falsen
			TFTransform = false;
			IDFTransform= false;
		}else if(model.equals("TF-IDF")) { //TF-IDF denean parametroak truen
			TFTransform = true;
			IDFTransform= true;
		}else {
			System.out.println("TransformRaw   ez da modeloa ondo definitu");
			System.exit(0);
		}
		
		if(originalArff.getParentFile().getName().equals("procesedFiles")) procesedFilesPath = originalArff.getParent();
		else procesedFilesPath = originalArff.getParent()+File.separator+"procesedFiles";
		//arff-a kargatu
		Instances data=null;
		try {
			DataSource source = new DataSource(originalArff.getAbsolutePath());
			data = source.getDataSet();
			data.setClassIndex(data.numAttributes()-1);
			System.out.println();
		} catch (Exception e) {
			System.out.println("error: TransformRaw.toSparse --> arff karga");
		}
		//filtroa aplikatu - String-ak hitzetan banantzeko
		Instances nonSparseData=null;
		String name = originalArff.getName().split(File.separator+".")[0]; //fitxeroaren izena lortzen du, parent barruan
		String newDiccionaryName = procesedFilesPath+File.separator+name+"_dictionary.txt"; //Sortuko dugun CSV berriaren izena definitu
		File dicc = new File(newDiccionaryName);
		try {
			StringToWordVector filter = new StringToWordVector();
			filter.setWordsToKeep(1000000);
			filter.setMinTermFreq(3);
			filter.setTFTransform(TFTransform);
			filter.setIDFTransform(IDFTransform);
			filter.setAttributeIndices("first-last");
			filter.setInputFormat(data);
			filter.setDictionaryFileToSaveTo(dicc);
			filter.setPeriodicPruning(100.0);
			nonSparseData = Filter.useFilter(data, filter);
		} catch (Exception e) {
			System.out.println("TransformRaw.toSparse --> filter error");
		}
		
		//Fitxategia sortu --> nameBOW.arff
		String newBOWFileName = procesedFilesPath+File.separator+name+"BOW.arff"; //Sortuko dugun CSV berriaren izena definitu
		File NonSparse = new File(newBOWFileName);
		System.out.println("diccionary generated:  "+newDiccionaryName);
		AppUtils.ordenagailuanGorde(nonSparseData, NonSparse);
		emaitza[0] = dicc;
		emaitza[1] = NonSparse;
	}
	


	private static void nonSparseToS(File bowNonSparse) {
		/*
		 *in: 
		 *    file1: NonSparse fitxategi bat, arff formatuan.
		 *out: 
		 *    file1: Sparse fitxategi bat, arff formatuan.
		 */
		//datuak Instantzian gorde
		Instances data=null;
		try {
			DataSource source = new DataSource(bowNonSparse.getAbsolutePath());
			data = source.getDataSet();
		} catch (Exception e) {
			System.out.println("error: TransformRaw.nonSparseToS --> get data source");
		}
		
		//filtroa erabili nonSparse datuak sparse bihurtzeko
		SparseToNonSparse converter = new SparseToNonSparse();
		Instances dataFiltered = null;
		try {
			converter.setInputFormat(data);
			dataFiltered = Filter.useFilter(data, converter);
		} catch (Exception e) {
			System.out.println("error: TransformRaw.nonSparseToS --> ");
			e.printStackTrace();
		}
		
		//sparse formatuan fitxategia konputagailuan gorde
		String name = bowNonSparse.getName().split(File.separator+".")[0]; //fitxeroaren izena lortzen du, parent barruan
		String newFileName = procesedFilesPath+File.separator+name+".arff"; //Sortuko dugun ARFF berriaren izena definitu
		File bowSparse = new File(newFileName);
		AppUtils.ordenagailuanGorde(dataFiltered, bowSparse);
		emaitza[1] = bowSparse;
	}
	

}
