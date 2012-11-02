//
//  OCJingleImpl.h
//  iosComm
//
//  Created by Kevin Chen on 10/31/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "XMPPFramework.h"

@interface OCJingleImpl : NSObject {
    NSString *state; //PENDING, ACTIVE, or ENDED
    XMPPJID *myJID;
    XMPPJID *toJID;
    NSString *myIPAddress;
    NSString *mySID; //CURRENT SESSION ID
    NSString *currentIQID; //Current IQ ID that you are waiting an ACK for.
    
    //TODO this class should include whatever socket we'll be using.
    
}

- (id) initWithJID: (XMPPJID *)JIDParam;

- (NSXMLElement *) jingleSessionInitiateTo: (XMPPJID *)to recvportnum: (uint16_t)portNum;

@end
