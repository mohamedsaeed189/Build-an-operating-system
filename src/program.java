
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.time.*;

public class program {
	static Semaphore userInputsemaphore = new Semaphore(1);
	static Semaphore userOutputsemaphore = new Semaphore(1);
	static Semaphore filesemaphore = new Semaphore(1);
	
	static QueueLinkedList Blocked= new QueueLinkedList();
	static QueueLinkedList Ready = new QueueLinkedList();
	static QueueLinkedList fileBlocked= new QueueLinkedList();
	static QueueLinkedList InputBlocked= new QueueLinkedList();
	static QueueLinkedList OutputBlocked= new QueueLinkedList();
	static QueueLinkedList Finished = new QueueLinkedList();
	
	
	static ArrayList<String> filePath = new ArrayList<String>();
	
	static LinkedHashMap<String,ArrayList<String>> map = new LinkedHashMap<String,ArrayList<String>>();
	static LinkedHashMap<String,ArrayList<String>> Mapper = new LinkedHashMap<String,ArrayList<String>>();
	static LinkedHashMap<String,Integer> mapTime = new LinkedHashMap<String,Integer>();
	
	static String fileOwner;
	static String userInputOwner;
	static String userOutputOwner;
	
	static int count=0;
	static int instruction=0;
	
	static boolean state=false;
	static boolean blocked=false;
	static boolean increase=false;
	
		
	public static void print(Object x,String filePath) throws IOException {
		ArrayList<String> a=map.get(filePath);
		String h = (String)x;
		if(h.contains(".txt")) {
			String f = readText(h);
			System.out.println(f);
		}
		else {
			String e =search(h,a);
			System.out.println(e);
		}
	}
	
	public static boolean isInteger(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	}
	
	public static void assign(Object x,Object y) {
	    	x=y;   	
    }
	

	public static String search(String x, ArrayList<String> b) {
		String z="";
		for(int i=0;i<b.size();i++) {
			String[] f = (b.get(i)).split(" ");
			if(x.equals(f[0]))
				z = f[1];
		}
		return z;
	}
	
	public static String readText(String filePath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		String s = "";
		while(line!=null) {
			s=s+line+"\n";
			line=br.readLine();
		}	    	  	    
		br.close();
		return s;
	}
	
		
	public static ArrayList<String> readFile(String filePath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();	
		ArrayList<String> a = new ArrayList<String>();
		while(line!=null) {
			a.add(line);
			line=br.readLine();
		}	    	  	    
		br.close();
		return a;
	}
	


	public static void execute(String inst,String filePath,int QuantumTime) throws IOException, InterruptedException {	
			ArrayList<String> data = Mapper.get(filePath);
			ArrayList<String> a = map.get(filePath);
			String[] line = inst.split(" ");
			switch (line[0]) {
			case "print":
				print(line[1],filePath);
				count++;
				break;
			case "assign":
				if(isInteger(line[2])) {
					assign(line[1],line[2]);
					String x= line[1] + " " + line[2];	
					a.add(x);
					count++;
				}
				else if (line[2].contains(".txt")) {
					assign(line[1],line[2]);
					String x= line[1] + " " + line[2];	
					a.add(x);	
					count++;
				}
				else if(line[2].equals("readFile")) {					
					String e =search(line[3],a);	
					String l = readText(e);
					count++;
					if(count==QuantumTime) {
			    		String str = "assign" + " " + line[1] + " " + l; 
			    		data.set(0,str);
			    		Mapper.replace(filePath,data);
			    		state =true;
			    		break;
			    	}
					assign(line[1],l);
					String f = line[1]+ " " +l;
					a.add(f);	
					count++;
					increase=true;
				}
				else if(!isInteger(line[2]) & !line[2].contains(".txt")){
					if(line[2].equals("input")) {
						Scanner sc = new Scanner(System.in);
				    	System.out.println("Please enter a value " + filePath);
				    	Object x= sc.next();
				    	count++;
				    	if(count==QuantumTime) {
				    		String str = "assign" + " " + line[1] + " " + x; 
				    		data.set(0,str);
				    		Mapper.replace(filePath,data);
				    		state =true;
				    		break;
				    	}
				    	assign(line[1],(String)x);
				    	String str= line[1] + " " + x;	
						a.add(str);
				    	count++;
				    	increase=true;
					}
					else {
						assign(line[1],line[2]);
						String x= line[1] + " " + line[2];	
						a.add(x);
						count++;
					}											
				}
				break;
			case "readFile":
				readFile(line[1]);
				count++;
				break;
			case "writeFile":				
				if(line[1].contains(".txt")) {
					writeFile(line[1],line[2]);
				}
				else {
					String f =search(line[1],a);
					String g = search(line[2],a);
					writeFile(f,g);
				}		
				count++;
				break;
			case "printFromTo":
				isInteger((String)line[1]);
				if(isInteger((String)line[1]))
					printFromTo(Integer.parseInt(line[1]),Integer.parseInt(line[2]));
				else {
					Object f = Integer.parseInt(search(line[1],a));
					Object g = Integer.parseInt(search(line[2],a));
					printFromTo((int)f,(int)g);					
				}
				count++;
				break;
			case "semWait":
				if (line[1].equals("userInput")) {
					semWait(userInputsemaphore,filePath);
				}
				else if (line[1].equals("userOutput")) {
					semWait(userOutputsemaphore,filePath);
				}
				else{
					semWait(filesemaphore,filePath);
				}
				count++;
				break;
			case "semSignal":
				if (line[1].equals("userInput")) {
					semSignal(userInputsemaphore,filePath);
				}
				else if(line[1].equals("userOutput")) {
					semSignal(userOutputsemaphore,filePath);
				}
				else{
					semSignal(filesemaphore,filePath);
				}
				count++;
				break;	
			}   	 
	}
		
