package proiektu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;


public class parser {
	
	public static void main(String[] args) throws IOException {
		String csvPath="C:\\Users\\andur\\Programas\\wekaData\\Proiektua_text_mining\\test.csv";
		String newFileName = csvPath.replace(".csv", ".arff");
		System.out.println(newFileName);
	}
}
