from Parser import Parser

if __name__ == "__main__":
	parser = Parser()
	parser.parse_query("""
						select r.roomNo, r.type, r.price
						from Room r, Hotel h, Booking b
						where r.roomNo = 5 and b.hotelNo = h.hotelNo
						""")

	parser.parse_query("""
						select m.movieTitle
						from StarsIn s, MovieStar m
						where s.starName = name and m.birthdate = 1960
						""")