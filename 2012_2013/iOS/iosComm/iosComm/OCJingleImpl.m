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
    NSString *ATTRIBUTE_NAME_MEDIA;
    
    NSString *ATTRIBUTE_NAME_XMLNS_VALUE;
    
    
    NSString *JINGLE_PACKET_NAME;
    NSString *CONTENT_PACKET_NAME;
}

//-------------------------------------------------------------------
// TODO: This function has been c/p-ed here. It belongs in its own class probably
// From: http://stackoverflow.com/questions/6807788/how-to-get-ip-address-of-iphone-programatically
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
        ATTRIBUTE_NAME_MEDIA = @"media";
        
        ATTRIBUTE_NAME_XMLNS_VALUE = @"urn:xmpp:jingle:1";
        
        JINGLE_PACKET_NAME = @"jingle";
        CONTENT_PACKET_NAME = @"content";
    }
    return self;
}

//-------------------------------------------------------------------
// Returns a Jingle IQ start session packet for sending to the server,
// or nil if state != ended.
// Also updates the state to pending.
//-------------------------------------------------------------------
- (NSXMLElement *) JingleSessionInitiateTo: (XMPPJID *)JIDTo {
    if (![state isEqualToString: STATE_ENDED]) {
        return nil;
    }
    
    /*Initialize the Beginning layer of the Jingle Packet.
     *The IQ packet has attributes 'from', 'id', 'to', and 'type.
     *id is unused by android, so we do not add it here as well!*/
    //TODO worry about id?
    NSXMLElement *toReturn = (NSXMLElement *)[XMPPIQ iqWithType: IQ_TYPE_SET to:JIDTo];
    [toReturn addAttributeWithName:ATTRIBUTE_NAME_FROM stringValue: [myJID user]];

    /*The IQ Packet has a child with name jingle
     *jingle has attributes xmlns, action, initiator, and SID. */
    //TODO SID can be user inputted... supposedly in android.
    NSXMLElement *jingleElement = [NSXMLElement elementWithName: JINGLE_PACKET_NAME];
    [jingleElement addAttributeWithName:ATTRIBUTE_NAME_XMLNS stringValue:ATTRIBUTE_NAME_XMLNS_VALUE];
    [jingleElement addAttributeWithName:ATTRIBUTE_NAME_ACTION stringValue:SESSION_INITIATE];
    [jingleElement addAttributeWithName:ATTRIBUTE_NAME_SID stringValue: [[NSUUID UUID] UUIDString]];
    [jingleElement addAttributeWithName:ATTRIBUTE_NAME_INITIATOR stringValue: [myJID user]];
    
    /*jingle has a child content
     *content has attributes creator and name*/
    NSXMLElement *contentElement = [NSXMLElement elementWithName: CONTENT_PACKET_NAME];
    
    
    return toReturn;
}

@end
