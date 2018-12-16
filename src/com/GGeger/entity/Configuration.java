package com.GGeger.entity;

import java.io.File;

public class Configuration {

	public static boolean RELEASE = false;
	//
	private static String LONER_PATH = new File("").getAbsolutePath() + File.separator + "LonerStudent";

	public static String LONER_RESULT_SERIALIZATION = LONER_PATH + File.separator + "resultSerialization.txt";

	public static String LONER_RESULT_RECORD = LONER_PATH + File.separator + "recordLog.txt";

	static {

		File file = new File(LONER_PATH);

		if (!file.exists()) {

			file.mkdir();

		}
		//
		file = null;
	}
}
