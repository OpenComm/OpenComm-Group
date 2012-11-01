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
    NSString *myIPAddress;
    
}
- (id) initWithJID: (XMPPJID *)JIDParam;

- (NSXMLElement *) JingleSessionInitiateTo: (XMPPJID *)to;

@end
