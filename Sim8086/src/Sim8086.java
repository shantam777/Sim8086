import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;
import javax.swing.*;

class Mem{
	byte memArray;
	Mem(){
		memArray = 0;
	}
}

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
	static byte AL,AH,BL,BH,CL,CH,DL,DH;
	static int memIndex;
	static int stackPointer;
	static short CS,DS,ES,SS;
	static String tempString;
	static boolean OF,DF,SF,TF,ZF,CF,PF,AF,IF;
	static Mem[] memMap;
	static byte tempByte;
	static byte[] tempSeg;
	static byte[] stack;
	public Sim8086() {
		memMap = new Mem[1024];
		stack = new byte[64];
		AL = 0;
		AH = 0;
		BL = 0;
		BH = 0;
		CL = 0;
		CH = 0;
		DL = 0;
		DH = 0;
		CS = 0;
		DS = 0;
		ES = 0;
		SS = 0;
		ZF = true;
		OF = false;
		DF = false;
		SF = false;
		TF = false;
		CF = false;
		PF = false;
		AF = false;
		IF = false;
		memIndex = 0;
		tempString = "";
		tempByte = 0;
		tempSeg = new byte[2];
		stackPointer = -1;
	}
	private static final int PAGE_SIZE = 25;

	private static void clearScreen() {
	    for (int i = 0; i < PAGE_SIZE; i++) {
	        System.out.println();
	    }
	}
	
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
	        		 Scanner sc = new Scanner(System.in);
	        		 int ch;
	        		 String bgStr, txtStr;
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
	        		 StringTokenizer instnComplete;
	        		 String instn, vmName;
	        			firstWindow.addWindowListener(new WindowAdapter() {
	        				public void windowClosing(WindowEvent windowevent) {
	        					System.exit(0);
	        				}
	        			});
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
	    new Sim8086();
	    while(true)
		{
	    	clearScreen();
	    	System.out.println("\t\t\t*********8086 Simulator v0.4a*************\n\n\n\n");
			System.out.println("1. Execute an instruction\n2. Display All Registers and Flags\n3. Alter memory map\n4. Exit\n\n");
			System.out.print("Enter your choice : ");
			ch = sc.nextInt();
			if(ch==1)
			{
				clearScreen();
				System.out.println("Choose from the following instructions : \nMOV XCHG LEA LDS LES ADD ADC SUB SBB INC DEC NEG CMP MUL NOT AND OR XOR TEST SHL/SAL SAR SHR ROL ROR RCL RCR IN OUT PUSH POP STC CLC CMC STD CLD STI CLI\n\n");
				System.out.println("Enter the instruction : ");
				sc.nextLine();
				instnComplete = new StringTokenizer(sc.nextLine());
				instn = instnComplete.nextToken();
				if("mov".equalsIgnoreCase(instn))
				{
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if(ch==1)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								AL = memMap[memIndex].memArray;
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								AH = memMap[memIndex].memArray;
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								BL = memMap[memIndex].memArray;
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								BH = memMap[memIndex].memArray;
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								CL = memMap[memIndex].memArray;
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								CH = memMap[memIndex].memArray;
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
								DL = memMap[memIndex].memArray;
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								DH = memMap[memIndex].memArray;
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = memMap[memIndex].memArray;
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = memMap[memIndex].memArray;
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = memMap[memIndex].memArray;
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==2)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register\n3. Memory Location");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								AL = tempByte;
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								AH = tempByte;
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								BL = tempByte;
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								BH = tempByte;
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								CL = tempByte;
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								CH = tempByte;
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
								DL = tempByte;
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								DH = tempByte;
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = tempByte;
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = tempByte;
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = tempByte;
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==3)
						{
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination location : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Destination memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							try{
								memMap[memIndex] = new Mem();
								memMap[memIndex].memArray = tempByte; 
							}catch(NullPointerException ex)
							{
								System.out.println("ERROR : Unable to perform this operation! Returning to main menu...");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==3)
					{
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register\n2. Memory Location");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								tempByte = AL;
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								tempByte = AH;
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								tempByte = BL;
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								tempByte = BH;
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								tempByte = CL;
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								tempByte = CH;
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
							    tempByte = DL;
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								tempByte = DH;
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = tempByte;
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = tempByte;
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = tempByte;
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								tempByte = AL;
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								tempByte = AH;
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								tempByte = BL;
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								tempByte = BH;
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								tempByte = CL;
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								tempByte = CH;
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
							    tempByte = DL;
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								tempByte = DH;
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination location : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Destination memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							try{
								memMap[memIndex] = new Mem();
								memMap[memIndex].memArray = tempByte; 
							}catch(NullPointerException ex)
							{
								System.out.println("ERROR : Unable to perform this operation! Returning to main menu...");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==4)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Memory Location");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("CS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							}
							else if("ES".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if("AX".equalsIgnoreCase(tempString))
							{
								AL = tempSeg[0];
								AH = tempSeg[1];
							}
							else if("BX".equalsIgnoreCase(tempString))
							{
								BL = tempSeg[0];
								BH = tempSeg[1];
							}
							else if("CX".equalsIgnoreCase(tempString))
							{
								CL = tempSeg[0];
								CH = tempSeg[1];
							}
							else if("DX".equalsIgnoreCase(tempString))
							{
								DL = tempSeg[0];
								DH = tempSeg[1];
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("CS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							}
							else if("ES".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination location : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1022)
							{
								System.out.println("ERROR : Destination memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							try{
								memMap[memIndex] = new Mem();
								memMap[memIndex+1] = new Mem();
								memMap[memIndex].memArray = tempSeg[0]; 
								memMap[memIndex+1].memArray = tempSeg[1]; 
							}catch(NullPointerException ex)
							{
								System.out.println("ERROR : Unable to perform this operation! Returning to main menu...");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else
					{
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				}
				else if("xchg".equalsIgnoreCase(instn))
				{
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. General Register\n3. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if(ch==1)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								tempByte = AL;
								AL = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								tempByte = AH;
								AH = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								tempByte = BL;
								BL = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								tempByte = BH;
								BH = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								tempByte = CL;
								CL = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								tempByte = CH;
								CL = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
								tempByte = DL;
								DL = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								tempByte = DH;
								DH = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
								ES = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempSeg[0];
								memMap[memIndex+1].memArray = tempSeg[1];
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
								DS = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempSeg[0];
								memMap[memIndex+1].memArray = tempSeg[1];
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
								SS = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempSeg[0];
								memMap[memIndex+1].memArray = tempSeg[1];
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==2)
					{
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if("AX".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = AL;
								tempSeg[1] = AH;
								System.out.print("Enter destination register : ");
								tempString = sc.next();
								if("ES".equalsIgnoreCase(tempString))
								{
									AL = (byte)(ES & 0xff);
									AH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("DS".equalsIgnoreCase(tempString))
								{
									AL = (byte)(DS & 0xff);
									AH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("SS".equalsIgnoreCase(tempString))
								{
									AL = (byte)(SS & 0xff);
									AH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else
								{
									System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							}
							else if("BX".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = BL;
								tempSeg[1] = BH;
								System.out.print("Enter destination register : ");
								tempString = sc.next();
								if("ES".equalsIgnoreCase(tempString))
								{
									BL = (byte)(ES & 0xff);
									BH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("DS".equalsIgnoreCase(tempString))
								{
									BL = (byte)(DS & 0xff);
									BH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("SS".equalsIgnoreCase(tempString))
								{
									BL = (byte)(SS & 0xff);
									BH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else
								{
									System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							}
							else if("CX".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = CL;
								tempSeg[1] = CH;
								System.out.print("Enter destination register : ");
								tempString = sc.next();
								if("ES".equalsIgnoreCase(tempString))
								{
									CL = (byte)(ES & 0xff);
									CH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("DS".equalsIgnoreCase(tempString))
								{
									CL = (byte)(DS & 0xff);
									CH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("SS".equalsIgnoreCase(tempString))
								{
									CL = (byte)(SS & 0xff);
									CH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else
								{
									System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							}
							else if("DX".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = DL;
								tempSeg[1] = DH;
								System.out.print("Enter destination register : ");
								tempString = sc.next();
								if("ES".equalsIgnoreCase(tempString))
								{
									DL = (byte)(ES & 0xff);
									DH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("DS".equalsIgnoreCase(tempString))
								{
									DL = (byte)(DS & 0xff);
									DH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("SS".equalsIgnoreCase(tempString))
								{
									DL = (byte)(SS & 0xff);
									DH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else
								{
									System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==3)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("CS".equalsIgnoreCase(tempString))
							{
								System.out.print("Enter destination register (AX,BX,CX,DX) : ");
								tempString = sc.next();
								if("AX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = AL;
									tempSeg[1] = AH;
									AL = (byte)(CS & 0xff);
									AH = (byte)((CS >> 8) & 0xff);
									CS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("BX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = BL;
									tempSeg[1] = BH;
									BL = (byte)(CS & 0xff);
									BH = (byte)((CS >> 8) & 0xff);
									CS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("CX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = CL;
									tempSeg[1] = CH;
									CL = (byte)(CS & 0xff);
									CH = (byte)((CS >> 8) & 0xff);
									CS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("DX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = DL;
									tempSeg[1] = DH;
									DL = (byte)(CS & 0xff);
									DH = (byte)((CS >> 8) & 0xff);
									CS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else
								{
									System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								System.out.print("Enter destination register (AX,BX,CX,DX) : ");
								tempString = sc.next();
								if("AX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = AL;
									tempSeg[1] = AH;
									AL = (byte)(DS & 0xff);
									AH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("BX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = BL;
									tempSeg[1] = BH;
									BL = (byte)(DS & 0xff);
									BH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("CX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = CL;
									tempSeg[1] = CH;
									CL = (byte)(DS & 0xff);
									CH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("DX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = DL;
									tempSeg[1] = DH;
									DL = (byte)(DS & 0xff);
									DH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else
								{
									System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							}
							else if("ES".equalsIgnoreCase(tempString))
							{
								System.out.print("Enter destination register (AX,BX,CX,DX) : ");
								tempString = sc.next();
								if("AX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = AL;
									tempSeg[1] = AH;
									AL = (byte)(ES & 0xff);
									AH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("BX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = BL;
									tempSeg[1] = BH;
									BL = (byte)(ES & 0xff);
									BH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("CX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = CL;
									tempSeg[1] = CH;
									CL = (byte)(ES & 0xff);
									CH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("DX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = DL;
									tempSeg[1] = DH;
									DL = (byte)(ES & 0xff);
									DH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else
								{
									System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								System.out.print("Enter destination register (AX,BX,CX,DX) : ");
								tempString = sc.next();
								if("AX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = AL;
									tempSeg[1] = AH;
									AL = (byte)(SS & 0xff);
									AH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("BX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = BL;
									tempSeg[1] = BH;
									BL = (byte)(SS & 0xff);
									BH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("CX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = CL;
									tempSeg[1] = CH;
									CL = (byte)(SS & 0xff);
									CH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else if("DX".equalsIgnoreCase(tempString))
								{
									tempSeg[0] = DL;
									tempSeg[1] = DH;
									DL = (byte)(SS & 0xff);
									DH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								}
								else
								{
									System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else
					{
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				}
				else if("lea".equalsIgnoreCase(instn))
				{
					
				}
				else if("lds".equalsIgnoreCase(instn))
				{
					
				}
				else if("les".equalsIgnoreCase(instn))
				{
					
				}
				else if("adc".equalsIgnoreCase(instn))
				{
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if(ch==1)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL ^ memMap[memIndex].memArray);
								if(CF) {
									AL++;
								}
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								AH = (byte) (AH ^ memMap[memIndex].memArray);
								if(CF) {
									AH++;
								}
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL ^ memMap[memIndex].memArray);
								if(CF) {
									BL++;
								}
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								BH = (byte) (BH ^ memMap[memIndex].memArray);
								if(CF) {
									BH++;
								}
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL ^ memMap[memIndex].memArray);
								if(CF) {
									CL++;
								}
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								CH = (byte) (CH ^ memMap[memIndex].memArray);
								if(CF) {
									CH++;
								}
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL ^ memMap[memIndex].memArray);
								if(CF) {
									DL++;
								}
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								DH = (byte) (DH ^ memMap[memIndex].memArray);
								if(CF) {
									DH++;
								}
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short) (ES ^ memMap[memIndex].memArray);
								if(CF) {
									ES++;
								}
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short) (DS ^ memMap[memIndex].memArray);
								if(CF) {
									DS++;
								}
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short) (SS ^ memMap[memIndex].memArray);
								if(CF) {
									SS++;
								}
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==2)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL ^ tempByte);
								if(CF) {
									AL++;
								}
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								AH = (byte) (AH ^ tempByte);
								if(CF) {
									AH++;
								}
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL ^ tempByte);
								if(CF) {
									BL++;
								}
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								BH = (byte) (BH ^ tempByte);
								if(CF) {
									BH++;
								}
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL ^ tempByte);
								if(CF) {
									CL++;
								}
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								CH = (byte) (CH ^ tempByte);
								if(CF) {
									CH++;
								}
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL ^ tempByte);
								if(CF) {
									DL++;
								}
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								DH = (byte) (DH ^ tempByte);
								if(CF) {
									DH++;
								}
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short)(ES ^ tempByte);
								if(CF) {
									ES++;
								}
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short)(DS ^ tempByte);
								if(CF) {
									DS++;
								}
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short)(SS ^ tempByte);
								if(CF) {
									SS++;
								}
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==3)
					{
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								tempByte = AL;
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								tempByte = AH;
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								tempByte = BL;
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								tempByte = BH;
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								tempByte = CL;
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								tempByte = CH;
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
							    tempByte = DL;
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								tempByte = DH;
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short)(ES ^ tempByte);
								if(CF) {
									ES++;
								}
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short)(DS ^ tempByte);
								if(CF) {
									DS++;
								}
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short)(SS ^ tempByte);
								if(CF) {
									SS++;
								}
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==4)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("CS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							}
							else if("ES".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if("AX".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL ^ tempSeg[0]);
								AH = (byte) (AH ^ tempSeg[1]);
								if(CF) {
									AL++;
								}
							}
							else if("BX".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL ^ tempSeg[0]);
								BH = (byte) (BH ^ tempSeg[1]);
								if(CF) {
									BL++;
								}
							}
							else if("CX".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL ^ tempSeg[0]);
								CH = (byte) (CH ^ tempSeg[1]);
								if(CF) {
									CL++;
								}
							}
							else if("DX".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL ^ tempSeg[0]);
								DH = (byte) (DH ^ tempSeg[1]);
								if(CF) {
									DL++;
								}
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else
					{
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				}
				else if("add".equalsIgnoreCase(instn)||"xor".equalsIgnoreCase(instn))
				{
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if(ch==1)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL ^ memMap[memIndex].memArray);
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								AH = (byte) (AH ^ memMap[memIndex].memArray);
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL ^ memMap[memIndex].memArray);
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								BH = (byte) (BH ^ memMap[memIndex].memArray);
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL ^ memMap[memIndex].memArray);
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								CH = (byte) (CH ^ memMap[memIndex].memArray);
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL ^ memMap[memIndex].memArray);
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								DH = (byte) (DH ^ memMap[memIndex].memArray);
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short) (ES ^ memMap[memIndex].memArray);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short) (DS ^ memMap[memIndex].memArray);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short) (SS ^ memMap[memIndex].memArray);
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==2)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL ^ tempByte);
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								AH = (byte) (AH ^ tempByte);
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL ^ tempByte);
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								BH = (byte) (BH ^ tempByte);
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL ^ tempByte);
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								CH = (byte) (CH ^ tempByte);
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL ^ tempByte);
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								DH = (byte) (DH ^ tempByte);
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short)(ES ^ tempByte);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short)(DS ^ tempByte);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short)(SS ^ tempByte);
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==3)
					{
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								tempByte = AL;
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								tempByte = AH;
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								tempByte = BL;
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								tempByte = BH;
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								tempByte = CL;
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								tempByte = CH;
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
							    tempByte = DL;
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								tempByte = DH;
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short)(ES ^ tempByte);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short)(DS ^ tempByte);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short)(SS ^ tempByte);
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==4)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("CS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							}
							else if("ES".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if("AX".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL ^ tempSeg[0]);
								AH = (byte) (AH ^ tempSeg[1]);
							}
							else if("BX".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL ^ tempSeg[0]);
								BH = (byte) (BH ^ tempSeg[1]);
							}
							else if("CX".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL ^ tempSeg[0]);
								CH = (byte) (CH ^ tempSeg[1]);
							}
							else if("DX".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL ^ tempSeg[0]);
								DH = (byte) (DH ^ tempSeg[1]);
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else
					{
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				}
				else if("sub".equalsIgnoreCase(instn))
				{
					
				}
				else if("sbb".equalsIgnoreCase(instn))
				{
					
				}
				else if("inc".equalsIgnoreCase(instn))
				{
					String destn;
					try{
					destn = instnComplete.nextToken();
					}catch(NoSuchElementException ex)
					{
						System.out.println("ERROR : Invalid instruction syntax! Returning to main menu....");
						sc.next();
						continue;
					}
					if("AL".equalsIgnoreCase(destn)||"AX".equalsIgnoreCase(destn))
					{
						AL++;
					}
					else if("BL".equalsIgnoreCase(destn)||"BX".equalsIgnoreCase(destn))
					{
						BL++;
					}
					else if("CL".equalsIgnoreCase(destn)||"CX".equalsIgnoreCase(destn))
					{
						CL++;
					}
					else if("DL".equalsIgnoreCase(destn)||"DX".equalsIgnoreCase(destn))
					{
						DL++;
					}
					else if("AH".equalsIgnoreCase(destn))
					{
						AH++;
					}
					else if("BH".equalsIgnoreCase(destn))
					{
						BH++;
					}
					else if("CH".equalsIgnoreCase(destn))
					{
						CH++;
					}
					else if("DH".equalsIgnoreCase(destn))
					{
						DH++;
					}
					else if("CS".equalsIgnoreCase(destn))
					{
						CS++;
					}
					else if("ES".equalsIgnoreCase(destn))
					{
						ES++;
					}
					else if("DS".equalsIgnoreCase(destn))
					{
						DS++;
					}
					else if("SS".equalsIgnoreCase(destn))
					{
						SS++;
					}
					else
					{
						try{
							memIndex = Integer.parseInt(destn);
							memMap[memIndex].memArray++;
						}catch(ArrayIndexOutOfBoundsException|NullPointerException|NumberFormatException ex)
						{
							System.out.println("ERROR : An error occured while trying to execute this instruction. Returning to main menu...");
							sc.next();
							continue;
						}
					}
				}
				else if("dec".equalsIgnoreCase(instn))
				{
					String destn;
					try{
					destn = instnComplete.nextToken();
					}catch(NoSuchElementException ex)
					{
						System.out.println("ERROR : Invalid instruction syntax! Returning to main menu....");
						sc.next();
						continue;
					}
					if("AL".equalsIgnoreCase(destn)||"AX".equalsIgnoreCase(destn))
					{
						AL--;
					}
					else if("BL".equalsIgnoreCase(destn)||"BX".equalsIgnoreCase(destn))
					{
						BL--;
					}
					else if("CL".equalsIgnoreCase(destn)||"CX".equalsIgnoreCase(destn))
					{
						CL--;
					}
					else if("DL".equalsIgnoreCase(destn)||"DX".equalsIgnoreCase(destn))
					{
						DL--;
					}
					else if("AH".equalsIgnoreCase(destn))
					{
						AH--;
					}
					else if("BH".equalsIgnoreCase(destn))
					{
						BH--;
					}
					else if("CH".equalsIgnoreCase(destn))
					{
						CH--;
					}
					else if("DH".equalsIgnoreCase(destn))
					{
						DH--;
					}
					else if("CS".equalsIgnoreCase(destn))
					{
						CS--;
					}
					else if("ES".equalsIgnoreCase(destn))
					{
						ES--;
					}
					else if("DS".equalsIgnoreCase(destn))
					{
						DS--;
					}
					else if("SS".equalsIgnoreCase(destn))
					{
						SS--;
					}
					else
					{
						try{
							memIndex = Integer.parseInt(destn);
							memMap[memIndex].memArray--;
						}catch(ArrayIndexOutOfBoundsException|NullPointerException|NumberFormatException ex)
						{
							System.out.println("ERROR : An error occured while trying to execute this instruction. Returning to main menu...");
							sc.next();
							continue;
						}
					}
				}
				else if("mul".equalsIgnoreCase(instn))
				{
					
				}
				else if("cmp".equalsIgnoreCase(instn))
				{
					
				}
				else if("neg".equalsIgnoreCase(instn))
				{
					
				}
				else if("not".equalsIgnoreCase(instn))
				{
					
				}
				else if("and".equalsIgnoreCase(instn))
				{
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if(ch==1)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL & memMap[memIndex].memArray);
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								AH = (byte) (AH & memMap[memIndex].memArray);
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL & memMap[memIndex].memArray);
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								BH = (byte) (BH & memMap[memIndex].memArray);
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL & memMap[memIndex].memArray);
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								CH = (byte) (CH & memMap[memIndex].memArray);
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL & memMap[memIndex].memArray);
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								DH = (byte) (DH & memMap[memIndex].memArray);
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short) (ES & memMap[memIndex].memArray);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short) (DS & memMap[memIndex].memArray);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short) (SS & memMap[memIndex].memArray);
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==2)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL & tempByte);
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								AH = (byte) (AH & tempByte);
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL & tempByte);
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								BH = (byte) (BH & tempByte);
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL & tempByte);
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								CH = (byte) (CH & tempByte);
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL & tempByte);
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								DH = (byte) (DH & tempByte);
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short)(ES & tempByte);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short)(DS & tempByte);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short)(SS & tempByte);
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==3)
					{
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								tempByte = AL;
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								tempByte = AH;
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								tempByte = BL;
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								tempByte = BH;
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								tempByte = CL;
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								tempByte = CH;
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
							    tempByte = DL;
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								tempByte = DH;
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short)(ES & tempByte);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short)(DS & tempByte);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short)(SS & tempByte);
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==4)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("CS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							}
							else if("ES".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if("AX".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL & tempSeg[0]);
								AH = (byte) (AH & tempSeg[1]);
							}
							else if("BX".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL & tempSeg[0]);
								BH = (byte) (BH & tempSeg[1]);
							}
							else if("CX".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL & tempSeg[0]);
								CH = (byte) (CH & tempSeg[1]);
							}
							else if("DX".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL & tempSeg[0]);
								DH = (byte) (DH & tempSeg[1]);
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else
					{
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				}
				else if("or".equalsIgnoreCase(instn))
				{
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if(ch==1)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL | memMap[memIndex].memArray);
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								AH = (byte) (AH | memMap[memIndex].memArray);
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL | memMap[memIndex].memArray);
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								BH = (byte) (BH | memMap[memIndex].memArray);
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL | memMap[memIndex].memArray);
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								CH = (byte) (CH | memMap[memIndex].memArray);
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL | memMap[memIndex].memArray);
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								DH = (byte) (DH | memMap[memIndex].memArray);
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if(memIndex<0||memIndex>1023)
							{
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if(memMap[memIndex]==null)
							{
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short) (ES | memMap[memIndex].memArray);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short) (DS | memMap[memIndex].memArray);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short) (SS | memMap[memIndex].memArray);
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==2)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL | tempByte);
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								AH = (byte) (AH | tempByte);
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL | tempByte);
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								BH = (byte) (BH | tempByte);
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL | tempByte);
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								CH = (byte) (CH | tempByte);
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL | tempByte);
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								DH = (byte) (DH | tempByte);
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else if(ch==2)
						{
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short)(ES | tempByte);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short)(DS | tempByte);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short)(SS | tempByte);
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==3)
					{
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("AL".equalsIgnoreCase(tempString))
							{
								tempByte = AL;
							}
							else if("AH".equalsIgnoreCase(tempString))
							{
								tempByte = AH;
							}
							else if("BL".equalsIgnoreCase(tempString))
							{
								tempByte = BL;
							}
							else if("BH".equalsIgnoreCase(tempString))
							{
								tempByte = BH;
							}
							else if("CL".equalsIgnoreCase(tempString))
							{
								tempByte = CL;
							}
							else if("CH".equalsIgnoreCase(tempString))
							{
								tempByte = CH;
							}
							else if("DL".equalsIgnoreCase(tempString))
							{
							    tempByte = DL;
							}
							else if("DH".equalsIgnoreCase(tempString))
							{
								tempByte = DH;
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if("ES".equalsIgnoreCase(tempString))
							{
								ES = (short)(ES | tempByte);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								DS = (short)(DS | tempByte);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								SS = (short)(SS | tempByte);
							}
							else
							{
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else if(ch==4)
					{
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if(ch==1)
						{
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if("CS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(CS | 0xff);
								tempSeg[1] = (byte)((CS >> 8) | 0xff);
							}
							else if("DS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(DS | 0xff);
								tempSeg[1] = (byte)((DS >> 8) | 0xff);
							}
							else if("ES".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(ES | 0xff);
								tempSeg[1] = (byte)((ES >> 8) | 0xff);
							}
							else if("SS".equalsIgnoreCase(tempString))
							{
								tempSeg[0] = (byte)(SS | 0xff);
								tempSeg[1] = (byte)((SS >> 8) | 0xff);
							}
							else
							{
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if("AX".equalsIgnoreCase(tempString))
							{
								AL = (byte) (AL | tempSeg[0]);
								AH = (byte) (AH | tempSeg[1]);
							}
							else if("BX".equalsIgnoreCase(tempString))
							{
								BL = (byte) (BL | tempSeg[0]);
								BH = (byte) (BH | tempSeg[1]);
							}
							else if("CX".equalsIgnoreCase(tempString))
							{
								CL = (byte) (CL | tempSeg[0]);
								CH = (byte) (CH | tempSeg[1]);
							}
							else if("DX".equalsIgnoreCase(tempString))
							{
								DL = (byte) (DL | tempSeg[0]);
								DH = (byte) (DH | tempSeg[1]);
							}
							else
							{
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						}
						else
						{
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					}
					else
					{
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				}
				else if("test".equalsIgnoreCase(instn))
				{
					
				}
				else if("shl".equalsIgnoreCase(instn)||"sal".equalsIgnoreCase(instn))
				{
					
				}
				else if("shr".equalsIgnoreCase(instn))
				{
					
				}
				else if("sar".equalsIgnoreCase(instn))
				{
					
				}
				else if("rol".equalsIgnoreCase(instn))
				{
					
				}
				else if("ror".equalsIgnoreCase(instn))
				{
					
				}
				else if("rcl".equalsIgnoreCase(instn))
				{
					
				}
				else if("rcr".equalsIgnoreCase(instn))
				{
					
				}
				else if("in".equalsIgnoreCase(instn))
				{
					
				}
				else if("out".equalsIgnoreCase(instn))
				{
					
				}
				else if("push".equalsIgnoreCase(instn))
				{
					String destn;
					try{
					destn = instnComplete.nextToken();
					}catch(NoSuchElementException ex)
					{
						System.out.println("ERROR : Invalid instruction syntax! Returning to main menu....");
						sc.next();
						continue;
					}
					if("AX".equalsIgnoreCase(destn))
					{
						tempSeg[0] = AL;
						tempSeg[1] = AH;
					}
					else if("BX".equalsIgnoreCase(destn))
					{
						tempSeg[0] = BL;
						tempSeg[1] = BH;
					}
					else if("CX".equalsIgnoreCase(destn))
					{
						tempSeg[0] = CL;
						tempSeg[1] = CH;
					}
					else if("DX".equalsIgnoreCase(destn))
					{
						tempSeg[0] = DL;
						tempSeg[1] = DH;
					}
					else if("CS".equalsIgnoreCase(destn))
					{
						tempSeg[0] = (byte)(CS | 0xff);
						tempSeg[1] = (byte)((CS >> 8) | 0xff);
					}
					else if("ES".equalsIgnoreCase(destn))
					{
						tempSeg[0] = (byte)(ES | 0xff);
						tempSeg[1] = (byte)((ES >> 8) | 0xff);
					}
					else if("DS".equalsIgnoreCase(destn))
					{
						tempSeg[0] = (byte)(DS | 0xff);
						tempSeg[1] = (byte)((DS >> 8) | 0xff);
					}
					else if("SS".equalsIgnoreCase(destn))
					{
						tempSeg[0] = (byte)(SS | 0xff);
						tempSeg[1] = (byte)((SS >> 8) | 0xff);
					}
					else
					{
						try{
						memIndex = Integer.parseInt(destn);
						}catch(NumberFormatException ex)
						{
							System.out.println("Invalid destination operand! (Enter AX,BX,CX,DX,CS,DS,ES,SS or a memory location between 0 and 1023).");
							System.out.println("Returning to main menu...");
							sc.next();
							continue;
						}
						if(memIndex<0||memIndex>1022)
						{
							System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
							sc.next();
							continue;
						}
						if(memMap[memIndex]==null||memMap[memIndex+1]==null)
						{
							System.out.println("ERROR : Source location is empty! Returning to main menu...");
							sc.next();
							continue;
						}
						try{
							tempSeg[0] = memMap[memIndex].memArray;
							tempSeg[1] = memMap[memIndex+1].memArray;
						}catch(ArrayIndexOutOfBoundsException|NullPointerException|NumberFormatException ex)
						{
							System.out.println("ERROR : An error occured while trying to execute this instruction. Returning to main menu...");
							sc.next();
							continue;
						}
					}
					try{
						stackPointer++;
						stack[stackPointer] = tempSeg[0];
						stackPointer++;
						stack[stackPointer] = tempSeg[1];
					}catch(ArrayIndexOutOfBoundsException ex)
					{
						System.out.println("ERROR : Stack index is out of bounds (Stack is full)! Returning to main menu...");
						sc.next();
						continue;
					}
				}
				else if("pop".equalsIgnoreCase(instn))
				{
					String destn;
					try{
					destn = instnComplete.nextToken();
					}catch(NoSuchElementException ex)
					{
						System.out.println("ERROR : Invalid instruction syntax! Returning to main menu....");
						sc.next();
						continue;
					}
					try{
						tempSeg[1] = stack[stackPointer];
						stackPointer--;
						tempSeg[0] = stack[stackPointer];
						stackPointer--;
					}catch(ArrayIndexOutOfBoundsException ex)
					{
						System.out.println("ERROR : Stack index is out of bounds (Stack is empty)! Returning to main menu...");
						stackPointer--;
						sc.next();
						continue;
					}
					if("AX".equalsIgnoreCase(destn))
					{
						AH = tempSeg[1];
						AL = tempSeg[0];
					}
					else if("BX".equalsIgnoreCase(destn))
					{
						BH = tempSeg[1];
						BL = tempSeg[0];
					}
					else if("CX".equalsIgnoreCase(destn))
					{
						CH = tempSeg[1];
						CL = tempSeg[0];
					}
					else if("DX".equalsIgnoreCase(destn))
					{
						DH = tempSeg[1];
						DL = tempSeg[0];
					}
					else if("CS".equalsIgnoreCase(destn))
					{
						CS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
					}
					else if("ES".equalsIgnoreCase(destn))
					{
						ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
					}
					else if("DS".equalsIgnoreCase(destn))
					{
						DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
					}
					else if("SS".equalsIgnoreCase(destn))
					{
						SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
					}
					else
					{
						try{
							memIndex = Integer.parseInt(destn);
							}catch(NumberFormatException ex)
							{
								System.out.println("Invalid destination operand! (Enter AX,BX,CX,DX,CS,DS,ES,SS or a memory location between 0 and 1023).");
								System.out.println("Returning to main menu...");
								sc.next();
								continue;
							}
						if(memIndex<0||memIndex>1022)
						{
							System.out.println("ERROR : Destination memory location out of bounds! Returning to main menu...");
							sc.next();
							continue;
						}
						if(memMap[memIndex]==null)
						{
							memMap[memIndex] = new Mem();
						}
						if(memMap[memIndex+1]==null)
						{
							memMap[memIndex+1] = new Mem(); 
						}
						try{
							memMap[memIndex+1].memArray = tempSeg[1];							
							memMap[memIndex].memArray = tempSeg[0];
						}catch(ArrayIndexOutOfBoundsException|NullPointerException|NumberFormatException ex)
						{
							System.out.println("ERROR : An error occured while trying to execute this instruction. Returning to main menu...");
							sc.next();
							continue;
						}		
					}
				}
				else if("stc".equalsIgnoreCase(instn))
				{
					CF=true;
				}
				else
				{
					System.out.println("ERROR : Invalid instruction entered! Returning to main menu....");
					sc.next();
				}
			}
			else if(ch==2)
			{
				Frame frame = new Frame("Flags and Registers - " + vmName);
   	            frame.setSize(700, 700);
				frame.setVisible( true );
				frame.setLayout(null);
				Label genHead = new Label();
				Label segHead = new Label();
				Label flagHead = new Label();
    	        Label axLabel = new Label();
    	        Label bxLabel = new Label();
    	        Label cxLabel = new Label();
    	        Label dxLabel = new Label();
    	        Label csLabel = new Label();
    	        Label dsLabel = new Label();
    	        Label esLabel = new Label();
    	        Label ssLabel = new Label();
    	        Label ofLabel = new Label();
    	        Label dfLabel = new Label();
    	        Label sfLabel = new Label();
    	        Label tfLabel = new Label();
    	        Label zfLabel = new Label();
    	        Label pfLabel = new Label();
    	        Label cfLabel = new Label();
    	        Label afLabel = new Label();
    	        Label ifLabel = new Label();
    	        genHead.setText("General Registers : ");
    	        segHead.setText("Segment Registers : ");
    	        flagHead.setText("Flags : ");
    	        axLabel.setText("AX: AH (MSB) = 0x" + Integer.toHexString(AH) + " AL (LSB) = 0x" + Integer.toHexString(AL));
    	        bxLabel.setText("BX: BH (MSB) = 0x" + Integer.toHexString(BH) + " BL (LSB) = 0x" + Integer.toHexString(BL));
    	        cxLabel.setText("CX: CH (MSB) = 0x" + Integer.toHexString(CH) + " CL (LSB) = 0x" + Integer.toHexString(CL));
    	        dxLabel.setText("DX: DH (MSB) = 0x" + Integer.toHexString(DH) + " DL (LSB) = 0x" + Integer.toHexString(DL));
    	        csLabel.setText("CS = 0x" + Integer.toHexString(CS));
    	        dsLabel.setText("DS = 0x" + Integer.toHexString(DS));
    	        esLabel.setText("ES = 0x" + Integer.toHexString(ES));
    	        ssLabel.setText("SS = 0x" + Integer.toHexString(SS));
    			ofLabel.setText("OF = " + (OF ? 1 : 0));
    			dfLabel.setText("DF = " + (DF ? 1 : 0));
    			sfLabel.setText("SF = " + (SF ? 1 : 0));
    			tfLabel.setText("TF = " + (TF ? 1 : 0));
    			zfLabel.setText("ZF = " + (ZF ? 1 : 0));
    			pfLabel.setText("PF = " + (PF ? 1 : 0));
    			cfLabel.setText("CF = " + (CF ? 1 : 0));
    			afLabel.setText("AF = " + (AF ? 1 : 0));
    			ifLabel.setText("IF = " + (IF ? 1 : 0));
    			genHead.setBounds(20, 30, 250, 20);
    			axLabel.setBounds(20, 50, 250, 20);
    			bxLabel.setBounds(20, 70, 250, 20);
    			cxLabel.setBounds(20, 90, 250, 20);
    			dxLabel.setBounds(20, 110, 250, 20);
    			segHead.setBounds(20, 130, 250, 20);
    			csLabel.setBounds(20, 150, 250, 20);
    			dsLabel.setBounds(20, 170, 250, 20);
    			esLabel.setBounds(20, 190, 250, 20);
    			ssLabel.setBounds(20, 210, 250, 20);
    			flagHead.setBounds(20, 230, 250, 20);
    			ofLabel.setBounds(20, 250, 250, 20);
    			dfLabel.setBounds(20, 270, 250, 20);
    			sfLabel.setBounds(20, 290, 250, 20);
    			tfLabel.setBounds(20, 310, 250, 20);
    			zfLabel.setBounds(20, 330, 250, 20);
    			pfLabel.setBounds(20, 350, 250, 20);
    			cfLabel.setBounds(20, 370, 250, 20);
    			afLabel.setBounds(20, 390, 250, 20);
    			ifLabel.setBounds(20, 410, 250, 20);
    			frame.add(genHead);
    			frame.add(axLabel);
    			frame.add(bxLabel);
    			frame.add(cxLabel);
    			frame.add(dxLabel);
    			frame.add(segHead);
    			frame.add(csLabel);
    			frame.add(dsLabel);
    			frame.add(esLabel);
    			frame.add(ssLabel);
    			frame.add(flagHead);
    			frame.add(ofLabel);
    			frame.add(dfLabel);
    			frame.add(sfLabel);
    			frame.add(tfLabel);
    			frame.add(zfLabel);
    			frame.add(pfLabel);
    			frame.add(cfLabel);
    			frame.add(afLabel);
    			frame.add(ifLabel);
    			frame.addWindowListener(new WindowAdapter() {
    				public void windowClosing(WindowEvent windowevent) {
    					System.exit(0);
    				}
    			});
    			sc.next();
    			frame.dispose();
				continue;
			}
			else if(ch==3)
			{
				while(true)
				{
					clearScreen();
					System.out.println("\t\t\t******Memory Map Operations********");
					System.out.println("\n\n1. Store a value at a memory location");
					System.out.println("2. Delete the value at a memory location");
					System.out.println("3. Replace the value at a memory location");
					System.out.println("4. View the value at a memory location");
					System.out.println("5. Go back to main menu");
					System.out.print("\n\nEnter your choice : ");
					ch = sc.nextInt();
					if(ch==1)
					{
						try{
							System.out.println("\n\nEnter a memory index to store the value at (Between 0 and 1023) : ");
							memIndex = sc.nextInt();
							memMap[memIndex] = new Mem();
							System.out.println("Enter the data to be stored in this location : ");
							memMap[memIndex].memArray = sc.nextByte();
						}catch(ArrayIndexOutOfBoundsException ex)
						{
							System.out.println("ERROR : Memory index is out of bounds! Enter again...");
							sc.next();
							continue;
						}
						catch(NullPointerException ex)
					    {
							System.out.println("ERROR : Cannot initialize the current memory location! Try again....");
							sc.next();
							continue;
						}
					}	
					else if(ch==2)
					{
						System.out.println("\n\nEnter the memory index to be cleared (Between 0 and 1023) : ");
						memIndex = sc.nextInt();
						memMap[memIndex] = null;
					}
					else if(ch==3)
					{
						try{
							System.out.println("\n\nEnter a memory index to replace the value at (Between 0 and 1023) : ");
							memIndex = sc.nextInt();
							System.out.println("Enter the new data to be stored in this location : ");
							memMap[memIndex].memArray = sc.nextByte();
						}catch(ArrayIndexOutOfBoundsException ex)
						{
							System.out.println("ERROR : Memory index is out of bounds! Enter again...");
							sc.next();
							continue;
						}
						catch(NullPointerException ex)
					    {
							System.out.println("ERROR : The memory location entered is empty! Use store operation instead....");
							sc.next();
							continue;
						}
					}
					else if(ch==4)
					{
						System.out.println("\n\nEnter the memory location whose value you want to display (Between 1 and 1023) : ");
						memIndex = sc.nextInt();
						try{
							System.out.println("Value at memory location " + memIndex + " is : " + memMap[memIndex]);
						}catch(ArrayIndexOutOfBoundsException ex)
						{
							System.out.println("ERROR : Memory index is out of bounds! Enter again...");
							sc.next();
							continue;
						}
						catch(NullPointerException ex)
					    {
							System.out.println("ERROR : The memory location entered is empty! Enter again....");
							sc.next();
							continue;
						}
					}
					else if(ch==5)
					{
						sc.next();
						break;
					}
					else
					{
						System.out.println("ERROR : Invalid choice entered! Enter again...");
						sc.next();
						continue;
					}
				}	
			}
			else if(ch==4)
			{
				sc.next();
				sc.close();
				System.exit(0);
			}
			else
			{
				System.out.println("ERROR : Invalid choice entered! Enter again...");
				sc.next();
				continue;
			}
			firstWindow.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent windowevent) {
					System.exit(0);
				}
			});
		}
	            }
        }); 
	}
}
