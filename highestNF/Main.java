/* @author Benzon Carlitos Salazar
*	CS366 - DBMS
*/

import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		try {
			List<AttributeSet> allKeyCandidatesm;
			List<FD> fdsm = new ArrayList<>();
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Enter the attributes of relation as a string, (ie, ABCD): ");
			String atts = "";

			atts = scan.nextLine();
			char att[] = atts.toCharArray();
			if(atts.length() == 0) throw new MyException("ERROR! Attributes Empty.");

			for(char ch : att) {
				if((ch >= 'A' && ch <= 'Z')) {}
				else throw new MyException("Set attributes to capital letters.");
			}

			DomainTable xyz = new DomainTable(att);
			Relation rel = new Relation(new AttributeSet(xyz, att));
			// System.out.println(rel);
			System.out.println("Enter functional dependencies (ie, AB>C), q to quit: ");
			String f = null;
			int counter = 0;
			while(true) {
				f = scan.nextLine();
				if(f.equals("q")) break;

				counter++;
				String lhs = "", rhs = "";
				int flag = 0, prob = 1;
				for(int i = 0; i < f.length(); i++) {
					if(f.charAt(i) == '>') flag = 1;
					else if(flag == 0) {
						lhs += f.charAt(i);
						for(char ch : att) {
							if(ch == f.charAt(i)) prob = 0;
						}
						if(prob != 0) {
							System.out.println("Attribute not within the domain.");
							break;
						}
					}else rhs += f.charAt(i);
				}

				if(lhs.length() == 0 || rhs.length() == 0) {
					System.out.println("Invalid Functional Dependencies");
					continue;
				}

				if(prob != 0) continue;
				char ls[] = lhs.toCharArray();
				for(int i = 0; i < rhs.length(); i++) {
					char rs[] = new char[1];
					rs[0] = rhs.charAt(i);
					FD fd = new FD(xyz, ls, rs);
					fdsm.add(fd);
				}
			}

			if(counter == 0) {
				System.out.println("Relation already satisfies BCNF.");
				throw new MyException("Nothing to do here.");
			}

			rel.setFDs(fdsm);
			allKeyCandidatesm = Algorithms.findAllCandidateKeys(rel.fds, rel.attributes);
			rel.setKeyCandidates(allKeyCandidatesm);
			rel.primaryKey = rel.allKeyCandidates.get(0);
			System.out.println(rel);

			if(Algorithms.isIn2NF(rel) == false) return;
			else if(Algorithms.isIn3NF(rel) == false) return;
			else if(Algorithms.isInBCNF(rel) == false) return;
			else System.out.println("The given relation is BCNF");
		}
		catch(MyException e) {
			System.out.println(e);
		}
	}
}