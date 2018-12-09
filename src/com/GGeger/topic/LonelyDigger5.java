package com.GGeger.topic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.GGeger.entity.Bill;
import com.GGeger.entity.LonerStudent;
import com.GGeger.entity.Student;
import com.GGeger.program.BaseProcess;
import com.GGeger.program.Main;
import com.GGeger.utils.DateTransfer;

public class LonelyDigger5 extends BaseProcess {

	// ���û�����5M
	private int bufferedSize = 5 * 1024 * 1024;

	// ʱ����
	private int T = 5;

	// �Է�Ƶ��
	private float R = 1 / 4;

	// �涨�Է��ܴ����ķ��ϱ�׼
	private int mealTotalCount = 3;

	// �����ѡ����
	// ͬ�Լ�������
	private float sameGenderFactor = 1.0f;

	// ���Լ�������
	private float differentGenderFactor = 1.0f;

	// ͬרҵ��ͬ�꼶��������
	private float sameMajorDifferentGradeFactor = 1.2f;

	// ͬרҵͬ�꼶��������
	private float sameMajorAndGradeFactor = 1.5f;

	// �������ļ�
	private String resultPath;

	// ���̲߳�����
	private Object lock = new Object();

	// ��������̲߳���������
	private int concurrentCount = 1;

	// �˵��б�
	private List<Bill> bills = new ArrayList<Bill>();

	// �������ƹ�Ƨѧ���������Ϣ
	private Map<String, LonerStudent> lonerStudentsInfo;

	// ��������ѧ������Ϣ
	private Map<String, Student> studentsInfo;

	// ���캯��
	public LonelyDigger5() {
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

		// String path = "C:/Users/DDenry/Desktop/_student_loner2.txt";
//		String path = "C:/Users/DDenry/Desktop/student_loner.txt";
		String path = "C:/Users/DDenry/Desktop/demo1000.txt";

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		// �ж�Ĭ���ļ��Ƿ���ڣ�����������ʾ�û�����
		while (!new File(path).exists()) {
			System.out.println("��������ȷ������·����");
			// C:/Users/DDenry/Desktop/student_loner.txt
			// ��ȡ�û�����
			path = scanner.nextLine();
		}

		System.out.println(">>>>>>>>>>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

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
		// ���ݵ�����Ա� String
		// ���ݵ�����꼶
		// ���ݵ����רҵ
		// ���ݵ����ʳ������

		// ��ȡרҵ��(ֻ��ȡ����)
		String majorName = values[4].replaceAll("[^(\\u4e00-\\u9fa5)]", "");

		return new Bill.BillBuilder().setMillis(DateTransfer.string2Date(values[0]).getTime())
				.setStudent(new Student(values[1], values[2], values[3], majorName)).setCanteenName(values[5]).build();
	}

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

						System.exit(0);
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

				timer.schedule(new TimerTask() {

					@Override
					public void run() {

						System.out.println("");

						System.out.println("Current index is " + index);

						System.out.println("");

					}
				}, 0, 5000);

				//
				findPossibleFriends();
			}

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	private Timer timer = new Timer();

