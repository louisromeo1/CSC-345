import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PHashtableTest {
    public static ArrayList<Patient> patients;
    public static int index;
    public static int time = 0;
    public static int testNum = 0;
    
    public static void main(String[] args) {
	patients = new ArrayList<Patient>();
	getPatients("patient_file_1.txt");
	double score = 0;
	ArrayList<Patient> list = new ArrayList<Patient>();

	//Test 1
        testNum++;
	score += test1_2(32, 25, list);

	//Test 2
        testNum++;
	score += test1_2(7, patients.size()-10, list);

	System.out.println("Score: " + score);
    }	

    private static double test1_2(int i, int cap, ArrayList<Patient> list) {
	Random gen = new Random(System.currentTimeMillis());
	System.out.println("\n*****Begin Test " + testNum + "*****");
	index = i;
	PHashtable table = new PHashtable();
	double score = 0;
	//check operations on empty HT
	System.out.println("\nHT is empty...");
	if(table.size() == 0) {
	    printMsg(true, "size");
	    score += 0.5;//0.5
	} else 
	    printMsg(false, "size");

	//insert Patients
	System.out.println("\nAdding some patients...");
	insertPatients(list, table, (int)(cap/2));
	if(table.size() == list.size()) {
	    score += 1.0;//1.5
	    printMsg(true, "size");
	} else 
	    printMsg(false, "size");
	
	//add the rest of the Patients
	System.out.println("\nAdding the rest of the patients...");
	insertPatients(list, table, (cap - list.size()));
	if(table.size() == list.size()) {
	    score += 1.0;//2.5
	    printMsg(true, "size");
	} else
	    printMsg(false, "size");
	
	//check get for a few random Patients in table
	System.out.println("Search for some random Patients...");
	boolean pass = true;
	for(int k = 0; k < 3; k++) {
	    int j = gen.nextInt(list.size());
	    Patient p1 = list.get(j);
	    String s = p1.name();
	    Patient p2 = table.get(s);
	    if(p1 == p2) {
		score += 0.5;
	    } else {
		System.out.println("Patients do not match");
		printExpAct(p1==null?"null":p1.toString(), p2==null?"null":p2.toString());
		pass = false;
	    }//4.0
	}
	if(!pass) 
	    printMsg(false, "get");
	else
	    printMsg(true, "get");
	
	//remove some Patients from table
	System.out.println("Remove some Patients...");
	pass = true;
	for(int k = 0; k < 4; k++) {
	    int j = gen.nextInt(list.size());
	    Patient p1 = list.remove(j);
	    Patient p2 = table.remove(p1.name());
	    if(p1 == p2) 
		score += 0.5;
	    else {
		System.out.println("Patients removed do not match.");
		printExpAct(p1.toString(), p2.toString());
	    }
	    if(list.size() != table.size()) {
		pass = false;
		System.out.println("Sizes do not match.");
		printExpAct(Integer.toString(list.size()), Integer.toString(table.size()));
	    }//6.0
	}
	if(pass)
	    printMsg(true, "remove");
	else
	    printMsg(false, "remove");
	    
	//search for a Patient not in table
	System.out.println("Search for a Patient not in table...");
	String name = "Patient Name";
	Patient p1 = new Patient(name, 0, 0);
	Patient p2 = table.get(name);
	if(p2 == null) {
	    score += 0.5;//6.5
	    printMsg(true, "get");
	} else {
	    printMsg(false, "get");
	    printExpAct("null", p2.toString());
	}
	p2 = table.remove(name);
	if(p2 == null) {
	    score += 0.5;//7.0
	    printMsg(true, "remove");
	} else {
	    printMsg(false, "remove");
	    printExpAct("null", p2.toString());
	}

	System.out.println("Total score for test " + testNum + ": " + score);
	return score;
    }

    private static String getExp(String fn) {
	BufferedReader br;
	try {
	    br = new BufferedReader(new FileReader(fn));
	    String line = br.readLine();
	    return line;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    private static void printMsg(boolean passed, String method) {
	if(passed) 
	    System.out.println(method + " passed");
	else
	    System.out.println(method + " failed");
    }
    
    private static void printExpAct(String exp, String act) {
	System.out.println("Expected: " + exp);
	System.out.println("Actual: " + act);
    }

    private static void insertPatients(ArrayList<Patient> list, PHashtable table, int num) {
	while(list.size() < num) {
	    Patient p = patients.get(index);
	    table.put(p);
	    insertToList(list, p);
	    incIndex();
	}
    }

    private static void insertToList(ArrayList<Patient> list, Patient p) {
	for(int i = 0; i < list.size(); i++) {
	    if(list.get(i).name().equals(p.name())) {
		list.set(i, p);
		return;
	    } 
	}
	list.add(p);
    }
    
    private static void getPatients(String fn) {
	BufferedReader reader;
	try {
	    reader = new BufferedReader(new FileReader(fn));
	    String line = reader.readLine();
	    while(line != null) {
		String[] split = line.split(",");
		if(split.length >= 2) {
		    Patient p = new Patient(split[0], Integer.parseInt(split[1]), time++);
		    patients.add(p);
		}
		line = reader.readLine();
	    }
	}catch (Exception e) {
	    e.printStackTrace();
	}  
    }

    private static void incIndex() {
	 index = (index + 1) % patients.size();
    }

    private static void printList(ArrayList<Patient> list) {
	System.out.println("\nLIST: ");
	for(Patient p : list)
	    System.out.print(p.toString() + " | ");
    }
}

    
	
