import javax.swing.JOptionPane; // program uses JOptionPane

public class Product {
	// public static void main(String args[]) {
	// String firstNumber;
	// String secondNumber;
	// int number1 = 0;
	// int number2 = 0;
	// int product = 0;
	// firstNumber = JOptionPane.showInputDialog("输入乘数");
	// secondNumber = JOptionPane.showInputDialog("输入被乘数");
	// try {
	// number1 = Integer.parseInt(firstNumber);
	// number2 = Integer.parseInt(secondNumber);
	// JOptionPane.showMessageDialog(null, number1 + "*" + number2 + "="
	// + product, "结果", JOptionPane.PLAIN_MESSAGE);
	// } catch (NumberFormatException ex) {
	// JOptionPane.showMessageDialog(null, "你在输入对话框中没有输入整数值", "消息",
	// JOptionPane.PLAIN_MESSAGE);
	// System.exit(0);
	// }
	// product = number1 * number2;
	// System.exit(0); // terminate application with window
	// } // end method main

	public static void main(String[] args) {
		String network = "10";
		String ssid = "P1003";

		String setSsid = "wpa_cli -i add_network " + network + " ssid \"" + ssid +"\"";
		System.out.println(setSsid);
	}

} // end class Addition
