package com.helun.menu.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FileLoader {

	public static JSONArray loadJsonArrayFile(String path) throws IOException {

		File file = new File(path);

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

		StringBuffer stringBuffer = new StringBuffer();

		String lineText = null;

		while ((lineText = reader.readLine()) != null) {

			stringBuffer.append(lineText);

		}
		reader.close();
		return new JSONArray(stringBuffer.toString());
	}

	public static JSONObject loadJsonObjectFile(String path) throws IOException {

		File file = new File(path);

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

		StringBuffer stringBuffer = new StringBuffer();

		String lineText = null;

		while ((lineText = reader.readLine()) != null) {

			stringBuffer.append(lineText);

		}
		reader.close();

		return new JSONObject(stringBuffer.toString());
	}

	public static JSONObject loadJsonObjectResource(Class<?> clazz, String resource) throws IOException {

		InputStream in = clazz.getResourceAsStream(resource);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i;
		try {
			while ((i = in.read()) != -1) {
				baos.write(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String jsonStr = baos.toString();
		return new JSONObject(jsonStr);
	}

	/**
	 * Get a file with property and value.
	 * 
	 * file: line 1: propertity=value. line 2: propertity=value. line 3:
	 * propertity=value.
	 */
	public static PropertiesFile loadProperties(String path) throws IOException {
		PropertiesFile propertieFile = new PropertiesFile();

		File file = new File(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		try {
			String lineText = null;
			while ((lineText = reader.readLine()) != null) {
				if (lineText.startsWith("#"))
					continue;
				StringTokenizer st = new StringTokenizer(lineText, "=");
				while (st != null && st.hasMoreTokens()) {
					String key = "";
					try {
						key = st.nextToken();
					} catch (Exception e) {
						continue;
					}
					String value = "";
					try {
						value = st.nextToken();
					} catch (Exception e) {
					}

					propertieFile.put(key, value);
				}
			}
		} finally {
			reader.close();
		}
		if (propertieFile.isEmpty()) {
		}
		return propertieFile;
	}

	public static List<String> readFileByLineOnResources(String path) {

		List<String> lines = Lists.newArrayList();
		//InputStream is = FileLoader.class.getResourceAsStream(path);
		File file = new File(path);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			String lineText = null;
			while ((lineText = reader.readLine()) != null) {
				if (lineText.startsWith("#") || lineText.trim().equals(""))
					continue;
				lines.add(lineText);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * Get a file with only values.
	 * 
	 * file: line 1: value. line 2: value. line 3: value.
	 */
	public static ValuesFile loadValuesFile(String path) throws IOException {
		ValuesFile valuesFile = new ValuesFile();

		File file = new File(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		try {
			String lineText = null;
			while ((lineText = reader.readLine()) != null) {
				if (lineText.startsWith("#"))
					continue;
				valuesFile.put(lineText);
			}
		} finally {
			reader.close();
		}
		if (valuesFile.isEmpty()) {
		}
		return valuesFile;

	}

	public static class ValuesFile {
		List<String> values = Lists.newArrayList();

		public void put(String value) {
			values.add(value);
		}

		public boolean isEmpty() {
			return values.isEmpty();
		}

		public String get(int index) {
			return values.get(index);
		}
	}

	public static class PropertiesFile {
		Map<String, String> properties = Maps.newLinkedHashMap();

		public void put(String propertiesName, String value) {
			properties.put(propertiesName, value);
		}

		public String get(String propertiesName) {
			return properties.get(propertiesName);
		}

		private boolean isEmpty() {
			return properties.isEmpty();

		}

		public Set<String> propertyKeys() {
			return properties.keySet();
		}
	}
}
