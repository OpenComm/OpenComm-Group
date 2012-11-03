//
//  OCJingleConstantsController.m
//  iosComm
//
//  Created by Kevin Chen on 11/1/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCJingleConstantsController.h"

@implementation OCJingleConstantsController

- (id) init {
    
    self = [super init];
    if (self) {
        NSString *errorDesc = nil;
        NSPropertyListFormat format;
        NSString *plistPath;
		// this should be the current directory
        NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
                                                                  NSUserDomainMask, YES) objectAtIndex:0];
        plistPath = [rootPath stringByAppendingPathComponent:@"JingleConstants.plist"];
        if (![[NSFileManager defaultManager] fileExistsAtPath:plistPath]) {
            plistPath = [[NSBundle mainBundle] pathForResource:@"JingleConstants" ofType:@"plist"];
        }
        NSData *plistXML = [[NSFileManager defaultManager] contentsAtPath:plistPath];
        NSDictionary *temp = (NSDictionary *)[NSPropertyListSerialization
                                              propertyListFromData:plistXML
                                              mutabilityOption:NSPropertyListMutableContainersAndLeaves
                                              format:&format
                                              errorDescription:&errorDesc];
        if (!temp) {
            NSLog(@"Error reading plist: %@, format: %d", errorDesc, format);
        }
        self.DEBUG_PARAM = [[temp objectForKey: @"DEBUG_PARAM"] boolValue];
        self.DEBUG_IS_SENDER = [[temp objectForKey: @"DEBUG_IS_SENDER"] boolValue];
        self.DEBUG_IS_RECEIVER = [[temp objectForKey: @"DEBUG_IS_RECEIVER"] boolValue];
        self.DEBUG_RECVPORTNUM_SENDER = (int)[[temp objectForKey: @"DEBUG_RECVPORTNUM_SENDER"] intValue];
        self.DEBUG_RECVPORTNUM_RECEIVER = (int)[[temp objectForKey: @"DEBUG_RECVPORTNUM_RECEIVER"] intValue];
        self.DEBUG_SENDER_JID = [temp objectForKey:@"DEBUG_SENDER_JID"];
        self.DEBUG_RECEIVER_JID = [temp objectForKey:@"DEBUG_RECEIVER_JID"];
        
        self.STATE_ENDED = [temp objectForKey:@"STATE_ENDED"];
		self.STATE_PENDING = [temp objectForKey:@"STATE_PENDING"];
		self.STATE_ACTIVE = [temp objectForKey:@"STATE_ACTIVE"];
        
 		self.IQ_TYPE_SET = [temp objectForKey:@"IQ_TYPE_SET"];
        self.IQ_TYPE_RESULT = [temp objectForKey:@"IQ_TYPE_RESULT"];
        
        self.SESSION_INITIATE = [temp objectForKey:@"SESSION_INITIATE"];
        self.SESSION_ACCEPT = [temp objectForKey:@"SESSION_ACCEPT"];
        self.SESSION_TERMINATE = [temp objectForKey:@"SESSION_TERMINATE"];
		
        self.ATTRIBUTE_FROM = [temp objectForKey:@"ATTRIBUTE_FROM"];
        self.ATTRIBUTE_XMLNS = [temp objectForKey:@"ATTRIBUTE_XMLNS"];
        self.ATTRIBUTE_ACTION = [temp objectForKey:@"ATTRIBUTE_ACTION"];
        self.ATTRIBUTE_INITIATOR = [temp objectForKey:@"ATTRIBUTE_INITIATOR"];
        self.ATTRIBUTE_RESPONDER = [temp objectForKey:@"ATTRIBUTE_RESPONDER"];
        self.ATTRIBUTE_SID = [temp objectForKey:@"ATTRIBUTE_SID"];
        self.ATTRIBUTE_CREATOR = [temp objectForKey:@"ATTRIBUTE_CREATOR"];
        self.ATTRIBUTE_NAME = [temp objectForKey:@"ATTRIBUTE_NAME"];
        self.ATTRIBUTE_SENDERS = [temp objectForKey:@"ATTRIBUTE_SENDERS"];
        self.ATTRIBUTE_MEDIA = [temp objectForKey:@"ATTRIBUTE_MEDIA"];
        self.ATTRIBUTE_ID = [temp objectForKey:@"ATTRIBUTE_ID"];
        self.ATTRIBUTE_COMPONENT = [temp objectForKey:@"ATTRIBUTE_COMPONENT"];
        self.ATTRIBUTE_GENERATION = [temp objectForKey:@"ATTRIBUTE_GENERATION"];
        self.ATTRIBUTE_IP = [temp objectForKey:@"ATTRIBUTE_IP"];
        self.ATTRIBUTE_PORT = [temp objectForKey:@"ATTRIBUTE_PORT"];
        
        self.ATTRIBUTE_XMLNS_JINGLE_VALUE = [temp objectForKey:@"ATTRIBUTE_XMLNS_JINGLE_VALUE"];
        self.ATTRIBUTE_NAME_VALUE_AUDIOCONTENT = [temp objectForKey:@"ATTRIBUTE_NAME_VALUE_AUDIOCONTENT"];
        self.ATTRIBUTE_SENDERS_VALUE_BOTH = [temp objectForKey:@"ATTRIBUTE_SENDERS_VALUE_BOTH"];
        self.ATTRIBUTE_XMLNS_DESCRIPTION_VALUE = [temp objectForKey:@"ATTRIBUTE_XMLNS_DESCRIPTION_VALUE"];
        self.ATTRIBUTE_MEDIA_VALUE_AUDIO = [temp objectForKey:@"ATTRIBUTE_MEDIA_VALUE_AUDIO"];
        self.ATTRIBUTE_ID_VALUE_PCMU = [temp objectForKey:@"ATTRIBUTE_ID_VALUE_PCMU"];
        self.ATTRIBUTE_NAME_VALUE_PCMU = [temp objectForKey:@"ATTRIBUTE_NAME_VALUE_PCMU"];
        self.ATTRIBUTE_ID_VALUE_PCMA = [temp objectForKey:@"ATTRIBUTE_ID_VALUE_PCMA"];
        self.ATTRIBUTE_NAME_VALUE_PCMA = [temp objectForKey:@"ATTRIBUTE_NAME_VALUE_PCMA"];
        self.ATTRIBUTE_XMLNS_TRANSPORT_VALUE_RAWUDP = [temp objectForKey:@"ATTRIBUTE_XMLNS_TRANSPORT_VALUE_RAWUDP"];
        self.ATTRIBUTE_COMPONENT_VALUE = [temp objectForKey:@"ATTRIBUTE_COMPONENT_VALUE"];
        self.ATTRIBUTE_GENERATION_VALUE = [temp objectForKey:@"ATTRIBUTE_GENERATION_VALUE"];
        self.ATTRIBUTE_ID_CANDIDATE_VALUE_INITIATE = [temp objectForKey: @"ATTRIBUTE_ID_CANDIDATE_VALUE_INITIATE"];
        self.ATTRIBUTE_ID_CANDIDATE_VALUE_ACCEPT = [temp objectForKey:@"ATTRIBUTE_ID_CANDIDATE_VALUE_ACCEPT"];
        
        self.JINGLE_ELEMENT_NAME = [temp objectForKey: @"JINGLE_ELEMENT_NAME"];
        self.CONTENT_ELEMENT_NAME = [temp objectForKey: @"CONTENT_ELEMENT_NAME"];
        self.DESCRIPTION_ELEMENT_NAME = [temp objectForKey: @"DESCRIPTION_ELEMENT_NAME"];
        self.PAYLOAD_TYPE_ELEMENT_NAME = [temp objectForKey: @"PAYLOAD_TYPE_ELEMENT_NAME"];
        self.TRANSPORT_ELEMENT_NAME = [temp objectForKey: @"TRANSPORT_ELEMENT_NAME"];
        self.REMOTE_CANDIDATE_ELEMENT_NAME = [temp objectForKey: @"REMOTE_CANDIDATE_ELEMENT_NAME"];
        self.REASON_ELEMENT_NAME = [temp objectForKey: @"REASON_ELEMENT_NAME"];
        self.SUCCESS_ELEMENT_NAME = [temp objectForKey: @"SUCCESS_ELEMENT_NAME"];
        self.DECLINE_ELEMENT_NAME = [temp objectForKey: @"DECLINE_ELEMENT_NAME"];
    }
    return self;
    
}

