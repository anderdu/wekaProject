/* oharrak:
 * Programaren erabilgarritasunagatik erabaki dugu erabiki dugu raw2bow eta raw2TFIDF ez dutela raw fitxategia artuko
 * orrela probak erabiltzaileari askoz obeto azaldu ahal diogulako.
 * 
 */

package proiektu;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

public class TransformRaw {
	private static String procesedFilesPath;
	private static String visualTracking;
	//private static String model;


	public static void main(String[] args) {
		try {
			File arff = new File(args[0]);
			transformRaw(arff,args[1],args[2]);
		} catch (Exception e) {
			System.out.println("error: TransformRaw");
		}
	}
	
	public static File transformRaw(File inputArff, String pvisualTracking, String model) {
		/*
		 * Aurrebaldintzak: arff-aren datuak ez dira nominalak izango, stringToNominal filtroarekin ragazita daude
		 * in: arff fitxategia String esaldiekin eta clasearekin
		 * out: fitxategia non hitz bakoitza atributu bat da, 2 emitz mota
		 * nonsparsed
		 *      Instantziak esaldiak dira, baina soilik definituz esaldian hitz hori dagoen edo es
		 *      hitza bitan agertzen bada ez da abisatuko
		 *      //TODO butzutan hizt batzuk galtzen dira
		 * 
		 * 
		 * PROIEKTU
		 * TransformRaw.jar 
		 * raw2BoW.jar   train.arff trainBOW.arff
		 * 
		 * Mas info:
		 * BOW: Bag Of Words
		 */
		
		//  dataBOW_NonSparse.arff
		visualTracking=pvisualTracking;
		File nonSparseArff = null;
		if(!visualTracking.equals("non") && (!visualTracking.equals("sparse"))){
			System.out.println("bad text input: please fefine sparse or nonsparse");
			return null;
		}
		try {
			nonSparseArff=toNonSparse(inputArff,model);
		} catch (Exception e) {
			System.out.println("error: TransformRaw.transformRaw.toSparse");
		}
		System.out.println("TransformRaw --> nonsparse done");
		if(visualTracking.equals("non")) return nonSparseArff;
		
		//  dataBOW_Sparse.arff
		File sparseArff = null;
		try {
			sparseArff=nonSparseToS(nonSparseArff);
		} catch (Exception e) {
			System.out.println("error: TransformRaw.transformRaw.NonSparseToS");
		}
		System.out.println("TransformRaw --> sparse done");
		if(visualTracking.equals("sparse")) return sparseArff;
		System.out.println("errore ezezaguna");
		return null;
	}

	
	private static File toNonSparse(File originalArff, String model) {
		/* Lehen sortutako arff fitxategiaren textuak hitzetan banatzen du
		 * in:  originalArff
		 * out: words.arff in parent folder
		 * 	  return wordsArff
		 */
		boolean TFTransform = false;
		boolean IDFTransform= false;
		if(model.equals("BOW")) {
			TFTransform = false;
			IDFTransform= false;
		}else if(model.equals("TF-IDF")) {
			TFTransform = true;
			IDFTransform= true;
		}else {
			System.out.println("ez da modeloa ondo definitu");
			System.exit(0);
		}
		
		if(originalArff.getParentFile().getName().equals("procesedFiles")) procesedFilesPath = originalArff.getParent();
		else procesedFilesPath = originalArff.getParent()+File.separator+"procesedFiles";
		//arff-a kargatu
		Instances data=null;
		try {
			DataSource source = new DataSource(originalArff.getAbsolutePath());
			data = source.getDataSet();
		} catch (Exception e) {
			System.out.println("error: TransformRaw.toSparse --> arff karga");
		}
		//filtroa aplikatu - String-ak hitzetan banantzeko
		Instances nonSparseData=null;
		try {
			StringToWordVector filter = new StringToWordVector();
			filter.setWordsToKeep(1000000);
			filter.setMinTermFreq(3);
			filter.setTFTransform(TFTransform);
			filter.setIDFTransform(IDFTransform);
			filter.setAttributeIndices("first-last");
			filter.setInputFormat(data);
			nonSparseData = Filter.useFilter(data, filter);
		} catch (Exception e) {
			System.out.println("TransformRaw.toSparse --> filter error");
		}
		
		//Fitxategia sortu --> nameBOW.arff
		String name = originalArff.getName().split(File.separator+".")[0]; //fitxeroaren izena lortzen du, parent barruan
		String newFileName = procesedFilesPath+File.separator+name+"BOW.arff"; //Sortuko dugun CSV berriaren izena definitu
		File bowArff = new File(newFileName);
		//newArff.getAbsolutePath()
		AppUtils.ordenagailuanGorde(nonSparseData, bowArff);
		return bowArff;
	}
	


	private static File nonSparseToS(File bowNonSparse) {
		/*
		 *Aurrebaldintzak: NonSparse fitxategi bat, arff formatuan.
		 *Postbaldintzak: Sparse fitxategi bat sortuko du, arff formatuan.
		 * newArff: NonSparse fitxategia
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
		String newFileName = procesedFilesPath+File.separator+name+".arff"; //Sortuko dugun CSV berriaren izena definitu
		File bowArff = new File(newFileName);
		AppUtils.ordenagailuanGorde(dataFiltered, bowArff);
		return bowArff;
	}
	

}
