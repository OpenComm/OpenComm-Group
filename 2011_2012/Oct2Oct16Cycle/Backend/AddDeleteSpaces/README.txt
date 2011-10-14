== Kris Kooi (kpk47) == Add/Delete Spaces == README.TXT == 10/13/2011

	Set up [SETUP]
	Documentations [DOC]
	JAVA Files [JAVA]
	Version Information [VER]
	Changes to Implement [CHANGE]

================================================================================
================================================================================

	SET UP [SETUP]
	
========================================

	1. Run through Android emulator
	2. To create a space, type the space name into the TextEdit and 
	click "Add Space"
	3. To delete a space, type the space name into the TextEdit and 
	click "Delete Space"
	
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

	1. AddDeleteActivity.java - Main activity class
	- Creates a GUI for debugging
	- Logs into jabber server as "opencommsec@jabber.org"
	- Initializes an ArrayList to hold all Spaces in use
	- creates or deletes spaces, including destroying the MUC they 	contain 
	and calling super.finalize() to clean up the Space objects

	2. Space.java - class to wrap MUC objects
	- contains a MUC and the information sent to the server to create it
	- Contains methods to create and destroy MUC
	
================================================================================
================================================================================

	VERSION INFORMATION [VER]
	
========================================

	1.0: Initial release 
		NOTES: Debug code left in and dummied out for ease of integration

================================================================================
================================================================================

	CHANGES TO BE IMPLEMENTED [CHANGE]
	
========================================

	- Pass in Connection from service/handler rather than create one
	- Pass in spaceIDs for delete from GUI/handler
	- Randomly generate spaceIDs for creating spaces (java.util.Random?) or 
	pass in from GUI/handler
	- Should we need to use custom configuration for MUCs, we will need to 
	find a way around a bug in asmack's Form class (form.createAnswerForm() 
	throws NullPointerException from source)