import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;


public class SimConsole {

	static byte AL, AH, BL, BH, CL, CH, DL, DH;
	static int memIndex;
	static int stackPointer;
	static short CS, DS, ES, SS;
	static String tempString;
	static boolean OF, DF, SF, TF, ZF, CF, PF, AF, IF;
	static Mem[] memMap;
	static byte tempByte;
	static byte[] tempSeg;
	static byte[] stack;
	private static final int PAGE_SIZE = 25;

	private static void clearScreen() {
		for (int i = 0; i < PAGE_SIZE; i++) {
			System.out.println();
		}
	}
	SimConsole() {
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

	public static void runConsole(String vmName) {


		Scanner sc = new Scanner(System. in );
		int ch;
		StringTokenizer instnComplete;
		String instn;



		new SimConsole();
		while (true) {
			clearScreen();
			System.out.println("\t\t\t*********8086 Simulator v0.6b*************\n\n\n\n");
			System.out.println("1. Execute an instruction\n2. Display All Registers and Flags\n3. Alter memory map\n4. Exit\n\n");
			System.out.print("Enter your choice : ");
			ch = sc.nextInt();
			if (ch == 1) {
				clearScreen();
				System.out.println("Choose from the following instructions : \nMOV XCHG LEA LDS LES ADD ADC SUB SBB INC DEC NEG CMP MUL NOT AND OR XOR TEST SHL/SAL SAR SHR ROL ROR RCL RCR IN OUT PUSH POP STC CLC CMC STD CLD STI CLI\n\n");
				System.out.println("Enter the instruction : ");
				sc.nextLine();
				instnComplete = new StringTokenizer(sc.nextLine());
				instn = instnComplete.nextToken();
				if ("mov".equalsIgnoreCase(instn)) {
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if (ch == 1) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = memMap[memIndex].memArray;
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = memMap[memIndex].memArray;
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = memMap[memIndex].memArray;
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = memMap[memIndex].memArray;
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = memMap[memIndex].memArray;
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = memMap[memIndex].memArray;
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = memMap[memIndex].memArray;
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = memMap[memIndex].memArray;
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = memMap[memIndex].memArray;
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = memMap[memIndex].memArray;
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = memMap[memIndex].memArray;
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 2) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register\n3. Memory Location");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = tempByte;
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = tempByte;
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = tempByte;
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = tempByte;
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = tempByte;
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = tempByte;
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = tempByte;
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = tempByte;
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = tempByte;
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = tempByte;
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = tempByte;
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 3) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination location : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Destination memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							try {
								memMap[memIndex] = new Mem();
								memMap[memIndex].memArray = tempByte;
							} catch (NullPointerException ex) {
								System.out.println("ERROR : Unable to perform this operation! Returning to main menu...");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 3) {
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register\n2. Memory Location");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								tempByte = AL;
							} else if ("AH".equalsIgnoreCase(tempString)) {
								tempByte = AH;
							} else if ("BL".equalsIgnoreCase(tempString)) {
								tempByte = BL;
							} else if ("BH".equalsIgnoreCase(tempString)) {
								tempByte = BH;
							} else if ("CL".equalsIgnoreCase(tempString)) {
								tempByte = CL;
							} else if ("CH".equalsIgnoreCase(tempString)) {
								tempByte = CH;
							} else if ("DL".equalsIgnoreCase(tempString)) {
								tempByte = DL;
							} else if ("DH".equalsIgnoreCase(tempString)) {
								tempByte = DH;
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = tempByte;
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = tempByte;
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = tempByte;
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								tempByte = AL;
							} else if ("AH".equalsIgnoreCase(tempString)) {
								tempByte = AH;
							} else if ("BL".equalsIgnoreCase(tempString)) {
								tempByte = BL;
							} else if ("BH".equalsIgnoreCase(tempString)) {
								tempByte = BH;
							} else if ("CL".equalsIgnoreCase(tempString)) {
								tempByte = CL;
							} else if ("CH".equalsIgnoreCase(tempString)) {
								tempByte = CH;
							} else if ("DL".equalsIgnoreCase(tempString)) {
								tempByte = DL;
							} else if ("DH".equalsIgnoreCase(tempString)) {
								tempByte = DH;
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination location : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Destination memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							try {
								memMap[memIndex] = new Mem();
								memMap[memIndex].memArray = tempByte;
							} catch (NullPointerException ex) {
								System.out.println("ERROR : Unable to perform this operation! Returning to main menu...");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 4) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Memory Location");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("CS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							} else if ("ES".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if ("AX".equalsIgnoreCase(tempString)) {
								AL = tempSeg[0];
								AH = tempSeg[1];
							} else if ("BX".equalsIgnoreCase(tempString)) {
								BL = tempSeg[0];
								BH = tempSeg[1];
							} else if ("CX".equalsIgnoreCase(tempString)) {
								CL = tempSeg[0];
								CH = tempSeg[1];
							} else if ("DX".equalsIgnoreCase(tempString)) {
								DL = tempSeg[0];
								DH = tempSeg[1];
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("CS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							} else if ("ES".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination location : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1022) {
								System.out.println("ERROR : Destination memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							try {
								memMap[memIndex] = new Mem();
								memMap[memIndex + 1] = new Mem();
								memMap[memIndex].memArray = tempSeg[0];
								memMap[memIndex + 1].memArray = tempSeg[1];
							} catch (NullPointerException ex) {
								System.out.println("ERROR : Unable to perform this operation! Returning to main menu...");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else {
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				} else if ("xchg".equalsIgnoreCase(instn)) {
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. General Register\n3. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if (ch == 1) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								tempByte = AL;
								AL = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							} else if ("AH".equalsIgnoreCase(tempString)) {
								tempByte = AH;
								AH = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							} else if ("BL".equalsIgnoreCase(tempString)) {
								tempByte = BL;
								BL = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							} else if ("BH".equalsIgnoreCase(tempString)) {
								tempByte = BH;
								BH = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							} else if ("CL".equalsIgnoreCase(tempString)) {
								tempByte = CL;
								CL = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							} else if ("CH".equalsIgnoreCase(tempString)) {
								tempByte = CH;
								CL = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							} else if ("DL".equalsIgnoreCase(tempString)) {
								tempByte = DL;
								DL = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							} else if ("DH".equalsIgnoreCase(tempString)) {
								tempByte = DH;
								DH = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempByte;
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
								ES = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempSeg[0];
								memMap[memIndex + 1].memArray = tempSeg[1];
							} else if ("DS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
								DS = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempSeg[0];
								memMap[memIndex + 1].memArray = tempSeg[1];
							} else if ("SS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
								SS = memMap[memIndex].memArray;
								memMap[memIndex].memArray = tempSeg[0];
								memMap[memIndex + 1].memArray = tempSeg[1];
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 2) {
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if ("AX".equalsIgnoreCase(tempString)) {
								tempSeg[0] = AL;
								tempSeg[1] = AH;
								System.out.print("Enter destination register : ");
								tempString = sc.next();
								if ("ES".equalsIgnoreCase(tempString)) {
									AL = (byte)(ES & 0xff);
									AH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("DS".equalsIgnoreCase(tempString)) {
									AL = (byte)(DS & 0xff);
									AH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("SS".equalsIgnoreCase(tempString)) {
									AL = (byte)(SS & 0xff);
									AH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else {
									System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							} else if ("BX".equalsIgnoreCase(tempString)) {
								tempSeg[0] = BL;
								tempSeg[1] = BH;
								System.out.print("Enter destination register : ");
								tempString = sc.next();
								if ("ES".equalsIgnoreCase(tempString)) {
									BL = (byte)(ES & 0xff);
									BH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("DS".equalsIgnoreCase(tempString)) {
									BL = (byte)(DS & 0xff);
									BH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("SS".equalsIgnoreCase(tempString)) {
									BL = (byte)(SS & 0xff);
									BH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else {
									System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							} else if ("CX".equalsIgnoreCase(tempString)) {
								tempSeg[0] = CL;
								tempSeg[1] = CH;
								System.out.print("Enter destination register : ");
								tempString = sc.next();
								if ("ES".equalsIgnoreCase(tempString)) {
									CL = (byte)(ES & 0xff);
									CH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("DS".equalsIgnoreCase(tempString)) {
									CL = (byte)(DS & 0xff);
									CH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("SS".equalsIgnoreCase(tempString)) {
									CL = (byte)(SS & 0xff);
									CH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else {
									System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							} else if ("DX".equalsIgnoreCase(tempString)) {
								tempSeg[0] = DL;
								tempSeg[1] = DH;
								System.out.print("Enter destination register : ");
								tempString = sc.next();
								if ("ES".equalsIgnoreCase(tempString)) {
									DL = (byte)(ES & 0xff);
									DH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("DS".equalsIgnoreCase(tempString)) {
									DL = (byte)(DS & 0xff);
									DH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("SS".equalsIgnoreCase(tempString)) {
									DL = (byte)(SS & 0xff);
									DH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else {
									System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 3) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("CS".equalsIgnoreCase(tempString)) {
								System.out.print("Enter destination register (AX,BX,CX,DX) : ");
								tempString = sc.next();
								if ("AX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = AL;
									tempSeg[1] = AH;
									AL = (byte)(CS & 0xff);
									AH = (byte)((CS >> 8) & 0xff);
									CS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("BX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = BL;
									tempSeg[1] = BH;
									BL = (byte)(CS & 0xff);
									BH = (byte)((CS >> 8) & 0xff);
									CS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("CX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = CL;
									tempSeg[1] = CH;
									CL = (byte)(CS & 0xff);
									CH = (byte)((CS >> 8) & 0xff);
									CS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("DX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = DL;
									tempSeg[1] = DH;
									DL = (byte)(CS & 0xff);
									DH = (byte)((CS >> 8) & 0xff);
									CS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else {
									System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							} else if ("DS".equalsIgnoreCase(tempString)) {
								System.out.print("Enter destination register (AX,BX,CX,DX) : ");
								tempString = sc.next();
								if ("AX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = AL;
									tempSeg[1] = AH;
									AL = (byte)(DS & 0xff);
									AH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("BX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = BL;
									tempSeg[1] = BH;
									BL = (byte)(DS & 0xff);
									BH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("CX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = CL;
									tempSeg[1] = CH;
									CL = (byte)(DS & 0xff);
									CH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("DX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = DL;
									tempSeg[1] = DH;
									DL = (byte)(DS & 0xff);
									DH = (byte)((DS >> 8) & 0xff);
									DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else {
									System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							} else if ("ES".equalsIgnoreCase(tempString)) {
								System.out.print("Enter destination register (AX,BX,CX,DX) : ");
								tempString = sc.next();
								if ("AX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = AL;
									tempSeg[1] = AH;
									AL = (byte)(ES & 0xff);
									AH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("BX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = BL;
									tempSeg[1] = BH;
									BL = (byte)(ES & 0xff);
									BH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("CX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = CL;
									tempSeg[1] = CH;
									CL = (byte)(ES & 0xff);
									CH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("DX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = DL;
									tempSeg[1] = DH;
									DL = (byte)(ES & 0xff);
									DH = (byte)((ES >> 8) & 0xff);
									ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else {
									System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							} else if ("SS".equalsIgnoreCase(tempString)) {
								System.out.print("Enter destination register (AX,BX,CX,DX) : ");
								tempString = sc.next();
								if ("AX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = AL;
									tempSeg[1] = AH;
									AL = (byte)(SS & 0xff);
									AH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("BX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = BL;
									tempSeg[1] = BH;
									BL = (byte)(SS & 0xff);
									BH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("CX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = CL;
									tempSeg[1] = CH;
									CL = (byte)(SS & 0xff);
									CH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else if ("DX".equalsIgnoreCase(tempString)) {
									tempSeg[0] = DL;
									tempSeg[1] = DH;
									DL = (byte)(SS & 0xff);
									DH = (byte)((SS >> 8) & 0xff);
									SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
								} else {
									System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
									sc.next();
									continue;
								}
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else {
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				} else if ("lea".equalsIgnoreCase(instn)) {

				} else if ("lds".equalsIgnoreCase(instn)) {

				} else if ("les".equalsIgnoreCase(instn)) {

				} else if ("adc".equalsIgnoreCase(instn)) {
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if (ch == 1) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL ^ memMap[memIndex].memArray);
								if (CF) {
									AL++;
								}
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH ^ memMap[memIndex].memArray);
								if (CF) {
									AH++;
								}
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL ^ memMap[memIndex].memArray);
								if (CF) {
									BL++;
								}
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH ^ memMap[memIndex].memArray);
								if (CF) {
									BH++;
								}
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL ^ memMap[memIndex].memArray);
								if (CF) {
									CL++;
								}
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH ^ memMap[memIndex].memArray);
								if (CF) {
									CH++;
								}
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL ^ memMap[memIndex].memArray);
								if (CF) {
									DL++;
								}
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH ^ memMap[memIndex].memArray);
								if (CF) {
									DH++;
								}
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES ^ memMap[memIndex].memArray);
								if (CF) {
									ES++;
								}
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS ^ memMap[memIndex].memArray);
								if (CF) {
									DS++;
								}
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS ^ memMap[memIndex].memArray);
								if (CF) {
									SS++;
								}
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 2) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL ^ tempByte);
								if (CF) {
									AL++;
								}
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH ^ tempByte);
								if (CF) {
									AH++;
								}
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL ^ tempByte);
								if (CF) {
									BL++;
								}
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH ^ tempByte);
								if (CF) {
									BH++;
								}
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL ^ tempByte);
								if (CF) {
									CL++;
								}
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH ^ tempByte);
								if (CF) {
									CH++;
								}
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL ^ tempByte);
								if (CF) {
									DL++;
								}
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH ^ tempByte);
								if (CF) {
									DH++;
								}
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES ^ tempByte);
								if (CF) {
									ES++;
								}
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS ^ tempByte);
								if (CF) {
									DS++;
								}
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS ^ tempByte);
								if (CF) {
									SS++;
								}
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 3) {
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								tempByte = AL;
							} else if ("AH".equalsIgnoreCase(tempString)) {
								tempByte = AH;
							} else if ("BL".equalsIgnoreCase(tempString)) {
								tempByte = BL;
							} else if ("BH".equalsIgnoreCase(tempString)) {
								tempByte = BH;
							} else if ("CL".equalsIgnoreCase(tempString)) {
								tempByte = CL;
							} else if ("CH".equalsIgnoreCase(tempString)) {
								tempByte = CH;
							} else if ("DL".equalsIgnoreCase(tempString)) {
								tempByte = DL;
							} else if ("DH".equalsIgnoreCase(tempString)) {
								tempByte = DH;
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES ^ tempByte);
								if (CF) {
									ES++;
								}
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS ^ tempByte);
								if (CF) {
									DS++;
								}
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS ^ tempByte);
								if (CF) {
									SS++;
								}
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 4) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("CS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							} else if ("ES".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if ("AX".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL ^ tempSeg[0]);
								AH = (byte)(AH ^ tempSeg[1]);
								if (CF) {
									AL++;
								}
							} else if ("BX".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL ^ tempSeg[0]);
								BH = (byte)(BH ^ tempSeg[1]);
								if (CF) {
									BL++;
								}
							} else if ("CX".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL ^ tempSeg[0]);
								CH = (byte)(CH ^ tempSeg[1]);
								if (CF) {
									CL++;
								}
							} else if ("DX".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL ^ tempSeg[0]);
								DH = (byte)(DH ^ tempSeg[1]);
								if (CF) {
									DL++;
								}
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else {
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				} else if ("add".equalsIgnoreCase(instn) || "xor".equalsIgnoreCase(instn)) {
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if (ch == 1) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL ^ memMap[memIndex].memArray);
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH ^ memMap[memIndex].memArray);
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL ^ memMap[memIndex].memArray);
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH ^ memMap[memIndex].memArray);
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL ^ memMap[memIndex].memArray);
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH ^ memMap[memIndex].memArray);
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL ^ memMap[memIndex].memArray);
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH ^ memMap[memIndex].memArray);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES ^ memMap[memIndex].memArray);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS ^ memMap[memIndex].memArray);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS ^ memMap[memIndex].memArray);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 2) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL ^ tempByte);
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH ^ tempByte);
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL ^ tempByte);
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH ^ tempByte);
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL ^ tempByte);
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH ^ tempByte);
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL ^ tempByte);
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH ^ tempByte);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES ^ tempByte);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS ^ tempByte);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS ^ tempByte);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 3) {
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								tempByte = AL;
							} else if ("AH".equalsIgnoreCase(tempString)) {
								tempByte = AH;
							} else if ("BL".equalsIgnoreCase(tempString)) {
								tempByte = BL;
							} else if ("BH".equalsIgnoreCase(tempString)) {
								tempByte = BH;
							} else if ("CL".equalsIgnoreCase(tempString)) {
								tempByte = CL;
							} else if ("CH".equalsIgnoreCase(tempString)) {
								tempByte = CH;
							} else if ("DL".equalsIgnoreCase(tempString)) {
								tempByte = DL;
							} else if ("DH".equalsIgnoreCase(tempString)) {
								tempByte = DH;
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES ^ tempByte);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS ^ tempByte);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS ^ tempByte);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 4) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("CS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							} else if ("ES".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if ("AX".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL ^ tempSeg[0]);
								AH = (byte)(AH ^ tempSeg[1]);
							} else if ("BX".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL ^ tempSeg[0]);
								BH = (byte)(BH ^ tempSeg[1]);
							} else if ("CX".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL ^ tempSeg[0]);
								CH = (byte)(CH ^ tempSeg[1]);
							} else if ("DX".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL ^ tempSeg[0]);
								DH = (byte)(DH ^ tempSeg[1]);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else {
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				} else if ("sub".equalsIgnoreCase(instn)) {
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if (ch == 1) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL - memMap[memIndex].memArray);
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH - memMap[memIndex].memArray);
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL - memMap[memIndex].memArray);
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH - memMap[memIndex].memArray);
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL - memMap[memIndex].memArray);
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH - memMap[memIndex].memArray);
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL - memMap[memIndex].memArray);
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH - memMap[memIndex].memArray);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES - memMap[memIndex].memArray);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS - memMap[memIndex].memArray);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS - memMap[memIndex].memArray);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 2) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL - tempByte);
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH - tempByte);
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL - tempByte);
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH - tempByte);
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL - tempByte);
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH - tempByte);
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL - tempByte);
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH - tempByte);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES - tempByte);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS - tempByte);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS - tempByte);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 3) {
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								tempByte = AL;
							} else if ("AH".equalsIgnoreCase(tempString)) {
								tempByte = AH;
							} else if ("BL".equalsIgnoreCase(tempString)) {
								tempByte = BL;
							} else if ("BH".equalsIgnoreCase(tempString)) {
								tempByte = BH;
							} else if ("CL".equalsIgnoreCase(tempString)) {
								tempByte = CL;
							} else if ("CH".equalsIgnoreCase(tempString)) {
								tempByte = CH;
							} else if ("DL".equalsIgnoreCase(tempString)) {
								tempByte = DL;
							} else if ("DH".equalsIgnoreCase(tempString)) {
								tempByte = DH;
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES - tempByte);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS - tempByte);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS - tempByte);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 4) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("CS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							} else if ("ES".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if ("AX".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL - tempSeg[0]);
								AH = (byte)(AH - tempSeg[1]);
							} else if ("BX".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL - tempSeg[0]);
								BH = (byte)(BH - tempSeg[1]);
							} else if ("CX".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL - tempSeg[0]);
								CH = (byte)(CH - tempSeg[1]);
							} else if ("DX".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL - tempSeg[0]);
								DH = (byte)(DH - tempSeg[1]);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else {
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;

				}} else if ("sbb".equalsIgnoreCase(instn)) {
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if (ch == 1) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL - memMap[memIndex].memArray - ( CF ? 1 : 0 ));
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH - memMap[memIndex].memArray - ( CF ? 1 : 0 ));
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL - memMap[memIndex].memArray - ( CF ? 1 : 0 ));
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH - memMap[memIndex].memArray - ( CF ? 1 : 0 ));
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL - memMap[memIndex].memArray - ( CF ? 1 : 0 ));
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH - memMap[memIndex].memArray - ( CF ? 1 : 0 ));
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL - memMap[memIndex].memArray - ( CF ? 1 : 0 ));
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH - memMap[memIndex].memArray - ( CF ? 1 : 0 ));
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES - memMap[memIndex].memArray - ( CF ? 1 : 0 ));
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS - memMap[memIndex].memArray - ( CF ? 1 : 0 ));
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS - memMap[memIndex].memArray - ( CF ? 1 : 0 ));
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 2) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL - tempByte - ( CF ? 1 : 0 ));
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH - tempByte - ( CF ? 1 : 0 ));
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL - tempByte - ( CF ? 1 : 0 ));
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH - tempByte - ( CF ? 1 : 0 ));
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL - tempByte - ( CF ? 1 : 0 ));
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH - tempByte - ( CF ? 1 : 0 ));
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL - tempByte - ( CF ? 1 : 0 ));
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH - tempByte - ( CF ? 1 : 0 ));
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES - tempByte - ( CF ? 1 : 0 ));
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS - tempByte - ( CF ? 1 : 0 ));
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS - tempByte - ( CF ? 1 : 0 ));
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 3) {
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								tempByte = AL;
							} else if ("AH".equalsIgnoreCase(tempString)) {
								tempByte = AH;
							} else if ("BL".equalsIgnoreCase(tempString)) {
								tempByte = BL;
							} else if ("BH".equalsIgnoreCase(tempString)) {
								tempByte = BH;
							} else if ("CL".equalsIgnoreCase(tempString)) {
								tempByte = CL;
							} else if ("CH".equalsIgnoreCase(tempString)) {
								tempByte = CH;
							} else if ("DL".equalsIgnoreCase(tempString)) {
								tempByte = DL;
							} else if ("DH".equalsIgnoreCase(tempString)) {
								tempByte = DH;
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES - tempByte - ( CF ? 1 : 0 ));
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS - tempByte - ( CF ? 1 : 0 ));
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS - tempByte - ( CF ? 1 : 0 ));
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 4) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("CS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							} else if ("ES".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if ("AX".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL - tempSeg[0] - ( CF ? 1 : 0 ));
								AH = (byte)(AH - tempSeg[1]);
							} else if ("BX".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL - tempSeg[0] - ( CF ? 1 : 0 ));
								BH = (byte)(BH - tempSeg[1]);
							} else if ("CX".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL - tempSeg[0] - ( CF ? 1 : 0 ));
								CH = (byte)(CH - tempSeg[1]);
							} else if ("DX".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL - tempSeg[0] - ( CF ? 1 : 0 ));
								DH = (byte)(DH - tempSeg[1]);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else {
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				} else if ("inc".equalsIgnoreCase(instn)) {
					String destn;
					try {
						destn = instnComplete.nextToken();
					} catch (NoSuchElementException ex) {
						System.out.println("ERROR : Invalid instruction syntax! Returning to main menu....");
						sc.next();
						continue;
					}
					if ("AL".equalsIgnoreCase(destn) || "AX".equalsIgnoreCase(destn)) {
						AL++;
					} else if ("BL".equalsIgnoreCase(destn) || "BX".equalsIgnoreCase(destn)) {
						BL++;
					} else if ("CL".equalsIgnoreCase(destn) || "CX".equalsIgnoreCase(destn)) {
						CL++;
					} else if ("DL".equalsIgnoreCase(destn) || "DX".equalsIgnoreCase(destn)) {
						DL++;
					} else if ("AH".equalsIgnoreCase(destn)) {
						AH++;
					} else if ("BH".equalsIgnoreCase(destn)) {
						BH++;
					} else if ("CH".equalsIgnoreCase(destn)) {
						CH++;
					} else if ("DH".equalsIgnoreCase(destn)) {
						DH++;
					} else if ("CS".equalsIgnoreCase(destn)) {
						CS++;
					} else if ("ES".equalsIgnoreCase(destn)) {
						ES++;
					} else if ("DS".equalsIgnoreCase(destn)) {
						DS++;
					} else if ("SS".equalsIgnoreCase(destn)) {
						SS++;
					} else {
						try {
							memIndex = Integer.parseInt(destn);
							memMap[memIndex].memArray++;
						} catch (ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException ex) {
							System.out.println("ERROR : An error occured while trying to execute this instruction. Returning to main menu...");
							sc.next();
							continue;
						}
					}
				} else if ("dec".equalsIgnoreCase(instn)) {
					String destn;
					try {
						destn = instnComplete.nextToken();
					} catch (NoSuchElementException ex) {
						System.out.println("ERROR : Invalid instruction syntax! Returning to main menu....");
						sc.next();
						continue;
					}
					if ("AL".equalsIgnoreCase(destn) || "AX".equalsIgnoreCase(destn)) {
						AL--;
					} else if ("BL".equalsIgnoreCase(destn) || "BX".equalsIgnoreCase(destn)) {
						BL--;
					} else if ("CL".equalsIgnoreCase(destn) || "CX".equalsIgnoreCase(destn)) {
						CL--;
					} else if ("DL".equalsIgnoreCase(destn) || "DX".equalsIgnoreCase(destn)) {
						DL--;
					} else if ("AH".equalsIgnoreCase(destn)) {
						AH--;
					} else if ("BH".equalsIgnoreCase(destn)) {
						BH--;
					} else if ("CH".equalsIgnoreCase(destn)) {
						CH--;
					} else if ("DH".equalsIgnoreCase(destn)) {
						DH--;
					} else if ("CS".equalsIgnoreCase(destn)) {
						CS--;
					} else if ("ES".equalsIgnoreCase(destn)) {
						ES--;
					} else if ("DS".equalsIgnoreCase(destn)) {
						DS--;
					} else if ("SS".equalsIgnoreCase(destn)) {
						SS--;
					} else {
						try {
							memIndex = Integer.parseInt(destn);
							memMap[memIndex].memArray--;
						} catch (ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException ex) {
							System.out.println("ERROR : An error occured while trying to execute this instruction. Returning to main menu...");
							sc.next();
							continue;
						}
					}
				} else if ("mul".equalsIgnoreCase(instn)) {
					String destn;
					try {
						destn = instnComplete.nextToken();
					} catch (NoSuchElementException ex) {
						System.out.println("ERROR : Invalid instruction syntax! Returning to main menu....");
						sc.next();
						continue;
					}
					if ("AL".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*AL) & 0xff);
						tempSeg[1] = (byte)(((AL*AL) >> 8) & 0xff);
						AL=(byte) Math.abs(tempSeg[0]);
						AH=(byte) Math.abs(tempSeg[1]);
					} else if("AX".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*AL) & 0xff);
						tempSeg[1] = (byte)(((AL*AL) >> 8) & 0xff);
						AL=(byte)Math.abs(tempSeg[0]);
						AH=(byte)Math.abs(tempSeg[1]);
						tempSeg[0] = (byte)((AH*AH) & 0xff);
						tempSeg[1] = (byte)(((AH*AH) >> 8) & 0xff);
						DL=(byte)Math.abs(tempSeg[0]);
						DH=(byte)Math.abs(tempSeg[1]);
					}
					else if ("BL".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*BL) & 0xff);
						tempSeg[1] = (byte)(((AL*BL) >> 8) & 0xff);
						AL=(byte)Math.abs(tempSeg[0]);
						AH=(byte)Math.abs(tempSeg[1]);
					} else if ("BX".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*BL) & 0xff);
						tempSeg[1] = (byte)(((AL*BL) >> 8) & 0xff);
						AL=(byte)Math.abs(tempSeg[0]);
						AH=(byte)Math.abs(tempSeg[1]);
						tempSeg[0] = (byte)((AH*BH) & 0xff);
						tempSeg[1] = (byte)(((AH*BH) >> 8) & 0xff);
						DL=(byte)Math.abs(tempSeg[0]);
						DH=(byte)Math.abs(tempSeg[1]);
					} else if ("CL".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*CL) & 0xff);
						tempSeg[1] = (byte)(((AL*CL) >> 8) & 0xff);
						AL=(byte)Math.abs(tempSeg[0]);
						AH=(byte)Math.abs(tempSeg[1]);
					} else if("CX".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*CL) & 0xff);
						tempSeg[1] = (byte)(((AL*CL) >> 8) & 0xff);
						AL=(byte)Math.abs(tempSeg[0]);
						AH=(byte)Math.abs(tempSeg[1]);
						tempSeg[0] = (byte)((AH*CH) & 0xff);
						tempSeg[1] = (byte)(((AH*CH) >> 8) & 0xff);
						DL=(byte)Math.abs(tempSeg[0]);
						DH=(byte)Math.abs(tempSeg[1]);
					} else if ("DL".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*DL) & 0xff);
						tempSeg[1] = (byte)(((AL*DL) >> 8) & 0xff);
						AL=(byte)Math.abs(tempSeg[0]);
						AH=(byte)Math.abs(tempSeg[1]);
					} else if ("DX".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*DL) & 0xff);
						tempSeg[1] = (byte)(((AL*DL) >> 8) & 0xff);
						AL=(byte)Math.abs(tempSeg[0]);
						AH=(byte)Math.abs(tempSeg[1]);
						tempSeg[0] = (byte)((AH*DH) & 0xff);
						tempSeg[1] = (byte)(((AH*DH) >> 8) & 0xff);
						DL=(byte)Math.abs(tempSeg[0]);
						DH=(byte)Math.abs(tempSeg[1]);
					} else if ("AH".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*AH) & 0xff);
						tempSeg[1] = (byte)(((AL*AH) >> 8) & 0xff);
						AL=(byte)Math.abs(tempSeg[0]);
						AH=(byte)Math.abs(tempSeg[1]);
					} else if ("BH".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*BH) & 0xff);
						tempSeg[1] = (byte)(((AL*BH) >> 8) & 0xff);
						AL=(byte)Math.abs(tempSeg[0]);
						AH=(byte)Math.abs(tempSeg[1]);
					} else if ("CH".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*CH) & 0xff);
						tempSeg[1] = (byte)(((AL*CH) >> 8) & 0xff);
						AL=(byte)Math.abs(tempSeg[0]);
						AH=(byte)Math.abs(tempSeg[1]);
					} else if ("DH".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)((AL*DH) & 0xff);
						tempSeg[1] = (byte)(((AL*DH) >> 8) & 0xff);
						AL=(byte)Math.abs(tempSeg[0]);
						AH=(byte)Math.abs(tempSeg[1]);
					} else if ("CS".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)(CS & 0xff);
						tempSeg[1] = (byte)((CS >> 8) & 0xff);
						AL=(byte)Math.abs(((AL*tempSeg[0]) & 0xff));
						AH=(byte)Math.abs((((AL*tempSeg[0]) >> 8) & 0xff));
						DL=(byte)Math.abs(((AH*tempSeg[1]) & 0xff));
						DH=(byte)Math.abs((((AH*tempSeg[1]) >> 8) & 0xff));
					} else if ("ES".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)(ES & 0xff);
						tempSeg[1] = (byte)((ES >> 8) & 0xff);
						AL=(byte)Math.abs(((AL*tempSeg[0]) & 0xff));
						AH=(byte)Math.abs((((AL*tempSeg[0]) >> 8) & 0xff));
						DL=(byte)Math.abs(((AH*tempSeg[1]) & 0xff));
						DH=(byte)Math.abs((((AH*tempSeg[1]) >> 8) & 0xff));
					} else if ("DS".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)(DS & 0xff);
						tempSeg[1] = (byte)((DS >> 8) & 0xff);
						AL=(byte)Math.abs(((AL*tempSeg[0]) & 0xff));
						AH=(byte)Math.abs((((AL*tempSeg[0]) >> 8) & 0xff));
						DL=(byte)Math.abs(((AH*tempSeg[1]) & 0xff));
						DH=(byte)Math.abs((((AH*tempSeg[1]) >> 8) & 0xff));
					} else if ("SS".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)(SS & 0xff);
						tempSeg[1] = (byte)((SS >> 8) & 0xff);
						AL=(byte)Math.abs(((AL*tempSeg[0]) & 0xff));
						AH=(byte)Math.abs((((AL*tempSeg[0]) >> 8) & 0xff));
						DL=(byte)Math.abs(((AH*tempSeg[1]) & 0xff));
						DH=(byte)Math.abs((((AH*tempSeg[1]) >> 8) & 0xff));
					} else {
						try {
							memIndex = Integer.parseInt(destn);
							tempSeg[0] = (byte)Math.abs(((AL*memMap[memIndex].memArray) & 0xff));
							tempSeg[1] = (byte)Math.abs((((AL*memMap[memIndex].memArray) >> 8) & 0xff));
							AL=tempSeg[0];
							AH=tempSeg[1];
						} catch (ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException ex) {
							System.out.println("ERROR : An error occured while trying to execute this instruction. Returning to main menu...");
							sc.next();
							continue;
						}
				}

				} else if ("cmp".equalsIgnoreCase(instn)) {

				} else if ("neg".equalsIgnoreCase(instn)) {
					String destn;
					try {
						destn = instnComplete.nextToken();
					} catch (NoSuchElementException ex) {
						System.out.println("ERROR : Invalid instruction syntax! Returning to main menu....");
						sc.next();
						continue;
					}
					if ("AL".equalsIgnoreCase(destn)) {
						AL*=-1;
					}else if("AX".equalsIgnoreCase(destn)){
						AL*=-1;
						AH*=-1;
					} else if ("BL".equalsIgnoreCase(destn)) {
						BL*=-1;
					}else if("BX".equalsIgnoreCase(destn)){
						BL*=-1;
						BH*=-1;
					} else if ("CL".equalsIgnoreCase(destn)) {
						CL*=-1;
					}else if("CX".equalsIgnoreCase(destn)){
						CL*=-1;
						CH*=-1;
					} else if ("DL".equalsIgnoreCase(destn)) {
						DL*=-1;
					}else if("DX".equalsIgnoreCase(destn)){
						DL*=-1;
						DH*=-1;
					} else if ("AH".equalsIgnoreCase(destn)) {
						AH*=-1;
					} else if ("BH".equalsIgnoreCase(destn)) {
						BH*=-1;
					} else if ("CH".equalsIgnoreCase(destn)) {
						CH*=-1;
					} else if ("DH".equalsIgnoreCase(destn)) {
						DH*=-1;
					} else if ("CS".equalsIgnoreCase(destn)) {
						CS*=-1;
					} else if ("ES".equalsIgnoreCase(destn)) {
						ES*=-1;
					} else if ("DS".equalsIgnoreCase(destn)) {
						DS*=-1;
					} else if ("SS".equalsIgnoreCase(destn)) {
						SS*=-1;
					} else {
						try {
							memIndex = Integer.parseInt(destn);
							memMap[memIndex].memArray*=-1;
						} catch (ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException ex) {
							System.out.println("ERROR : An error occured while trying to execute this instruction. Returning to main menu...");
							sc.next();
							continue;
						}
					}
				} else if ("not".equalsIgnoreCase(instn)) {
					String destn;
					try {
						destn = instnComplete.nextToken();
					} catch (NoSuchElementException ex) {
						System.out.println("ERROR : Invalid instruction syntax! Returning to main menu....");
						sc.next();
						continue;
					}
					if ("AL".equalsIgnoreCase(destn)) {
						AL=(byte) ~AL;
					} else if("AX".equalsIgnoreCase(destn)){
						AL=(byte) ~AL;
						AH=(byte) ~AH;
					}else if ("BL".equalsIgnoreCase(destn)) {
						BL=(byte) ~BL;
					}else if("BX".equalsIgnoreCase(destn)){
						BL=(byte) ~BL;
						BH=(byte) ~BH;
					} else if ("CL".equalsIgnoreCase(destn)) {
						CL=(byte) ~CL;
					}else if("CX".equalsIgnoreCase(destn)){
						CL=(byte) ~CL;
						CH=(byte) ~CH;
					} else if ("DL".equalsIgnoreCase(destn)) {
						DL=(byte) ~DL;
					}else if("DX".equalsIgnoreCase(destn)){
						DL=(byte) ~DL;
						DH=(byte) ~DH;
					} else if ("AH".equalsIgnoreCase(destn)) {
						AH=(byte) ~AH;
					} else if ("BH".equalsIgnoreCase(destn)) {
						BH=(byte) ~BH;
					} else if ("CH".equalsIgnoreCase(destn)) {
						CH=(byte) ~CH;
					} else if ("DH".equalsIgnoreCase(destn)) {
						DH=(byte) ~DH;
					} else if ("CS".equalsIgnoreCase(destn)) {
						CS=(short) ~CS;
					} else if ("ES".equalsIgnoreCase(destn)) {
						ES=(short) ~ES;
					} else if ("DS".equalsIgnoreCase(destn)) {
						DS=(short) ~DS;
					} else if ("SS".equalsIgnoreCase(destn)) {
						SS=(short) ~SS;
					} else {
						try {
							memIndex = Integer.parseInt(destn);
							memMap[memIndex].memArray=(byte) ~(memMap[memIndex].memArray);
						} catch (ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException ex) {
							System.out.println("ERROR : An error occured while trying to execute this instruction. Returning to main menu...");
							sc.next();
							continue;
						}
				}} else if ("and".equalsIgnoreCase(instn)) {
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if (ch == 1) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL & memMap[memIndex].memArray);
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH & memMap[memIndex].memArray);
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL & memMap[memIndex].memArray);
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH & memMap[memIndex].memArray);
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL & memMap[memIndex].memArray);
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH & memMap[memIndex].memArray);
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL & memMap[memIndex].memArray);
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH & memMap[memIndex].memArray);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES & memMap[memIndex].memArray);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS & memMap[memIndex].memArray);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS & memMap[memIndex].memArray);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 2) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL & tempByte);
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH & tempByte);
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL & tempByte);
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH & tempByte);
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL & tempByte);
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH & tempByte);
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL & tempByte);
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH & tempByte);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES & tempByte);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS & tempByte);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS & tempByte);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 3) {
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								tempByte = AL;
							} else if ("AH".equalsIgnoreCase(tempString)) {
								tempByte = AH;
							} else if ("BL".equalsIgnoreCase(tempString)) {
								tempByte = BL;
							} else if ("BH".equalsIgnoreCase(tempString)) {
								tempByte = BH;
							} else if ("CL".equalsIgnoreCase(tempString)) {
								tempByte = CL;
							} else if ("CH".equalsIgnoreCase(tempString)) {
								tempByte = CH;
							} else if ("DL".equalsIgnoreCase(tempString)) {
								tempByte = DL;
							} else if ("DH".equalsIgnoreCase(tempString)) {
								tempByte = DH;
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES & tempByte);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS & tempByte);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS & tempByte);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 4) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("CS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(CS & 0xff);
								tempSeg[1] = (byte)((CS >> 8) & 0xff);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(DS & 0xff);
								tempSeg[1] = (byte)((DS >> 8) & 0xff);
							} else if ("ES".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(ES & 0xff);
								tempSeg[1] = (byte)((ES >> 8) & 0xff);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(SS & 0xff);
								tempSeg[1] = (byte)((SS >> 8) & 0xff);
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if ("AX".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL & tempSeg[0]);
								AH = (byte)(AH & tempSeg[1]);
							} else if ("BX".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL & tempSeg[0]);
								BH = (byte)(BH & tempSeg[1]);
							} else if ("CX".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL & tempSeg[0]);
								CH = (byte)(CH & tempSeg[1]);
							} else if ("DX".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL & tempSeg[0]);
								DH = (byte)(DH & tempSeg[1]);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else {
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				} else if ("or".equalsIgnoreCase(instn)) {
					System.out.println("\nSelect the type of source : \n1. Memory Location\n2. Immediate Number\n3. General Register\n4. Segment Register");
					System.out.print("\nEnter your choice : ");
					ch = sc.nextInt();
					if (ch == 1) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL | memMap[memIndex].memArray);
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH | memMap[memIndex].memArray);
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL | memMap[memIndex].memArray);
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH | memMap[memIndex].memArray);
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL | memMap[memIndex].memArray);
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH | memMap[memIndex].memArray);
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL | memMap[memIndex].memArray);
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH | memMap[memIndex].memArray);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source address : ");
							memIndex = sc.nextInt();
							if (memIndex < 0 || memIndex > 1023) {
								System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
								sc.next();
								continue;
							}
							if (memMap[memIndex] == null) {
								System.out.println("ERROR : Source location is empty! Returning to main menu...");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES | memMap[memIndex].memArray);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS | memMap[memIndex].memArray);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS | memMap[memIndex].memArray);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 2) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register\n2. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL | tempByte);
							} else if ("AH".equalsIgnoreCase(tempString)) {
								AH = (byte)(AH | tempByte);
							} else if ("BL".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL | tempByte);
							} else if ("BH".equalsIgnoreCase(tempString)) {
								BH = (byte)(BH | tempByte);
							} else if ("CL".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL | tempByte);
							} else if ("CH".equalsIgnoreCase(tempString)) {
								CH = (byte)(CH | tempByte);
							} else if ("DL".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL | tempByte);
							} else if ("DH".equalsIgnoreCase(tempString)) {
								DH = (byte)(DH | tempByte);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else if (ch == 2) {
							System.out.print("\nEnter source value : ");
							tempByte = sc.nextByte();
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES | tempByte);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS | tempByte);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS | tempByte);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 3) {
						System.out.println("\n\nSelect the type of destination : \n1. Segment Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("AL".equalsIgnoreCase(tempString)) {
								tempByte = AL;
							} else if ("AH".equalsIgnoreCase(tempString)) {
								tempByte = AH;
							} else if ("BL".equalsIgnoreCase(tempString)) {
								tempByte = BL;
							} else if ("BH".equalsIgnoreCase(tempString)) {
								tempByte = BH;
							} else if ("CL".equalsIgnoreCase(tempString)) {
								tempByte = CL;
							} else if ("CH".equalsIgnoreCase(tempString)) {
								tempByte = CH;
							} else if ("DL".equalsIgnoreCase(tempString)) {
								tempByte = DL;
							} else if ("DH".equalsIgnoreCase(tempString)) {
								tempByte = DH;
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register : ");
							tempString = sc.next();
							if ("ES".equalsIgnoreCase(tempString)) {
								ES = (short)(ES | tempByte);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								DS = (short)(DS | tempByte);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								SS = (short)(SS | tempByte);
							} else {
								System.out.println("ERROR : Invalid/Unsupported destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else if (ch == 4) {
						System.out.println("\n\nSelect the type of destination : \n1. General Register");
						System.out.print("\nEnter your choice : ");
						ch = sc.nextInt();
						if (ch == 1) {
							System.out.print("\nEnter source register : ");
							tempString = sc.next();
							if ("CS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(CS | 0xff);
								tempSeg[1] = (byte)((CS >> 8) | 0xff);
							} else if ("DS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(DS | 0xff);
								tempSeg[1] = (byte)((DS >> 8) | 0xff);
							} else if ("ES".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(ES | 0xff);
								tempSeg[1] = (byte)((ES >> 8) | 0xff);
							} else if ("SS".equalsIgnoreCase(tempString)) {
								tempSeg[0] = (byte)(SS | 0xff);
								tempSeg[1] = (byte)((SS >> 8) | 0xff);
							} else {
								System.out.println("ERROR : Invalid source register entered! Returning to main menu....");
								sc.next();
								continue;
							}
							System.out.print("Enter destination register (AX,BX,CX,DX) : ");
							tempString = sc.next();
							if ("AX".equalsIgnoreCase(tempString)) {
								AL = (byte)(AL | tempSeg[0]);
								AH = (byte)(AH | tempSeg[1]);
							} else if ("BX".equalsIgnoreCase(tempString)) {
								BL = (byte)(BL | tempSeg[0]);
								BH = (byte)(BH | tempSeg[1]);
							} else if ("CX".equalsIgnoreCase(tempString)) {
								CL = (byte)(CL | tempSeg[0]);
								CH = (byte)(CH | tempSeg[1]);
							} else if ("DX".equalsIgnoreCase(tempString)) {
								DL = (byte)(DL | tempSeg[0]);
								DH = (byte)(DH | tempSeg[1]);
							} else {
								System.out.println("ERROR : Invalid destination register entered! Returning to main menu....");
								sc.next();
								continue;
							}
						} else {
							System.out.println("ERROR : Invalid destination choice entered! Returning to main menu....");
							sc.next();
							continue;
						}
					} else {
						System.out.println("Invalid source choice entered! Returning to main menu....");
						sc.next();
						continue;
					}
				} else if ("test".equalsIgnoreCase(instn)) {
					System.out.println("\nTEST opcode was invoked.\n");
					sc.next();
					continue;
				} else if ("shl".equalsIgnoreCase(instn) || "sal".equalsIgnoreCase(instn)) {

				} else if ("shr".equalsIgnoreCase(instn)) {

				} else if ("sar".equalsIgnoreCase(instn)) {

				} else if ("rol".equalsIgnoreCase(instn)) {

				} else if ("ror".equalsIgnoreCase(instn)) {

				} else if ("rcl".equalsIgnoreCase(instn)) {

				} else if ("rcr".equalsIgnoreCase(instn)) {

				} else if ("in".equalsIgnoreCase(instn)) {

				} else if ("out".equalsIgnoreCase(instn)) {

				} else if ("push".equalsIgnoreCase(instn)) {
					String destn;
					try {
						destn = instnComplete.nextToken();
					} catch (NoSuchElementException ex) {
						System.out.println("ERROR : Invalid instruction syntax! Returning to main menu....");
						sc.next();
						continue;
					}
					if ("AX".equalsIgnoreCase(destn)) {
						tempSeg[0] = AL;
						tempSeg[1] = AH;
					} else if ("BX".equalsIgnoreCase(destn)) {
						tempSeg[0] = BL;
						tempSeg[1] = BH;
					} else if ("CX".equalsIgnoreCase(destn)) {
						tempSeg[0] = CL;
						tempSeg[1] = CH;
					} else if ("DX".equalsIgnoreCase(destn)) {
						tempSeg[0] = DL;
						tempSeg[1] = DH;
					} else if ("CS".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)(CS | 0xff);
						tempSeg[1] = (byte)((CS >> 8) | 0xff);
					} else if ("ES".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)(ES | 0xff);
						tempSeg[1] = (byte)((ES >> 8) | 0xff);
					} else if ("DS".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)(DS | 0xff);
						tempSeg[1] = (byte)((DS >> 8) | 0xff);
					} else if ("SS".equalsIgnoreCase(destn)) {
						tempSeg[0] = (byte)(SS | 0xff);
						tempSeg[1] = (byte)((SS >> 8) | 0xff);
					} else {
						try {
							memIndex = Integer.parseInt(destn);
						} catch (NumberFormatException ex) {
							System.out.println("Invalid destination operand! (Enter AX,BX,CX,DX,CS,DS,ES,SS or a memory location between 0 and 1023).");
							System.out.println("Returning to main menu...");
							sc.next();
							continue;
						}
						if (memIndex < 0 || memIndex > 1022) {
							System.out.println("ERROR : Source memory location out of bounds! Returning to main menu...");
							sc.next();
							continue;
						}
						if (memMap[memIndex] == null || memMap[memIndex + 1] == null) {
							System.out.println("ERROR : Source location is empty! Returning to main menu...");
							sc.next();
							continue;
						}
						try {
							tempSeg[0] = memMap[memIndex].memArray;
							tempSeg[1] = memMap[memIndex + 1].memArray;
						} catch (ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException ex) {
							System.out.println("ERROR : An error occured while trying to execute this instruction. Returning to main menu...");
							sc.next();
							continue;
						}
					}
					try {
						stackPointer++;
						stack[stackPointer] = tempSeg[0];
						stackPointer++;
						stack[stackPointer] = tempSeg[1];
					} catch (ArrayIndexOutOfBoundsException ex) {
						System.out.println("ERROR : Stack index is out of bounds (Stack is full)! Returning to main menu...");
						sc.next();
						continue;
					}
				} else if ("pop".equalsIgnoreCase(instn)) {
					String destn;
					try {
						destn = instnComplete.nextToken();
					} catch (NoSuchElementException ex) {
						System.out.println("ERROR : Invalid instruction syntax! Returning to main menu....");
						sc.next();
						continue;
					}
					try {
						tempSeg[1] = stack[stackPointer];
						stackPointer--;
						tempSeg[0] = stack[stackPointer];
						stackPointer--;
					} catch (ArrayIndexOutOfBoundsException ex) {
						System.out.println("ERROR : Stack index is out of bounds (Stack is empty)! Returning to main menu...");
						stackPointer--;
						sc.next();
						continue;
					}
					if ("AX".equalsIgnoreCase(destn)) {
						AH = tempSeg[1];
						AL = tempSeg[0];
					} else if ("BX".equalsIgnoreCase(destn)) {
						BH = tempSeg[1];
						BL = tempSeg[0];
					} else if ("CX".equalsIgnoreCase(destn)) {
						CH = tempSeg[1];
						CL = tempSeg[0];
					} else if ("DX".equalsIgnoreCase(destn)) {
						DH = tempSeg[1];
						DL = tempSeg[0];
					} else if ("CS".equalsIgnoreCase(destn)) {
						CS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
					} else if ("ES".equalsIgnoreCase(destn)) {
						ES = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
					} else if ("DS".equalsIgnoreCase(destn)) {
						DS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
					} else if ("SS".equalsIgnoreCase(destn)) {
						SS = ByteBuffer.wrap(tempSeg).order(ByteOrder.LITTLE_ENDIAN).getShort();
					} else {
						try {
							memIndex = Integer.parseInt(destn);
						} catch (NumberFormatException ex) {
							System.out.println("Invalid destination operand! (Enter AX,BX,CX,DX,CS,DS,ES,SS or a memory location between 0 and 1023).");
							System.out.println("Returning to main menu...");
							sc.next();
							continue;
						}
						if (memIndex < 0 || memIndex > 1022) {
							System.out.println("ERROR : Destination memory location out of bounds! Returning to main menu...");
							sc.next();
							continue;
						}
						if (memMap[memIndex] == null) {
							memMap[memIndex] = new Mem();
						}
						if (memMap[memIndex + 1] == null) {
							memMap[memIndex + 1] = new Mem();
						}
						try {
							memMap[memIndex + 1].memArray = tempSeg[1];
							memMap[memIndex].memArray = tempSeg[0];
						} catch (ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException ex) {
							System.out.println("ERROR : An error occured while trying to execute this instruction. Returning to main menu...");
							sc.next();
							continue;
						}
					}
				} else if ("stc".equalsIgnoreCase(instn)) {
					CF = true;
				} else if ("clc".equalsIgnoreCase(instn)) {
					CF = false;
				} else if ("cmc".equalsIgnoreCase(instn)) {
					if(CF = true)
						CF = false;
					else
						CF = true;
				} else if ("std".equalsIgnoreCase(instn)) {
					DF = true;
				} else if ("cld".equalsIgnoreCase(instn)) {
					DF = false;
				} else if ("sti".equalsIgnoreCase(instn)) {
					IF = true;
				} else if ("cli".equalsIgnoreCase(instn)) {
					IF = false;
				}else {
					System.out.println("ERROR : Invalid instruction entered! Returning to main menu....");
					sc.next();
				}
			} else if (ch == 2) {
				Frame frame = new Frame("Flags and Registers - " + vmName);
				frame.setSize(700, 700);
				frame.setVisible(true);
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
			} else if (ch == 3) {
				while (true) {
					clearScreen();
					System.out.println("\t\t\t******Memory Map Operations********");
					System.out.println("\n\n1. Store a value at a memory location");
					System.out.println("2. Delete the value at a memory location");
					System.out.println("3. Replace the value at a memory location");
					System.out.println("4. View the value at a memory location");
					System.out.println("5. Go back to main menu");
					System.out.print("\n\nEnter your choice : ");
					ch = sc.nextInt();
					if (ch == 1) {
						try {
							System.out.println("\n\nEnter a memory index to store the value at (Between 0 and 1023) : ");
							memIndex = sc.nextInt();
							memMap[memIndex] = new Mem();
							System.out.println("Enter the data to be stored in this location : ");
							memMap[memIndex].memArray = sc.nextByte();
						} catch (ArrayIndexOutOfBoundsException ex) {
							System.out.println("ERROR : Memory index is out of bounds! Enter again...");
							sc.next();
							continue;
						} catch (NullPointerException ex) {
							System.out.println("ERROR : Cannot initialize the current memory location! Try again....");
							sc.next();
							continue;
						}
					} else if (ch == 2) {
						System.out.println("\n\nEnter the memory index to be cleared (Between 0 and 1023) : ");
						memIndex = sc.nextInt();
						memMap[memIndex] = null;
					} else if (ch == 3) {
						try {
							System.out.println("\n\nEnter a memory index to replace the value at (Between 0 and 1023) : ");
							memIndex = sc.nextInt();
							System.out.println("Enter the new data to be stored in this location : ");
							memMap[memIndex].memArray = sc.nextByte();
						} catch (ArrayIndexOutOfBoundsException ex) {
							System.out.println("ERROR : Memory index is out of bounds! Enter again...");
							sc.next();
							continue;
						} catch (NullPointerException ex) {
							System.out.println("ERROR : The memory location entered is empty! Use store operation instead....");
							sc.next();
							continue;
						}
					} else if (ch == 4) {
						System.out.println("\n\nEnter the memory location whose value you want to display (Between 1 and 1023) : ");
						memIndex = sc.nextInt();
						try {
							System.out.println("Value at memory location " + memIndex + " is : " + memMap[memIndex]);
						} catch (ArrayIndexOutOfBoundsException ex) {
							System.out.println("ERROR : Memory index is out of bounds! Enter again...");
							sc.next();
							continue;
						} catch (NullPointerException ex) {
							System.out.println("ERROR : The memory location entered is empty! Enter again....");
							sc.next();
							continue;
						}
					} else if (ch == 5) {
						sc.next();
						break;
					} else {
						System.out.println("ERROR : Invalid choice entered! Enter again...");
						sc.next();
						continue;
					}
				}
			} else if (ch == 4) {
				sc.next();
				sc.close();
				System.exit(0);
			} else {
				System.out.println("ERROR : Invalid choice entered! Enter again...");
				sc.next();
				continue;
			}

		}
	}
}