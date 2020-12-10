Download all the csv files from Canvas and then FTP them to Unix (if there is no sftp available, please use cut and paste, it works too). 
After you have that files please use the following command to load each file to the corresponding table:
At linux console, type:
```
$ mysql -u <your net id> -p --local-infile <your database name>
```
Example: 
```
$ mysql -u nguyenh -p --local-infile cs366_nguyenh
```
You will be asked to type in your login name.
Then after login successfully inside the MySQL, load a csv file into a table as follows:
```
mysql>  LOAD  DATA  LOCAL  INFILE  <path  to  the  csv  file>  INTO  TABLE  <table  name>  FIELDS  TERMINATED  BY  ','  LINES TERMINATED BY '\n';
```
Example:
```
mysql> LOAD DATA LOCAL INFILE '~nguyenh/CS366/universitydb/student.csv' INTO TABLE student FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';
```
You will load all 4 files into 4 corresponding table.