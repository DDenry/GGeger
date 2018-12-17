package com.GGeger.topic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
import com.GGeger.entity.Configuration;
import com.GGeger.entity.LonerInfo;
import com.GGeger.entity.LonerResult;
import com.GGeger.entity.LonerStudent;
import com.GGeger.entity.Student;
import com.GGeger.entity.StudentsCountInfo;
import com.GGeger.program.BaseProcess;
import com.GGeger.program.Main;
import com.GGeger.utils.DateTransfer;
import com.GGeger.utils.Logger;
import com.google.gson.Gson;

public class LonelyDigger5 extends BaseProcess {
	// 时间间隔
	private int T = 5;

	// 吃饭频率
	private float R = 1 / 4;

	// 规定吃饭总次数的符合标准
	private int mealTotalCount = 3;

	// 定义可选参数
	// 同性计数因子
	private float sameGenderFactor = 1.0f;

	// 异性计数因子
	private float differentGenderFactor = 0.8f;

	// 同专业不同年级计数因子
	private float sameMajorDifferentGradeFactor = 1.1f;

	// 同专业同年级计数因子
	private float sameMajorAndGradeFactor = 1.2f;

	// 同年级不同专业
	private float sameGradeDifferentMajor = 1.1f;

	// 不同年级不同专业
	private float fewLink = 0.5f;

	// 保存孤僻学子
	private List<LonerInfo> lonerStudentInfoList = new ArrayList<LonerInfo>();

	// 多线程并发锁
	private Object lock = new Object();

	// 允许最大线程并发的数量
	private int concurrentCount = 1;

	// 账单列表
	private List<Bill> bills = new ArrayList<Bill>();

	// 储存疑似孤僻学生的相关信息
	private Map<String, LonerStudent> lonerStudentsInfo;

	// 储存所有学生的信息
	private Map<String, Student> studentsInfo;

	//
	FileWriter fileWriter;

	// 准备写入文件
	BufferedWriter bufferedWriter;

	// 设置缓存量5M
	private int bufferedSize = 5 * 1024 * 1024;

	//
	private int maleStudentsCount = 0;

	private int femaleStudentsCount = 0;

	// 构造函数
	public LonelyDigger5() {
		// 构造函数
		for (int i = 0; i < 30; i++)
			System.out.print("=");
		System.out.println("");
		System.out.println("专题一：疑似孤僻学生分析");
	}

