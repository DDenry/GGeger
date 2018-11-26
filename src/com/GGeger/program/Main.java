package com.GGeger.program;

import java.util.Scanner;

import com.GGeger.topic.LonelyDigger4;
import com.GGeger.topic.PoorDigger;

public class Main {

	//
	static String[] topics = new String[] { "ר��һ�����ƹ�Ƨѧ������", "ר�������������ѧ������", "ר�����������ӿ�ѧ������", "ר���ģ�����ʧ��ѧ������",
			"ר���壺������Ϊ�쳣ѧ������" };

	// �������
	public static void main(String[] args) {
		//
		ShowMenu();
	}

	//
	public static void ShowMenu() {
		// ���ɲ˵�
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
			System.out.println("��������Ҫ��ѯ��ר���ţ�");

			type = scanner.nextInt();
			//
			isAllowed = true;
			//
			switch (type) {
			// ר��һ�����ƹ�Ƨѧ������
			case 1:
				new LonelyDigger4().execute();
				break;
			// ר�������������ѧ������
			case 2:
				new PoorDigger().Execute();
				break;
			// ר�����������ӿ�ѧ������
			case 3:
				break;
			// ר���ģ�����ʧ��ѧ������
			case 4:
				break;
			// ר���壺������Ϊ�쳣ѧ������
			case 5:
				break;

			default:
				isAllowed = false;
				break;
			}

		} while (!isAllowed);
	}
}
