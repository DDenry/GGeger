import java.util.Vector;

public class VectorDemo {

	public VectorDemo() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Vector<String> vector = new Vector();
		vector.add(0, "1");
		vector.add(1, "2");
		vector.add(2, "3");

		System.out.println(vector.size());
		System.out.println(vector.get(0));

		vector.remove(0);

		System.out.println(vector.size());
		System.out.println(vector.get(0));
	}

}
