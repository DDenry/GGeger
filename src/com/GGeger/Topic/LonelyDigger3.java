package com.GGeger.Topic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import com.GGeger.Entity.Bill;
import com.GGeger.Entity.Friend;
import com.GGeger.Interface.Data2Bill;
import com.GGeger.Program.Main;
import com.GGeger.Utils.DateTransfer;

public class LonelyDigger3 implements Data2Bill {

	// R=1/4
	private double R = 1 / 4;
	// T=5����
	private int T = 5;
	// ���û�����5M
	private int bufferedSize = 5 * 1024 * 1024;
	// �洢����
	private List<Bill> bills = new ArrayList<Bill>();
	// �������
	private List<Friend> friends = new ArrayList<Friend>();
	//
	private List<String> _students = new ArrayList<String>();

	private int[][] friendshipGroup;

	// ���캯��
	public LonelyDigger3() {
		for (int i = 0; i < 30; i++)
			System.out.print("=");
		System.out.println("");
		System.out.println("ר��һ�����ƹ�Ƨѧ������");
	}

	// ר��һ��
	public void Execute() {
		// ����Ĭ�ϵ������ļ�·��
		String path = "C:/Users/DDenry/Desktop/student_loner.txt";

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		// �ж�Ĭ���ļ��Ƿ���ڣ�����������ʾ�û�����
		while (!new File(path).exists()) {
			System.out.println("��������ȷ������·����");
			// C:/Users/DDenry/Desktop/student_loner.txt
			// ��ȡ�û�����
			path = scanner.nextLine();
		}

		System.out.println(">>>>>>>>>>>>>>>>>>>>");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		// �ļ����ڣ���ʼ��ȡ����
		System.out.println("���ڻ�ȡ����... ...");

		// �ļ���ȡ
		FileReader fileReader;

		// ��׽�쳣
		try {
			// ��ȡ�����ļ�
			fileReader = new FileReader(path);
			//
			BufferedReader bufferedReader = new BufferedReader(fileReader, bufferedSize);
			// �����̶߳�ȡ����
			new Thread(new Runnable() {
				@Override
				public void run() {
					// �ݴ��ȡ��ÿһ������
					String line = "";

					try {
						// ��δ�����ļ�ĩβ��һֱ��ȡ
						while ((line = bufferedReader.readLine()) != null) {
							// ������ת��ΪBill������ӵ�bills�б���
							bills.add(transfer2Bill(line));
						}
						// �رն�ȡ��
						bufferedReader.close();
						//
						fileReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("Total " + bills.size() + " lines of data!");

					System.out.println("<<<<<<<<<<<<<<<<<<<<");
					System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

					System.out.println("ȫ�������ѳɹ�ת��Ϊ�˵���");

					// ��ѧ��ѧ�Ŵ���
					MatchStudentId();
				}
			}).start();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//
	private void MatchStudentId() {
		System.out.println("��ʼ��������ѧ����ѧ��~");
		System.out.println("<<<<<<<<<<<<<<<<<<<<");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		// ����BillList
		// ����һ��������ӵ�����ѧ�ŵ�List��
		_students.add(bills.get(0).getStudentId());

		// ���������˵�
		for (int i = 0; i < bills.size(); i++) {
			// ��־��ǰѧ���Ƿ��Ѿ�������List��
			boolean exist = false;
			// ����ѧ��List
			for (int j = 0; j < _students.size(); j++) {
				// �����ǰѧ�Ų����������ѭ��
				if (!_students.get(j).equals(bills.get(i).getStudentId())) {
					continue;
				} else {
					// ��־��ǰѧ������ӵ�List�У��˳�ѭ��
					exist = true;
					break;
				}
			}
			// �����ǰѧ��û�д���List�������
			if (!exist)
				_students.add(bills.get(i).getStudentId());
		}
		//
		System.out.println("We have collected " + _students.size() + " students' id");

		// ����ѧ������������ά����(Ĭ��ֵΪ0)
		friendshipGroup = new int[_students.size()][_students.size()];

		System.out.println("<<<<<<<<<<<<<<<<<<<<");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("ѧ�Ŵ�����ϣ�");

		// �����˵�
		HandleBill();
	}

	/**
	 * ������ѧ�Ŵ���_students(List)�� �����˵����ݣ�����ѧ�ź��_students��ȡ��indexOf��Ϊ�����±�
	 */
	// �����˵�
	private void HandleBill() {
		//
		System.out.println("�˵����ݴ�����... ...");

		//
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				//
				if (bills != null && bills.size() != 0) {
					//
					System.gc();
					// �������棬���ߵȼ�
					Runtime.getRuntime().gc();
					System.out.println("[System]:���� " + bills.size() + " �������д�����");
				} else {
					//
					timer.cancel();
				}
			}
		}, 5000, 10000);

		// TODO:hmmmmmmmmmmmmmm
		timer.cancel();

		//
		int index = 0;

		// �ӵ�һ�ʼ�����˵�
		while (!(index > (bills.size() - 1))) {

			// ��ʶ��һ�����ݵ��±�
			int next = index + 1;

			// �˵���Ϣ�е�ѧ��id
			int signal = _students.indexOf(bills.get(index).getStudentId());

			// �����Է���������
			friendshipGroup[signal][signal]++;

			// ������ʱList�������������ͳ�ƹ���ѧ����Ϣ
			List<String> temp_added_student = new ArrayList<String>();

			// ������ں�����Ϣ
			while (next < bills.size()) {
				// �жϱȽϵ��˵��Ƿ�Ϊ5����֮�ڵ���Ч�˵�
				if (((int) (bills.get(next).getMillis() - bills.get(index).getMillis())) > (T * 60 * 1000)) {
					break;
				}

				// �Ƚϵ��˵���ѧ��id
				int _signal = _students.indexOf(bills.get(next).getStudentId());

				// �жϵ�ǰѧ���Ƿ��Ѿ���������ʱList��
				if (temp_added_student.indexOf(bills.get(next).getStudentId()) != -1) {
					//
					/*
					 * System.out.println(bills.get(next).getStudentId() + "�ھ�"
					 * + new SimpleDateFormat().format(new
					 * Date(bills.get(index).getMillis())) + "��" + T +
					 * "�������ظ�ˢ����");
					 */
					//
					next++;

					continue;
				}

				// �жϱȽϵ��˵�ѧ��_student_id�뵱ǰstudent_id��ͬ����ʳ����Ϣ��ͬ
				if (signal != _signal && bills.get(next).getCanteenName().equals(bills.get(index).getCanteenName())) {

					// ��ǰѧ������һλѧ������ʱ����λ���ϵĺ�������
					friendshipGroup[signal][_signal]++;

					// ͬ����һλѧ���뵱ǰѧ��Ҳ����ʱ����λ���ϵĺ�������
					friendshipGroup[_signal][signal]++;

					// ���ú�����ӵ���ʱList��
					temp_added_student.add(bills.get(next).getStudentId());
				}
				// ����������һλ
				next++;
			}
			// �Ƴ��Ѿ��жϹ�������
			bills.remove(index);
			//
			temp_added_student.clear();
			temp_added_student = null;
		}

		// (ǿ��֢)�ж����ݴ����겢�ÿ�
		if (bills.size() == 0) {
			bills = null;
		}

		System.out.println("<<<<<<<<<<<<<<<<<<<<");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//
		System.out.println("ȫ���˵���Ϣ������ϣ�");

		// ��������ѵĶ�ά�б�
		HandleResult();
	}

