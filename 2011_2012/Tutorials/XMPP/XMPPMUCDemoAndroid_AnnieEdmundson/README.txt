== Annie Edmundson (ae243) == XMPP Multiuser Chat Demo (Android) == README.TXT ==

	Set up [SETUP]
	Documentations [DOC]
	JAVA Files [JAVA]
	Version Information [VER]
	Changes to Implement [CHANGE]

================================================================================
================================================================================

	SET UP [SETUP]

========================================

	1. Download and install Pidgin, a universal chat client
	(http://www.pidgin.im/)
	
	2. Download asmack jar file (http://code.google.com/p/asmack/)

	3. In Eclipse, right-click on the project:
	Build Path -> Add External Archives
	Add asmack-2010.05.07.jar

	4. Run MUCDemo_AndroidActivity.java

================================================================================
================================================================================

	DOCUMENTATIONS [DOC]

========================================

	asmack:
	http://code.google.com/p/asmack/

================================================================================
================================================================================

	JAVA Files [JAVA]

========================================

	MUCDemo_AndroidActivity.java:
		-Connects to jabber.org
		-Logs in with username: opencommsec and password: secopencomm
		-Creates a MultiUserChat and joins it
		-Bans a user from the room (mucopencomm@jabber.org)
		-Creates and sends a message
		-Invites a user to a room (mucopencomm@jabber.org)
		-Leaves the chat
		-Disconnects

================================================================================
================================================================================

	VERSION INFORMATION [VER]

========================================

	1.0: Demo
	1.1: Android Demo

================================================================================
================================================================================

	CHANGES TO BE IMPLEMENTED [CHANGE]

========================================

	-I haven't gotten the LogCat print statements to work, so I havent' been able to debug.
	-Add a UI.