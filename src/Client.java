import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

class clientWindowAux extends JPanel {

	private JTextField field1;
	private JButton button;

	public clientWindowAux() {
		JLabel tittle = new JLabel("Client");
		add(tittle);
		this.field1 = new JTextField(20);
		add(field1);
		this.button = new JButton("Send");
		SendText event = new SendText();
		button.addActionListener(event);
		add(button);
	}
	
	private class SendText implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//creates socket connection to the serv IP throw
				//the specified port
				Socket clientSocket = 
						new Socket("#SET_YOUR_IP_OR_SERVER IP"
								,5000);
				DataOutputStream outStream  = 
						new DataOutputStream
						(clientSocket.getOutputStream());
				outStream.writeUTF(field1.getText());
				outStream.close();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
			}
		}
	}
}

