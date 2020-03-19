package proiektu;

import java.io.File;

public class MakeCompatible {
	
	public static void main(String[] args) {
		try {
			File train = new File(args[0]);
			File test = new File(args[1]);
			makeCompatible(train,test);
		} catch (Exception e) {
			System.out.println("");
		}
	}
	
	public static void makeCompatible(File train, File test) {
		/*
		 * 
		 * 
		 * 
		 * raw2TFIDF.jar
		 */
	}

}
