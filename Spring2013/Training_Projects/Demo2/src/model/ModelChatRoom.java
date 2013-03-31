package model;

import java.util.*;

import network.NetConnection;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class ModelChatRoom {
	public static final String DEFAULT_HOSTNAME = "@cuopencomm";
	MultiUserChat muc;
	ArrayList<MultiUserChat> musl = new ArrayList<MultiUserChat>();
	XMPPConnection conn;
	public ModelChatRoom()
	{
		this.conn = NetConnection.conn;
	}
	public boolean makeRoom(String rName)
	{
		muc = new MultiUserChat(conn, rName+DEFAULT_HOSTNAME);
		musl.add(muc);
		try
		{
			muc.join("NICKNAME");
		}
		catch(XMPPException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public void banUser(String jid, String reason)
	{
		try 
		{
			muc.banUser(jid, reason);
		} 
		catch (XMPPException e) 
		{
			e.printStackTrace();
		}
	}
	public void inviteUser(String jid, String reason)
	{
		muc.invite(jid, reason);
	}
	public String printUserList()
	{
		Collection<Affiliate> memList;
		try {
			memList = muc.getMembers();
		} catch (XMPPException e) {
			e.printStackTrace();
			return "memList cannot be created";
		}
		String memIDs="";
		for(Affiliate a : memList)
		{
			memIDs += (a.getJid() +" / ");
		}
		return memIDs;
	}
	public boolean sendMessage(String mes)
	{
		try
		{
			muc.sendMessage(mes);
			return true; //if successful
		}
		catch(XMPPException e)
		{
			return false; // if unsuccessful
		}
	}
	public MultiUserChat fetchRoom(String rName)
	{
		//we may fetch a room according to the rName
		return muc;
	}
	
	
	
}
