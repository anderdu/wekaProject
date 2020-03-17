package proiektu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

// C:\Users\andur\Programas\wekaData\Proiektua_text_mining\train.csv
public class Probak {
	public static void main(String[] args) throws Exception {
		String csvPath="C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\train.csv";
		File originalArff = GetRaw.getRaw(csvPath);
		//3. StringToWord filtroa aplikatu fitxategiaren textuak hitzetan banantzeko
		File newArff = TransformRaw.toWordVector(originalArff); //TODO puede ser nonsparse o sparse 
		System.out.println(newArff.getAbsolutePath());
	}
	


	

}
