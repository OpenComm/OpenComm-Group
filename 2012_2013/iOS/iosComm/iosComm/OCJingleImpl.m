//
//  OCJingleImpl.m
//  iosComm
//
//  Created by Kevin Chen on 10/31/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCJingleImpl.h"
//TODO these includes are for GetIPAddress
#include <ifaddrs.h>
#include <arpa/inet.h>

//TODO ACK IQ messages that return with the ID of the given request packet? id's are unused by android though.
//TODO check what happens if we get two IQ session-initiates at the same time...
//Delegates are ALWAYS INVOKED Asynchronously!!!

@implementation OCJingleImpl

//-------------------------------------------------------------------
// TODO: This function has been c/p-ed here. It belongs in its own class probably
// From: http://stackoverflow.com/questions/6807788/how-to-get-ip-address-of-iphone-programatically
// Returns your own IP Address.
//-------------------------------------------------------------------
- (NSString *)getIPAddress {
    
    NSString *address = @"error";
    struct ifaddrs *interfaces = NULL;
    struct ifaddrs *temp_addr = NULL;
    int success = 0;
    // retrieve the current interfaces - returns 0 on success
    success = getifaddrs(&interfaces);
    if (success == 0) {
        // Loop through linked list of interfaces
        temp_addr = interfaces;
        while(temp_addr != NULL) {
            if(temp_addr->ifa_addr->sa_family == AF_INET) {
                // Check if interface is en0 which is the wifi connection on the iPhone
                if([[NSString stringWithUTF8String:temp_addr->ifa_name] isEqualToString:@"en0"]) {
                    // Get NSString from C String
                    address = [NSString stringWithUTF8String:inet_ntoa(((struct sockaddr_in *)temp_addr->ifa_addr)->sin_addr)];
                    
                }
                
            }
            
            temp_addr = temp_addr->ifa_next;
        }
    }
    // Free memory
    freeifaddrs(interfaces);
    return address;
    
}


//-------------------------------------------------------------------
// Creates the OCJingleImpl object. State is initially at ended. Initializes constants
//-------------------------------------------------------------------
- (id)initWithJID: (XMPPJID *)JIDParam {
    self = [super init];
    if (self) {
        jingleConstants = [[OCJingleConstantsController alloc] init];
        state = [jingleConstants STATE_ENDED];
        myJID = JIDParam;
        toJID = nil;
        myIPAddress = [self getIPAddress];
        toIPAddress = nil;
        myPort = nil;
        toPort = nil;
        mySID = nil;
        toSID = nil;
        //currentIQID = nil;
        pendingAck = false;
    }
    return self;
}

/********************************* HELPER FUNCTIONS TO MAKE JINGLE PACKETS *****************************/

//-------------------------------------------------------------------
// Returns an IQ base NSXML Element suitable for Jingle
// Used to initialize the Beginning layer of the Jingle Packet.
// The IQ packet has attributes 'from', 'id', 'to', and 'type.
// id is unused by android, so we do not add it here as well!*/
//-------------------------------------------------------------------
- (NSXMLElement *) createBaseIQElement: (XMPPJID *)JIDTo JingleElement: (NSXMLElement *) jingle {
    NSXMLElement *IQElement = (NSXMLElement *)[XMPPIQ iqWithType: [jingleConstants IQ_TYPE_SET] to: JIDTo];
    [IQElement addAttributeWithName: [jingleConstants ATTRIBUTE_FROM] stringValue: [myJID full]];
    [IQElement addChild: jingle];
    return IQElement;
}