	// 执行
	// 执行方法
	public void execute() {

		System.out.println("Algorithm is running ......");

		//
		lonerStudentsInfo = new HashMap<String, LonerStudent>();

		studentsInfo = new HashMap<String, Student>();

		// String path = "C:/Users/DDenry/Desktop/_student_loner2.txt";
		String path = "C:/Users/DDenry/Desktop/model5000.txt";

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		// 判断默认文件是否存在，不存在则提示用户输入
		while (!new File(path).exists()) {

			System.out.println("请输入正确的数据路径：");
			// C:/Users/DDenry/Desktop/student_loner.txt
			// 获取用户输入
			path = scanner.nextLine();
		}

		Logger.getInstance().print(">>>>>>>>>>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

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
		// 数据第三项： 学生身份
		// 数据第四项：性别 String
		// 数据第五项：年级
		// 数据第六项：专业
		// 数据第七项：食堂名称

		// 获取专业名(只提取中文)
		String majorName = values[5].replaceAll("[^(\\u4e00-\\u9fa5)]", "");

		return new Bill.BillBuilder().setMillis(DateTransfer.string2Date(values[0]).getTime())
				.setStudent(new Student(values[1], values[2], values[3], values[4], majorName))
				.setCanteenName(values[6]).build();
	}

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
				Logger.getInstance().print("Waiting for bills handling~");

				lock.wait();

				timer.schedule(new TimerTask() {

					@Override
					public void run() {

						Logger.getInstance().print("Current index is " + index);

						Logger.getInstance().print("");

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

	// 寻找疑似好友
	// 遍历账单
	private void findPossibleFriends() {

		ExecutorService threadPool = Executors.newFixedThreadPool(concurrentCount);

		for (int i = 0; i < bills.size(); i++)
			threadPool.execute(compareBills);

		try {
			// 等待所有的线程执行完毕
			synchronized (lock) {

				lock.wait();

				Logger.getInstance().print("All " + bills.size() + " threads have finished!");

				timer.cancel();

				workResultOut();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 遍历下标
	private int index = 0;

	// 并发线程的实例
	// 并发线程实例
	private Runnable compareBills = new Runnable() {

		@Override
		public void run() {
			// 取出第index条数据
			int inner_index = index++;

			// 获取基准账单（作为参考项）
			Bill base = bills.get(inner_index);

			// 如果学生信息列表中无该生信息
			if (!lonerStudentsInfo.containsKey(base.getStudent().getStudentId())) {

				LonerStudent lonerStudent = new LonerStudent();

				lonerStudent.setStudentId(base.getStudent().getStudentId());

				lonerStudent.setBillsInfoList(new ArrayList<Bill>());

				lonerStudent.setPosibleFriendsList(new HashMap<String, Integer>());

				// Logger.getInstance().print(">" +
				// lonerStudent.getStudentId());

				//
				lonerStudentsInfo.put(base.getStudent().getStudentId(), lonerStudent);
			}

			// 获取该生的LonerStudent实例
			LonerStudent lonerStudent = lonerStudentsInfo.get(base.getStudent().getStudentId());

			//
			lonerStudent.getBillsInfoList().add(base);

			// 获取该生的吃饭次数
			int mealCount = lonerStudent.getMealCount();

			//
			boolean haveSubtracted = false;

			// 暂时吃饭次数自增
			lonerStudent.setMealCount(++mealCount);// ++先自增，再运算

			int loop = inner_index + 1;

			// 标记为疑似好友
			HashMap<String, Integer> friendsList = lonerStudent.getPosibleFriendsList();

			// 记录五分钟内已判断过的好友数量
			List<String> haveHandledStudentId = new ArrayList<String>();

			// 向下循环比较账单
			while (loop < bills.size()) {

				Bill next = bills.get(loop);

				// 判断时间是否符合规定间隔
				if (next.getMillis() - base.getMillis() > T * 60 * 1000)
					break;

				// 判断如果是T分钟内的相同StudentId数据
				else if (next.getStudent().getStudentId().equals(base.getStudent().getStudentId())) {

					loop++;

					// 如果本次循环已经裁剪过则直接进行下一次循环
					if (haveSubtracted)
						continue;

					// 吃饭次数减1
					// lonerStudent.setMealCount(--mealCount);//
					// --先自减，再运算

					// 标识该生已经裁剪过吃饭次数
					haveSubtracted = true;

					continue;
				}
				// 同一食堂进餐
				else if (next.getCanteenName().equals(base.getCanteenName())) {

					// 如果next.StudentID已经在暂存列表中，则直接进行下一次循环
					if (haveHandledStudentId.indexOf(next.getStudent().getStudentId()) != -1) {

						loop++;

						continue;

					} else {

						// 将next.StudentID添加到暂存列表中
						haveHandledStudentId.add(next.getStudent().getStudentId());

					}

					int commonMealCount = 0;

					if (friendsList.containsKey(next.getStudent().getStudentId()))
						commonMealCount = friendsList.get(next.getStudent().getStudentId());

					commonMealCount++;

					friendsList.put(next.getStudent().getStudentId(), commonMealCount);

				}
				//
				loop++;
			}

			// 置空暂存列表
			haveHandledStudentId.clear();
			haveHandledStudentId = null;

			// 象征意义的手动GC
			System.gc();

			synchronized (lock) {
				// 线程执行完毕
				if (index == bills.size()) {

					lock.notify();

				}
			}
		}
	};

	// 遍历疑似孤僻学生的好友信息
	private void workResultOut() {

		// 程序自检
		if (lonerStudentsInfo.size() != studentsInfo.size()) {
			System.err.println("Wrong result! Please find the danger bug or bugs!");
			return;
		}

		String recordPath = Configuration.LONER_RESULT_RECORD;

		// 判断当前输出文件是否存在
		File saveFile = new File(recordPath);

		if (saveFile.exists())
			saveFile.delete();

		log("Working real friends out now ......");

		Timer innerTimer = new Timer();

		innerTimer.schedule(new TimerTask() {

			@Override
			public void run() {

				Logger.getInstance().print((studentsInfo.size() - line) + " lines rest will be written later......");

			}

		}, 1000, 10000);

		try {

			fileWriter = new FileWriter(recordPath, true);

			// 准备写入文件
			bufferedWriter = new BufferedWriter(fileWriter, bufferedSize);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (String studentId : studentsInfo.keySet()) {

			// 统计学生性别信息
			if (studentsInfo.get(studentId).getGender().equals("男"))
				maleStudentsCount++;
			else if (studentsInfo.get(studentId).getGender().equals("女"))
				femaleStudentsCount++;

			LonerStudent lonerStudent = lonerStudentsInfo.get(studentId);

			// 获取该生的吃饭总次数
			int mealCounts = lonerStudent.getMealCount();

			// 迭代器
			Iterator<Entry<String, Integer>> iterator = lonerStudent.getPosibleFriendsList().entrySet().iterator();

			// 遍历LonerStudent.posibleFriendsList
			while (iterator.hasNext()) {

				Map.Entry<String, Integer> entry = iterator.next();

				String possibleStudentId = entry.getKey();

				//
				float commonMealCount = (float) entry.getValue();

				// 判断当前二人性别关系
				// 同性
				if (studentsInfo.get(lonerStudent.getStudentId()).getGender()
						.equals(studentsInfo.get(possibleStudentId).getGender())) {
					commonMealCount *= sameGenderFactor;
				} else
					// 异性
					commonMealCount *= differentGenderFactor;

				// 两人同一专业
				if (studentsInfo.get(lonerStudent.getStudentId()).getClass()
						.equals(studentsInfo.get(possibleStudentId).getClass())
						&& !studentsInfo.get(lonerStudent.getStudentId()).getClass().equals("")) {

					if (studentsInfo.get(lonerStudent.getStudentId()).getGrade()
							.equals(studentsInfo.get(possibleStudentId).getGrade()))
						// 相同年级
						commonMealCount *= sameMajorAndGradeFactor;
					else
						// 不同年级
						commonMealCount *= sameMajorDifferentGradeFactor;
				} else {
					if (studentsInfo.get(lonerStudent.getStudentId()).getGrade()
							.equals(studentsInfo.get(possibleStudentId).getGrade()))
						// 相同年级
						commonMealCount *= sameGradeDifferentMajor;
					else
						// 不同年级
						commonMealCount *= fewLink;
				}

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

					// Logger.getInstance().print("Continue to handle next~");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 序列化孤僻结果
		serialization();

		// 关闭写入
		try {

			bufferedWriter.close();

			fileWriter.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Logger.getInstance().print("Total " + lonerStudentInfoList.size() + " lonely student found!");

		Logger.getInstance().print("Final result has output in " + recordPath);

		// 该算法执行完毕
		System.out.println("LonelyDigger has accomplished!");

		// 清空所有变量数据
		lonerStudentInfoList.clear();

		innerTimer.cancel();

		bills.clear();

		studentsInfo.clear();

		lonerStudentsInfo.clear();

		lock = null;

		// 结果分析
		new ResultShow().lonerResult();

		// 显示主菜单
		Main.ShowMenu();

	}

	/**
	 * 将按规则输出的结果序列化
	 */
	@Override
	public void serialization() {

		LonerResult lonerResult = new LonerResult(DateTransfer.date2String(bills.get(0).getMillis()),
				DateTransfer.date2String(bills.get(bills.size() - 1).getMillis()),
				new StudentsCountInfo(studentsInfo.size(), maleStudentsCount, femaleStudentsCount),
				(float) bills.size() / studentsInfo.size());

		lonerResult.setLonerInfos(lonerStudentInfoList);

		StringBuilder resultSerialization = new StringBuilder(new Gson().toJson(lonerResult));

		try {

			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					new FileOutputStream(new File(Configuration.LONER_RESULT_SERIALIZATION)), "UTF-8");

			outputStreamWriter.write(resultSerialization.toString());

			outputStreamWriter.flush();

			outputStreamWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//
	private int line = 0;

	// 按格式输出学生的好友信息
	private void outResultInRule(LonerStudent lonerStudent) {

		// log("Outputting result in rules ......");

		// StudentId#realFriendsCount[,friendAId,friendBId,...]
		StringBuilder result = new StringBuilder(
				lonerStudent.getStudentId() + "#" + lonerStudent.getRealFriendsList().size());

		// 好友数量为0则标记为孤僻学子
		if (lonerStudent.getRealFriendsList().size() == 0) {

			// 判断是否吃饭次数少于规定次数
			if (lonerStudent.getMealCount() < mealTotalCount) {

				Logger.getInstance().print("Cannot regard the one as loner due 2 few meal count ("
						+ lonerStudent.getStudentId() + ")" + "=> Total mealCount is " + lonerStudent.getMealCount());

			} else {

				// 添加孤僻学子到结果列表
				lonerStudentInfoList.add(new LonerInfo(studentsInfo.get(lonerStudent.getStudentId()), lonerStudent));

				Logger.getInstance().print(0, "Find lonely student " + lonerStudent.getStudentId()
						+ "=> Total mealCount is " + lonerStudent.getMealCount());

			}

		} else {

			for (int i = 0; i < lonerStudent.getRealFriendsList().size(); i++) {
				result = result.append("," + lonerStudent.getRealFriendsList().get(i));
			}
		}

		// 将结果写入文件
		try {

			// 将该行数据写入文件
			bufferedWriter.append(result.toString());

			// 换行
			bufferedWriter.newLine();

			line++;

			// 刷新
			bufferedWriter.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
