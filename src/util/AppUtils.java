package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class AppUtils {
	public static void main(String[] args) throws Exception {
		//C:\Users\andur\Programas\wekaData\Proiektua_text_mining\exp\trainreducido.arff
		//C:\Users\andur\Programas\wekaData\Proiektua_text_mining\exp\devCOMP.arff
		Instances tr = AppUtils.file2instances("C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\exp\\trainreducido.arff", "last");
		Instances ts = AppUtils.file2instances("C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\exp\\devCOMP.arff", "0");
		Instances procesado =  AppUtils.sameAttributes(tr,ts);
		File fp = new File("C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\exp\\procesado.arff");
		AppUtils.ordenagailuanGorde(procesado, fp);
	}
	
	public static String getFileExtension(File file) {
		if(file.getName().split("[.]").length>1) return file.getName().split("[.]")[1];
		else return "0";
	}
	
	public static File ordenagailuanGorde(Instances data,String fitx) {
		return ordenagailuanGorde(data,new File(fitx));
	}
	public static File ordenagailuanGorde(Instances data,File fitx) {
		try {
			//filewriter prestatu
			FileWriter writer = new FileWriter(fitx);

			//fitxategia prozezatu
			String name = fitx.getName().split("\\.")[0].split("[.]")[0]; //fitxeroaren izena lortzen du, parent barruan
			writer.append("@relation " +name+"\n\n");
			
			int num = data.numAttributes();
			for (int i = 0; i < num; i++) {
				writer.append(data.attribute(i).toString()+"\n");
				writer.flush();
			}
			writer.append("\n@data\n");
			for (Instance instance : data) {
				writer.append(instance.toString()+"\n");
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("ordenagailuanGorde error");
		}
		System.out.println("File saved:  "+fitx);
		return fitx;
	}
	
	public static void disableWarning() {
	    System.err.close();
	    System.setErr(System.out);
	}
	
	public static int minIndex(Instances data) {
		int[] count = data.attributeStats(data.classIndex()).nominalCounts;
		int minValue = count[0];
		int minIndex = 0;
		int numAtributes = count.length;
		for (int i = 0; i < numAtributes; i++) {
			int val = count[i];
			if(val<minValue && val!=0) {
				minValue = val;
				minIndex = i;
			}
		}
		
		return minIndex;
	}
	
	
	public static Instances file2instances(String file,String classIndex) throws Exception {
		Instances data = null;
		DataSource source = new DataSource(file);
		data = source.getDataSet();
		if(classIndex.equals("last")) {
			data.setClassIndex(data.numAttributes()-1);
		}else {
			data.setClassIndex(Integer.parseInt(classIndex));
		}
		return data;
	}
	
	public static Double getMean(ArrayList<Double> in) {
		Double total = 0.0;
		for (Double val : in) {
			total = total+val;
		}
		return total/in.size();
	}
	
	public static Instances sameAttributes(Instances train,Instances test) throws Exception {
		//test trainen atributu berdinak izateko
		Instances testCompatible = null;
		Remove remove = new Remove();
		String rangeList = "";
		ArrayList<String> nireAtt = new ArrayList<>();
		//HashMap<Integer, String> tranPos = new HashMap<>();
		for(int i=0;i<train.numAttributes();i++) {
			nireAtt.add(train.attribute(i).name());
			//tranPos.put(i,train.attribute(i).name());
		}
		for(int i=0;i<test.numAttributes();i++) {
			if(!nireAtt.contains(test.attribute(i).name())) {
				int num = i+1;
				rangeList = rangeList+num+",";
				
			}
		}
		rangeList=rangeList.substring(0, rangeList.length()-1);
		remove.setAttributeIndices(rangeList);
		remove.setInputFormat(test);
		testCompatible = Filter.useFilter(test, remove);
		//System.out.println(testCompatible.numAttributes());
		
		return testCompatible;
	}
	 
	    public static void modifyFile(File file, String oldString, String newString) {
	    	modifyFile(file.getAbsolutePath(), oldString, newString);
	    }
		public static void modifyFile(String filePath, String oldString, String newString){
	        File fileToBeModified = new File(filePath);
	        String oldContent = "";
	        BufferedReader reader = null;
	        FileWriter writer = null;
	        try{
	            reader = new BufferedReader(new FileReader(fileToBeModified));
	            //Reading all the lines of input text file into oldContent
	            String line = reader.readLine();
	            while (line != null) {
	                oldContent = oldContent + line + System.lineSeparator();
	                line = reader.readLine();
	            }
	            //Replacing oldString with newString in the oldContent
	            String newContent = oldContent.replaceAll(oldString, newString);
	            //Rewriting the input text file with newContent
	            writer = new FileWriter(fileToBeModified);
	            writer.write(newContent);
	        }
	        catch (IOException e){
	            e.printStackTrace();
	        }finally{
	            try{
	                //Closing the resources
	                reader.close();
	                writer.close();
	            } 
	            catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

		public static File bow2dict(String arff) {
			File dict=null;
			try {
				dict = new File(arff.replace(".arff", "_dict.txt"));
				dict.createNewFile(); //Fisikoki sortu
				FileWriter writer = new FileWriter(dict);
				BufferedReader reader = new BufferedReader(new FileReader(arff));
				String line;
				reader.readLine();reader.readLine();
					while (true) {
						line = reader.readLine();
						if(line.equals("@data")) break;
						if(line==null) break;
					    line = line.replace("@attribute ", "");
					    line = line.replace(" numeric", ",1");
					    line = line.replace("label {DESC,ENTY,ABBR,HUM,NUM,LOC}", "");
					    //line = line.replace("?", "\\?");
					    if(!line.equals(""))writer.append(line+"\n");
					    writer.flush();
					}
				reader.close();
				writer.close();
			} catch (Exception e) {
				System.out.println("fileParser - error");
			}
			System.out.println("small dictionary created --> "+dict.getAbsolutePath());
			return dict;
		}
	
}
