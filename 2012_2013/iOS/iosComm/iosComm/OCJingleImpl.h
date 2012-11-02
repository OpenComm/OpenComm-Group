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
    OCJingleConstantsController *jingleConstants; //contains all the string constants
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
    
}

/**For implementation details / specs, check the .m file**/

/**Init the object with YOUR OWN JID**/
- (id) initWithJID: (XMPPJID *)JIDParam;

/**Send a session initiate to JIDto. The port you will be listening on is at portNum**/
- (NSXMLElement *) jingleSessionInitiateTo: (XMPPJID *)JIDto recvportnum: (uint16_t)portNum SID: (NSString *)sid;

/**Given a packet, it spits out an ACK packet that you need to send back upon immediately receiving it**/
- (NSXMLElement *) jingleAckForPacket: (XMPPIQ *)recvPacket;

/**Process a received ACK packet**/
- (void) jingleReceiveAck;

/**Send a session accept to the session initiate packet from jingleIQPacket**/
- (NSXMLElement *) jingleRespondSessionAcceptFromPacket: (XMPPIQ *)jingleIQPacket recvportnum: (uint16_t)portNum SID: (NSString *)sid;

/**Process session accept in response to your sent session initiate packet. The accept packet is jingleIQPacket**/
- (void) jingleReceiveSessionAcceptFromPacket: (XMPPIQ *)jingleIQPacket;

/**Terminate the current session**/
- (NSXMLElement *) jingleSessionTerminate;

/**Process a received session-terminate packet**/
- (void) jingleReceiveSessionTerminate;

//TODO Support Application and Transport functions.
//This is just stubbed to true on the android side.
//- (bool) supportedFunctionalty: (XMPPIQ *)jingleIQPacket;

@end
