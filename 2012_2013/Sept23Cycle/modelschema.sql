#Create a table for each model.
#add a participantList table to efficiently maintain participants in different category
#categories are: Conference, Invitation and Space.

create table if not exists Users(username varchar(25) primary key not null, nickname varchar(25), passwords varchar(25) not null, email varchar(58) unique not null);
create table if not exists ContactLists(ownername varchar(25), contactname varchar(25), foreign key (ownername) references Users(username), foreign key (contactname) references Users(username));
create table if not exists ParticipantList(username varchar(25), listID int not null, category varchar(25) not null, primary key (listID, category), foreign key (username) references Users(username));
create table if not exists Conference(roomID int primary key not null, roomname varchar(25), invitername varchar(25), starttime datetime, endtime datetime, recurrence varchar(25), cparticipants int, note varchar(200), foreign key (cparticipants) references ParticipantList(listID));
create table if not exists Inviofusertations(room varchar(25), inviter varchar(25), reason varchar(25), passwords varchar(25), invitees int, foreign key (invitees) references ParticipantList(listID) );
create table if not exists Spaces(spaceID varchar(25) primary key not null, ownerID varchar(25),sparticipants int, foreign key (ownerID) references Users(username),foreign key (ownerID) references Users(username), foreign key (sparticipants) references ParticipantList(listID));
