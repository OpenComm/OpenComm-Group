//
//  OCXMPPDelegateHandler.h
//  iosComm
//
//  Created by Kevin Chen on 10/4/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "XMPPFramework.h"
#import "OCJingleImpl.h"
#import "OCViewController.h"

@class OCViewController;

@interface OCXMPPDelegateHandler : NSObject <UIAlertViewDelegate> {
    NSString *password;
    XMPPRoster *myXMPPRoster;
    XMPPRosterCoreDataStorage *myXMPPRosterStorage;
    XMPPStream *myXMPPStream;
    OCJingleImpl *jingleObj;
    OCViewController *viewController;
    OCDefaultServerConstantsController *defaults;
}

- (id)initWithPassword: (NSString *) password andView:(OCViewController *)controller andDefaults:(OCDefaultServerConstantsController *) def;
- (XMPPStream *) getXMPPStream;
- (OCDefaultServerConstantsController *) getDefaults;
- (void)setXMPPRosterStorage:(XMPPRosterCoreDataStorage *)storage roster:(XMPPRoster *)r stream:(XMPPStream *)s;
- (void)setJingleImpl:(OCJingleImpl *)jingleObjParam;
- (void)xmppStreamDidConnect:(XMPPStream *) sender;
- (void)xmppStreamDidAuthenticate:(XMPPStream *) sender;
- (void)xmppStream:(XMPPStream *)sender didSendPresence:(XMPPPresence *)presence;
- (void)xmppStream:(XMPPStream *)sender didNotAuthenticate:(NSXMLElement *)error;
- (void)xmppStream:(XMPPStream *)sender didReceiveError:(id) error;

- (BOOL)xmppStream:(XMPPStream *)sender didReceiveIQ:(XMPPIQ *)iq;
- (NSManagedObjectContext *)managedObjectContext_roster;
- (void)xmppStream:(XMPPStream *)sender didReceiveMessage:(XMPPMessage *)message;

@end
