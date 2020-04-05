package classify;

import java.io.File;

import util.AppUtils;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.instance.RemovePercentage;

public class FeatureSubSel {
	public static void main(String[] args) throws Exception {
		AppUtils.disableWarning();
		File f = new File(args[0]);
		File emaitza = apply(f,args[1],300);
	}
	
	public static Instances apply(Instances data,Integer num) throws Exception {
		/*
		 * in: instances 
		 * out: instances with less atributes
		 * 
		 */
		AttributeSelection filter = new AttributeSelection();
		InfoGainAttributeEval eval = new InfoGainAttributeEval();
		Ranker search = new Ranker();
		search.setNumToSelect(num);
		search.setThreshold(0.01);
		
		filter.setEvaluator(eval);
		filter.setSearch(search);
		filter.setInputFormat(data);
		
		Instances emaitza = Filter.useFilter(data,filter);
		System.out.println("file atributes reduced to 300");
		return emaitza;
	}
	
	public static File apply(File file,String classIndex,Integer num) throws Exception {
		/*
		 * in: 
		 *     arff file
		 *     classIndex
		 * out
		 *     arrf eith less instances
		 * 
		 */
		System.out.println(file.getAbsolutePath());
		Instances data = AppUtils.file2instances(file.getAbsolutePath(), classIndex);
		System.out.println(data.numInstances());
		
		AttributeSelection filter = new AttributeSelection();
		InfoGainAttributeEval eval = new InfoGainAttributeEval();
		Ranker search = new Ranker();
		search.setNumToSelect(num);
		search.setThreshold(0.01);
		
		filter.setEvaluator(eval);
		filter.setSearch(search);
		filter.setInputFormat(data);
		
		Instances ins = Filter.useFilter(data,filter);
		String name = file.getAbsolutePath().replace(".arff", "fss.arff");
		File filtered = AppUtils.ordenagailuanGorde(ins, new File(name));
		System.out.println("file "+filtered.getName()+" created with "+ins.numAttributes()+" attributes");
		return filtered;
		
	}

}
