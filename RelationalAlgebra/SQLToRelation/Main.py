from Parser import Parser

if __name__ == "__main__":
	parser = Parser()
	# parser.parse_query("""
	# 					select r.roomNo as room, r.type as type, r.price as price
	# 					from Room r, Hotel h, Booking b
	# 					where r.roomNo = 5 and b.hotelNo = h.hotelNo
	# 					""")

	parser.parse_query("""
						select e.eName, e.age
						from Emp e, Dept d
						inner join Works
						where e.pctTime = 100 and d.Id = 1
						on e.eId = d.dId
						""")