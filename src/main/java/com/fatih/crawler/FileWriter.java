package com.fatih.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;

public class FileWriter {

	private String fileName;

	public FileWriter(String fileName) {
		this.fileName = fileName;
	}

	public void writeFile(Map<String, Integer> map) {
		File fout = new File(fileName);

		try (FileOutputStream fos = new FileOutputStream(fout);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
			for (Entry<String, Integer> entry : map.entrySet()) {
				bw.write(entry.getKey() + " #: " + entry.getValue());
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