	// Ѱ�����ƺ���
	// �����˵�
	private void findPossibleFriends() {

		ExecutorService threadPool = Executors.newFixedThreadPool(concurrentCount);

		for (int i = 0; i < bills.size(); i++)
			threadPool.execute(compareBills);

		try {
			// �ȴ����е��߳�ִ�����
			synchronized (lock) {

				lock.wait();

				System.out.println("All " + bills.size() + " threads have finished!");

				timer.cancel();

				workResultOut();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// �����±�
	private int index = 0;

	// �����̵߳�ʵ��
	// �����߳�ʵ��
	private Runnable compareBills = new Runnable() {

		@Override
		public void run() {
			// ȡ����index������
			int inner_index = index++;

			// ��ȡ��׼�˵�����Ϊ�ο��
			Bill base = bills.get(inner_index);

			// ���ѧ����Ϣ�б����޸�����Ϣ
			if (lonerStudentsInfo.get(base.getStudent().getStudentId()) == null) {

				LonerStudent lonerStudent = new LonerStudent();

				lonerStudent.setStudentId(base.getStudent().getStudentId());

				System.out.println(">" + lonerStudent.getStudentId());

				//
				lonerStudentsInfo.put(base.getStudent().getStudentId(), lonerStudent);
			}

			// ��ȡ�����ĳԷ�����
			int mealCount = lonerStudentsInfo.get(base.getStudent().getStudentId()).getMealCount();

			// ��ʱ�Է���������
			lonerStudentsInfo.get(base.getStudent().getStudentId()).setMealCount(++mealCount);// ++��������������

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
					lonerStudentsInfo.get(base.getStudent().getStudentId()).setMealCount(--mealCount);// --���Լ���������
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

			// ����������ֶ�GC
			System.gc();

			synchronized (lock) {
				// �߳�ִ�����
				if (index == bills.size()) {

					lock.notify();

				}
			}

		}

	};

	// �������ƹ�Ƨѧ���ĺ�����Ϣ
	private void workResultOut() {
		// �����Լ�
		if (lonerStudentsInfo.size() != studentsInfo.size()) {
			System.err.println("Wrong result! Please find the danger bug or bugs!");
			return;
		}

		resultPath = new File(".").getAbsoluteFile() + "/_log.txt";

		// �жϵ�ǰ����ļ��Ƿ����
		File saveFile = new File(resultPath);

		if (saveFile.exists())
			saveFile.delete();

		log("Working real friends out now ......");

		Timer innerTimer = new Timer();

		innerTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println((studentsInfo.size() - line) + " lines rest will be written later......");
			}
		}, 1000, 10000);

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

				// �жϵ�ǰ�����Ա��ϵ
				// ͬ��
				if (studentsInfo.get(lonerStudent.getStudentId()).getGender()
						.equals(studentsInfo.get(possibleStudentId).getGender())) {
					commonMealCount *= sameGenderFactor;
				} else
					// ����
					commonMealCount *= differentGenderFactor;

				// ����ͬһרҵ
				if (studentsInfo.get(lonerStudent.getStudentId()).getClass()
						.equals(studentsInfo.get(possibleStudentId).getClass())
						&& !studentsInfo.get(lonerStudent.getStudentId()).getClass().equals("")) {

					if (studentsInfo.get(lonerStudent.getStudentId()).getGrade()
							.equals(studentsInfo.get(possibleStudentId).getGrade()))
						// ��ͬ�꼶
						commonMealCount *= sameMajorAndGradeFactor;
					else
						// ��ͬ�꼶
						commonMealCount *= sameMajorDifferentGradeFactor;
				}

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

					// System.out.println("Continue to handle next~");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		innerTimer.cancel();

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

	//
	private int line = 0;

	// ����ʽ���ѧ���ĺ�����Ϣ
	private void outResultInRule(LonerStudent lonerStudent) {

		// log("Outputting result in rules ......");

		// StudentId#realFriendsCount[,friendAId,friendBId,...]
		StringBuilder result = new StringBuilder(
				lonerStudent.getStudentId() + "#" + lonerStudent.getRealFriendsList().size());

		// ��������Ϊ0����Ϊ��Ƨѧ��
		if (lonerStudent.getRealFriendsList().size() == 0) {

			// �ж��Ƿ�Է��������ڹ涨����
			if (lonerStudent.getMealCount() < mealTotalCount) {
				System.out.println(
						"Cannot regard the one as loner due 2 few meal count (" + lonerStudent.getStudentId() + ")");
			} else {

				lonelyStudentCount++;

				System.err.println(
						"Find lonely student " + lonerStudent.getStudentId() + " " + lonerStudent.getMealCount());

			}

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

			line++;

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