//-------------------------------------------------------------------
// Returns a Jingle NSXML Element, child of IQ
// if SID is nil, a new random one is created.
// if initiator (or responder respectively) is nil, the attributes are not added.
// child is either content or reason
// jingle has attributes xmlns, action, initiator, responder, and SID. */
//-------------------------------------------------------------------
- (NSXMLElement *) createJingleElementWithAction: (NSString *)action SID: (NSString *)SID
Initiator: (NSString *)initiator Responder: (NSString *)responder childElement: (NSXMLElement *) child {
    NSXMLElement *jingleElement = [NSXMLElement elementWithName: [jingleConstants JINGLE_ELEMENT_NAME]];
    [jingleElement addAttributeWithName: [jingleConstants ATTRIBUTE_XMLNS]
                            stringValue: [jingleConstants ATTRIBUTE_XMLNS_JINGLE_VALUE]];
    [jingleElement addAttributeWithName: [jingleConstants ATTRIBUTE_ACTION]
                            stringValue: action];
    if (SID == nil) {
        mySID = [[NSUUID UUID] UUIDString];
        [jingleElement addAttributeWithName: [jingleConstants ATTRIBUTE_SID]
                                stringValue: mySID];
    }
    else {
        mySID = SID;
        [jingleElement addAttributeWithName: [jingleConstants ATTRIBUTE_SID]
                                stringValue: mySID];
    }
    if (initiator != nil) {
        [jingleElement addAttributeWithName: [jingleConstants ATTRIBUTE_INITIATOR]
                                stringValue: initiator];
    }
    if (responder != nil) {
        [jingleElement addAttributeWithName: [jingleConstants ATTRIBUTE_RESPONDER]
                                stringValue: responder];
    }
    [jingleElement addChild: child];
    return jingleElement;
}

//-------------------------------------------------------------------
// Returns a Content NSXML Element, child of Jingle
// Description and Transport are children of Content.
// content has attributes creator, name, and senders -- stubbed for OpenComm based on android
//-------------------------------------------------------------------
- (NSXMLElement *) createContentElementWithDescription: (NSXMLElement *) description
                                             Transport: (NSXMLElement *) transport {
    NSXMLElement *contentElement = [NSXMLElement elementWithName: [jingleConstants CONTENT_ELEMENT_NAME]];
    [contentElement addAttributeWithName: [jingleConstants ATTRIBUTE_CREATOR]
                             stringValue: [jingleConstants ATTRIBUTE_INITIATOR]];
    [contentElement addAttributeWithName: [jingleConstants ATTRIBUTE_NAME]
                             stringValue: [jingleConstants ATTRIBUTE_NAME_VALUE_AUDIOCONTENT]];
    [contentElement addAttributeWithName: [jingleConstants ATTRIBUTE_SENDERS]
                             stringValue: [jingleConstants ATTRIBUTE_SENDERS_VALUE_BOTH]];
    
    [contentElement addChild: description];
    [contentElement addChild: transport];
    return contentElement;
    
}

//-------------------------------------------------------------------
// Returns a Reason NSXML Element, child of Jingle
// ASSUMPTION: the reason is "success", meaning it is user terminated
//   or "decline", meaning the remote point declined to start a session.
// reason must be a valid element name that belongs under the Reason element
//-------------------------------------------------------------------
- (NSXMLElement *) createReasonElementWithReason: (NSString *) reason {
    NSXMLElement *child = [NSXMLElement elementWithName: reason];
    NSXMLElement *reasonElement = [NSXMLElement elementWithName: [jingleConstants REASON_ELEMENT_NAME]];
    [reasonElement addChild: child];
    return reasonElement;
}

