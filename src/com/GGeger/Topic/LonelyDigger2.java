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
	// T=5分钟
	private int T = 5;
	// 设置缓存量5M
	private int bufferedSize = 5 * 1024 * 1024;
	// 好友输出
	private List<Friend> friends = new ArrayList<Friend>();
	//
	private Map<String, Student> students = new HashMap<String, Student>();
	//
	private Queue<Bill> queue = new LinkedList<Bill>();

	// 构造函数
	public LonelyDigger2() {
		for (int i = 0; i < 30; i++)
			System.out.print("=");
		System.out.println("");
		System.out.println("专题一：疑似孤僻学生分析");
	}

	// 专题一：
	public void Execute() {
		// 定义默认的数据文件路径
		String path = "";

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		// 判断默认文件是否存在，不存在则提示用户输入
		while (!new File(path).exists()) {
			System.out.println("请输入正确的数据路径：");
			// 获取用户输入
			path = scanner.nextLine();
		}

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
					// 标志读取数量
					int count = 0;

					try {
						// 当未读到文件末尾则一直读取
						while ((line = bufferedReader.readLine()) != null) {
							// 数量自增1
							count++;
							//
							Bill bill = data2Bill(line);

							// 将数据添加到队列中
							queue.offer(bill);
							// 如果当前队列只有一个元素，则继续
							if (queue.size() == 1)
								continue;

							//
							while ((bill.getMillis() - queue.peek().getMillis()) > (T * 60 * 1000)) {
								//
								CheckFriends(bill);
							}
						}
						// 关闭读取流
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
					// 处理数据
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
			// 处理队列数据
			// 取出队首元素并从队列中移除
			Bill head = queue.poll();

			// 账单信息中的学生id
			String student_id = head.getStudentId();

			// 如果不存在该生的字段则声明
			if (!students.containsKey(student_id)) {
				Student student = new Student();
				student.setId(student_id);
				students.put(student_id, student);
			}
			// 该生吃饭次数自增
			students.get(student_id).setMealCount(students.get(student_id).getMealCount() + 1);

			// 遍历队列Bill
			queue.forEach(Bill -> {
				// 比较的账单的学生id
				String _student_id = Bill.getStudentId();

				// 判断比较的账单学生_student_id与当前student_id不同并且食堂信息相同
				if (!student_id.equals(_student_id) && head.getCanteenName().equals(Bill.getCanteenName())) {
					// 当前学生与下一位学生满足时间与位置上的好友条件
					students.get(student_id).getFriends().put(_student_id,
							students.get(student_id).getFriends().get(_student_id) == null ? 0
									: students.get(student_id).getFriends().get(_student_id) + 1);
					//
					if (!students.containsKey(_student_id)) {
						Student student = new Student();
						student.setId(_student_id);
						students.put(_student_id, student);
					}
					// 同理，下一位学生与当前学生也满足时间与位置上的好友条件
					students.get(_student_id).getFriends().put(student_id,
							students.get(_student_id).getFriends().get(student_id) == null ? 0
									: students.get(_student_id).getFriends().get(student_id) + 1);
				}
			});
		}
	}

	// 筛选疑似好友
	private void CheckFriends(Bill bill) {
		// 处理队列数据
		// 取出队首元素并从队列中移除
		Bill head = queue.poll();

		// 账单信息中的学生id
		String student_id = head.getStudentId();

		// 如果不存在该生的字段则声明
		if (!students.containsKey(student_id)) {
			Student student = new Student();
			student.setId(student_id);
			students.put(student_id, student);
		}
		// 该生吃饭次数自增
		students.get(student_id).setMealCount(students.get(student_id).getMealCount() + 1);

		// 遍历队列Bill
		queue.forEach(Bill -> {
			if (Bill == bill)
				return;
			// 比较的账单的学生id
			String _student_id = Bill.getStudentId();

			// 判断比较的账单学生_student_id与当前student_id不同并且食堂信息相同
			if (!student_id.equals(_student_id) && head.getCanteenName().equals(Bill.getCanteenName())) {
				// 当前学生与下一位学生满足时间与位置上的好友条件
				students.get(student_id).getFriends().put(_student_id,
						students.get(student_id).getFriends().get(_student_id) == null ? 0
								: students.get(student_id).getFriends().get(_student_id) + 1);
				//
				if (!students.containsKey(_student_id)) {
					Student student = new Student();
					student.setId(_student_id);
					students.put(_student_id, student);
				}
				// 同理，下一位学生与当前学生也满足时间与位置上的好友条件
				students.get(_student_id).getFriends().put(student_id,
						students.get(_student_id).getFriends().get(student_id) == null ? 0
								: students.get(_student_id).getFriends().get(student_id) + 1);
			}
		});
	}

	// 处理结果
	private void HandleResult() {
		//
		students.forEach((key, value) -> {
			// 储存好友学号
			List<String> temp_list_friends = new ArrayList<String>();
			for (String _key : value.getFriends().keySet()) {
				if (value.getFriends().get(_key) / (double) value.getMealCount() > R) {
					temp_list_friends.add(_key);
				}
			}
			// 将当前学生的好友列表存入friends列表
			Friend friend = new Friend();
			friend.setStudent_id(key);
			friend.setFriends_id(temp_list_friends);
			friends.add(friend);
		});

		// 象征意义的置空变量
		students.clear();
		students = null;
		//
		System.out.println("数据处理完毕！");
		// 输出数据
		OutputResult();
	}

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

	// 数据转换为账单
	private Bill data2Bill(String data) {
		// 分隔数据
		String[] values = data.split(",");
		// 创建账单实例
		Bill bill = new Bill();
		// 数据第一项：String转换为milliseconds
		bill.setMillis(string2Date(values[0]).getTime());
		// 数据第二项：学生id
		bill.setStudentId(values[1]);
		// 数据第三项：食堂名称
		bill.setCanteenName(values[2]);
		// 数据第四项：Pos机编号
		bill.setPos(Integer.parseInt(values[3]));

		return bill;
	}

	// 字符串转换为日期类型
	private Date string2Date(String stringFromat) {
		// 根据数据时间格式转换成日期
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.parse(stringFromat, new ParsePosition(0));
	}
}
