== Network Spring 2011 == Risa Naka (rn96) == May 14, 2011 ==

================================================================================
================================================================================

Content:
    Set-up
    Connecting to Internet
    Services and AndroidManifest.xml
    
    Connecting via XMPP and Logging In
    Starting MUC Room Creation/Destruction
    Creating a MultiUserChat Room
    Sending MUC Room Invitations
    Accepting/rejecting MUC Room Invitation
    Granting Moderator Privilege to All Users
    Sending Messages to the Room
    Receiving Messages from the Room
    Leaving the Room
    Kicking Out Users from the Room
    Destroying a MultiUserChat Room
    Disconnecting via XMPP

================================================================================
================================================================================

    Set-up 
    
========================================

    Build path for ASMACK.jar and JZLib.jar files into the project
    
    Users and passwords:
    risan@jabber.org, reesaspbc176
    opencommss@jabber.org, ssopencomm
    mucopencomm@jabber.org, opencommmuc
    

================================================================================
================================================================================

    Connecting to Internet

========================================

    Under AndroidManifest.xml, give the application permission to access the internet:
    
    AndroidManifest.xml:
    <?xml version="1.0" encoding="utf-8"?>
        <manifest ...>
            <application ...>
            ...
            </application>
            <uses-sdk android:minSdkVersion="8" />
            <uses-permission android:name="android.permission.INTERNET" />
        </manifest> 


================================================================================
================================================================================

    Services and AndroidManifest.xml

========================================

    Whenever a service is created and is called, one must manuall include it in the AndroidManifest.xml. Also, 
    an intent-filter (specifies the types of intents that an activity, service, or broadcast receiver can respond to) 
    must be specified. In the example below, XMPPService is called whenever the intent's action is 
    edu.cornell.opencomm.network.ACTION_CONNECT and edu.cornell.opencomm.network.ACTION_LOGIN:
    
    <?xml version="1.0" encoding="utf-8"?>
        <manifest ...>
            <application ...>
                <activity ...>
                </activity>
            <service android:name=".XMPPService">
                <intent-filter>
                    <action android:name="edu.cornell.opencomm.network.ACTION_CONNECT" />
                    <action android:name="edu.cornell.opencomm.network.ACTION_LOGIN" />
                </intent-filter>
            </service>
            </application>
            <uses-sdk android:minSdkVersion="8" />
            <uses-permission android:name="android.permission.INTERNET" />
        </manifest> 

================================================================================
================================================================================

    Connecting via XMPP and Logging In

========================================

        In order to log into an account, we must first establish an XMPP connection to a specific host server 
        through a specific port.
        
        NetworkGUI.java:
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.main);
                
                // connect via XMPP to host server and port of choice
                Log.i(NetworkGUI.LOGTAG, "Establishing connection via XMPP:\n" + 
                        "host server: " + this.host + "\nport: " + this.port);
                Log.i(NetworkGUI.LOGTAG, "Log in:\n" + 
                        "username: " + this.username + "@" + this.host + "\npassword: " + this.password);
                Intent iLogin = new Intent(this, XMPPService.class);
                iLogin.putExtra(Networks.KEY_HOST, this.host);
                iLogin.putExtra(Networks.KEY_PORT, this.port);
                iLogin.putExtra(Networks.KEY_USERNAME, this.username);
                iLogin.putExtra(Networks.KEY_PASSWORD, this.password);
                iLogin.setAction(Networks.ACTION_LOGIN);
                startService(iLogin);

            }
        
        
        XMPPService.java:
            public int onStartCommand(Intent intent, int flags, int startId) {		
                // if connecting via XMPP to specific host and port
                if (Networks.ACTION_LOGIN.equals(intent.getAction())) {
                            // connect via XMPP
                }
                return startId;
            }

================================================================================
================================================================================

    Starting MUC Room Creation/Destruction

========================================

    When the user successfully connects and logs into the account, ther service calls the activity that contains 
    the GUI for creating and destroying MUC and adding and removing users.
    
    NOTE: the code currently sets a flag on the intent; during integration, one can incorprate user interaction by 
        toast notifications, notification bar notification, etc., as needed
        
    XMPPService.java:
        public int onStartCommand(Intent intent, int flags, int startId) {		
            // if connecting via XMPP to specific host and port
            if (Networks.ACTION_LOGIN.equals(intent.getAction())) {
                ...		
                // start creating/removing MUC rooms and users
                Intent iMUC = new Intent(this.getBaseContext(), MUCGUI.class);
                iMUC.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
                iMUC.putExtra(Networks.KEY_CONNECT, conn);
                iMUC.putExtra(Networks.KEY_USERNAME, username);
                iMUC.setAction(Networks.ACTION_START_MUC);
                startActivity(iMUC);
            }	
            // as we want the service to keep running until we explicitly stop it
            return START_STICKY;
        } // end onStartCommand method

================================================================================
================================================================================

    Creating a MultiUserChat Room

