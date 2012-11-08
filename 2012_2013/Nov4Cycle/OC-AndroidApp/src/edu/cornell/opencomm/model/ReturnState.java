package edu.cornell.opencomm.model;

/**
 * The enum defining the opcodes for Login Task:
 * <ul>
 * <li>SUCCEEDED - app successfully connected to the server and signed in as 
 * the given pair (email, password), which represents a valid user</li>
 * <li>COULDNT_CONNECT - app could not connect to the server</li>
 * <li>WRONG_PASSWORD - app could connect to the server, but the given pair 
 * was invalid</li>
 * </ul>
 */
public enum ReturnState {
	SUCCEEDED, COULDNT_CONNECT, WRONG_PASSWORD
};
