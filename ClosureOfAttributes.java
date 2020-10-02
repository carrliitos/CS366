/* @author Benzon Carlitos Salazar CS366 - DBMS
*	This programs finds attribute closures.
*	Usage: java ClosureOfAttributes <dependency text file> <closure set you want>
*	Dependency text file will have the first line with all the attributes, then the
*		FDs are the lines with a space between the left-hand side and right-hand side
*	Closure set is whatever closure set you are looking for
*
*	Example: Given the relation R(ABCDE) with FDs {A->B, CD->E, AC->D}
*		DependencyFile.txt will have
*			ABCDE
*			A B
*			CD E
*			AC D
*		Find the closure of (AC)+
*
*	run: 	java ClosureOfAttributes DependencyFile.txt AC
*	output: (AC)+ = {ABCDE}
*/

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

import java.io.FileNotFoundException;

public class ClosureOfAttributes {
	class FunctionalDependencies{
		HashSet<Character> lhs; char rhs;

		public FunctionalDependencies(HashSet<Character> l, char r) {
			lhs = l;
			rhs = r;
		}

		public boolean equals(Object obj) {
			FunctionalDependencies fd2 = (FunctionalDependencies)obj;
			return lhs.equals(fd2.lhs) && rhs == fd2.rhs;
		}
	};

	// all attributes
	HashSet<Character> R = new HashSet<>();
	// the set of FDs
	HashSet<FunctionalDependencies> F = new HashSet<>();
	HashSet<Character> X = null;

	/* 1. Split FDs so each FD has a single attribute on the right */
	public ClosureOfAttributes(String filename) {
		Scanner in = null;

		try {
			in = new Scanner(new File(filename));
		}catch(FileNotFoundException e) {
			System.out.println("File: <" + filename + "> not found");
			System.exit(-1);
		}

		String line = in.nextLine();
		for(int i = 0; i < line.length(); i++) R.add(line.charAt(i));
		while(in.hasNextLine()) {
			HashSet<Character> l = new HashSet<>();
			String[] terms = in.nextLine().split(" ");
			for(int i = 0; i < terms[0].length(); i++) l.add(terms[0].charAt(i));
			for(int i = 0; i < terms[1].length(); i++) {
				F.add(new FunctionalDependencies(l, terms[1].charAt(i)));
			}
		}
		in.close();
	}

	HashSet<Character> string2set(String X) {
		HashSet<Character> Y = new HashSet<>();
		for(int i = 0; i < X.length(); i++) Y.add(X.charAt(i));
		return Y;
	}

	void printSet(Set<Character> X) {
		System.out.print("{");
		for(char c : X){
			System.out.print(c);
		} 
		System.out.print("}");
		System.out.println();
	}

	HashSet<Character> closure(HashSet<Character> Xinit) {
		int len = 0;
		/* 2. Initialize */
		X = new HashSet<Character>(Xinit);
		/* 3. Push out */
		do {
			len = X.size();
			F.forEach(fd -> {
				if(X.containsAll(fd.lhs) && !X.contains(fd.rhs)) {
					X.add(fd.rhs);
				}
			});
		}while (X.size() > len);
		return X; /* 4. Closure of X was found */
	}

	boolean followedBy(FunctionalDependencies fd) {
		boolean status;
		status = closure(fd.lhs).contains(fd.rhs);
		return status;
	}

	void example01() {
		System.out.println();
		System.out.println(followedBy(new FunctionalDependencies(string2set("AB"), 'D')));
		System.out.println(followedBy(new FunctionalDependencies(string2set("D"), 'A')));
	}

	public static void main(String[] args) {
		ClosureOfAttributes attrClosure = new ClosureOfAttributes(args[0]);
		HashSet<Character> X = attrClosure.string2set(args[1]);
		System.out.print("(" + args[1] + ")+ = ");
		attrClosure.printSet(attrClosure.closure(X));
		// attrClosure.example01();
	}
}