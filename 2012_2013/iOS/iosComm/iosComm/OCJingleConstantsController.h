//
//  OCJingleConstantsController.h
//  iosComm
//
//  Created by Kevin Chen on 11/1/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface OCJingleConstantsController : NSObject {
    /*
    Boolean DEBUG_PARAM;
    Boolean DEBUG_IS_SENDER;
    Boolean DEBUG_IS_RECEIVER;
    int DEBUG_RECVPORTNUM_SENDER;
    int DEBUG_RECVPORTNUM_RECEIVER;
    NSString *DEBUG_SENDER_JID;
    NSString *DEBUG_RECEIVER_JID;
     
    NSString *STATE_ENDED;
    NSString *STATE_PENDING;
    NSString *STATE_ACTIVE;
    NSString *IQ_TYPE_SET;
    NSString *IQ_TYPE_RESULT;
    
    NSString *SESSION_INITIATE;
    NSString *SESSION_ACCEPT;
    NSString *SESSION_TERMINATE;
    
    NSString *ATTRIBUTE_FROM;
    NSString *ATTRIBUTE_XMLNS;
    NSString *ATTRIBUTE_ACTION;
    NSString *ATTRIBUTE_INITIATOR;
    NSSTRING *ATTRIBUTE_RESPONDER;
    NSString *ATTRIBUTE_SID;
    NSString *ATTRIBUTE_CREATOR;
    NSString *ATTRIBUTE_NAME;
    NSString *ATTRIBUTE_SENDERS;
    NSString *ATTRIBUTE_MEDIA;
    NSString *ATTRIBUTE_ID;
    NSString *ATTRIBUTE_COMPONENT;
    NSString *ATTRIBUTE_GENERATION;
    NSString *ATTRIBUTE_IP;
    NSString *ATTRIBUTE_PORT;
    
    NSString *ATTRIBUTE_XMLNS_JINGLE_VALUE;
    NSString *ATTRIBUTE_NAME_VALUE_AUDIOCONTENT;
    NSString *ATTRIBUTE_SENDERS_VALUE_BOTH;
    NSString *ATTRIBUTE_XMLNS_DESCRIPTION_VALUE;
    NSString *ATTRIBUTE_MEDIA_VALUE_AUDIO;
    NSString *ATTRIBUTE_ID_VALUE_PCMU;
    NSString *ATTRIBUTE_NAME_VALUE_PCMU;
    NSString *ATTRIBUTE_ID_VALUE_PCMA;
    NSString *ATTRIBUTE_NAME_VALUE_PCMA;
    NSString *ATTRIBUTE_XMLNS_TRANSPORT_VALUE_RAWUDP;
    NSString *ATTRIBUTE_COMPONENT_VALUE;
    NSString *ATTRIBUTE_GENERATION_VALUE;
    NSString *ATTRIBUTE_ID_CANDIDATE_VALUE_INITIATE; //10 digits
    NSString *ATTRIBUTE_ID_CANDIDATE_VALUE_ACCEPT;
    
    
    NSString *JINGLE_ELEMENT_NAME;
    NSString *CONTENT_ELEMENT_NAME;
    NSString *DESCRIPTION_ELEMENT_NAME;
    NSString *PAYLOAD_TYPE_ELEMENT_NAME;
    NSString *TRANSPORT_ELEMENT_NAME;
    NSString *REMOTE_CANDIDATE_ELEMENT_NAME;
    NSString *REASON_ELEMENT_NAME;
    NSString *SUCCESS_ELEMENT_NAME;
    NSString *DECLINE_ELEMENT_NAME;
    */
}

/*Boolean constants for debugging*/
@property (nonatomic) Boolean DEBUG_PARAM;
@property (nonatomic) Boolean DEBUG_IS_SENDER;
@property (nonatomic) Boolean DEBUG_IS_RECEIVER;
@property (nonatomic) int DEBUG_RECVPORTNUM_SENDER;
@property (nonatomic) int DEBUG_RECVPORTNUM_RECEIVER;
@property (copy, nonatomic) NSString *DEBUG_SENDER_JID;
@property (copy, nonatomic) NSString *DEBUG_RECEIVER_JID;


/*String constants for the state*/
@property (copy, nonatomic) NSString *STATE_ENDED;
@property (copy, nonatomic) NSString *STATE_PENDING;
@property (copy, nonatomic) NSString *STATE_ACTIVE;

