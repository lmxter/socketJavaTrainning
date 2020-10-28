import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

public class Server {
	
	public static void main(String[] args) {
		servWindow svWindow = new servWindow();
		svWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class servWindow extends JFrame implements Runnable {
	
	private JTextArea textField;
	
	public servWindow() {
		setBounds(1200,300,280,350);
		JPanel servWindow = new JPanel();
		servWindow.setLayout(new BorderLayout());
		textField = new JTextArea();
		servWindow.add(textField, BorderLayout.CENTER);
		add(servWindow);
		setVisible(true);
		Thread servThread = new Thread(this);
		servThread.start();
	}

	@Override
	public void run() {
		try {
			ServerSocket servSocket = new ServerSocket(5000);
			while (true) {
				Socket clientsSocket = servSocket.accept();
				DataInputStream inputStream = new DataInputStream(clientsSocket.getInputStream());
				String text_msg = inputStream.readUTF();
				inputStream.close();
				textField.append(text_msg + "\n");
				clientsSocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}