========================================

    To create a multiuser chat room, there must a be a valid XMPP Connection (an instance of XMPPConnParcel) 
    as well as a room name that has not yet been taken. An XMPPError is thrown if the room cannot be created 
    for whatever reason (it already exists, user doesn't have the ability to create rooms)
    MUCGUI.java:
        // To create a room
        MultiUserChat muc = new MultiUserChat(conn, roomName);
        try {
			muc.create(username);
		} catch (XMPPException e) {
			Log.e(MUCGUI.LOGTAG, "XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

Alternatively:
	-muc.join(username);
	 muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));

================================================================================
================================================================================

    Sending MUC Room Invitations

========================================

    Invite each person into a room using their JID (user@host.org) as well as a reason for the invitaiton. Method 
    invitationDeclined is called if the invitee(s) reject the invitation
    NOTE:
        invitees is a String array temporarily hard-coded into MUCGUI.java
        reason is a String temporarily hard-coded into MUCGUI.java
    
    MUCGUI.java:
        // Send an invitation
		for (String inv : invitees) {
			muc.invite(inv, reason);
		}
        
        public void invitationDeclined(String invitee, String reason) {
            Log.i(MUCGUI.LOGTAG, invitee + " rejected your invitation because " + reason);
        }
        
================================================================================
================================================================================

    Accepting/rejecting MUC Room Invitation

========================================

    When an invitation is sent by another user to join a MUC room, the method invitationReceived is called. 
    Depending on the user's selection (currently hard-coded inside MUCGUI.java under boolean acceptInvite), 
    it will either join the room or decline the invitation.
    
    MUCGUI.java:
        public void invitationReceived(Connection conn, String room,
                String inviter, String reason, String password, Message message) {
            // if accepting the invitation
            if (this.acceptInvite) {
                MultiUserChat muc = new MultiUserChat(conn, room);
                try {
                    muc.join(this.username, password);
                } catch (XMPPException e) {
                    ...
                }
                Log.i(MUCGUI.LOGTAG, this.username + " joined room " + room);
                
            }
            // if rejecting the invitation
            else {
                MultiUserChat.decline(conn, room, inviter, reason);
                Log.i(MUCGUI.LOGTAG, this.username + " declined invitation to room " + room);
            }
        } // end invitationReceived method
               

================================================================================
================================================================================

    Granting Moderator Privilege to All Users

========================================

    In order to grant moderator privilege to a user, its nickname is needed.
    
    MUCGUI.java:
        // Grant moderator access to all users
		try {
			Occupant[] members = (Occupant[]) muc.getParticipants().toArray();
			for (Occupant mem : members) {
				muc.grantModerator(mem.getNick());
			}
		} catch (XMPPException e1) {
			...
		}
    
    Various methods exist that can be called as the occupancy and status of the various 
    members in the room are called

================================================================================
================================================================================

    Granting Moderator Privilege to All Users

========================================

    In order to grant moderator privilege to a user, its nickname is needed.
    
    MUCGUI.java:
        // Grant moderator access to all users
		try {
			Occupant[] members = (Occupant[]) muc.getParticipants().toArray();
			for (Occupant mem : members) {
				muc.grantModerator(mem.getNick());
			}
		} catch (XMPPException e1) {
			...
		}
    
    Various methods exist that can be called as the occupancy and status of the various 
    members in the room are called

		// to send a message
		try {
			muc.sendMessage("Hello");
		} catch (XMPPException e) {
			Log.e(MUCGUI.LOGTAG, "XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

================================================================================
================================================================================

    Sending Messages to the Room

========================================

    MUCGUI.java:
		// to send a message
		try {
			muc.sendMessage("Hello");
		} catch (XMPPException e) {
			...
		}

================================================================================
================================================================================

    Receiving messages from the Room

========================================

    processMessage is called when a message is received from the MUC
    
    MUCGUI.java:
        public void processMessage(Chat chat, Message message) {
            String from = StringUtils.parseResource(message.getFrom());
            Log.i(MUCGUI.LOGTAG, "[" + df.format(new Date()) + "]" +
                    from + ": " + message.getBody());
        } // end processMessage method

================================================================================
================================================================================

    Leaving the Room

========================================

    MUCGUI.java:
        muc.leave();
    
================================================================================
================================================================================

    Kicking Out Users from a Room

========================================

    One can kick out any user in the room as long as they are not a moderator or administrator and said person 
    has the authority to do so. The kicked participant will receive a notification regarding the kick
    
    MUCGUI.java:
		// Kick users out of the room
		try {
			Occupant[] members = (Occupant[]) muc.getParticipants().toArray();
			for (Occupant mem : members) {
				muc.kickParticipant(mem.getNick(), reason);
			}
		} catch (XMPPException e) {
			...
		}	

================================================================================
================================================================================

    Destroying a MultiUserChat Room

========================================

    To create a multiuser chat room, one need only use the destroy function along with a reason why the room is 
    destroyed. If there is an alternate location, replace null with the Jabber ID of the alternate locaiton. If the user 
    is not the creator of the room, it cannot destroy the room. When the room is destroyed, the other users are 
    notified.
    
    MUCGUI.java:
		// To destroy a room
		try {
			muc.destroy(this.reason, null);
		} catch (XMPPException e) {
			Log.e(MUCGUI.LOGTAG, "XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
        
================================================================================
================================================================================

    Disconnecting via XMPP

========================================

    Disconnect from the host server by ending the service that established the connection:
    
    NetworkGUI.java
        // Disconnect from the host server
        stopService(iConnect);
        Log.i(NetworkGUI.LOGTAG, "Disconnected from host server");   

    
    XMPPService.java
    public void onDestroy() {
        ...
    }

================================================================================
================================================================================
