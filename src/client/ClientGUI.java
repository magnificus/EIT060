package client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientGUI extends JFrame implements ActionListener {
	private JPanel panel;
	private JLabel output;
	private JTextField input;
	private JButton send;
	private PrintWriter out;
	private BufferedReader in;

	public ClientGUI (ClientConnectionHandler connection, BufferedReader read, PrintWriter out, BufferedReader in)  {
		
		this.out = out;
		this.in = in;
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(400,400));
		

		output = new JLabel("Response: ");
		output.setPreferredSize(new Dimension(200,40));
		input = new JTextField("");
		input.setPreferredSize(new Dimension(200,40));
		send = new JButton("Send");
		send.setPreferredSize(new Dimension(80,80));

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
		setPreferredSize(new Dimension(400,400));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		// first send our message
		System.out.println(out);
		out.println(input.getText());
		out.flush();
		
		// then wait for response
		try {
			output.setText("Response: " +in.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