//-------------------------------------------------------------------
// Returns a Description NSXML Element, child of Content.
// The description element will contain two children of "payload-type".
// description has attributes xmlns and media -- stubbed for OpenComm based on android
// payload-type has attributes id, name, clockrate, and channels (clockrate and channels unused)
//    -- also stubbed for OpenComm based on android
//-------------------------------------------------------------------
- (NSXMLElement *) createDescriptionAndPayloadElements {
    NSXMLElement *descriptionElement = [NSXMLElement elementWithName: [jingleConstants DESCRIPTION_ELEMENT_NAME]];
    [descriptionElement addAttributeWithName: [jingleConstants ATTRIBUTE_XMLNS]
                                 stringValue: [jingleConstants ATTRIBUTE_XMLNS_DESCRIPTION_VALUE]];
    [descriptionElement addAttributeWithName: [jingleConstants ATTRIBUTE_MEDIA]
                                 stringValue: [jingleConstants ATTRIBUTE_MEDIA_VALUE_AUDIO]];
    
    //first payload type is for PCMU
    NSXMLElement *payloadElementOne = [NSXMLElement elementWithName: [jingleConstants PAYLOAD_TYPE_ELEMENT_NAME]];
    [payloadElementOne addAttributeWithName: [jingleConstants ATTRIBUTE_ID]
                                stringValue: [jingleConstants ATTRIBUTE_ID_VALUE_PCMU]];
    [payloadElementOne addAttributeWithName: [jingleConstants ATTRIBUTE_NAME]
                                stringValue: [jingleConstants ATTRIBUTE_NAME_VALUE_PCMU]];
    
    //second payload type is for PCMA
    NSXMLElement *payloadElementTwo = [NSXMLElement elementWithName: [jingleConstants PAYLOAD_TYPE_ELEMENT_NAME]];
    [payloadElementTwo addAttributeWithName: [jingleConstants ATTRIBUTE_ID]
                                stringValue: [jingleConstants ATTRIBUTE_ID_VALUE_PCMA]];
    [payloadElementTwo addAttributeWithName: [jingleConstants ATTRIBUTE_NAME]
                                stringValue: [jingleConstants ATTRIBUTE_NAME_VALUE_PCMA]];
    
    [descriptionElement addChild: payloadElementOne];
    [descriptionElement addChild: payloadElementTwo];
    return descriptionElement;
}

//-------------------------------------------------------------------
// Returns a Transport NSXML Element, child of Content.
// The transport element will contain one child "remote-candidate"
// transport has attributes xmlns, pwd, and ufrag (pwd and ufrag unused because we are using RAWUDP)
//   -- stubbed for OpenComm based on android for RAWUDP
// remote-candidate has attributes compontent, foundation, generation, id, ip, network, port,
// priority, protocol, reladdr, relport, and type (foundation, network, priority, protocl, reladdr, relport,
// type unused) -- stubbed for OpenComm based on android for RAWUDP
//-------------------------------------------------------------------
- (NSXMLElement *) createTransportAndRemoteCandidateElementsWithIP: (NSString *)ip Port: (NSString *)port Action: (NSString *)action {
    NSXMLElement *transportElement = [NSXMLElement elementWithName: [jingleConstants TRANSPORT_ELEMENT_NAME]];
    [transportElement addAttributeWithName: [jingleConstants ATTRIBUTE_XMLNS]
                               stringValue: [jingleConstants ATTRIBUTE_XMLNS_TRANSPORT_VALUE_RAWUDP]];
    
    NSXMLElement *remoteCandidateElement = [NSXMLElement elementWithName: [jingleConstants REMOTE_CANDIDATE_ELEMENT_NAME]];
    [remoteCandidateElement addAttributeWithName: [jingleConstants ATTRIBUTE_COMPONENT]
                                     stringValue: [jingleConstants ATTRIBUTE_COMPONENT_VALUE]];
    [remoteCandidateElement addAttributeWithName: [jingleConstants ATTRIBUTE_GENERATION]
                                     stringValue: [jingleConstants ATTRIBUTE_GENERATION_VALUE]];
    
    if ([action isEqualToString: [jingleConstants SESSION_INITIATE]]) {
        [remoteCandidateElement addAttributeWithName: [jingleConstants ATTRIBUTE_ID]
                                         stringValue: [jingleConstants ATTRIBUTE_ID_CANDIDATE_VALUE_INITIATE]];
    }
    else if ([action isEqualToString: [jingleConstants SESSION_ACCEPT]]) {
        [remoteCandidateElement addAttributeWithName: [jingleConstants ATTRIBUTE_ID]
                                         stringValue: [jingleConstants ATTRIBUTE_ID_CANDIDATE_VALUE_ACCEPT]];
    }
    
    //TODO should we make the port here?
    [remoteCandidateElement addAttributeWithName: [jingleConstants ATTRIBUTE_IP]
                                     stringValue: ip];
    [remoteCandidateElement addAttributeWithName: [jingleConstants ATTRIBUTE_PORT]
                                     stringValue: port];
    
    [transportElement addChild: remoteCandidateElement];
    return transportElement;
}

