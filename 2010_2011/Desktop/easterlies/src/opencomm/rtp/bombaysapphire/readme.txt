Risa Naka-- rn96 -- OpenComm Desktop Parallel RTP Sessions
Sprint 4: Bombay Sapphire -- Feb.25.2011


############################
############################

DemoRTP.java

	Version 1: takes the three computers in the Upson Media Lab 
	and runs 3 parallel RTP sessions
	
AudioConnect.java

	Version 1: stores the information (username and IP address) 
	of the audio receiver, audio sender, and the port used
	
AudioSender.java

	Version 1: creates a serversocket, accepts connection from client, 
	and sends audio
	
AudioReceiver.java

	Version 1: creates a client socket and sends max. 10 tcp connection requests.
	if the connection is accepted, receives audio.
	
UserRTP.java
	
	Version 1: contains username and IP address of user

############################
############################

Integration
	
	Stable integration method unavailable as of this time
	
############################
############################

Non-implemented changes

	- stable integration method
	- safe end and restart of connection between lines