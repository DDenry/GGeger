package com.GGeger.Topic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.print.attribute.standard.DateTimeAtCompleted;

import com.GGeger.Entity.Bill;
import com.GGeger.Entity.Friend;
import com.GGeger.Entity.Student;
import com.GGeger.Program.Main;

public class LonelyDigger3 {

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
	private Map<String, Student> students = new HashMap<String, Student>();

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
		String path = "";

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		// �ж�Ĭ���ļ��Ƿ���ڣ�����������ʾ�û�����
		while (!new File(path).exists()) {
			System.out.println("��������ȷ������·����");
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
					// ��־��ȡ����
					int count = 0;

					try {
						// ��δ�����ļ�ĩβ��һֱ��ȡ
						while ((line = bufferedReader.readLine()) != null) {
							// ��������1
							count++;
							// ������ת��ΪBill������ӵ�bills�б���
							bills.add(data2Bill(line));
						}
						// �رն�ȡ��
						bufferedReader.close();
						//
						fileReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("Total " + count + " lines of data!");

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
		_students.add(bills.get(0).getStudentId());
		for (int i = 0; i < bills.size(); i++) {
			boolean exist = false;
			for (int j = 0; j < _students.size(); j++) {
				if (!_students.get(j).equals(bills.get(i).getStudentId())) {
					continue;
				} else {
					exist = true;
					break;
				}
			}
			//
			if (!exist)
				_students.add(bills.get(i).getStudentId());
		}
		//
		System.out.println("We have collected " + _students.size() + " students' id");
		//
		friendshipGroup = new int[_students.size()][_students.size()];

		System.out.println("<<<<<<<<<<<<<<<<<<<<");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("ѧ�Ŵ�����ϣ�");

		//
		HandleBill();
	}

	//
	private void HandleBill() {
		//
		System.out.println("�˵����ݴ�����... ...");

		//

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (bills != null && bills.size() != 0) {
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

			// ������ں�����Ϣ
			while (next < bills.size()) {
				// �жϱȽϵ��˵��Ƿ�Ϊ5����֮�ڵ���Ч�˵�
				if (((int) (bills.get(next).getMillis() - bills.get(index).getMillis())) > (T * 60 * 1000)) {
					break;
				}

				// �Ƚϵ��˵���ѧ��id
				int _signal = _students.indexOf(bills.get(next).getStudentId());

				// �жϱȽϵ��˵�ѧ��_student_id�뵱ǰstudent_id��ͬ����ʳ����Ϣ��ͬ
				if (signal != _signal
						&& bills.get(next).getCanteenNumber().equals(bills.get(index).getCanteenNumber())) {

					// ��ǰѧ������һλѧ������ʱ����λ���ϵĺ�������
					friendshipGroup[signal][_signal]++;

					// ͬ����һλѧ���뵱ǰѧ��Ҳ����ʱ����λ���ϵĺ�������
					friendshipGroup[_signal][signal]++;
				}
				// ����������һλ
				next++;
			}
			// �Ƴ��Ѿ��жϹ�������
			bills.remove(index);
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
		//
		for (int i = 0; i < _students.size(); i++) {
			//
			List<String> temp_list_friends = new ArrayList<String>();

			for (int j = 0; j < _students.size(); j++) {

				if (i != j && friendshipGroup[i][i] > 0 && friendshipGroup[i][j] <= friendshipGroup[i][i]) {
					//
					if ((friendshipGroup[i][j] / (double) friendshipGroup[i][i]) > R) {
						temp_list_friends.add(_students.get(j));
					}
				}
			}

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

	//5b56187ff89c7e20c4e59140
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

	// ����ת��Ϊ�˵�
	private Bill data2Bill(String data) {
		// �ָ�����
		String[] values = data.split(",");
		// �����˵�ʵ��
		Bill bill = new Bill();
		// ���ݵ�һ�Stringת��Ϊmilliseconds
		bill.setMillis(string2Date(values[0]).getTime());
		// ���ݵڶ��ѧ��id
		bill.setStudentId(values[1]);
		// ���ݵ����ʳ������
		bill.setCanteenNumber(values[2]);
		// ���ݵ����Pos�����
		bill.setPos(Integer.parseInt(values[3]));

		return bill;
	}

	// �ַ���ת��Ϊ��������
	private Date string2Date(String stringFromat) {
		// ��������ʱ���ʽת��������
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.parse(stringFromat, new ParsePosition(0));
	}
}
