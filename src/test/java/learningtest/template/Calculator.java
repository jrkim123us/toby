package learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
			int result = callback.doSomethingWithReader(bufferedReader);
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
		BufferedReaderCallback sumCallback = 
			new BufferedReaderCallback() {				
				@Override
				public Integer doSomethingWithReader(BufferedReader bufferedReader) throws IOException {
					Integer sum = 0;
					String line = null;
					while((line = bufferedReader.readLine()) != null) {
						sum += Integer.valueOf(line);
					}
					
					return sum;
				}
			};
		return this.fileReadTemplate(filepath, sumCallback);
	}
	
	public Integer calcMultiply(String filepath) throws IOException {
		BufferedReaderCallback multiplyCallback = 
				new BufferedReaderCallback() {				
					@Override
					public Integer doSomethingWithReader(BufferedReader bufferedReader) throws IOException {
						Integer multiply = 1;
						String line = null;
						while((line = bufferedReader.readLine()) != null) {
							multiply *= Integer.valueOf(line);
						}
						
						return multiply;
					}
				};
			return this.fileReadTemplate(filepath, multiplyCallback);
	}
}
