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
		attributeSet r = a.clone();
		int lastAtts;
		do{
			lastAtts = r.attMask();
			for(FD fd : fds) {
				if(fd.getLHS().isSubsetOf(r)) {
					r.union(fd.getRHS());
				}
			}
		}while(lastAtts != r.attMask());
		return r;
	}

	
}