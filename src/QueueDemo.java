import java.util.LinkedList;
import java.util.Queue;

public class QueueDemo {
	// �趨���г���Ϊ5
	private static int limitSize = 5;
	private static Queue<Integer> queue;
	private static int[] numbers = new int[100];
	private static int sum;

	public static void main(String[] args) {
		// ��ʼ������
		queue = new LinkedList<Integer>();
		// �����������
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = (int) (Math.random() * (100 - 1 + 1));
		}

		for (int i = 0; i < numbers.length; i++) {
			// ��Ӷ���Ԫ��
			if (queue.size() < limitSize) {
				// ��Ԫ�����
				queue.offer(numbers[i]);
				continue;
			}
			// ��������
			// TODO:��Ҫ���еĲ���
			// ���磺��ǰ���е�ƽ���������
			sum = 0;
			// ���������е�����Ԫ��
			queue.forEach(Integer -> {
				sum += Integer;
			});
			//
			System.out.println(sum / queue.size());
			// �Ƴ�����Ԫ��
			queue.poll();
			// ��ǰ����������ӵ���β
			queue.offer(numbers[i]);
		}
	}
}
