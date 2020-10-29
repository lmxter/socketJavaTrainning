import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {

	public static void main(String[] args) {
		clientWindow clWindow = new clientWindow();
		clWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class clientWindow extends JFrame {

	public clientWindow() {
		setBounds(600, 300, 280, 350);
		clientWindowAux clientWindow = new clientWindowAux();
		add(clientWindow);
		setVisible(true);
	}
}

class clientWindowAux extends JPanel implements Runnable {

	private JTextField field1,  id;
	private JButton button;
	private JTextArea textArea;
	private JPanel panel;

	public clientWindowAux() {
		//client chat username
		this.id = new JTextField(5);
		add(id);
		JLabel tittle = new JLabel("CHAT SIMULATOR");
		add(tittle);
		this.textArea = new JTextArea(12, 20);
		add(textArea);
		this.field1 = new JTextField(20);
		add(field1);
		this.button = new JButton("Send");
		SendText event = new SendText();
		button.addActionListener(event);
		add(button);
		Thread servThread = new Thread(this);
		servThread.start();
	}
	
	private class SendText implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//creates socket connection to the serv IP throw
				//the specified port
				Socket clientSocket = 
						new Socket("LOCALHOST"
								,5000);
				
				PackageToSend data = new PackageToSend();
				data.setId(id.getText());
				data.setMsg(field1.getText());
				ObjectOutputStream outStream =
						new ObjectOutputStream
						(clientSocket.getOutputStream());
				outStream.writeObject(data);
				outStream.close();
				clientSocket.close();
				
				/*
				 * DataOutputStream outStream = new DataOutputStream
				 * (clientSocket.getOutputStream()); outStream.writeUTF(field1.getText());
				 * outStream.close();
				 */
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket servClient = new ServerSocket(9090);
			Socket client;
			String id, msg;
			PackageToSend packageReceived;
			while(true) {
				client = servClient.accept();
				ObjectInputStream inStream = 
						new ObjectInputStream(
								client.getInputStream());
				packageReceived = (PackageToSend) inStream.readObject();
				id = packageReceived.getId();
				msg = packageReceived.getMsg();
				this.textArea.append(id + ": " + msg + "\n");
				inStream.close();
				client.close();
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
}

class PackageToSend implements Serializable{
	
	private String id, msg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