/******************************* END HELPER FUNCTIONS TO MAKE JINGLE PACKETS ***************************/

//-------------------------------------------------------------------
// Returns a Jingle IQ start session packet for sending to the server to user JIDTo
// or nil if state != ended.
// SID can be a user provided SID or nil.
// Also updates the state to pending.
//-------------------------------------------------------------------
- (NSXMLElement *) jingleSessionInitiateTo: (XMPPJID *)JIDTo
                               recvportnum: (uint16_t)portNum
                                       SID: (NSString *)sid {
    if (![state isEqualToString: [jingleConstants STATE_ENDED]]) {
        return nil;
    }
    toJID = JIDTo;
    
    /**Create all the elements, hook them all together and return the finished packet**/
    NSXMLElement *descriptionElement = [self createDescriptionAndPayloadElements];
    NSXMLElement *transportElement = [self createTransportAndRemoteCandidateElementsWithIP: myIPAddress Port:[NSString stringWithFormat: @"%d", portNum] Action: [jingleConstants SESSION_INITIATE]];
    NSXMLElement *contentElement = [self createContentElementWithDescription: descriptionElement
                                                                   Transport:transportElement];
    //i'm the initiator, the responder is the other person
    NSXMLElement *jingleElement = [self createJingleElementWithAction: [jingleConstants SESSION_INITIATE] SID: sid Initiator: [myJID full] Responder: [toJID full] childElement: contentElement];
    pendingAck = true;
    state = [jingleConstants STATE_PENDING];
    return [self createBaseIQElement: toJID JingleElement: jingleElement];
}

//-------------------------------------------------------------------
// Returns a Jingle Ack packet for the received packet.
// This is merely returning an IQ packet to them with type 'result'
//-------------------------------------------------------------------
- (NSXMLElement *) jingleAckForPacket: (XMPPIQ *)recvPacket {
    NSXMLElement *ack = (NSXMLElement *) [XMPPIQ iqWithType: [jingleConstants IQ_TYPE_RESULT] to:[recvPacket from]];
    [ack addAttributeWithName: [jingleConstants ATTRIBUTE_FROM] stringValue: [myJID full]];
    return ack;
}

//-------------------------------------------------------------------
// Process a received ack.
// Special rules depending on which state you are in.
//-------------------------------------------------------------------
- (void) jingleReceiveAck {
    //If you receive an ack after you sent a session accept, your state changes to active
    if ([state isEqualToString: [jingleConstants STATE_PENDING]] && toIPAddress != nil) {
        state = [jingleConstants STATE_ACTIVE];
    }
}

/******************************** HELPER FUNCTIONS TO PROCESS JINGLE PACKETS *****************************/

//-------------------------------------------------------------------
// Assuming that the packet is a valid Jingle packet,
// Get the remote information and save it in this object.
// This includes toJID, IP, port, and remote SID
//-------------------------------------------------------------------
- (void) getRemoteInformationFromPacket: (XMPPIQ *)jingleIQPacket {
    //SID info in the jingleElement
    toJID = [XMPPJID jidWithString:[jingleIQPacket attributeStringValueForName: [jingleConstants ATTRIBUTE_FROM]]];
    NSXMLElement *jingleElement = [jingleIQPacket childElement];
    toSID = [jingleElement attributeStringValueForName: [jingleConstants ATTRIBUTE_SID]];
    
    //remote IP and remote Port in the remoteCandidate element
    NSXMLElement *remoteCandidateElement =
    [[jingleElement elementForName: [jingleConstants TRANSPORT_ELEMENT_NAME]]
                    elementForName: [jingleConstants REMOTE_CANDIDATE_ELEMENT_NAME]];
    toIPAddress = [remoteCandidateElement attributeStringValueForName: [jingleConstants ATTRIBUTE_IP]];
    toPort = [remoteCandidateElement attributeStringValueForName: [jingleConstants ATTRIBUTE_PORT]];

}