@synthesize DEBUG_PARAM;
@synthesize DEBUG_IS_SENDER;
@synthesize DEBUG_IS_RECEIVER;
@synthesize DEBUG_RECVPORTNUM_SENDER;
@synthesize DEBUG_RECVPORTNUM_RECEIVER;
@synthesize DEBUG_SENDER_JID;
@synthesize DEBUG_RECEIVER_JID;

@synthesize STATE_ENDED;
@synthesize STATE_PENDING;
@synthesize STATE_ACTIVE;

@synthesize IQ_TYPE_SET;
@synthesize IQ_TYPE_RESULT;

@synthesize SESSION_INITIATE;
@synthesize SESSION_ACCEPT;
@synthesize SESSION_TERMINATE;

@synthesize ATTRIBUTE_FROM;
@synthesize ATTRIBUTE_XMLNS;
@synthesize ATTRIBUTE_ACTION;
@synthesize ATTRIBUTE_INITIATOR;
@synthesize ATTRIBUTE_RESPONDER;
@synthesize ATTRIBUTE_SID;
@synthesize ATTRIBUTE_CREATOR;
@synthesize ATTRIBUTE_NAME;
@synthesize ATTRIBUTE_SENDERS;
@synthesize ATTRIBUTE_MEDIA;
@synthesize ATTRIBUTE_ID;
@synthesize ATTRIBUTE_COMPONENT;
@synthesize ATTRIBUTE_GENERATION;
@synthesize ATTRIBUTE_IP;
@synthesize ATTRIBUTE_PORT;

