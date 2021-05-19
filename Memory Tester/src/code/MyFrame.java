package code;

import javax.swing.JFrame;

public class MyFrame extends JFrame {
	
	MyPanel panel;
	
	public MyFrame() {
		new JFrame();
		setSize(300, 450);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Memory Tester");
		
		panel = new MyPanel();
		add(panel);
		
		panel.revalidate();
		panel.repaint();
	}
}

