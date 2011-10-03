== Kris Kooi (kpk47) == XMPP MultiUserChat Demo == README.TXT == 9/28/2011

	Set up [SETUP]
	Documentations [DOC]
	JAVA Files [JAVA]
	Version Information [VER]
	Changes to Implement [CHANGE]

================================================================================
================================================================================

	SET UP [SETUP]
	
========================================

	1. Run program. It will automatically login to opencommsec@jabber.org and
	enter the chatroom "XMPPMultiUserChatDemoAndroidKris@conference.jabber.org".
	2. To invite or kick users from the chat, type their username and server
	(ie "sample@sample.com") into the field and click the appropriate button.
	3. To leave the chat or logout, click the appropriate button.
	
================================================================================
================================================================================

	DOCUMENTATIONS [DOC]

========================================

	Smack Documentation:
	http://www.igniterealtime.org/builds/smack/docs/latest/documentation/
	
================================================================================
================================================================================

	JAVA Files [JAVA]

========================================

	XMPPMUCDemo_AndroidActivity
	1. onCreate() sets up all views and adapters used. Then connects to 
	server as "opencommsec" and joins multi-user chat 	"XMPPMultiUserChatDemoAndroidKris@conference.jabber.org". Sends and
	receives packets to allow messages to be sent and received.

================================================================================
================================================================================

	VERSION INFORMATION [VER]
	
========================================

	1.0: Initial release 

================================================================================
================================================================================

	CHANGES TO BE IMPLEMENTED [CHANGE]
	
========================================

	1. The messages window does not always update in real time. An update can
	be forced by clicking the "Send" button.
	2. The "Chat Members" textview does not populate or update properly.
			