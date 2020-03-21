package proiektu;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;

public class MakeCompatible {
	private static String procesedFilesPath;

	public static void main(String[] args) {
		try {
			File train = new File(args[0]);
			File test = new File(args[1]);
			makeCompatible(train, test);
		} catch (Exception e) {
			System.out.println("");
		}
	}

	public static void makeCompatible(File train, File test) {
		/*
		 * Bi liburutegien konpatibilitatea bermatzeko atributuen posizioak berdindu
		 * in: 
		 *     trainBow.arff
		 *     testBOW.arff
		 * out:
		 *     
		 * PROIEKTU
		 * raw2TFIDF.jar trainBow.arff testBOW.arff
		 */

		Instances dataTr = null;
		Instances dataTe = null;

		try {
			DataSource source = new DataSource(train.getAbsolutePath());
			dataTr = source.getDataSet();
		} catch (Exception e) {
			System.out.println("error: TransformRaw.nonSparseToS --> get data source");
		}

		try {
			DataSource source = new DataSource(test.getAbsolutePath());
			dataTe = source.getDataSet();
		} catch (Exception e) {
			System.out.println("error: TransformRaw.nonSparseToS --> get data source");
		}

		dataTr.setClassIndex(dataTr.numAttributes() - 1);
		dataTe.setClassIndex(dataTe.numAttributes() - 1);

		int[] array = new int[dataTe.numAttributes() - 1];
		int kont = 0;
		for (int x = 0; x < dataTr.numAttributes(); x++) {
			for (int y = 0; y < dataTe.numAttributes(); y++) {
				if (dataTr.attribute(x).name() == dataTe.attribute(y).name()) {
					array[kont] = x;
					kont++;
				}
			}
		}
		Remove rm = new Remove();
		rm.setInvertSelection(true);
		rm.setAttributeIndicesArray(array);

		try {
			rm.setInputFormat(dataTe);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Instances newdataTe = Filter.useFilter(dataTe, rm);
			boolean aurkituta = false;
			for (int x = 0; x < dataTr.numAttributes(); x++) {
				aurkituta = false;
				for (int y = 0; y < newdataTe.numAttributes(); y++) {
					if (dataTr.attribute(x).name() == newdataTe.attribute(y).name()) {
						aurkituta = true;
					}
				}
				if (aurkituta == false) {
					Add newAddFilter = new Add();
					newAddFilter.setAttributeName(dataTr.attribute(x).name());
					newAddFilter.setInputFormat(newdataTe);
					newdataTe = Filter.useFilter(newdataTe, newAddFilter);
				}
			}
			System.out.println(newdataTe.numAttributes());
			System.out.println(dataTr.numAttributes());
			File compArff = new File(procesedFilesPath + "\\comArff.arff");
			try {
				ArffSaver saver = new ArffSaver();
				saver.setInstances(newdataTe);
				saver.setFile(compArff);
				saver.writeBatch();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
