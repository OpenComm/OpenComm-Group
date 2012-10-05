//
//  OCXMPPDelegateHandler.h
//  iosComm
//
//  Created by Kevin Chen on 10/4/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "XMPP.h"

@interface OCXMPPDelegateHandler : NSObject {
}

- (void)xmppStreamDidConnect:(XMPPStream *) sender;
- (void)xmppStreamDidAuthenticate:(XMPPStream *) sender;
- (void)xmppStream:(XMPPStream *)sender didSendPresence:(XMPPPresence *)presence;
- (void)xmppStream:(XMPPStream *)sender didNotAuthenticate:(NSXMLElement *)error;
- (void)xmppStream:(XMPPStream *)sender didReceiveError:(id) error;

@end
