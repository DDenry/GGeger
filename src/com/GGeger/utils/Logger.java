package com.GGeger.utils;

import com.GGeger.entity.Configuration;

public class Logger {

	private static volatile Logger instance = null;

	private Logger() {
	}

	public static Logger getInstance() {

		if (instance == null) {

			synchronized (Logger.class) {

				if (instance == null) {

					instance = new Logger();

				}

			}

		}

		return instance;

	}

	public void print(String content) {

		print(1, content);

	}

	/**
	 * level: 0-error 1-info
	 */
	public void print(int level, String content) {

		if (!Configuration.RELEASE) {

			switch (level) {
			case 0:
				System.err.println(content);
				break;
			case 1:
				System.out.println(content);
				break;
			default:
				break;
			}

		}
	}
}
