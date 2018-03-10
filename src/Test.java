import java.awt.event.ActionEvent;

import javax.swing.JDialog;

import view.InputDialog;
import view.InputDialog.OnClickListener;

public class Test {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			final InputDialog dialog = new InputDialog("«Î ‰»Î√‹¬Î");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);

			dialog.setOnComfirmclikListener(new OnClickListener() {

				@Override
				public void onClick(ActionEvent e, String passWord) {
					dialog.dispose();
					System.out
							.println("----------close dialog ---------  pasword: "
									+ passWord);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
