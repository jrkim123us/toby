package learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	public Integer calcSum(String filepath) throws IOException {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
			Integer sum = 0;
			String line = null;
			while((line = bufferedReader.readLine()) != null) {
				sum += Integer.valueOf(line);
			}
			bufferedReader.close();
			return sum;
		} catch(IOException e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if(bufferedReader != null) {
				try {bufferedReader.close();}
				catch(IOException e) { System.out.println(e.getMessage());}
			}
		}
		
		
	}
}