	// ������
	private void HandleResult() {
		System.out.println("���������Ϣ... ...");
		// ����friendshipGroup
		for (int i = 0; i < _students.size(); i++) {
			//
			List<String> temp_list_friends = new ArrayList<String>();

			for (int j = 0; j < _students.size(); j++) {
				// ������Է��ܴ���>0���Ҹ�ĳ���ѳԷ�����С�������ܳԷ�����
				if (i != j && friendshipGroup[i][j] <= friendshipGroup[i][i]) {
					// ���˹�ͬ�Է�����ռ���˳Է������ı���>R
					if ((friendshipGroup[i][j] / (double) friendshipGroup[i][i]) > R) {
						// ����ѧ����ӵ������б���
						temp_list_friends.add(_students.get(j));
					}
				}
			}

			//
			System.out.println(_students.get(i) + "#" + friendshipGroup[i][i]);

			// ����ǰѧ���ĺ����б����friends�б�
			Friend friend = new Friend();
			friend.setStudent_id(_students.get(i));
			friend.setFriends_id(temp_list_friends);
			friends.add(friend);
		}

		// ����������ÿձ���
		_students.clear();
		_students = null;
		//
		System.out.println("<<<<<<<<<<<<<<<<<<<<");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		System.out.println("������Ϣ������ϣ�");

		// �������
		OutputResult();
	}

