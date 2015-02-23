package client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientGUI extends JFrame implements ActionListener,
		WindowListener, KeyListener {
	private JPanel panel;
	private JTextField output;
	private JTextField input;
	private JButton send;
	private PrintWriter out;
	private BufferedReader in;
	private BufferedReader read;
	private SSLSocket socket;

	public String getPassword() {
		String password = JOptionPane.showInputDialog("Enter password");
		return password;
	}

	public void openConsole(BufferedReader read, PrintWriter out,
			BufferedReader in, SSLSocket socket) {

		this.out = out;
		this.in = in;
		this.read = read;
		this.socket = socket;

		panel = new JPanel(new GridBagLayout());
		panel.setPreferredSize(new Dimension(400, 400));

		output = new JTextField("Response: ");
		output.setPreferredSize(new Dimension(300, 40));
		output.setEditable(false);
		input = new JTextField("");
		input.setPreferredSize(new Dimension(200, 40));
		send = new JButton("Send");
		send.setPreferredSize(new Dimension(80, 40));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;

		panel.add(output, c);
		c.gridy++;
		panel.add(input, c);
		c.gridx++;
		panel.add(send, c);
		add(panel);
		setVisible(true);

		panel.setVisible(true);
		send.addActionListener(this);
		input.addKeyListener(this);
		setSize(new Dimension(400, 150));

		addWindowListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == send || arg0.getSource() == input) {
			sendAndWaitForResponse();

		}

	}

	private void sendAndWaitForResponse() {
		// first send our message
		System.out.println(out);
		out.println(input.getText());
		out.flush();

		 output.setText("Awaiting response...");
		
		// then wait for response
		try {
			output.setText("Response: " + in.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void disconnect() {
		try {
			in.close();
			out.close();
			read.close();
			socket.close();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void windowClosing(java.awt.event.WindowEvent arg0) {
		disconnect();

	}

	public void windowDeactivated(java.awt.event.WindowEvent arg0) {

	}

	public void windowDeiconified(java.awt.event.WindowEvent arg0) {

	}

	public void windowIconified(java.awt.event.WindowEvent arg0) {

	}

	public void windowOpened(java.awt.event.WindowEvent arg0) {

	}

	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			sendAndWaitForResponse();
		}

	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
