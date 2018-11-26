package com.GGeger.topic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Vector;

import com.GGeger.entity.Bill;
import com.GGeger.entity.LonerStudent;
import com.GGeger.entity.Student;
import com.GGeger.program.BaseProcess;
import com.GGeger.program.Main;
import com.GGeger.utils.DateTransfer;

public class LonelyDigger4 extends BaseProcess {

	// ���û�����5M
	private int bufferedSize = 5 * 1024 * 1024;

	// ʱ����
	private int T = 5;

	// �Է�Ƶ��
	private float R = 1 / 4;

	// �������ļ�
	private String resultPath;

	// ���̲߳�����
	private Object lock = new Object();

	// ��������̲߳���������
	private int concurrentCount = 1;

	// ���̲߳�����
	private Vector<Thread> concurrentThreadPool = new Vector<Thread>();

	// �˵��б�
	private List<Bill> bills = new ArrayList<Bill>();

	// �������ƹ�Ƨѧ���������Ϣ
	private Map<String, LonerStudent> lonerStudentsInfo;

	// ��������ѧ������Ϣ
	private Map<String, Student> studentsInfo;

	// ���캯��
	// ���캯��
	public LonelyDigger4() {
		// ���캯��
		for (int i = 0; i < 30; i++)
			System.out.print("=");
		System.out.println("");
		System.out.println("ר��һ�����ƹ�Ƨѧ������");
	}

	// ִ��
	// ִ�з���
	public void execute() {
		//
		lonerStudentsInfo = new HashMap<String, LonerStudent>();

		studentsInfo = new HashMap<String, Student>();

//		String path = "C:/Users/DDenry/Desktop/_student_loner2.txt";
		String path = "C:/Users/DDenry/Desktop/student_loner.txt";
//		String path = "C:/Users/DDenry/Desktop/test.txt";

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		// �ж�Ĭ���ļ��Ƿ���ڣ�����������ʾ�û�����
		while (!new File(path).exists()) {
			System.out.println("��������ȷ������·����");
			// C:/Users/DDenry/Desktop/student_loner.txt
			// ��ȡ�û�����
			path = scanner.nextLine();
		}

		//
		digData(path);
	}

	// �ַ�������ת��Ϊ�˵�
	// �˵�ת��
	@Override
	public Bill transfer2Bill(String data) {
		// �ָ�����
		String[] values = data.split(",");
		// ���ݵ�һ��˵�ʱ��
		// ���ݵڶ��ѧ��id ObjectId(.....)
		// ���ݵ����ʳ������
		// ���ݵ�����Ա� String
		// ���ݵ�����༶

		return new Bill.BillBuilder().setMillis(DateTransfer.string2Date(values[0]).getTime())
				.setStudent(new Student(values[1], values[3], values[4])).setCanteenName(values[2]).build();
	}

