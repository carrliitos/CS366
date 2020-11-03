CREATE TABLE Student(snum BIGINT,
				sname VARCHAR(50),
				major VARCHAR(50),
				level CHAR(2),
				age INT,
				PRIMARY KEY (snum));

CREATE TABLE ENROLL(snum BIGINT,
				cname VARCHAR(255),
				FOREIGN KEY (snum) REFERENCES Student(snum),
				FOREIGN KEY (cname) REFERENCES Class(cname),
				PRIMARY KEY (snum, cname));

CREATE TABLE Class(cname VARCHAR(255),
				meets-at VARCHAR(255),
				room VARCHAR(255),
				fid int,
				PRIMARY KEY (cname));