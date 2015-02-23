package databas;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Database db = new Database();
		Scanner scan = new Scanner(System.in);
		if(db.openConnection("db51", "marcus")){
			System.out.println("connected");
		}
		System.out.println(db.getClearance("0123456789")+" "+db.getGroup("0123456789"));
		while(true){
			String s = scan.next();
			System.out.println(db.Command(s, "0123456789"));
			
		}

	}

}
