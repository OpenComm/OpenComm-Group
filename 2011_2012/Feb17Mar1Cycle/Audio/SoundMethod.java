/** ==================================================

	SOUND METHODS (RN96)
	
=================================================== */

/** MUCBuddy to be converted into JingleUser stored within User object */

/** 1. Configure providers - XMPPClientSettings.java */
	private void configureProviders(){
    	ProviderManager pm = ProviderManager.getInstance();
    	if (pm != null){
    		Log.i(XMPPClientLogger.TAG, "Got ProviderManager!!");
    		
    		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user", new MUCUserProvider());
    	} else {
    		Log.e(XMPPClientLogger.TAG, "ProviderManager is null!!!");
    	}
    }
	
/** 2. Connect and log in - XMPPClientSettings.java */
	ConnectionConfiguration connConfig =
                new ConnectionConfiguration(host, Integer.parseInt(port));
        XMPPConnection connection = new XMPPConnection(connConfig);
		connection.connect();

		
/** 3. Set SASL authentication - XMPPClientSettings.java */
    SASLAuthentication.supportSASLMechanism("PLAIN", 0);
        	
            
/** 4. Log in - XMPPClientSettings.java */
	connection.login(username, password);

/** 5. Set status to available - XMPPClientSettings.java */
	Presence presence = new Presence(Presence.Type.available);
	connection.sendPacket(presence);
            
/** 6. Create room with name FQRoomName with user nickname nickname - XMPPRoomCreateDialog.java */
	MultiUserChat muc = new MultiUserChat(xmppClient.getConnection(), FQRoomName);
	muc.create(nickname);
    muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
    xmppClient.setMUC(muc);
	
/** 7. Invite users (e.g. hecate@shakespeare.lit) to room - XMPPInviteDialog.java */
	muc.invite(userid, inviteMsg);
	
/** INVITED PERSON  */
/** 0. Add Invitation Listener - XMPPClient.java */
	MultiUserChat.addInvitationListener(connection, new InvitationListener() {        		
		public void invitationReceived(Connection conn, String room,
				String inviter, String reason, String password,
				Message message) {
			invitationRecvdDialog.setReceivedParameters(room, inviter, reason, password);
			Log.i("TestXMPPCLient", "Invite received from: " + inviter);
			mHandler.post(new Runnable() {
				public void run() {	                    	
					invitationRecvdDialog.show();
				}
			});
			
		}
	});
/** 1. Join the room using nickname */
	muc.join(nickname, password);
	
/** 2. Set Jingle with all users in the room */
	ArrayList<String> occupantsList = new ArrayList<String>();
	occupantsList.add(inviter);
	Iterator<String> occupants = occupantsList.iterator();
	
	// Initiate Jingle session with all members of the chat room.
	if(occupants == null || !occupants.hasNext()){
		Log.i("XMPPClient", "MUC has no occupants!!");
	} else {
		while(occupants.hasNext()){
			String occupant = occupants.next();
			// Send session-initiate per person (not per room!)
			buddy.getJiqActionMessageSender().sendSessionInitiate(xmppClient.getLoggedInJID(), occupant, buddy);
			buddy.getSessionState().changeSessionState(JingleIQPacket.AttributeActionValues.SESSION_INITIATE);
		}
	}						

/** Include JingleIQActionMessages.java */

/** ======================================

SOUND SPATIALIZATION

======================================= */

/** Called in ReceiverThread; instance of SoundSpatializer object stored within JingleUser */
/** set and play spatialized sound: Line 160 in ReceiverThread.java */
/** Throughout ReceiverThread.java, alter the length of write based on ITD addition of bytes */
rtp_socket.receive(rtp_packet);
	if (timeout != 0) { //normal receipt of packet
		tg.stopTone();
		track.pause();
		/** WRITE IN SPATIALIZED SOUND instead of lin2 */
		user += track.write(lin2,0,BUFFER_SIZE);
		user += track.write(lin2,0,BUFFER_SIZE);
		track.play();
		cnt += 2*BUFFER_SIZE;
		empty();


