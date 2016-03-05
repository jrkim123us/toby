package learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initialValue) throws IOException {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
			T result = initialValue;
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
	public String concatenate(String filepath) throws IOException {
		LineCallback<String> concatnateCallback =
			new LineCallback<String>() {				
				@Override
				public String doSomethingWithLine(String line, String value) {
					return value + line;
				}
			};
		return this.lineReadTemplate(filepath, concatnateCallback, "");
	}
	public Integer calcSum(String filepath) throws IOException {
		LineCallback<Integer> sumCallback = 
			new LineCallback<Integer>() {				
				@Override
				public Integer doSomethingWithLine(String line, Integer value) {					
					return value + Integer.valueOf(line);
				}
			};
		return lineReadTemplate(filepath, sumCallback, 0);		
	}
	
	public Integer calcMultiply(String filepath) throws IOException {
		LineCallback<Integer> multiplyCallback = 
			new LineCallback<Integer>() {				
				@Override
				public Integer doSomethingWithLine(String line, Integer value) {					
					return value * Integer.valueOf(line);
				}
			};
		return this.lineReadTemplate(filepath, multiplyCallback, 1);		
	}
}
