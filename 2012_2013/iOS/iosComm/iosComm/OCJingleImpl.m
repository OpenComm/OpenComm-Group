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

@implementation OCJingleImpl {
    //TODO stubbed constants. move it into a plist later!
    NSString *STATE_ENDED;
    NSString *STATE_PENDING;
    NSString *STATE_ACTIVE;
    NSString *IQ_TYPE_SET;
    
    NSString *SESSION_INITIATE;
    
    NSString *ATTRIBUTE_NAME_FROM;
    NSString *ATTRIBUTE_NAME_XMLNS;
    NSString *ATTRIBUTE_NAME_ACTION;
    NSString *ATTRIBUTE_NAME_INITIATOR;
    NSString *ATTRIBUTE_NAME_SID;
    NSString *ATTRIBUTE_NAME_CREATOR;
    NSString *ATTRIBUTE_NAME_NAME;
    NSString *ATTRIBUTE_NAME_SENDERS;
    NSString *ATTRIBUTE_NAME_MEDIA;
    NSString *ATTRIBUTE_NAME_ID;
    NSString *ATTRIBUTE_NAME_COMPONENT;
    NSString *ATTRIBUTE_NAME_GENERATION;
    NSString *ATTRIBUTE_NAME_IP;
    NSString *ATTRIBUTE_NAME_PORT;
    
    NSString *ATTRIBUTE_NAME_XMLNS_JINGLE_VALUE;
    NSString *ATTRIBUTE_NAME_NAME_VALUE_AUDIOCONTENT;
    NSString *ATTRIBUTE_NAME_SENDERS_VALUE_BOTH;
    NSString *ATTRIBUTE_NAME_DESCRIPTION_XMLNS_VALUE;
    NSString *ATTRIBUTE_NAME_MEDIA_VALUE_AUDIO;
    NSString *ATTRIBUTE_NAME_ID_VALUE_PCMU;
    NSString *ATTRIBUTE_NAME_NAME_VALUE_PCMU;
    NSString *ATTRIBUTE_NAME_ID_VALUE_PCMA;
    NSString *ATTRIBUTE_NAME_NAME_VALUE_PCMA;
    NSString *ATTRIBUTE_NAME_XMLNS_TRANSPORT_VALUE_RAWUDP;
    NSString *ATTRIBUTE_NAME_COMPONENT_VALUE;
    NSString *ATTRIBUTE_NAME_GENERATION_VALUE;
    NSString *ATTRIBUTE_NAME_ID_CANDIDATE_VALUE_INITIATE; //10 digits
    NSString *ATTRIBUTE_NAME_ID_CANDIDATE_VALUE_ACCEPT;
    
    
    NSString *JINGLE_PACKET_NAME;
    NSString *CONTENT_PACKET_NAME;
    NSString *DESCRIPTION_PACKET_NAME;
    NSString *PAYLOAD_TYPE_PACKET_NAME;
    NSString *TRANSPORT_PACKET_NAME;
    NSString *REMOTE_CANDIDATE_PACKET_NAME;
}

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
        myJID = JIDParam;
        myIPAddress = [self getIPAddress];
        mySID = nil;
        currentIQID = nil;
        
        STATE_ENDED = @"ENDED";
        STATE_PENDING = @"PENDING";
        STATE_ACTIVE = @"ACTIVE";
        state = STATE_ENDED;
        
        IQ_TYPE_SET = @"set";
        
        SESSION_INITIATE = @"session-initiate";
        
        ATTRIBUTE_NAME_FROM = @"from";
        ATTRIBUTE_NAME_XMLNS = @"xmlns";
        ATTRIBUTE_NAME_ACTION = @"action";
        ATTRIBUTE_NAME_INITIATOR = @"initiator";
        ATTRIBUTE_NAME_SID = @"sid";
        ATTRIBUTE_NAME_CREATOR = @"creator";
        ATTRIBUTE_NAME_NAME = @"name";
        ATTRIBUTE_NAME_SENDERS = @"senders";
        ATTRIBUTE_NAME_MEDIA = @"media";
        ATTRIBUTE_NAME_ID = @"id";
        ATTRIBUTE_NAME_COMPONENT = @"component";
        ATTRIBUTE_NAME_GENERATION = @"generation";
        
        ATTRIBUTE_NAME_XMLNS_JINGLE_VALUE = @"urn:xmpp:jingle:1";
        ATTRIBUTE_NAME_NAME_VALUE_AUDIOCONTENT = @"audio-content";
        ATTRIBUTE_NAME_SENDERS_VALUE_BOTH = @"both";
        ATTRIBUTE_NAME_DESCRIPTION_XMLNS_VALUE = @"urn:xmpp:jingle:apps:rtp:1";
        ATTRIBUTE_NAME_MEDIA_VALUE_AUDIO = @"audio";
        ATTRIBUTE_NAME_ID_VALUE_PCMU = @"0";
        ATTRIBUTE_NAME_NAME_VALUE_PCMU = @"PCMU";
        ATTRIBUTE_NAME_ID_VALUE_PCMA = @"8";
        ATTRIBUTE_NAME_NAME_VALUE_PCMA = @"PCMA";
        ATTRIBUTE_NAME_XMLNS_TRANSPORT_VALUE_RAWUDP = @"urn:xmpp:jingle:transports:raw-udp:1";
        ATTRIBUTE_NAME_COMPONENT_VALUE = @"1";
        ATTRIBUTE_NAME_GENERATION_VALUE = @"0";
        //stubbed from the java value. In reality, should be random?
        ATTRIBUTE_NAME_ID_CANDIDATE_VALUE_INITIATE = @"0000000000";
        ATTRIBUTE_NAME_ID_CANDIDATE_VALUE_ACCEPT = @"0000000001";
        
        JINGLE_PACKET_NAME = @"jingle";
        CONTENT_PACKET_NAME = @"content";
        DESCRIPTION_PACKET_NAME = @"description";
        PAYLOAD_TYPE_PACKET_NAME = @"payload-type";
        TRANSPORT_PACKET_NAME = @"transport";
        REMOTE_CANDIDATE_PACKET_NAME = @"remote-candidate";
    }
    return self;
}