@synthesize ATTRIBUTE_XMLNS_JINGLE_VALUE;
@synthesize ATTRIBUTE_NAME_VALUE_AUDIOCONTENT;
@synthesize ATTRIBUTE_SENDERS_VALUE_BOTH;
@synthesize ATTRIBUTE_XMLNS_DESCRIPTION_VALUE;
@synthesize ATTRIBUTE_MEDIA_VALUE_AUDIO;
@synthesize ATTRIBUTE_ID_VALUE_PCMU;
@synthesize ATTRIBUTE_NAME_VALUE_PCMU;
@synthesize ATTRIBUTE_ID_VALUE_PCMA;
@synthesize ATTRIBUTE_NAME_VALUE_PCMA;
@synthesize ATTRIBUTE_XMLNS_TRANSPORT_VALUE_RAWUDP;
@synthesize ATTRIBUTE_COMPONENT_VALUE;
@synthesize ATTRIBUTE_GENERATION_VALUE;
@synthesize ATTRIBUTE_ID_CANDIDATE_VALUE_INITIATE;
@synthesize ATTRIBUTE_ID_CANDIDATE_VALUE_ACCEPT;

@synthesize JINGLE_ELEMENT_NAME;
@synthesize CONTENT_ELEMENT_NAME;
@synthesize DESCRIPTION_ELEMENT_NAME;
@synthesize PAYLOAD_TYPE_ELEMENT_NAME;
@synthesize TRANSPORT_ELEMENT_NAME;
@synthesize REMOTE_CANDIDATE_ELEMENT_NAME;
@synthesize REASON_ELEMENT_NAME;
@synthesize SUCCESS_ELEMENT_NAME;
@synthesize DECLINE_ELEMENT_NAME;

@end