/****************************** END HELPER FUNCTIONS TO PROCESS JINGLE PACKETS ***************************/

//-------------------------------------------------------------------
// Returns a Jingle IQ accept session packet for sending to the server to the user who sent jingleIQPacket
// or nil if state != ended.
// Also updates the state to pending.
//-------------------------------------------------------------------
- (NSXMLElement *) jingleRespondSessionAcceptFromPacket: (XMPPIQ *)jingleIQPacket
                                            recvportnum: (uint16_t)portNum
                                                    SID: (NSString *)sid {
    if (![state isEqualToString: [jingleConstants STATE_ENDED]]) {
        return nil;
    }
    //TODO check that the incoming IQ Packet transport and app is supported? android forgoes this for now.
    
    [self getRemoteInformationFromPacket: jingleIQPacket];
    
    NSXMLElement *descriptionElement = [self createDescriptionAndPayloadElements];
    NSXMLElement *transportElement = [self createTransportAndRemoteCandidateElementsWithIP: myIPAddress Port:[NSString stringWithFormat: @"%d", portNum] Action: [jingleConstants SESSION_ACCEPT]];
    NSXMLElement *contentElement = [self createContentElementWithDescription: descriptionElement
                                                                   Transport:transportElement];
    NSXMLElement *jingleElement = [self createJingleElementWithAction: [jingleConstants SESSION_ACCEPT] SID: sid Initiator: nil Responder: [myJID full] childElement: contentElement]; //the responder is myself
    
    
    pendingAck = true;
    state = [jingleConstants STATE_PENDING];
    return [self createBaseIQElement: toJID JingleElement: jingleElement];
}

//-------------------------------------------------------------------
// Processes an Accept Session packet from the remote end.
//-------------------------------------------------------------------
- (void) jingleReceiveSessionAcceptFromPacket: (XMPPIQ *)jingleIQPacket {
    [self getRemoteInformationFromPacket: jingleIQPacket];
    state = [jingleConstants STATE_ACTIVE]; //they officially accepted, so now it's active!
}

//-------------------------------------------------------------------
// Returns a Jingle IQ session-terminate packet for sending to the server to the other person
// or nil if state != active.
// Also updates the state to ended.
//-------------------------------------------------------------------
- (NSXMLElement *) jingleSessionTerminate {
    if (![state isEqualToString: [jingleConstants STATE_ACTIVE]]) {
        return nil;
    }
    
    //Termination with no error reason.
    NSXMLElement *reasonElement = [self createReasonElementWithReason: [jingleConstants SUCCESS_ELEMENT_NAME]];
    NSXMLElement *jingleElement = [self createJingleElementWithAction: [jingleConstants SESSION_TERMINATE] SID: mySID Initiator: nil Responder: nil childElement: reasonElement];
    
    //Reset state
    state = [jingleConstants STATE_ENDED];
    toJID = nil;
    toSID = nil;
    toIPAddress = nil;
    toPort = nil;
    
    return [self createBaseIQElement: toJID JingleElement: jingleElement];
}

//-------------------------------------------------------------------
// Actions to do upon receiving a session terminate from the remote side.
//-------------------------------------------------------------------
- (void) jingleReceiveSessionTerminate {
    //Reset state
    state = [jingleConstants STATE_ENDED];
    toJID = nil;
    toSID = nil;
    toIPAddress = nil;
    toPort = nil;
}

@end
