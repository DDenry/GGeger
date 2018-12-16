package com.GGeger.pattern.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.GGeger.entity.Configuration;
import com.GGeger.entity.LonerResult;
import com.GGeger.utils.Logger;
import com.google.gson.Gson;

public class LonerStudentResult implements RuledResult {

	public LonerStudentResult() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T> T foldResult() {

		Logger.getInstance().print("Searching serialization result now ... ...");

		File file = new File(Configuration.LONER_RESULT_SERIALIZATION);

		if (file.exists()) {

			StringBuilder builder = new StringBuilder();

			// 读取序列化JSON
			try {

				FileReader fileReader = new FileReader(file);

				BufferedReader bufferedReader = new BufferedReader(fileReader);

				String line = "";

				while ((line = bufferedReader.readLine()) != null)
					builder.append(line);

				bufferedReader.close();

				fileReader.close();

				Logger.getInstance().print("Have got the serialization result!");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 将JSON反序列化
			return (T) new Gson().fromJson(builder.toString(), LonerResult.class);

		} else {

			Logger.getInstance().print(0, "There's no result to fold, please run the main function first!");

		}

		return null;
	}

}
