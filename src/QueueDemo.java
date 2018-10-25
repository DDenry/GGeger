import java.util.LinkedList;
import java.util.Queue;

public class QueueDemo {
	// 设定队列长度为5
	private static int limitSize = 5;
	private static Queue<Integer> queue;
	private static int[] numbers = new int[100];
	private static int sum;

	public static void main(String[] args) {
		// 初始化队列
		queue = new LinkedList<Integer>();
		// 随机生成数组
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = (int) (Math.random() * (100 - 1 + 1));
		}

		for (int i = 0; i < numbers.length; i++) {
			// 添加队列元素
			if (queue.size() < limitSize) {
				// 将元素入队
				queue.offer(numbers[i]);
				continue;
			}
			// 队列已满
			// TODO:需要进行的操作
			// 例如：求当前队列的平均数并输出
			sum = 0;
			// 遍历队列中的所有元素
			queue.forEach(Integer -> {
				sum += Integer;
			});
			//
			System.out.println(sum / queue.size());
			// 移除队首元素
			queue.poll();
			// 当前数组数据添加到队尾
			queue.offer(numbers[i]);
		}
	}
}
