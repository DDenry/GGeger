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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import com.GGeger.Entity.Bill;
import com.GGeger.Entity.Friend;
import com.GGeger.Entity.Student;
import com.GGeger.Program.Main;

public class LonelyDigger2 {

	// R=1/4
	private double R = 1 / 4;
	// T=5����
	private int T = 5;
	// ���û�����5M
	private int bufferedSize = 5 * 1024 * 1024;
	// �������
	private List<Friend> friends = new ArrayList<Friend>();
	//
	private Map<String, Student> students = new HashMap<String, Student>();
	//
	private Queue<Bill> queue = new LinkedList<Bill>();

	// ���캯��
	public LonelyDigger2() {
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
							//
							Bill bill = data2Bill(line);

							// ��������ӵ�������
							queue.offer(bill);
							// �����ǰ����ֻ��һ��Ԫ�أ������
							if (queue.size() == 1)
								continue;

							//
							while ((bill.getMillis() - queue.peek().getMillis()) > (T * 60 * 1000)) {
								//
								CheckFriends(bill);
							}
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
					//
					CheckFriends();
					// ��������
					HandleResult();
				}
			}).start();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//
	private void CheckFriends() {
		while (queue.size() > 1) {
			// �����������
			// ȡ������Ԫ�ز��Ӷ������Ƴ�
			Bill head = queue.poll();

			// �˵���Ϣ�е�ѧ��id
			String student_id = head.getStudentId();

			// ��������ڸ������ֶ�������
			if (!students.containsKey(student_id)) {
				Student student = new Student();
				student.setId(student_id);
				students.put(student_id, student);
			}
			// �����Է���������
			students.get(student_id).setMealCount(students.get(student_id).getMealCount() + 1);

			// ��������Bill
			queue.forEach(Bill -> {
				// �Ƚϵ��˵���ѧ��id
				String _student_id = Bill.getStudentId();

				// �жϱȽϵ��˵�ѧ��_student_id�뵱ǰstudent_id��ͬ����ʳ����Ϣ��ͬ
				if (!student_id.equals(_student_id) && head.getCanteenName().equals(Bill.getCanteenName())) {
					// ��ǰѧ������һλѧ������ʱ����λ���ϵĺ�������
					students.get(student_id).getFriends().put(_student_id,
							students.get(student_id).getFriends().get(_student_id) == null ? 0
									: students.get(student_id).getFriends().get(_student_id) + 1);
					//
					if (!students.containsKey(_student_id)) {
						Student student = new Student();
						student.setId(_student_id);
						students.put(_student_id, student);
					}
					// ͬ����һλѧ���뵱ǰѧ��Ҳ����ʱ����λ���ϵĺ�������
					students.get(_student_id).getFriends().put(student_id,
							students.get(_student_id).getFriends().get(student_id) == null ? 0
									: students.get(_student_id).getFriends().get(student_id) + 1);
				}
			});
		}
	}

	// ɸѡ���ƺ���
	private void CheckFriends(Bill bill) {
		// �����������
		// ȡ������Ԫ�ز��Ӷ������Ƴ�
		Bill head = queue.poll();

		// �˵���Ϣ�е�ѧ��id
		String student_id = head.getStudentId();

		// ��������ڸ������ֶ�������
		if (!students.containsKey(student_id)) {
			Student student = new Student();
			student.setId(student_id);
			students.put(student_id, student);
		}
		// �����Է���������
		students.get(student_id).setMealCount(students.get(student_id).getMealCount() + 1);

		// ��������Bill
		queue.forEach(Bill -> {
			if (Bill == bill)
				return;
			// �Ƚϵ��˵���ѧ��id
			String _student_id = Bill.getStudentId();

			// �жϱȽϵ��˵�ѧ��_student_id�뵱ǰstudent_id��ͬ����ʳ����Ϣ��ͬ
			if (!student_id.equals(_student_id) && head.getCanteenName().equals(Bill.getCanteenName())) {
				// ��ǰѧ������һλѧ������ʱ����λ���ϵĺ�������
				students.get(student_id).getFriends().put(_student_id,
						students.get(student_id).getFriends().get(_student_id) == null ? 0
								: students.get(student_id).getFriends().get(_student_id) + 1);
				//
				if (!students.containsKey(_student_id)) {
					Student student = new Student();
					student.setId(_student_id);
					students.put(_student_id, student);
				}
				// ͬ����һλѧ���뵱ǰѧ��Ҳ����ʱ����λ���ϵĺ�������
				students.get(_student_id).getFriends().put(student_id,
						students.get(_student_id).getFriends().get(student_id) == null ? 0
								: students.get(_student_id).getFriends().get(student_id) + 1);
			}
		});
	}

	// ������
	private void HandleResult() {
		//
		students.forEach((key, value) -> {
			// �������ѧ��
			List<String> temp_list_friends = new ArrayList<String>();
			for (String _key : value.getFriends().keySet()) {
				if (value.getFriends().get(_key) / (double) value.getMealCount() > R) {
					temp_list_friends.add(_key);
				}
			}
			// ����ǰѧ���ĺ����б����friends�б�
			Friend friend = new Friend();
			friend.setStudent_id(key);
			friend.setFriends_id(temp_list_friends);
			friends.add(friend);
		});

		// ����������ÿձ���
		students.clear();
		students = null;
		//
		System.out.println("���ݴ�����ϣ�");
		// �������
		OutputResult();
	}

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
		bill.setCanteenName(values[2]);
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
