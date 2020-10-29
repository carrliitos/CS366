delimiter $$
create trigger LevelTrig
after update on Student
for each row
begin
	if new.level = 'PS' then
		insert into SpecialStudent(snum, sname, age, major)
		values(new.snum, new.sname, new.age, new.major);
	end if;
end $$
delimiter ;