//
//  OCJingleImpl.h
//  iosComm
//
//  Created by Kevin Chen on 10/31/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "XMPPFramework.h"
#import "OCJingleConstantsController.h"

/**
 * This class is an object that handles Jingle IQ Packets and creates them.
 * Also contains the state for the current Jingle Session
 */
@interface OCJingleImpl : NSObject {
    //OCJingleConstantsController *jingleConstants;
    NSString *state; //PENDING, ACTIVE, or ENDED as defined in jingleConstants
    XMPPJID *myJID;
    XMPPJID *toJID;
    NSString *myIPAddress;
    NSString *toIPAddress;
    NSString *myPort;
    NSString *toPort;
    NSString *mySID; //CURRENT SESSION ID
    NSString *toSID;
    //NSString *currentIQID; //Current IQ ID that you are waiting an ACK for.
    bool pendingAck; //true if you are waiting for an ack of a packet you sent.
    //TODO this class should include whatever socket we'll be using.
    
    XMPPStream *xmppStream;
    
}

//contains all the string constants
@property (strong, nonatomic) OCJingleConstantsController *jingleConstants;

/**For implementation details / specs, check the .m file**/

/**Init the object with YOUR OWN JID and xmpp stream to the server**/
- (id) initWithJID: (XMPPJID *)JIDParam xmppStream: (XMPPStream *)xmppStreamParam;

- (NSString *)getIPAddress;

/**Send a session initiate to JIDto. The port you will be listening on is at portNum. The JIDto must have a FULL JID (including resource). This can be found via presence. Only needed for session-initiate.**/
- (NSXMLElement *) jingleSessionInitiateTo: (XMPPJID *)JIDto recvportnum: (uint16_t)portNum SID: (NSString *)sid;

/**Given a packet, it spits out an ACK packet that you need to send back upon immediately receiving it**/
- (NSXMLElement *) jingleAckForPacket: (XMPPIQ *)recvPacket;

/**Process a received ACK packet. Returns true if expecting an ack from this person.**/
- (bool) jingleReceiveAck: (XMPPIQ *) ackPacket;

/**Send a session accept to the session initiate packet from jingleIQPacket**/
- (NSXMLElement *) jingleRespondSessionAcceptFromPacket: (XMPPIQ *)jingleIQPacket recvportnum: (uint16_t)portNum SID: (NSString *)sid;

/**Process session accept in response to your sent session initiate packet. The accept packet is jingleIQPacket. Returns true if the packet is meant for you and is processed.**/
- (bool) jingleReceiveSessionAcceptFromPacket: (XMPPIQ *)jingleIQPacket;

/**Create a session-terminate packet with the given reason to the sender of the IQPacket
 **Possible reasons are listed in the .plist: Success or Decline are supported for now**/
- (NSXMLElement *) jingleSessionTerminateWithReason: (NSString *)reason inResponseTo: (XMPPIQ *)IQPacket;

/**Process a received session-terminate packet**/
- (bool) jingleReceiveSessionTerminateFromPacket: (XMPPIQ *)jingleIQPacket;

/**Returns whether the given packet is a jingle packet**/
- (bool) isJinglePacket: (XMPPIQ *)IQPacket;

/**Returns whether the given packet is an ACK packet**/
- (bool) isAckPacket: (XMPPIQ *)IQPacket;

/**Returns whether the given packet is of the given action (session-initiate, session-accept, session-terminate). These actions are listed in the constants controller.
    IT ASSUMES THE PACKET IS A JINGLE PACKET! check isJinglePacket first before calling this func**/
- (bool) isJinglePacket: (XMPPIQ *)JingleIQPacket OfAction: (NSString *)Action;

/**General function that contains the logic of changing state / calling the appropriate function
 **Based on jingle standards. Basically contains Jingle Application Flow.
 **It returns true if the packet is successfully handled.
 **False if the packet is not meant for Jingle processing.**/
- (bool) processPacketForJingle: (XMPPIQ *)IQPacket;

/**For debug purposes, prints the state of the jingle object in NSLog**/
- (void) printJingleObject;

//TODO Support Application and Transport functions.
//This is just stubbed to true on the android side.
//If I were to implement this, it would return nil if everything is supported
//Or return the appropriate error string defined by XMPP Jingle if something is not supported.
//- (NSString *) supportedFunctionalty: (XMPPIQ *)jingleIQPacket;
@end
