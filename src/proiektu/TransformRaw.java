package proiektu;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

public class TransformRaw {
	private static String procesedFilesPath;


	public static void main(String[] args) {
		try {
			File arff = new File(args[0]);
			toSparse(arff);
		} catch (Exception e) {
			System.out.println("error: TransformRaw");
		}
	}
	
	public static File transformRaw(File inputArrf, String dataMode) {
		/*
		 * Aurrebaldintzak: arrf-aren datuak ez dira nominalak izango, stringToNominal filtroarekin ragazita daude
		 * in: arff fitxategia String esaldiekin eta clasearekin
		 * out: fitxategia non hitz bakoitza atributu bat da, 2 emitz mota
		 * nonsparsed
		 *      Instantziak esaldiak dira, baina soilik definituz esaldian hitz hori dagoen edo es
		 *      hitza bitan agertzen bada ez da abisatuko
		 *      //TODO butzutan hizt batzuk galtzen dira
		 * 
		 * 
		 * PROIEKTU
		 * raw2BoW.jar   train.arff trainBOW.arff
		 */
		
		//  dataBOW_NonSparse.arff
		File nonSparseArrf = null;
		try {
			nonSparseArrf=toSparse(inputArrf);
		} catch (Exception e) {
			System.out.println("error: TransformRaw.transformRaw.toSparse");
		}
		if(dataMode.equals("nonsparse")) return nonSparseArrf;
		
		//  dataBOW_Sparse.arff
		File spaseArrf = null;
		try {
			spaseArrf=nonSparseToS(nonSparseArrf);
		} catch (Exception e) {
			System.out.println("error: TransformRaw.transformRaw.NonSparseToS");
		}
		if(dataMode.equals("sparse")) return nonSparseArrf;
		System.out.println("bad text input: please fefine sparse or nonsparse");
		return null;
	}

	
	private static File toSparse(File originalArff) {
		/* Lehen sortutako arrf fitxategiaren textuak hitzetan banatzen du
		 * in:  originalArff
		 * out: words.arff in parent folder
		 * 	  return wordsArff
		 */
		if(originalArff.getParentFile().getName().equals("procesedFiles")) procesedFilesPath = originalArff.getParent();
		else procesedFilesPath = originalArff.getParent()+"\\procesedFiles";
		//arrf-a kargatu
		Instances data=null;
		try {
			DataSource source = new DataSource(originalArff.getAbsolutePath());
			data = source.getDataSet();
		} catch (Exception e) {
			System.out.println("error: TransformRaw.toSparse --> arrf karga");
		}
		//filtroa aplikatu - String-ak hitzetan banantzeko
		Instances wordsData=null;
		try {
			StringToWordVector filter = new StringToWordVector();
			filter.setInputFormat(data);
			wordsData = Filter.useFilter(data, filter);
		} catch (Exception e) {
			System.out.println("TransformRaw.toSparse --> filter error");
		}
		//Fitxategia sortu, words.arrf, filtroa aplikatuta dago
		File wordsArff = new File(procesedFilesPath+"\\words.arff");
		try {
		    ArffSaver saver = new ArffSaver();
		    saver.setInstances(wordsData);
		    saver.setFile(wordsArff);
		    saver.writeBatch();
		} catch (Exception e) {
			System.out.println("TransformRaw.toSparse --> file creator error");
		}
		return wordsArff;
	}
	


	private static File nonSparseToS(File newArff) {
		/*
		 *Aurrebaldintzak: NonSparse fitxategi bat, arff formatuan.
		 *Postbaldintzak: Sparse fitxategi bat sortuko du, arff formatuan.
		 * newArff: NonSparse fitxategia
		 */
		
		//datuak Instantzian gorde
		Instances data=null;
		try {
			DataSource source = new DataSource(newArff.getAbsolutePath());
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
		File sparseArff = new File(procesedFilesPath+"\\sparseArff.arff");
		try {
			 ArffSaver saver = new ArffSaver();
			 saver.setInstances(dataFiltered);
			 saver.setFile(sparseArff);
			 saver.writeBatch();
		} catch (IOException e1) {
			System.out.println("error:  TransformRaw.nonSparseToS --> ");
			e1.printStackTrace();
		}
		return sparseArff;
	}
	

}
