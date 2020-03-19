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
		String trainCSV="C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\train.csv";
		String testCSV="C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\train.csv";
		// 1. raw datuak dituen fitxategia aurreprosezatu konpatibilitate arazoak ekiditzeko
		File trainARFF = GetRaw.getRaw(trainCSV);
		// 2. StringToWord filtroa aplikatu fitxategiaren textuak hitzetan banantzeko
		File trainBOWarff = TransformRaw.transformRaw(trainARFF,"nonsparsed");
		// 3. train eta test konpatibleak egin: atributuak eta posizioak berdindu
		
		//berdina test-ekin
		File testARRF = GetRaw.getRaw(trainCSV);
		File testBOWarff = TransformRaw.transformRaw(testARRF,"nonsparsed"); //TODO puede ser nonsparse o sparse 

		
	}
	


	

}