	public static void writeFile(String FilePath, String y) throws IOException {		
		File file = new File(FilePath); 
		boolean result;
		try {
			result=file.createNewFile();
			if(result) {
				FileOutputStream a = new FileOutputStream(FilePath,true);
		        byte [] b = y.getBytes();
		        a.write(b);
		        a.write('\n');
		        a.close();
			}
			else {
				FileOutputStream a = new FileOutputStream(FilePath,true);
		        byte [] b = y.getBytes();
		        a.write(b);
		        a.write('\n');
		        a.close();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printFromTo(int x, int y) {
		for(;x<=y;x++) 
			System.out.println(x);			
	}
	
	public static void semWait(Semaphore s,String filePath) throws InterruptedException {	
		if(s.equals(userInputsemaphore)) {
			if(userInputsemaphore.availablePermits()==1) {
				if(userInputOwner!=null) {
					if(userInputOwner==filePath) {
						userInputsemaphore.acquire();
					}
					else {
						InputBlocked.enqueue(filePath);
						blocked=true;
					}
				}
				else {
					userInputsemaphore.acquire();
					userInputOwner=filePath;
				}
			}
			else {
				InputBlocked.enqueue(filePath);
				blocked=true;
			}
		}	
		else if(s.equals(userOutputsemaphore)) {
			if(userOutputsemaphore.availablePermits()==1) {
				if(userOutputOwner!=null) {
					if(userOutputOwner==filePath) {
						userOutputsemaphore.acquire();
					}
					else {
						OutputBlocked.enqueue(filePath);
						blocked=true;
					}
				}
				else {
					userOutputsemaphore.acquire();
					userOutputOwner=filePath;
				}		
			}
			else {
				OutputBlocked.enqueue(filePath);
				blocked=true;
			}
		}	
		else{
			if(filesemaphore.availablePermits()==1) {
				if(fileOwner!=null) {
					if(fileOwner==filePath) {
						filesemaphore.acquire();
					}
					else {
						fileBlocked.enqueue(filePath);
						blocked=true;
					}
				}
				else {
					filesemaphore.acquire();
					fileOwner=filePath;
				}		
			}
			else {
				fileBlocked.enqueue(filePath);
				blocked=true;
			}
		}						
	}
	
	public static void semSignal(Semaphore s,String filePath) {
		if(s.equals(userInputsemaphore)) {
			if(userInputOwner.equals(filePath)) {
				if(!InputBlocked.isempty()) {
					String tmp = (String) InputBlocked.dequeue();
					userInputOwner=tmp;
					Blocked.enqueue(tmp);
					blocked=false;
					userInputsemaphore.release();
				}
				else{
					userInputsemaphore.release();
					userInputOwner=null;
				}
			}
		}
		else if(s.equals(userOutputsemaphore)) {
			if(userOutputOwner.equals(filePath)) {
				if(!OutputBlocked.isempty()) {
					String tmp = (String) OutputBlocked.dequeue();
					userOutputOwner=tmp;
					Blocked.enqueue(tmp);
					blocked=false;
					userOutputsemaphore.release();
				}
				else{
					userOutputsemaphore.release();
					userOutputOwner=null;
				}
			}
		}
		else {
			if(fileOwner.equals(filePath)) {
				if(!fileBlocked.isempty()) {
					String tmp = (String) fileBlocked.dequeue();
					fileOwner=tmp;
					Blocked.enqueue(tmp);
					blocked=false;
					filesemaphore.release();
				}
				else{
					filesemaphore.release();
					fileOwner=null;
				}
			}				
		}
	}
	
	public static void process(String s) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("please enter "+s+" process arrival time:");
		int arrivalTime=sc.nextInt();
		map.put(s,new ArrayList<String>());
		Mapper.put(s,readFile(s));	
		mapTime.put(s,arrivalTime);
		filePath.add(s);
	}
	
	public static void run() throws IOException, InterruptedException {
		Scanner sc = new Scanner(System.in);
		System.out.println("please enter Quantum time: ");
		int x =sc.nextInt();
		schedule(x);
	}
	
	
	public static void schedule(int quantumTime) throws IOException, InterruptedException {	
		int j=1;
		int nextArrival=0;
		Ready.enqueue(filePath.get(0));
		if(filePath.size()>1) {
			nextArrival=mapTime.get(filePath.get(j));
		}		
		
	

		while(!Ready.isempty()) {
				String file = (String)Ready.peek();	     // to get process name
				ArrayList<String> data =Mapper.get(file);   // to get data of text file
				while(count!=quantumTime) {
					System.out.println("Cycle: "+instruction);
					System.out.println("Ready Queue: ");
					Ready.display();
					System.out.println("Blocked Queue:");
					Blocked.display();
					System.out.println("Finished Queue:");
					Finished.display();
					System.out.println("              ---------------------------------------------------");
					String currentInstruction = data.get(0);   //to get first instruction
					System.out.println("Process: " + file );
					System.out.println("Instruction: "+ currentInstruction);
					execute(currentInstruction,file,quantumTime);	//executing instruction
					instruction++;
					System.out.println("Input Blocked Queue: ");
					InputBlocked.display();
					System.out.println("File Blocked Queue: ");
					fileBlocked.display();
					System.out.println("Output Blocked Queue: ");
					OutputBlocked.display();
					
					System.out.println("---------------------------------------------------");
					
					
					if(increase==true) {
						if(instruction==nextArrival )  {
							Blocked.enqueue(filePath.get(j));
							if(j<filePath.size()-1) {
								nextArrival =mapTime.get(filePath.get(++j));	
							}						
						}
						instruction++;
					}
					if(blocked==true) {
						if(instruction==nextArrival )  {
							Blocked.enqueue(filePath.get(j));
							if(j<filePath.size()-1) {
								nextArrival =mapTime.get(filePath.get(++j));	
							}						
						}
						Ready.dequeue();
						Ready.enqueue(Blocked.dequeue());
						blocked=false;
						break;
					}
					if(state==false) {
						data.remove(0);                                 //removing executed instruction
						Mapper.replace(file,data);					// replace new data of text file after removing instruction
					}	
					else {
						state=false;
					}
					
					if(instruction==nextArrival )  {
						Blocked.enqueue(filePath.get(j));
						if(j<filePath.size()-1) {
							nextArrival =mapTime.get(filePath.get(++j));	
						}						
					}
					
					if(data.size()==0) {
						Finished.enqueue(file);
						Ready.dequeue();
						if(Ready.isempty() && Blocked.isempty() && InputBlocked.isempty() && fileBlocked.isempty() && OutputBlocked.isempty()) {
							break;
						}
						Ready.enqueue(Blocked.dequeue());
						file = (String)Ready.peek();	
						data =Mapper.get(file);  
						count=0;
					}
				}								
				if(count==quantumTime) {
					if(Ready.isempty() && Blocked.isempty() && InputBlocked.isempty() && fileBlocked.isempty() && OutputBlocked.isempty() ) {
						break;
					}
					if(!Blocked.isempty()) {
						Ready.dequeue();
						Ready.enqueue(Blocked.dequeue());
						Blocked.enqueue(file);
					}					
						
				}	
				count=0;
		 }	
		System.out.println("Ready Queue: ");
		Ready.display();
		System.out.println("Blocked Queue:");
		Blocked.display();
		System.out.println("Finished Queue:");
		Finished.display();
		System.out.println("Input Blocked Queue: ");
		InputBlocked.display();
		System.out.println("File Blocked Queue: ");
		fileBlocked.display();
		System.out.println("Output Blocked Queue: ");
		OutputBlocked.display();
		System.out.println("---------------------------------------------------");

	}	
			
	public static void main(String[] args) throws IOException, InterruptedException{
		process("Program_1.txt");
		process("Program_2.txt");
		process("Program_3.txt");
		run();
	}	
}
