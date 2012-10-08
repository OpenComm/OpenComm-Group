//
//  OCXMPPDelegateHandler.m
//  iosComm
//
//  Created by Kevin Chen on 10/4/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCXMPPDelegateHandler.h"

@implementation OCXMPPDelegateHandler

- (id)initWithPassword: (NSString *) passwordParam {
    self = [super init];
    if (self) {
        password = passwordParam;
    }
    return self;
}

//-------------------------------------------------------------------
// kfc35 Created Method to go online
//-------------------------------------------------------------------
- (void) goOnline:(XMPPStream *) sender
{
    XMPPPresence *presc = [XMPPPresence presence];
    [sender sendElement: presc]; //available is implicit supposedly
    NSLog(@"I'm supposedly online");
}

//-------------------------------------------------------------------
// kfc35 Created Method to send a test message
//-------------------------------------------------------------------
- (void) sendMessageWith:(XMPPStream *)sender message:(NSString *)content to:(NSString *)to
{
    //http://stackoverflow.com/questions/4460823/send-a-message-via-xmppframework-for-ios
    //MY CODE IS NEARLY LIKE THIS WHY ISNT IT SENDINGGGG
    //10.5.2012: Qiming - fixed. Can send messages now
    
    NSXMLElement *body = [NSXMLElement elementWithName:@"body"];
    [body setStringValue: content];
    
    NSXMLElement *message = [NSXMLElement elementWithName:@"message"];
    [message addAttributeWithName:@"type" stringValue:@"chat"];
    [message addAttributeWithName:@"to" stringValue:to];
    [message addChild:body];
    
    [sender sendElement: message];
    
    NSLog(@"sent chat");
}

//-------------------------------------------------------------------
// Delegate method called once the stream is connected, implements XMPP stream delegate
//-------------------------------------------------------------------
- (void)xmppStreamDidConnect:(XMPPStream *)sender
{
    //[NSThread sleepForTimeInterval: 10];
    /*while (true) {
        if (sender.isConnected) {
            NSLog(@"I'm finally connected");
        }
        else {
            NSLog(@"I'm not connected");
        }
    }*/
    NSLog(@"I get in here at Did Connect");
    /*DigestMD5 should be used to connect to Facebook instead of XFacebook, because XFacebook
     *Requires a appID and token. MD5 only requires the password*/
    
    NSError *error = nil;
    BOOL result;
    if (DEBUG == YES) {
        XMPPPlainAuthentication *auth = [[XMPPPlainAuthentication alloc]initWithStream: sender password: password];
        result = [sender authenticate:auth error:&error];
        
    }
    else {
        XMPPDigestMD5Authentication *auth = [[XMPPDigestMD5Authentication alloc]initWithStream: sender password: password];
        result = [sender authenticate:auth error:&error];
    }
    if (!result) {
        NSLog(@"Oops, I probably forgot something: %@", error);
    }
    if (sender.isAuthenticated) {
        NSLog(@"I'm authenticated in Did Connect");
    }
    NSLog(@"Goodbye!");
    
    //This is not really printed... so I hypothesize that this delegate is called
    //Before the state is set to connected.
    if (sender.isConnected) {
        NSLog(@"Connected 2!");
    }
}


//-------------------------------------------------------------------
// Delegate method called once the login is authenticated, implements XMPP stream delegate
//-------------------------------------------------------------------
- (void)xmppStreamDidAuthenticate:(XMPPStream *)sender
{
    NSLog(@"I get in here too");
    if (sender.isAuthenticated) { //This usually prints
        NSLog(@"I'm authenticated in Did Authenticate");
    }
    if (sender.isConnected) { //This always prints
        NSLog(@"I'm connected in Did Authenticate");
    }
    /*I think it's safe to assume that, in this method, EVERYTHING is good with the state
     *as in, authentication and connection have fully succeeded. So write stuff here to do!*/
    
    /*This gets me online!*/
    [self goOnline:sender];
    
    //[self sendMessage: sender];
    
    //NSLog(@"I've sent the presence");
    //[NSThread sleepForTimeInterval: 10];
    //NSLog(@"I'm about to disc!");
    
    //[sender disconnect];
}


//-------------------------------------------------------------------
// Delegate method called once the presence is sent, implements XMPP stream delegate
//-------------------------------------------------------------------
- (void)xmppStream:(XMPPStream *)sender didSendPresence:(XMPPPresence *)presence {
    NSLog(@"I sent my presence");
    //[NSThread sleepForTimeInterval: 30];
    [self sendMessageWith:sender message:@"QIMING IS COOL" to:@"qimingiscool@cuopencomm" ];
}


//-------------------------------------------------------------------
// Delegate method called once login fails, implements XMPP stream delegate
//-------------------------------------------------------------------
- (void)xmppStream:(XMPPStream *)sender didNotAuthenticate:(NSXMLElement *)error
{
    NSLog(@"I did not authenticate");
    NSLog(@"%@", error);
}


//-------------------------------------------------------------------
// Delegate method called once an error is received, implements XMPP stream delegate
//-------------------------------------------------------------------
- (void)xmppStream:(XMPPStream *)sender didReceiveError:(id)error
{
	NSLog(@"Received an error");
}


@end