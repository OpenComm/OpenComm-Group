package edu.cornell.opencomm;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class Space {

	private MultiUserChat space;
	private boolean isMainSpace;
	private Connection conn;
	private String spaceID;

	/**
	 * 
	 * @param conn
	 *            - Connection passed fro AddDeleteActivity
	 * @param isMainSpace
	 *            - whether or not this is the main space
	 * @param spaceID
	 *            - the name of the MUC
	 */
	public Space(Connection conn, boolean isMainSpace, String spaceID) {
		this.isMainSpace = isMainSpace;
		this.conn = conn;
		this.spaceID = spaceID;
		if (isMainSpace)
			space = new MultiUserChat(this.conn, this.spaceID
					+ "@conference.jabber.org");
		else
			space = new MultiUserChat(this.conn, this.spaceID
					+ "@conference.jabber.org");
	}

	/**
	 * Creates MUC, joins as owner, sumbits default configuration form.
	 * 
	 * @param chatName
	 * @throws XMPPException
	 */
	public void join(String chatName) throws XMPPException {

		// Create MUC
		space.join(chatName);

		// Configure MUC
		space.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));

		/*
		 * NOTE: There seems to be a bug in asmack with custom MUC
		 * configuration. This code works on desktop, but throws a
		 * NullPointerException in android.
		 */
		
		/*
		Form form = space.getConfigurationForm();
		Form submitForm = form.createAnswerForm();
		for (Iterator<FormField> fields = form.getFields(); fields.hasNext();) {
			FormField field = (FormField) fields.next();
			if (!FormField.TYPE_HIDDEN.equals(field.getType())
					&& field.getVariable() != null) {
				submitForm.setDefaultAnswer(field.getVariable());
			}
		}

		// Set self as owner of room
		submitForm.setAnswer("space#roomconfig_roomowner",
				"opencommsec@jabber.org"); // Submit configuration form
		space.sendConfigurationForm(submitForm);
	*/
	}

	/**
	 * Calls server to destroy MUC, then cleans up the Space object. Won't
	 * destroy the main space.
	 * 
	 * @throws Throwable
	 *             - if there's a problem destroying the Space
	 */
	public void destroy() throws Throwable {
		if (!isMainSpace) {
			space.destroy("", space.getRoom());
			// DEBUG: Log.v("STRING", space.getRoom());

			// Clean up Space --just in case
			super.finalize();
		}
	}

	// GETTERS

	public String toString() {
		return space.getRoom();
	}

	public boolean isMainSpace() {
		return isMainSpace;
	}

	public MultiUserChat getMUC() {
		return space;
	}

	public String getSpaceId() {
		return spaceID;
	}

	public Connection getConnection() {
		return conn;
	}
}