	// 5b56187ff89c7e20c4e59140
	// ������(��Ƨѧ�Ӳ���������ı�)
	private void OutputResult() {
		//
		System.out.println("����д���ļ��Լ���������");

		// ��ʶ�ҵ��Ĺ�Ƨѧ�ӵ�����
		int lonerCount = 0;

		try {
			// Ĭ������ļ�Ϊ����ǰ·��/_log.txt
			FileWriter fileWriter = new FileWriter(new File(".").getAbsoluteFile() + "/_log.txt");
			// ׼��д���ļ�
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter, bufferedSize);

			// ���������б�
			for (Friend friend : friends) {
				// ������ѧ���ĺ�����Ϣ��������ָ���ļ�
				// ����д���ʽΪ��ѧ��id#��������[,����id,����id,...]
				// (���߳����ַ������� StringBuilder)
				StringBuilder output = new StringBuilder()
						.append(friend.getStudent_id() + "#" + friend.getFriendsCount());
				for (int i = 0; i < friend.getFriendsCount(); i++)
					output.append("," + friend.getFriends_id().get(i));
				// ����������д���ļ�
				bufferedWriter.write(output.toString());
				// ����
				bufferedWriter.newLine();
				// ˢ��
				bufferedWriter.flush();
				// �ҵ���Ƨѧϰ������̨���
				if (friend.getFriendsCount() == 0) {
					//
					lonerCount++;

					System.err.println("Find lonely student :" + friend.getStudent_id());
				}
				//
				output = null;
			}

			// �ر�д��
			bufferedWriter.close();
			//
			fileWriter.close();
			//
			friends.clear();
			friends = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		System.out.println("<<<<<<<<<<<<<<<<<<<<");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//
		System.out.println("�ļ�д����ϣ�");
		System.out.println("����·��Ϊ��" + new File("").getAbsoluteFile() + File.separator + "_log.txt");

		// �����ڹ�Ƨѧ��
		if (lonerCount == 0)
			System.out.println("ѧ���Ƕ��ܿ������ƣ���δ����\"��Ƨѧ��\"Ŷ~");
		else
			System.out.println("���ҵ�" + lonerCount + "��\"��Ƨѧ��\"Ŷ~");

		System.out.println("");

		// ���ص��˵�����
		Main.ShowMenu();
	}

	@Override
	public Bill transfer2Bill(String data) {
		// �ָ�����
		String[] values = data.split(",");
		// ���ݵ�һ��˵�ʱ��
		// ���ݵڶ��ѧ��id
		// ���ݵ����ʳ������
		// ���ݵ����POS�����
		return new Bill.BillBuilder().setMillis(DateTransfer.string2Date(values[0]).getTime()).setStudentId(values[1])
				.setCanteenName(values[2]).setPointofsales(Integer.parseInt(values[3])).build();
	}
}
