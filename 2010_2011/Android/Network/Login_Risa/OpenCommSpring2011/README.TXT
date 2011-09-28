== Risa Naka (rn96) == ANDROID XMPP Log == README.TXT ==

================================================================================
================================================================================

	SET UP [SETUP]
	
========================================

	1. Obtain the following JAR files:
		- ASMACK, an Android-version of SMACK (Java XMPP client library :: 
		http://www.igniterealtime.org/downloads/index.jsp#smack)
		- JZLib (Used to add the packet compression support to 

	2. Download and install Pidgin, a universal chat client
	(http://www.pidgin.im/)
	
	3. In Eclipse, right-click on the project:
	Build Path -> Add External Archives
	Add asmack-2010.05.07.jar and jzlib-1.0.7.jar
	
	4. Sign in as user 2 and user 3 (default: risan (pwd: reesaspbc176) and 
        mucopencomm (pwd: opencommmuc)) in Pidgin 
        (you can sign onto both accounts on one Pidgin application).
        
        ** If you want to sign in as other users, change the static users 
        at the top of the MUCDemo.java
	
	5. Import the zipped files into the necessary locations.
		
	
	6. Run Login.java (success of Login verified through LogCat with tag "XMPPService")
	
================================================================================
================================================================================

	DOCUMENTATIONS [DOC]

========================================

	SMACK Extensions: Multi User Chat
	http://www.igniterealtime.org/builds/smack/docs/latest/documentation/extensions/muc.html
	
	JZLib: http://www.jcraft.com/jzlib/
	
================================================================================
================================================================================

	JAVA Files [JAVA]

========================================

	Login.java: (ACTIVITY) main method, runs the Login demo:
		1. Creates a UI to receive user input for username and password
		2. When the sign in button is clicked, calls XMPPService
	
	XMPPService.java: (SERVICE) attempts to log in using the given username 
		and password, and logs out once verified:
		1. Creates a XMPP connection to jabber.org through port 5222
		2. Logs in as opencommss@jabber.org
		3. Verify the success of logging in
		4. Disconnects from the server
		
	NetworkServices.java: contains constants needed to run application
	
	XMPPUser.java: a pre-created XMPP user; its username and password is stored
	
	XMPPConnectConfig.java: creates a configured XMPP connection to a specified
		host server through a specified post

