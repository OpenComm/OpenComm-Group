
create table if not exists Conference(roomID int primary key not null, roomname varchar(25), invitername varchar(25), starttime datetime, endtime datetime, recurrence varchar(25), description varchar(2000), plistID int, foreign key (rosterID) references ofRoster(rosterID));

create table if not exists Invitations(room varchar(25), inviter varchar(25), reason varchar(25), plistID int, foreign key (plistID) references ofRoster(rosterID));