	// ��������Դ
	// ��������Դ
	@Override
	public void digData(String path) {
		log("���ڻ�ȡ���ݡ���");

		FileReader fileReader;

		try {

			fileReader = new FileReader(path);

			BufferedReader bufferedReader = new BufferedReader(fileReader, bufferedSize);

			// �����̶߳�ȡ����
			new Thread(new Runnable() {

				@Override
				public void run() {
					//
					String line = "";

					try {
						while ((line = bufferedReader.readLine()) != null) {
							// ��ȡBill
							Bill bill = transfer2Bill(line);
							bills.add(bill);
							// ����ѧ����Ϣ�б�
							if (studentsInfo.get(bill.getStudent().getStudentId()) == null)
								studentsInfo.put(bill.getStudent().getStudentId(), bill.getStudent());
						}
						//
						bufferedReader.close();

						fileReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					log("There're " + bills.size() + " lines data!");

					log("There're " + studentsInfo.size() + " students found!");

					log("Handle bills thread is starting~(Allowed concurrent thread is " + concurrentCount + ")");

					synchronized (lock) {
						lock.notify();
					}

				}
			}).start();

			synchronized (lock) {
				System.out.println("Waiting for bills handling~");

				lock.wait();

				findPossibleFriends();
			}

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	// Ѱ�����ƺ���
	// �����˵�
	private void findPossibleFriends() {

		// �ж��Ƿ�������
		// �������
		if (index == bills.size()) {

			log("Find possible friends completely!");

			// ����̳߳�
			concurrentThreadPool.clear();

			// ����������ֶ�GC
			System.gc();

			// ɸѡ����
			workResultOut();
		}
		// δ�������
		else {

			try {
				// �߳�ͨ��
				synchronized (lock) {
					// �жϵ�ǰ�����߳�����
					// System.out.println("Current competing queue's size is " +
					// concurrentThreadPool.size());

					// ���̳߳ز���ʱ
					if (concurrentThreadPool.size() < concurrentCount) {

						// �½��߳�
						Thread subThread = new Thread(compareBills);

						// ���߳�������
						subThread.setName("Thread-" + index);

						// System.out.println("Pushing " + subThread.getName() + " in the task queue!");

						// �����µı����̣߳����
						concurrentThreadPool.add(subThread);

						// �����߳�
						subThread.start();

					}
					// �̳߳���ʱ
					else {

						// System.out.println("Current queue is busy, waiting for other tasks done!");

						// �ȴ������߳�ִ����Ϻ�ص�
						lock.wait();

						System.gc();

						// System.out.println("End waiting, push some other in the task queue!");
					}
				}
			} catch (Exception e) {
			}

			// System.out.println(">>>>>>>>>>");

			// �ݹ�
			findPossibleFriends();
		}

	}

	// �����±�
	private int index = 0;

	// ���ص�ǰ�߳����̳߳��е��±�
	// �����߳�����жϵ�ǰ�����е�λ��
	private int currentPositionInUse(int index) {
		for (int i = 0; i < concurrentThreadPool.size(); i++) {
			if (concurrentThreadPool.get(i).getName().equals("Thread-" + index))
				return i;
		}
		return -1;
	}

	// �����̵߳�ʵ��
	// �����߳�ʵ��
	private Runnable compareBills = new Runnable() {

		@Override
		public void run() {
			// ȡ����index������
			int inner_index = index++;

			// System.out.println(">>>>>>>>>>>>>>>>>>>>");

			// System.out.println(inner_index + " =>Thread is starting ~");

			// ��ȡ��׼�˵�����Ϊ�ο��
			Bill base = bills.get(inner_index);

			// ���ѧ����Ϣ�б����޸�����Ϣ
			if (lonerStudentsInfo.get(base.getStudent().getStudentId()) == null) {
				LonerStudent lonerStudent = new LonerStudent();
				lonerStudent.setStudentId(base.getStudent().getStudentId());
				//
				lonerStudentsInfo.put(base.getStudent().getStudentId(), lonerStudent);
			}

			// ��ȡ�����ĳԷ�����
			int mealCount = lonerStudentsInfo.get(base.getStudent().getStudentId()).getMealCount();

			// ��ʱ�Է���������
			lonerStudentsInfo.get(base.getStudent().getStudentId()).setMealCount(mealCount++);

			int loop = inner_index + 1;

			// ����ѭ���Ƚ��˵�
			while (loop < bills.size()) {

				Bill next = bills.get(loop);

				// �ж�ʱ���Ƿ���Ϲ涨���
				if (next.getMillis() - base.getMillis() > T * 60 * 1000) {
					break;
				}

				// �ж������T�����ڵ���ͬStudentId����
				else if (next.getStudent().getStudentId().equals(base.getStudent().getStudentId())) {
					// �Է�������1
					lonerStudentsInfo.get(base.getStudent().getStudentId()).setMealCount(mealCount--);
					break;
				}
				// ͬһʳ�ý���
				else if (next.getCanteenName().equals(base.getCanteenName())) {
					// ���Ϊ���ƺ���
					HashMap<String, Integer> friendsList = lonerStudentsInfo.get(base.getStudent().getStudentId())
							.getPosibleFriendsList();
					friendsList.put(next.getStudent().getStudentId(),
							friendsList.get(next.getStudent().getStudentId()) == null ? 1
									: friendsList.get(next.getStudent().getStudentId()) + 1);
				}

				//
				loop++;
			}

			// �߳�ִ�н���
			try {
				//
				synchronized (lock) {

					// ��ȡ��ǰ�߳��ڶ�����λ��
					int currentPositionInUse = currentPositionInUse(inner_index);

					concurrentThreadPool.get(currentPositionInUse).interrupt();

					// �Ӷ������Ƴ���ǰ��ִ����ϵ��߳�
					concurrentThreadPool.remove(currentPositionInUse);

					// System.out.println(inner_index + " thread which is at " +
					// currentPositionInUse + " has sent the notify!");

					// ֪ͨ�����߳�
					lock.notify();
				}
			} catch (Exception e) {
			}

			// ����������ֶ�GC
			System.gc();

			// System.out.println("LonerStudentsInfo's size is " +
			// lonerStudentsInfo.size());

			// System.out.println("<<<<<<<<<<<<<<<<<<<<");

			// System.out.println("");
		}

	};

	// �������ƹ�Ƨѧ���ĺ�����Ϣ
	private void workResultOut() {
		// �����Լ�
		if (lonerStudentsInfo.size() != studentsInfo.size()) {
			System.err.println("Wrong result! Please find the danger bug or bugs!");
			return;
		}

		log("Working real friends out now ......");

		for (String studentId : studentsInfo.keySet()) {

			LonerStudent lonerStudent = lonerStudentsInfo.get(studentId);

			// ��ȡ�����ĳԷ��ܴ���
			int mealCounts = lonerStudent.getMealCount();

			// ������
			Iterator<Entry<String, Integer>> iterator = lonerStudent.getPosibleFriendsList().entrySet().iterator();

			// ����LonerStudent.posibleFriendsList
			while (iterator.hasNext()) {

				Map.Entry<String, Integer> entry = iterator.next();

				String possibleStudentId = entry.getKey();

				int commonMealCount = entry.getValue();

				// �����¼�Ĺ�ͬ�Է�����ռ�ܳԷ�������Ƶ�ʴ���R������Ϊ����
				if ((float) commonMealCount / mealCounts > R) {

					// ���ú�����ӵ������б�
					lonerStudent.getRealFriendsList().add(possibleStudentId);
				}

				//
				System.gc();
			}

			try {
				synchronized (lock) {

					// ����д����߳�
					new Thread(new Runnable() {

						@Override
						public void run() {

							// ����Ѵ���õĽ��
							outResultInRule(lonerStudent);

							synchronized (lock) {

								lock.notify();

							}
						}
					}).start();

					// �ȴ�д�����
					lock.wait();

//					System.out.println("Continue to handle next~");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		studentsInfo.clear();

		lonerStudentsInfo.clear();

		lock = null;

		System.out.println("");

		System.out.println("Total " + lonelyStudentCount + " lonely student found!");

		System.out.println("Final result has output in " + resultPath);

		// ���㷨ִ�����
		log("LonelyDigger has accomplished!");

		// ��ʾ���˵�
		Main.ShowMenu();

	}

	// ͳ�ƹ�Ƨѧ�ӵ�����
	private int lonelyStudentCount = 0;

	// ����ʽ���ѧ���ĺ�����Ϣ
	private void outResultInRule(LonerStudent lonerStudent) {

//		log("Outputting result in rules ......");

		// StudentId#realFriendsCount[,friendAId,friendBId,...]
		StringBuilder result = new StringBuilder(
				lonerStudent.getStudentId() + "#" + lonerStudent.getRealFriendsList().size());

		// ��������Ϊ0����Ϊ��Ƨѧ��
		if (lonerStudent.getRealFriendsList().size() == 0) {

			lonelyStudentCount++;

			System.err.println("Find lonely student " + lonerStudent.getStudentId());

		} else {

			for (int i = 0; i < lonerStudent.getRealFriendsList().size(); i++) {
				result = result.append("," + lonerStudent.getRealFriendsList().get(i));
			}
		}

		// �����д���ļ�
		try {

			FileWriter fileWriter;

			// ��������ļ�
			resultPath = new File(".").getAbsoluteFile() + "/_log.txt";

			fileWriter = new FileWriter(resultPath, true);

			// ׼��д���ļ�
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter, bufferedSize);

			// ����������д���ļ�
			bufferedWriter.append(result.toString());

			// ����
			bufferedWriter.newLine();

			// ˢ��
			bufferedWriter.flush();

			bufferedWriter.close();

			fileWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
