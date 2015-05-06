import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;



class ExitListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}

class AboutListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		JFrame newframe = new JFrame("About this app");
		newframe.setSize(300, 200);
		newframe.setLayout(null);
		newframe.setVisible(true);
		JLabel msglabel1 = new JLabel();
		msglabel1.setText("Made by Shantam Sinha");
		msglabel1.setBounds(20, 50, 170, 20);
		newframe.add(msglabel1);
	}
}

public class Sim8086{
		
	public static void main(String args[]) throws IOException{
		JFrame firstWindow = new JFrame("8086 Simulator");
		ImageIcon img = new ImageIcon(ImageIO.read(new File("res/images/projLogo.PNG")));
		JButton logo = new JButton();
		logo.setIcon(img);
		logo.setBounds(100,50,459,201);
		JLabel msglabel1 = new JLabel();
		msglabel1.setText("Enter the name of the VM : ");
		msglabel1.setBounds(90, 315, 250, 20);
		firstWindow.add(msglabel1);
		JLabel msglabel2 = new JLabel();
		msglabel2.setText("Select the b/g color : ");
		msglabel2.setBounds(90, 355, 250, 20);
		firstWindow.add(msglabel2);
		JLabel msglabel3 = new JLabel();
		msglabel3.setText("Select the text color : ");
		msglabel3.setBounds(90, 405, 250, 20);
		firstWindow.add(msglabel3);
		JMenuBar mb = new JMenuBar();
		firstWindow.setJMenuBar(mb);
		JMenu fileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");
		mb.add(fileMenu);
		mb.add(helpMenu);
		JMenuItem exitItem = new JMenuItem("Exit");
		JMenuItem aboutItem = new JMenuItem("About");
		fileMenu.add(exitItem);
		ExitListener exitL = new ExitListener();
		exitItem.addActionListener(exitL);
		helpMenu.add(aboutItem);
		AboutListener aboutL = new AboutListener();
		aboutItem.addActionListener(aboutL);
		JTextField name = new JTextField(15);
		name.setBounds(250,310,300,30);
		JRadioButton a1 = new JRadioButton("White");
		JRadioButton a2 = new JRadioButton("Black");
		JRadioButton a3 = new JRadioButton("Blue");
		JRadioButton a4 = new JRadioButton("Purple");
		JRadioButton b1 = new JRadioButton("White");
		JRadioButton b2 = new JRadioButton("Black");
		JRadioButton b3 = new JRadioButton("Green");
		JRadioButton b4 = new JRadioButton("Red");
		a1.setBounds(250, 350, 75, 30);
		a2.setBounds(325, 350, 75, 30);
		a3.setBounds(400, 350, 75, 30);
		a4.setBounds(475, 350, 75, 30);
		b1.setBounds(250, 400, 75, 30);
		b2.setBounds(325, 400, 75, 30);
		b3.setBounds(400, 400, 75, 30);
		b4.setBounds(475, 400, 75, 30);
		firstWindow.add(a1);
		firstWindow.add(a2);
		firstWindow.add(a3);
		firstWindow.add(a4);
		firstWindow.add(b1);
		firstWindow.add(b2);
		firstWindow.add(b3);
		firstWindow.add(b4);
		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(a1);
		bg1.add(a2);
		bg1.add(a3);
		bg1.add(a4);
		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(b1);
		bg2.add(b2);
		bg2.add(b3);
		bg2.add(b4);
		boolean isa1Selected = a1.isSelected();
		boolean isa2Selected = a2.isSelected();
		boolean isa3Selected = a3.isSelected();
		boolean isa4Selected = a4.isSelected();
		boolean isb1Selected = b1.isSelected();
		boolean isb2Selected = b2.isSelected();
		boolean isb3Selected = b3.isSelected();
		boolean isb4Selected = b4.isSelected();
		firstWindow.setSize(700,700);
		firstWindow.setLayout(null);
		firstWindow.setVisible(true);
		firstWindow.add(logo);
		firstWindow.add(name);
		
		firstWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				System.exit(0);
					}
				});
		logo.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e)
	            {
	            	firstWindow.addWindowListener(new WindowAdapter() {
	    				public void windowClosing(WindowEvent windowevent) {
	    					System.exit(0);
	    				}
	    			}); 
	           	 String bgStr, txtStr;
	           	 String vmName;
	             bgStr = "";
	       		 txtStr = "";
	       		 if (isa1Selected) {
	       			    bgStr = "White";
	       			}
	       			else if(isa2Selected){
	       				bgStr = "Black";
	       			}
	       			else if(isa3Selected){
	       				bgStr = "Blue";
	       			}
	       			else if(isa4Selected){
	       				bgStr = "Purple";
	       			}
	       			if (isb1Selected) {
	       			    txtStr = "White";
	       			}
	       			else if(isb2Selected){
	       				txtStr = "Black";
	       			}
	       			else if(isb3Selected){
	       				txtStr = "Green";
	       			}
	       			else if(isb4Selected){
	       				txtStr = "Red";
	       			}
	       		 vmName = name.getText();
	    		 if(vmName=="") {
	    			vmName = "8086";
	    		}
	    		 if("white".equalsIgnoreCase(bgStr))
	    		 {
	    			 if("white".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	    				 Runtime.getRuntime().exec("color FF");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("black".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color F0");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("green".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color F2");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("red".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color F4");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	    		 }
	    		 else if("black".equalsIgnoreCase(bgStr))
	    		 {
	    			 if("white".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	    				 Runtime.getRuntime().exec("color 0F");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("black".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color 00");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("green".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color 02");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("red".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color 04");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	    		 }
	    		 else if("blue".equalsIgnoreCase(bgStr))
	    		 {
	    			 if("white".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	    				 Runtime.getRuntime().exec("color 1F");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("black".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color 10");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("green".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color 12");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("red".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color 14");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	    		 }
	    		 else if("Purple".equalsIgnoreCase(bgStr))
	    		 {
	    			 if("white".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	    				 Runtime.getRuntime().exec("color 5F");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("black".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color 50");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("green".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color 52");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	       		 else if("red".equalsIgnoreCase(txtStr))
	       		 {
	       			 try{
	       			 Runtime.getRuntime().exec("color 54");
	       			 }catch(IOException ex)
	       			 {
	       				 System.out.println();
	       			 }
	       		 }
	    		 }
	            	new SimConsole();
	    		 	SimConsole.runConsole(vmName);
	            }
        });
		firstWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				System.exit(0);
			}
		});
	}
	}

