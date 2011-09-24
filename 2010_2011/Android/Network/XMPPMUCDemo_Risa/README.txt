== Risa Naka (rn96) == XMPP Multiuser Chat Room Test == README.TXT ==

================================================================================
================================================================================

	SET UP [SETUP]
	
========================================

	1. Download and unzip Smack, a Java XMPP client library
	(http://www.igniterealtime.org/downloads/index.jsp#smack)
	
	2. Download and install Pidgin, a universal chat client
	(http://www.pidgin.im/)
	
	3. In Eclipse, right-click on the project:
	Build Path -> Add External Archives
	Add both smack.jar and smackx.jar
	
	4. Sign in as user 2 and user 3 (default: risan (pwd: reesaspbc176) and 
        mucopencomm (pwd: opencommmuc)) in Pidgin 
        (you can sign onto both accounts on one Pidgin application).
        
        ** If you want to sign in as other users, change the static users 
        at the top of the MUCDemo.java
	
	5. Import the 3 JAVA files into package xmpp.muc
	
	6. Run MUCDemo.java
	
================================================================================
================================================================================

	DOCUMENTATIONS [DOC]

========================================

	SMACK Extensions: Multi User Chat
	http://www.igniterealtime.org/builds/smack/docs/latest/documentation/extensions/muc.html
	
================================================================================
================================================================================

	JAVA Files [JAVA]

========================================

	MUCDemo.java: main method, runs the MUC demo:
		1. Creates a XMPP connection to jabber.org through port 5222
		2. Logs in as opencommss@jabber.org
		3. Creates an multiuser chat room with default configurations
		4. Invites users risan and mucopencomm
		5. Chats amongst the three users until bye is inputted by the room owner
		6. Destroys the MUC room
		7. Disconnects from the server
		
	XMPPUser.java: a pre-created XMPP user; its username and password is stored
	
	XMPPConnectConfig.java: creates a configured XMPP connection to a specified
		host server through a specified post
	
	