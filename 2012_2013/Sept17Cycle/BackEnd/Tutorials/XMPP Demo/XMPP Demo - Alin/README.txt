Alin Barsan XMPP Demo


User Interface:

The user interface is fairly straightforward, the user is prompted 
to enter necessary information at first, and must reenter the info
until it is valid (i.e. the server address, the username, the pass)
upon which the user can then choose from a list of available commands
that allow him/her to operate the MUC application.  The interface
itself is done through the command line/eclipse console.  There seem
to be errors with my path variables such that running this from the
command line does not work on my machine (the javac command fails to 
recognize the smack jars), but it compiles successfully in eclipse.


Design Decisions:

I tried to avoid hardcoding when possible.  For example, I used the 
roster method to see the users that were in the same connection, and 
identified the ones labeled "[Buddies]".  This way, I did not have to
hardcode a list of fake online/offline buddies.  While this produces a
more realistically running program, its consequence is that the buddy
list shows that all buddies are offline since they ARE offline.

In testing this app, I built it so any user could be invited.  I then hard
coded that a certain user - designopencomm@jabber.org - would log in, and
then tested the chat by inviting that user.  Basically, i hardcoded the 
designopencomm@jabber.org user's functions and actions, but coded the remainder
by passing in designopencomm@jabber.org as an input to my invite function and
message function.

Since the room can be created once the MUC object is created, and there
is no need to create either until the user wants to begin a chat, both
initializations are done upon the user's attempt to invite someone to
join a chat room.