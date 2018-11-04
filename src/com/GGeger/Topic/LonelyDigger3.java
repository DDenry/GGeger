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
	// T=5分钟
	private int T = 5;
	// 设置缓存量5M
	private int bufferedSize = 5 * 1024 * 1024;
	// 存储数据
	private List<Bill> bills = new ArrayList<Bill>();
	// 好友输出
	private List<Friend> friends = new ArrayList<Friend>();
	//
	private List<String> _students = new ArrayList<String>();

	private int[][] friendshipGroup;

	// 构造函数
	public LonelyDigger3() {
		for (int i = 0; i < 30; i++)
			System.out.print("=");
		System.out.println("");
		System.out.println("专题一：疑似孤僻学生分析");
	}

	// 专题一：
	public void Execute() {
		// 定义默认的数据文件路径
		String path = "C:/Users/DDenry/Desktop/student_loner.txt";

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		// 判断默认文件是否存在，不存在则提示用户输入
		while (!new File(path).exists()) {
			System.out.println("请输入正确的数据路径：");
			// C:/Users/DDenry/Desktop/student_loner.txt
			// 获取用户输入
			path = scanner.nextLine();
		}

		System.out.println(">>>>>>>>>>>>>>>>>>>>");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		// 文件存在，开始获取数据
		System.out.println("正在获取数据... ...");

		// 文件读取
		FileReader fileReader;

		// 捕捉异常
		try {
			// 读取数据文件
			fileReader = new FileReader(path);
			//
			BufferedReader bufferedReader = new BufferedReader(fileReader, bufferedSize);
			// 开启线程读取数据
			new Thread(new Runnable() {
				@Override
				public void run() {
					// 暂存读取的每一行数据
					String line = "";

					try {
						// 当未读到文件末尾则一直读取
						while ((line = bufferedReader.readLine()) != null) {
							// 将数据转换为Bill并且添加到bills列表中
							bills.add(transfer2Bill(line));
						}
						// 关闭读取流
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

					System.out.println("全部数据已成功转换为账单！");

					// 将学生学号储存
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
		System.out.println("开始储存所有学生的学号~");
		System.out.println("<<<<<<<<<<<<<<<<<<<<");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		// 遍历BillList
		// 将第一条数据添加到储存学号的List中
		_students.add(bills.get(0).getStudentId());

		// 遍历所有账单
		for (int i = 0; i < bills.size(); i++) {
			// 标志当前学号是否已经存在与List中
			boolean exist = false;
			// 遍历学号List
			for (int j = 0; j < _students.size(); j++) {
				// 如果当前学号不存在则继续循环
				if (!_students.get(j).equals(bills.get(i).getStudentId())) {
					continue;
				} else {
					// 标志当前学号已添加到List中，退出循环
					exist = true;
					break;
				}
			}
			// 如果当前学号没有存入List中则添加
			if (!exist)
				_students.add(bills.get(i).getStudentId());
		}
		//
		System.out.println("We have collected " + _students.size() + " students' id");

		// 根据学生个数声明二维数组(默认值为0)
		friendshipGroup = new int[_students.size()][_students.size()];

		System.out.println("<<<<<<<<<<<<<<<<<<<<");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("学号储存完毕！");

		// 处理账单
		HandleBill();
	}

	/**
	 * 将所有学号存入_students(List)中 遍历账单数据，读出学号后从_students中取出indexOf作为数组下标
	 */
	// 处理账单
	private void HandleBill() {
		//
		System.out.println("账单数据处理中... ...");

		//
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				//
				if (bills != null && bills.size() != 0) {
					//
					System.gc();
					// 或者下面，两者等价
					Runtime.getRuntime().gc();
					System.out.println("[System]:还有 " + bills.size() + " 条数据有待处理");
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

		// 从第一项开始遍历账单
		while (!(index > (bills.size() - 1))) {

			// 标识下一项数据的下标
			int next = index + 1;

			// 账单信息中的学生id
			int signal = _students.indexOf(bills.get(index).getStudentId());

			// 该生吃饭次数自增
			friendshipGroup[signal][signal]++;

			// 声名临时List，储存五分钟内统计过的学生信息
			List<String> temp_added_student = new ArrayList<String>();

			// 如果存在后续信息
			while (next < bills.size()) {
				// 判断比较的账单是否为5分钟之内的有效账单
				if (((int) (bills.get(next).getMillis() - bills.get(index).getMillis())) > (T * 60 * 1000)) {
					break;
				}

				// 比较的账单的学生id
				int _signal = _students.indexOf(bills.get(next).getStudentId());

				// 判断当前学生是否已经存在于临时List中
				if (temp_added_student.indexOf(bills.get(next).getStudentId()) != -1) {
					//
					/*
					 * System.out.println(bills.get(next).getStudentId() + "在距"
					 * + new SimpleDateFormat().format(new
					 * Date(bills.get(index).getMillis())) + "的" + T +
					 * "分钟内重复刷卡！");
					 */
					//
					next++;

					continue;
				}

				// 判断比较的账单学生_student_id与当前student_id不同并且食堂信息相同
				if (signal != _signal && bills.get(next).getCanteenName().equals(bills.get(index).getCanteenName())) {

					// 当前学生与下一位学生满足时间与位置上的好友条件
					friendshipGroup[signal][_signal]++;

					// 同理，下一位学生与当前学生也满足时间与位置上的好友条件
					friendshipGroup[_signal][signal]++;

					// 将该好友添加到临时List中
					temp_added_student.add(bills.get(next).getStudentId());
				}
				// 继续遍历下一位
				next++;
			}
			// 移除已经判断过的数据
			bills.remove(index);
			//
			temp_added_student.clear();
			temp_added_student = null;
		}

		// (强迫症)判断数据处理完并置空
		if (bills.size() == 0) {
			bills = null;
		}

		System.out.println("<<<<<<<<<<<<<<<<<<<<");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//
		System.out.println("全部账单信息处理完毕！");

		// 处理储存好友的二维列表
		HandleResult();
	}

	// 处理结果
	private void HandleResult() {
		System.out.println("整理好友信息... ...");
		// 遍历friendshipGroup
		for (int i = 0; i < _students.size(); i++) {
			//
			List<String> temp_list_friends = new ArrayList<String>();

			for (int j = 0; j < _students.size(); j++) {
				// 当自身吃饭总次数>0并且跟某好友吃饭次数小于自身总吃饭次数
				if (i != j && friendshipGroup[i][j] <= friendshipGroup[i][i]) {
					// 两人共同吃饭次数占个人吃饭次数的比例>R
					if ((friendshipGroup[i][j] / (double) friendshipGroup[i][i]) > R) {
						// 将该学生添加到好友列表中
						temp_list_friends.add(_students.get(j));
					}
				}
			}

			//
			System.out.println(_students.get(i) + "#" + friendshipGroup[i][i]);

			// 将当前学生的好友列表存入friends列表
			Friend friend = new Friend();
			friend.setStudent_id(_students.get(i));
			friend.setFriends_id(temp_list_friends);
			friends.add(friend);
		}

		// 象征意义的置空变量
		_students.clear();
		_students = null;
		//
		System.out.println("<<<<<<<<<<<<<<<<<<<<");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		System.out.println("好友信息整理完毕！");

		// 输出数据
		OutputResult();
	}

	// 5b56187ff89c7e20c4e59140
	// 输出结果(孤僻学子不会输出到文本)
	private void OutputResult() {
		//
		System.out.println("正在写入文件以及输出结果：");

		// 标识找到的孤僻学子的数量
		int lonerCount = 0;

		try {
			// 默认输出文件为程序当前路径/_log.txt
			FileWriter fileWriter = new FileWriter(new File(".").getAbsoluteFile() + "/_log.txt");
			// 准备写入文件
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter, bufferedSize);

			// 遍历好友列表
			for (Friend friend : friends) {
				// 将所有学生的好友信息结果输出到指定文件
				// 数据写入格式为：学生id#好友数量[,好友id,好友id,...]
				// (单线程中字符串操作 StringBuilder)
				StringBuilder output = new StringBuilder()
						.append(friend.getStudent_id() + "#" + friend.getFriendsCount());
				for (int i = 0; i < friend.getFriendsCount(); i++)
					output.append("," + friend.getFriends_id().get(i));
				// 将该行数据写入文件
				bufferedWriter.write(output.toString());
				// 换行
				bufferedWriter.newLine();
				// 刷新
				bufferedWriter.flush();
				// 找到孤僻学习，控制台输出
				if (friend.getFriendsCount() == 0) {
					//
					lonerCount++;

					System.err.println("Find lonely student :" + friend.getStudent_id());
				}
				//
				output = null;
			}

			// 关闭写入
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
		System.out.println("文件写入完毕！");
		System.out.println("保存路径为：" + new File("").getAbsoluteFile() + File.separator + "_log.txt");

		// 不存在孤僻学子
		if (lonerCount == 0)
			System.out.println("学生们都很开朗友善，暂未发现\"孤僻学子\"哦~");
		else
			System.out.println("共找到" + lonerCount + "名\"孤僻学子\"哦~");

		System.out.println("");

		// 返回到菜单界面
		Main.ShowMenu();
	}

	@Override
	public Bill transfer2Bill(String data) {
		// 分隔数据
		String[] values = data.split(",");
		// 数据第一项：账单时间
		// 数据第二项：学生id
		// 数据第三项：食堂名称
		// 数据第四项：POS机编号
		return new Bill.BillBuilder().setMillis(DateTransfer.string2Date(values[0]).getTime()).setStudentId(values[1])
				.setCanteenName(values[2]).setPointofsales(Integer.parseInt(values[3])).build();
	}
}
