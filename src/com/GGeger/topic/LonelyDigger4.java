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

	// 设置缓存量5M
	private int bufferedSize = 5 * 1024 * 1024;

	// 时间间隔
	private int T = 5;

	// 吃饭频率
	private float R = 1 / 4;

	// 输出结果文件
	private String resultPath;

	// 多线程并发锁
	private Object lock = new Object();

	// 允许最大线程并发的数量
	private int concurrentCount = 1;

	// 多线程并发池
	private Vector<Thread> concurrentThreadPool = new Vector<Thread>();

	// 账单列表
	private List<Bill> bills = new ArrayList<Bill>();

	// 储存疑似孤僻学生的相关信息
	private Map<String, LonerStudent> lonerStudentsInfo;

	// 储存所有学生的信息
	private Map<String, Student> studentsInfo;

	// 构造函数
	// 构造函数
	public LonelyDigger4() {
		// 构造函数
		for (int i = 0; i < 30; i++)
			System.out.print("=");
		System.out.println("");
		System.out.println("专题一：疑似孤僻学生分析");
	}

	// 执行
	// 执行方法
	public void execute() {
		//
		lonerStudentsInfo = new HashMap<String, LonerStudent>();

		studentsInfo = new HashMap<String, Student>();

//		String path = "C:/Users/DDenry/Desktop/_student_loner2.txt";
		String path = "C:/Users/DDenry/Desktop/student_loner.txt";
//		String path = "C:/Users/DDenry/Desktop/test.txt";

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		// 判断默认文件是否存在，不存在则提示用户输入
		while (!new File(path).exists()) {
			System.out.println("请输入正确的数据路径：");
			// C:/Users/DDenry/Desktop/student_loner.txt
			// 获取用户输入
			path = scanner.nextLine();
		}

		//
		digData(path);
	}

	// 字符串数据转换为账单
	// 账单转换
	@Override
	public Bill transfer2Bill(String data) {
		// 分隔数据
		String[] values = data.split(",");
		// 数据第一项：账单时间
		// 数据第二项：学生id ObjectId(.....)
		// 数据第三项：食堂名称
		// 数据第四项：性别 String
		// 数据第五项：班级

		return new Bill.BillBuilder().setMillis(DateTransfer.string2Date(values[0]).getTime())
				.setStudent(new Student(values[1], values[3], values[4])).setCanteenName(values[2]).build();
	}

	// 解析数据源
	// 解析数据源
	@Override
	public void digData(String path) {
		log("正在获取数据……");

		FileReader fileReader;

		try {

			fileReader = new FileReader(path);

			BufferedReader bufferedReader = new BufferedReader(fileReader, bufferedSize);

			// 开启线程读取数据
			new Thread(new Runnable() {

				@Override
				public void run() {
					//
					String line = "";

					try {
						while ((line = bufferedReader.readLine()) != null) {
							// 提取Bill
							Bill bill = transfer2Bill(line);
							bills.add(bill);
							// 储存学生信息列表
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

	// 寻找疑似好友
	// 遍历账单
	private void findPossibleFriends() {

		// 判断是否遍历完成
		// 遍历完成
		if (index == bills.size()) {

			log("Find possible friends completely!");

			// 清空线程池
			concurrentThreadPool.clear();

			// 象征意义的手动GC
			System.gc();

			// 筛选好友
			workResultOut();
		}
		// 未遍历完成
		else {

			try {
				// 线程通信
				synchronized (lock) {
					// 判断当前队列线程数量
					// System.out.println("Current competing queue's size is " +
					// concurrentThreadPool.size());

					// 当线程池不满时
					if (concurrentThreadPool.size() < concurrentCount) {

						// 新建线程
						Thread subThread = new Thread(compareBills);

						// 给线程起名字
						subThread.setName("Thread-" + index);

						// System.out.println("Pushing " + subThread.getName() + " in the task queue!");

						// 增加新的遍历线程，入队
						concurrentThreadPool.add(subThread);

						// 启动线程
						subThread.start();

					}
					// 线程池满时
					else {

						// System.out.println("Current queue is busy, waiting for other tasks done!");

						// 等待其他线程执行完毕后回调
						lock.wait();

						System.gc();

						// System.out.println("End waiting, push some other in the task queue!");
					}
				}
			} catch (Exception e) {
			}

			// System.out.println(">>>>>>>>>>");

			// 递归
			findPossibleFriends();
		}

	}

	// 遍历下标
	private int index = 0;

	// 返回当前线程在线程池中的下标
	// 根据线程序号判断当前队列中的位置
	private int currentPositionInUse(int index) {
		for (int i = 0; i < concurrentThreadPool.size(); i++) {
			if (concurrentThreadPool.get(i).getName().equals("Thread-" + index))
				return i;
		}
		return -1;
	}

	// 并发线程的实例
	// 并发线程实例
	private Runnable compareBills = new Runnable() {

		@Override
		public void run() {
			// 取出第index条数据
			int inner_index = index++;

			// System.out.println(">>>>>>>>>>>>>>>>>>>>");

			// System.out.println(inner_index + " =>Thread is starting ~");

			// 获取基准账单（作为参考项）
			Bill base = bills.get(inner_index);

			// 如果学生信息列表中无该生信息
			if (lonerStudentsInfo.get(base.getStudent().getStudentId()) == null) {
				LonerStudent lonerStudent = new LonerStudent();
				lonerStudent.setStudentId(base.getStudent().getStudentId());
				//
				lonerStudentsInfo.put(base.getStudent().getStudentId(), lonerStudent);
			}

			// 获取该生的吃饭次数
			int mealCount = lonerStudentsInfo.get(base.getStudent().getStudentId()).getMealCount();

			// 暂时吃饭次数自增
			lonerStudentsInfo.get(base.getStudent().getStudentId()).setMealCount(mealCount++);

			int loop = inner_index + 1;

			// 向下循环比较账单
			while (loop < bills.size()) {

				Bill next = bills.get(loop);

				// 判断时间是否符合规定间隔
				if (next.getMillis() - base.getMillis() > T * 60 * 1000) {
					break;
				}

				// 判断如果是T分钟内的相同StudentId数据
				else if (next.getStudent().getStudentId().equals(base.getStudent().getStudentId())) {
					// 吃饭次数减1
					lonerStudentsInfo.get(base.getStudent().getStudentId()).setMealCount(mealCount--);
					break;
				}
				// 同一食堂进餐
				else if (next.getCanteenName().equals(base.getCanteenName())) {
					// 标记为疑似好友
					HashMap<String, Integer> friendsList = lonerStudentsInfo.get(base.getStudent().getStudentId())
							.getPosibleFriendsList();
					friendsList.put(next.getStudent().getStudentId(),
							friendsList.get(next.getStudent().getStudentId()) == null ? 1
									: friendsList.get(next.getStudent().getStudentId()) + 1);
				}

				//
				loop++;
			}

			// 线程执行结束
			try {
				//
				synchronized (lock) {

					// 获取当前线程在队列中位置
					int currentPositionInUse = currentPositionInUse(inner_index);

					concurrentThreadPool.get(currentPositionInUse).interrupt();

					// 从队列中移除当前已执行完毕的线程
					concurrentThreadPool.remove(currentPositionInUse);

					// System.out.println(inner_index + " thread which is at " +
					// currentPositionInUse + " has sent the notify!");

					// 通知发放线程
					lock.notify();
				}
			} catch (Exception e) {
			}

			// 象征意义的手动GC
			System.gc();

			// System.out.println("LonerStudentsInfo's size is " +
			// lonerStudentsInfo.size());

			// System.out.println("<<<<<<<<<<<<<<<<<<<<");

			// System.out.println("");
		}

	};

	// 遍历疑似孤僻学生的好友信息
	private void workResultOut() {
		// 程序自检
		if (lonerStudentsInfo.size() != studentsInfo.size()) {
			System.err.println("Wrong result! Please find the danger bug or bugs!");
			return;
		}

		log("Working real friends out now ......");

		for (String studentId : studentsInfo.keySet()) {

			LonerStudent lonerStudent = lonerStudentsInfo.get(studentId);

			// 获取该生的吃饭总次数
			int mealCounts = lonerStudent.getMealCount();

			// 迭代器
			Iterator<Entry<String, Integer>> iterator = lonerStudent.getPosibleFriendsList().entrySet().iterator();

			// 遍历LonerStudent.posibleFriendsList
			while (iterator.hasNext()) {

				Map.Entry<String, Integer> entry = iterator.next();

				String possibleStudentId = entry.getKey();

				int commonMealCount = entry.getValue();

				// 如果记录的共同吃饭次数占总吃饭次数的频率大于R，则视为好友
				if ((float) commonMealCount / mealCounts > R) {

					// 将该好友添加到好友列表
					lonerStudent.getRealFriendsList().add(possibleStudentId);
				}

				//
				System.gc();
			}

			try {
				synchronized (lock) {

					// 开启写入的线程
					new Thread(new Runnable() {

						@Override
						public void run() {

							// 输出已处理好的结果
							outResultInRule(lonerStudent);

							synchronized (lock) {

								lock.notify();

							}
						}
					}).start();

					// 等待写入完毕
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

		// 该算法执行完毕
		log("LonelyDigger has accomplished!");

		// 显示主菜单
		Main.ShowMenu();

	}

	// 统计孤僻学子的数量
	private int lonelyStudentCount = 0;

	// 按格式输出学生的好友信息
	private void outResultInRule(LonerStudent lonerStudent) {

//		log("Outputting result in rules ......");

		// StudentId#realFriendsCount[,friendAId,friendBId,...]
		StringBuilder result = new StringBuilder(
				lonerStudent.getStudentId() + "#" + lonerStudent.getRealFriendsList().size());

		// 好友数量为0则标记为孤僻学子
		if (lonerStudent.getRealFriendsList().size() == 0) {

			lonelyStudentCount++;

			System.err.println("Find lonely student " + lonerStudent.getStudentId());

		} else {

			for (int i = 0; i < lonerStudent.getRealFriendsList().size(); i++) {
				result = result.append("," + lonerStudent.getRealFriendsList().get(i));
			}
		}

		// 将结果写入文件
		try {

			FileWriter fileWriter;

			// 设置输出文件
			resultPath = new File(".").getAbsoluteFile() + "/_log.txt";

			fileWriter = new FileWriter(resultPath, true);

			// 准备写入文件
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter, bufferedSize);

			// 将该行数据写入文件
			bufferedWriter.append(result.toString());

			// 换行
			bufferedWriter.newLine();

			// 刷新
			bufferedWriter.flush();

			bufferedWriter.close();

			fileWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
