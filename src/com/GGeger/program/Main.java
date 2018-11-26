package com.GGeger.program;

import java.util.Scanner;

import com.GGeger.topic.LonelyDigger4;
import com.GGeger.topic.PoorDigger;

public class Main {

	//
	static String[] topics = new String[] { "专题一：疑似孤僻学生分析", "专题二：疑似困难学生分析", "专题三：疑似逃课学生分析", "专题四：疑似失联学生分析",
			"专题五：生活行为异常学生分析" };

	// 程序入口
	public static void main(String[] args) {
		//
		ShowMenu();
	}

	//
	public static void ShowMenu() {
		// 生成菜单
		for (int i = 0; i < 7; i++) {
			if (i < 1 || i > topics.length) {
				for (int j = 0; j < 30; j++) {
					System.out.print("*");
				}
			} else {
				String spaces = "";
				for (int m = 0; m < 35 - 4 - topics[i - 1].length() * 2 - 2; m++)
					spaces += "  ";
				System.out.println("*** " + topics[i - 1] + spaces + "#" + i + " *");
			}
			System.out.println("");
		}

		//
		int type;
		boolean isAllowed = false;
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		do {
			System.out.println("请输入您要查询的专题标号：");

			type = scanner.nextInt();
			//
			isAllowed = true;
			//
			switch (type) {
			// 专题一：疑似孤僻学生分析
			case 1:
				new LonelyDigger4().execute();
				break;
			// 专题二：疑似困难学生分析
			case 2:
				new PoorDigger().Execute();
				break;
			// 专题三：疑似逃课学生分析
			case 3:
				break;
			// 专题四：疑似失联学生分析
			case 4:
				break;
			// 专题五：生活行为异常学生分析
			case 5:
				break;

			default:
				isAllowed = false;
				break;
			}

		} while (!isAllowed);
	}
}
