== Annie Edmundson (ae243) == XMPP Multiuser Chat Demo == README.TXT ==

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

	2. Download and unzip Smack (http://www.igniterealtime.org/downloads/index.jsp#smack)

	3. In Eclipse, right-click on the project:
	Build Path -> Add External Archives
	Add both smack.jar and smackx.jar

	4. Run MultiUserChatDemo.java

================================================================================
================================================================================

	DOCUMENTATIONS [DOC]

========================================

	Smack:
	http://www.igniterealtime.org/builds/smack/docs/latest/documentation/

================================================================================
================================================================================

	JAVA Files [JAVA]

========================================

	MultiUserChatDemo.java:
		-Establishes connection to jabber.org
		-Logs in with username: opencommsec and password: secopencomm
		-Creates a MultiUserChat
		-Prints the current roster every 30 seconds

================================================================================
================================================================================

	VERSION INFORMATION [VER]

========================================

	1.0: Demo

================================================================================
================================================================================

	CHANGES TO BE IMPLEMENTED [CHANGE]

========================================

	Adapt to Android platform.