/*String constants for the type field for IQ*/
@property (copy, nonatomic) NSString *IQ_TYPE_SET;
@property (copy, nonatomic) NSString *IQ_TYPE_RESULT;

/*String constants for the ACTION attribute in the IQ packet*/
@property (copy, nonatomic) NSString *SESSION_INITIATE;
@property (copy, nonatomic) NSString *SESSION_ACCEPT;
@property (copy, nonatomic) NSString *SESSION_TERMINATE;

/*Various String Constants denoting attribute names*/
@property (copy, nonatomic) NSString *ATTRIBUTE_FROM;
@property (copy, nonatomic) NSString *ATTRIBUTE_XMLNS;
@property (copy, nonatomic) NSString *ATTRIBUTE_ACTION;
@property (copy, nonatomic) NSString *ATTRIBUTE_INITIATOR;
@property (copy, nonatomic) NSString *ATTRIBUTE_RESPONDER;
@property (copy, nonatomic) NSString *ATTRIBUTE_SID;
@property (copy, nonatomic) NSString *ATTRIBUTE_CREATOR;
@property (copy, nonatomic) NSString *ATTRIBUTE_NAME;
@property (copy, nonatomic) NSString *ATTRIBUTE_SENDERS;
@property (copy, nonatomic) NSString *ATTRIBUTE_MEDIA;
@property (copy, nonatomic) NSString *ATTRIBUTE_ID;
@property (copy, nonatomic) NSString *ATTRIBUTE_COMPONENT;
@property (copy, nonatomic) NSString *ATTRIBUTE_GENERATION;
@property (copy, nonatomic) NSString *ATTRIBUTE_IP;
@property (copy, nonatomic) NSString *ATTRIBUTE_PORT;

/*Various String constants for various values of the denoted attribute*/
@property (copy, nonatomic) NSString *ATTRIBUTE_XMLNS_JINGLE_VALUE;
@property (copy, nonatomic) NSString *ATTRIBUTE_NAME_VALUE_AUDIOCONTENT;
@property (copy, nonatomic) NSString *ATTRIBUTE_SENDERS_VALUE_BOTH;
@property (copy, nonatomic) NSString *ATTRIBUTE_XMLNS_DESCRIPTION_VALUE;
@property (copy, nonatomic) NSString *ATTRIBUTE_MEDIA_VALUE_AUDIO;
@property (copy, nonatomic) NSString *ATTRIBUTE_ID_VALUE_PCMU;
@property (copy, nonatomic) NSString *ATTRIBUTE_NAME_VALUE_PCMU;
@property (copy, nonatomic) NSString *ATTRIBUTE_ID_VALUE_PCMA;
@property (copy, nonatomic) NSString *ATTRIBUTE_NAME_VALUE_PCMA;
@property (copy, nonatomic) NSString *ATTRIBUTE_XMLNS_TRANSPORT_VALUE_RAWUDP;
@property (copy, nonatomic) NSString *ATTRIBUTE_COMPONENT_VALUE;
@property (copy, nonatomic) NSString *ATTRIBUTE_GENERATION_VALUE;
@property (copy, nonatomic) NSString *ATTRIBUTE_ID_CANDIDATE_VALUE_INITIATE;
@property (copy, nonatomic) NSString *ATTRIBUTE_ID_CANDIDATE_VALUE_ACCEPT;

/*Various String constants for the name of specific nsxml elements in Jingle packets*/
@property (copy, nonatomic) NSString *JINGLE_ELEMENT_NAME;
@property (copy, nonatomic) NSString *CONTENT_ELEMENT_NAME;
@property (copy, nonatomic) NSString *DESCRIPTION_ELEMENT_NAME;
@property (copy, nonatomic) NSString *PAYLOAD_TYPE_ELEMENT_NAME;
@property (copy, nonatomic) NSString *TRANSPORT_ELEMENT_NAME;
@property (copy, nonatomic) NSString *REMOTE_CANDIDATE_ELEMENT_NAME;
@property (copy, nonatomic) NSString *REASON_ELEMENT_NAME;
@property (copy, nonatomic) NSString *SUCCESS_ELEMENT_NAME;
@property (copy, nonatomic) NSString *DECLINE_ELEMENT_NAME;


@end

