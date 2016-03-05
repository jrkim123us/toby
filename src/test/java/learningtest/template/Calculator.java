package learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	public Integer lineReadTemplate(String filepath, LineCallback callback, int initialValue) throws IOException {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
			Integer result = initialValue;
			String line = null;
			while((line = bufferedReader.readLine()) != null) {
				result = callback.doSomethingWithLine(line, result);
			}
			return result;
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
	public Integer calcSum(String filepath) throws IOException {
		LineCallback sumCallback = 
			new LineCallback() {				
				@Override
				public Integer doSomethingWithLine(String line, Integer value) {					
					return value + Integer.valueOf(line);
				}
			};
		return lineReadTemplate(filepath, sumCallback, 0);		
	}
	
	public Integer calcMultiply(String filepath) throws IOException {
		LineCallback multiplyCallback = 
			new LineCallback() {				
				@Override
				public Integer doSomethingWithLine(String line, Integer value) {					
					return value * Integer.valueOf(line);
				}
			};
		return this.lineReadTemplate(filepath, multiplyCallback, 1);		
	}
}
