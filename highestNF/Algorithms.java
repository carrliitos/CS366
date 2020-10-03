/* @author Benzon Carlitos Salazar
*	CS366 - DBMS
*/

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Algorithms {
	public static List<AttributeSet> findAllCandidateKeys(List<FD> fds, AttributeSet atts) {
		List<AttributeSet> keys = new ArrayList<>();

		if(fds == null || fds.size() == 0) {
			keys.add(atts);
			return keys;
		}
		
		int mask = atts.attMask();

		for(int l = 0; l <= mask; l++) {
			int keyMask = mask & l;
			if(keyMask != 0) {
				AttributeSet key = new AttributeSet(atts.domain());
				key.setMask(keyMask);
				if(isSuperKey(key, atts, fds)) {
					boolean addKey = true;
					for(Iterator<AttributeSet> i = keys.iterator(); i.hasNext();) {
						AttributeSet k = i.next();
						if(key.containsAttSet(k)) addKey = false;
						else if(k.containsAttSet(key)) i.remove();
					}
					if(addKey) keys.add(key);
				}
			}
		}

		return keys;
	}

	public static boolean isSuperKey(AttributeSet key, AttributeSet atts, List<FD> fds) {
		AttributeSet b = attributeClosure(key, fds);
		return b.equals(atts);
	}

	public static AttributeSet attributeClosure(AttributeSet a, List<FD> fds) {
		AttributeSet r = a.clone();
		int lastAtts;
		
		do{
			lastAtts = r.attMask();
			for(FD fd : fds) {
				if(fd.getLHS().isSubSetOf(r)) {
					r.union(fd.getRHS());
				}
			}
		}while(lastAtts != r.attMask());

		return r;
	}

	public static boolean isIn2NF(Relation rel) {
		List<FD> fd2NF = rel.fds;
		List<FD> not2NF = new ArrayList<>();
		int keyMask = 0;

		for(AttributeSet k : rel.allKeyCandidates) {
			keyMask = keyMask | k.attMask;
		}
		
		for(FD f : fd2NF) {
			if((keyMask & f.getRHS().attMask) != 0) continue;
			if(rel.allKeyCandidates.contains(f.getLHS())) continue;
			if((f.getLHS().attMask | (keyMask)) == keyMask) not2NF.add(f);
		}

		int ret = not2NF.size();
		if(ret != 0) {
			System.out.printf("The highest normal form that the relation satisfies is 1NF.\n");
			System.out.printf("Decomposing to 2NF.\n");
			System.out.printf("------------------------------\n");
		}else return true;

		List<Relation> drel = new ArrayList<>();
		Relation relc = rel.clone();
		List<FD> not2NFb = new ArrayList<>();
		not2NFb.addAll(not2NF);

		for(FD f : not2NFb) {
			relc.fds.remove(f);
			relc.attributes.removeAttSet(f.getRHS());
		}

		for(FD f : fd2NF) {
			if(relc.attributes.containsAttSet(f.getLHS()) == false) relc.fds.remove(f);
			if(relc.attributes.containsAttSet(f.getRHS()) == false) relc.fds.remove(f);
		}

		for(FD f : not2NF) {
			Relation r = new Relation();
			AttributeSet x = f.getLHS().clone();
			x.union(f.getRHS());
			r.setAttributes(x);
			
			List<FD> fdsm = new ArrayList<>();
			fdsm.add(f);
			r.setFDs(fdsm);
			drel.add(r);
		}

		drel.add(relc);
		for(Relation r : drel) {
			List<AttributeSet> allKeyCandidatesm;
			allKeyCandidatesm = Algorithms.findAllCandidateKeys(r.fds, r.attributes);
			r.setKeyCandidates(allKeyCandidatesm);
			r.primaryKey = r.allKeyCandidates.get(0);
			System.out.println(r);
		}

		return false;
	}

	public static boolean isIn3NF(Relation rel) {
		List<FD> fd3NF = rel.fds;
		List<FD> not3NF = new ArrayList<>();
		List<Relation> drel = new ArrayList<>();
		Relation relc = rel.clone();
		int keyMask = 0;

		for(AttributeSet k : rel.allKeyCandidates) {
			keyMask = keyMask | k.attMask;
		}

		for(FD f : fd3NF) {
			if(rel.allKeyCandidates.contains(f.getLHS())) continue;
			if((keyMask & f.getRHS().attMask) != 0) continue;
			else {
				relc.fds.remove(f);
				AttributeSet x = f.getRHS().clone();
				relc.attributes.removeAttSet(x);
				not3NF.add(f);
			}
		}

		if(not3NF.size() != 0) {
			System.out.printf("The highest normal form that the relation satisifes is 2NF.\n");
			System.out.printf("Decomposing to 3NF.\n");
			System.out.printf("------------------------------\n");
		}else return true;

		List<FD> not2NFb = new ArrayList<>();
		not2NFb.addAll(not3NF);

		int m = 0; 
		for(FD f : not2NFb) {
			m = (m | f.getRHS().attMask);
		}
		for(FD f : fd3NF) {
			if((f.getRHS().attMask & m) != 0) {
				if(relc.fds.contains(f)) relc.fds.remove(f);
			}
		}

		drel.add(relc);
		for(FD f : not3NF) {
			Relation r = new Relation();
			AttributeSet x = f.getLHS().clone();
			x.union(f.getRHS());
			r.setAttributes(x);

			List<FD> fdsm = new ArrayList<>();
			fdsm.add(f);
			r.setFDs(fdsm);
			drel.add(r);
		}

		for(Relation r : drel) {
			List<AttributeSet> allKeyCandidatesm;
			allKeyCandidatesm = Algorithms.findAllCandidateKeys(r.fds, r.attributes);
			r.setKeyCandidates(allKeyCandidatesm);
			r.primaryKey = r.allKeyCandidates.get(0);
			System.out.println(r);
		}
		return false;
	}

	public static boolean isInBCNF(Relation rel) {
		List<FD> fdBCNF = rel.fds;
		List<FD> notBCNF = new ArrayList<>();
		List<Relation> drel = new ArrayList<>();
		Relation relc = rel.clone();
		for(FD f : fdBCNF) {
			if(rel.allKeyCandidates.contains(f.getLHS())) continue;
			else {
				relc.fds.remove(f);
				AttributeSet x = f.getRHS().clone();
				x.removeAttSet(f.getLHS());
				relc.attributes.removeAttSet(x);
				notBCNF.add(f);
			}
		}

		if(notBCNF.size() != 0) {
			System.out.printf("The highest normal form that the relation satisfies is 3NF.\n");
			System.out.printf("Decomposing to make it BCNF.\n");
			System.out.printf("------------------------------\n");
		}else return true;

		List<FD> not2NFb = new ArrayList<>();
		not2NFb.addAll(notBCNF);

		int m = 0;
		for(FD f : not2NFb) {
			m = (m | f.getRHS().attMask);
		}

		for(FD f : fdBCNF) {
			if((f.getRHS().attMask & m) != 0) {
				if(relc.fds.contains(f)) relc.fds.remove(f);
			}else if((f.getLHS().attMask & m) != 0) {
				if(relc.fds.contains(f)) relc.fds.remove(f);
			}
		}

		drel.add(relc);
		for(FD f : notBCNF) {
			Relation r = new Relation();
			AttributeSet x =  f.getLHS().clone();
			x.union(f.getRHS());
			r.setAttributes(x);

			List<FD> fdsm = new ArrayList<>();
			fdsm.add(f);
			r.setFDs(fdsm);
			drel.add(r);
		}

		for(Relation r : drel) {
			List<AttributeSet> allKeyCandidatesm;
			allKeyCandidatesm = Algorithms.findAllCandidateKeys(r.fds, r.attributes);
			r.setKeyCandidates(allKeyCandidatesm);
			r.primaryKey = r.allKeyCandidates.get(0);
			System.out.println(r);
		}
		return false;
	}
}