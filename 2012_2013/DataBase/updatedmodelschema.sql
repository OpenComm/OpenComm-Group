
create table if not exists Conference(roomID varchar(50) primary key not null, roomname varchar(25), invitername varchar(25), starttime timestamp, endtime timestamp, recurrence varchar(25), description varchar(2000), foreign key (roomID) references ofGroupUser(groupName));

create table if not exists Invitations(roomID varchar(25), inviter varchar(25), reason varchar(25), foreign key (roomID) references ofGroupUser(roomID));




