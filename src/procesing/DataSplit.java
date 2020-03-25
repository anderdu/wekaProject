package procesing;

import java.io.File;
import java.util.Random;

import util.AppUtils;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

public class DataSplit {
	public static void main(String[] args) {
		
	}
	
	public static File[] split(File dataFile,int percent) throws Exception {
		Instances data=null;
		Instances train=null;
		Instances test=null;
		String parentDir = dataFile.getParent()+File.separatorChar;
		DataSource source = new DataSource(dataFile.getAbsolutePath());
		data = source.getDataSet();
		data.setClassIndex(data.numAttributes()-1);
		
		File[] emaitza = new File[2];
		data.randomize(new Random(1));
		RemovePercentage zatiFiltro = new RemovePercentage();
		
		zatiFiltro.setInputFormat(data);
		zatiFiltro.setPercentage(percent);
		zatiFiltro.setInvertSelection(false);
		train = Filter.useFilter(data, zatiFiltro);//train
		zatiFiltro.setInputFormat(data);
		zatiFiltro.setPercentage(percent);
		zatiFiltro.setInvertSelection(true);
		test = Filter.useFilter(data, zatiFiltro);//dev edo test
		emaitza[0] = AppUtils.ordenagailuanGorde(train, new File(parentDir+"train.arff"));
		emaitza[1] = AppUtils.ordenagailuanGorde(test, new File(parentDir+"test.arff"));
		System.out.println("DataSplit: done");
		return emaitza;
		
	}
	

}