//-------------------------------------------------------------------
// Returns a Jingle IQ base NSXML Element
// Used to initialize the Beginning layer of the Jingle Packet.
// The IQ packet has attributes 'from', 'id', 'to', and 'type.
// id is unused by android, so we do not add it here as well!*/
//-------------------------------------------------------------------
- (NSXMLElement *) createBaseIQPacket: (XMPPJID *)JIDTo {
    NSXMLElement *IQPacket = (NSXMLElement *)[XMPPIQ iqWithType: IQ_TYPE_SET to: JIDTo];
    [IQPacket addAttributeWithName: ATTRIBUTE_NAME_FROM stringValue: [myJID full]];
    return IQPacket;
}

//-------------------------------------------------------------------
// Returns a Jingle IQ start session packet for sending to the server,
// or nil if state != ended.
// Also updates the state to pending.
//-------------------------------------------------------------------
- (NSXMLElement *) jingleSessionInitiateTo: (XMPPJID *)JIDTo recvportnum: (uint16_t)portNum {
    if (![state isEqualToString: STATE_ENDED]) {
        return nil;
    }
    toJID = JIDTo;
    
    
    //TODO worry about id?
    NSXMLElement *toReturn = [self createBaseIQPacket: toJID];

    /*The IQ Packet has a child with name jingle
     *jingle has attributes xmlns, action, initiator, and SID. */
    //TODO SID can be user inputted... supposedly in android.
    NSXMLElement *jingleElement = [NSXMLElement elementWithName: JINGLE_PACKET_NAME];
    [jingleElement addAttributeWithName: ATTRIBUTE_NAME_XMLNS stringValue: ATTRIBUTE_NAME_XMLNS_JINGLE_VALUE];
    [jingleElement addAttributeWithName: ATTRIBUTE_NAME_ACTION stringValue: SESSION_INITIATE];
    mySID = [[NSUUID UUID] UUIDString];
    [jingleElement addAttributeWithName: ATTRIBUTE_NAME_SID stringValue: mySID];
    [jingleElement addAttributeWithName: ATTRIBUTE_NAME_INITIATOR stringValue: [myJID user]];
    
    /*jingle has a child content
     *content has attributes creator and name*/
    NSXMLElement *contentElement = [NSXMLElement elementWithName: CONTENT_PACKET_NAME];
    [contentElement addAttributeWithName: ATTRIBUTE_NAME_CREATOR stringValue: ATTRIBUTE_NAME_INITIATOR];
    [contentElement addAttributeWithName: ATTRIBUTE_NAME_NAME stringValue: ATTRIBUTE_NAME_NAME_VALUE_AUDIOCONTENT];
    [contentElement addAttributeWithName: ATTRIBUTE_NAME_SENDERS stringValue: ATTRIBUTE_NAME_SENDERS_VALUE_BOTH];
    /*content has a child description
     *description has attributes xmlns and media*/
    NSXMLElement *descriptionElement = [NSXMLElement elementWithName: DESCRIPTION_PACKET_NAME];
    [descriptionElement addAttributeWithName: ATTRIBUTE_NAME_XMLNS stringValue: ATTRIBUTE_NAME_DESCRIPTION_XMLNS_VALUE];
    [descriptionElement addAttributeWithName: ATTRIBUTE_NAME_MEDIA stringValue: ATTRIBUTE_NAME_MEDIA_VALUE_AUDIO];
    
    /*description has two children payload-type
     *payload-type has attributes id, name, clockrate, and channels (clockrate and channels unused)*/
    //first payload type is for PCMU
    NSXMLElement *payloadElementOne = [NSXMLElement elementWithName: PAYLOAD_TYPE_PACKET_NAME];
    [payloadElementOne addAttributeWithName: ATTRIBUTE_NAME_ID stringValue: ATTRIBUTE_NAME_ID_VALUE_PCMU];
    [payloadElementOne addAttributeWithName: ATTRIBUTE_NAME_NAME stringValue: ATTRIBUTE_NAME_NAME_VALUE_PCMU];
    
    //second payload type is for PCMA
    NSXMLElement *payloadElementTwo = [NSXMLElement elementWithName: PAYLOAD_TYPE_PACKET_NAME];
    [payloadElementTwo addAttributeWithName: ATTRIBUTE_NAME_ID stringValue: ATTRIBUTE_NAME_ID_VALUE_PCMA];
    [payloadElementTwo addAttributeWithName: ATTRIBUTE_NAME_NAME stringValue: ATTRIBUTE_NAME_NAME_VALUE_PCMA];
    
    /*content has a child transport
     *transport has attributes xmlns, pwd, and ufrag (pwd and ufrag unused because we are using RAWUDP)*/
    NSXMLElement *transportElement = [NSXMLElement elementWithName: TRANSPORT_PACKET_NAME];
    [transportElement addAttributeWithName: ATTRIBUTE_NAME_XMLNS stringValue: ATTRIBUTE_NAME_XMLNS_TRANSPORT_VALUE_RAWUDP];
    
    /*Transport has a child remote-candidate
     *remote-candidate has attributes compontent, foundation, generation, id, ip, network, port,
     *priority, protocol, reladdr, relport, and type (foundation, network, priority, protocl, reladdr, relport, 
     *type unused)*/
    NSXMLElement *remoteCandidateElement = [NSXMLElement elementWithName: REMOTE_CANDIDATE_PACKET_NAME];
    [remoteCandidateElement addAttributeWithName: ATTRIBUTE_NAME_COMPONENT stringValue: ATTRIBUTE_NAME_COMPONENT_VALUE];
    [remoteCandidateElement addAttributeWithName: ATTRIBUTE_NAME_GENERATION stringValue: ATTRIBUTE_NAME_GENERATION_VALUE];
    [remoteCandidateElement addAttributeWithName: ATTRIBUTE_NAME_ID stringValue: ATTRIBUTE_NAME_ID_CANDIDATE_VALUE_INITIATE];
    
    //TODO should we make the port here?
    [remoteCandidateElement addAttributeWithName: ATTRIBUTE_NAME_IP stringValue: myIPAddress];
    [remoteCandidateElement addAttributeWithName: ATTRIBUTE_NAME_PORT stringValue: [NSString stringWithFormat: @"%d", portNum]];
    
    /*Connect all the elements together correctly*/
    
    
    
    
    return toReturn;
}

@end
