import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
			String id, msg;
			PackageToSend packageReceived;
			ResponseMsg response = new ResponseMsg();
			while (true) {
				//creates and listen a client socket
				//and write on serv textArea the client msg
				Socket clientsSocket = servSocket.accept();
				ObjectInputStream data =
						new ObjectInputStream
						(clientsSocket.getInputStream());
				packageReceived = (PackageToSend) data.readObject();
				id = packageReceived.getId();
				msg = packageReceived.getMsg();
				if(id.equals(""))
					textField.append("Unknown: " + msg + "\n");
				else
					textField.append(id + ": " + msg + "\n");
				data.close();
				//*****************
				//creates and sends to client a server response msg 
				Socket sendResponse = new Socket
						("localhost", 9090);
				PackageToSend packageResponse = 
						new PackageToSend();
				packageResponse.setId("Bot");
				packageResponse.setMsg(
						response.sendResponse(msg));
				ObjectOutputStream dataResponse = 
						new ObjectOutputStream
						(sendResponse.getOutputStream());
				dataResponse.writeObject(packageResponse);
				dataResponse.close();
				sendResponse.close();
				clientsSocket.close();
//				DataInputStream inputStream = new DataInputStream(clientsSocket.getInputStream());
//				String text_msg = inputStream.readUTF();
//				inputStream.close();
//				textField.append(text_msg + "\n");
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

class ResponseMsg{
	
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String sendResponse(String input) {
		String output = "";
		switch (input.toLowerCase()) {
		case "hi":
		case "good morning":
		case "hello":
		case "good afternoon":
		case "greetings":
		case "good night":
			output = "Welcome to Luis's ChatBot, "
					+ "How Can i Help you";
			break;
		default:
			output = "Unknown Key message, Sending Default"
					+ " Response";
			break;
		}
		return output;
	}
}
