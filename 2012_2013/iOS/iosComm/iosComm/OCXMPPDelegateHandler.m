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

- (void)setXMPPRosterStorage:(XMPPRosterCoreDataStorage *)storage roster:(XMPPRoster *) r stream:(XMPPStream *)s
{
    myXMPPRosterStorage = storage;
    myXMPPRoster = r;
    myXMPPStream = s;
}

//-------------------------------------------------------------------
// kfc35 Created Method to go online
//-------------------------------------------------------------------
- (void) goOnline:(XMPPStream *) sender
{
    XMPPPresence *presc = [XMPPPresence presence];
    [sender sendElement: presc]; //available is implicit supposedly
    NSLog(@"I'm supposedly online");
    
    //kfc35 - JINGLEOBJ test - must get the fullJID of the receiver if you are the sender first.
    jingleObj = [[OCJingleImpl alloc] initWithJID:[myXMPPStream myJID] xmppStream: myXMPPStream];
    if ([[jingleObj jingleConstants] DEBUG_PARAM]) {
        NSLog(@"I'm debugging in jingle obj");
        if ([[jingleObj jingleConstants] DEBUG_IS_SENDER]) {
            NSLog(@"I'm the sender for the jingle demo");
            XMPPPresence *subsc = [XMPPPresence presenceWithType:@"subscribe" to: [XMPPJID jidWithString: [[jingleObj jingleConstants] DEBUG_RECEIVER_JID]]];
            [subsc addAttributeWithName: [[jingleObj jingleConstants] ATTRIBUTE_FROM] stringValue: [[myXMPPStream myJID] full]];
            NSLog(@"%@", subsc);
            [sender sendElement: subsc];
        }
    }
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
    
    //if (DEBUG == YES) {
        XMPPPlainAuthentication *auth = [[XMPPPlainAuthentication alloc]initWithStream: sender password: password];
        result = [sender authenticate:auth error:&error];
        
    //}
    //else {
        //XMPPDigestMD5Authentication *auth = [[XMPPDigestMD5Authentication alloc]initWithStream: sender password: password];
        //result = [sender authenticate:auth error:&error];
    //}
    
     /*
    XMPPDigestMD5Authentication *auth = [[XMPPDigestMD5Authentication alloc]initWithStream: sender password: password];
    result = [sender authenticate:auth error:&error];
     */
    
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
    //NSLog(@"I sent my presence");
    //[self sendMessageWith:sender message:@"QIMING IS COOL" to:@"opencommsec@cuopencomm" ];
    
    //init jingleObj here once we go online -- we are now open for jingle connections
}

- (void)xmppStream:(XMPPStream *)sender didReceivePresence:(XMPPPresence *)presence {
    NSLog(@"%@", presence);
    if ([[jingleObj jingleConstants] DEBUG_PARAM]) {
        //receiver has receive a subscribe request. must return with a "subscribed" type to
        //give the sender their full JID
        if ([[jingleObj jingleConstants] DEBUG_IS_RECEIVER]) {
            NSXMLElement *subsc = [XMPPPresence presenceWithType:@"subscribed" to: [XMPPJID jidWithString: [[jingleObj jingleConstants] DEBUG_RECEIVER_JID]]];
            [subsc addAttributeWithName: [[jingleObj jingleConstants] ATTRIBUTE_FROM] stringValue: [[myXMPPStream myJID] full]];
            [sender sendElement: subsc];
        }
        else {
            //Sender has received the full JID of the person in this presence?
            NSLog(@"%@", [presence from]);
            //If you're the sender, send an session-initiate to the "receiver".
            //NSXMLElement *element = [jingleObj jingleSessionInitiateTo:[XMPPJID jidWithString: [[jingleObj jingleConstants] DEBUG_RECEIVER_JID]] recvportnum: (uint16_t)[[jingleObj jingleConstants] DEBUG_RECVPORTNUM_SENDER] SID:nil];
            //[sender sendElement: element];
            //NSLog(@"DEBUG: Sent the following session-initiate msg: %@", element);
            //[jingleObj printJingleObject];
        }
    }
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

- (BOOL)xmppStream:(XMPPStream *)sender didReceiveIQ:(XMPPIQ *)iq
{
	NSLog(@"%@", iq);
    if ([jingleObj processPacketForJingle: iq]) {
        [jingleObj printJingleObject];
        return YES;
    }
    else {
        //If there are other IQ packets that are not jingle, process here.
        NSLog(@"The previously printed packet is not a jingle IQ packet");
    }
	return NO;
}

- (NSManagedObjectContext *)managedObjectContext_roster
{
	return [myXMPPRosterStorage mainThreadManagedObjectContext];
}

- (void)xmppStream:(XMPPStream *)sender didReceiveMessage:(XMPPMessage *)message
{    
	// A simple example of inbound message handling.
    
	if ([message isChatMessageWithBody])
	{
		XMPPUserCoreDataStorageObject *user = [myXMPPRosterStorage userForJID:[message from]
		                                                         xmppStream:myXMPPStream
		                                               managedObjectContext:[self managedObjectContext_roster]];
		
		NSString *body = [[message elementForName:@"body"] stringValue];
		NSString *displayName = [user displayName];
        
		if ([[UIApplication sharedApplication] applicationState] == UIApplicationStateActive)
		{
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:displayName
                                                                message:body
                                                               delegate:nil
                                                      cancelButtonTitle:@"Ok"
                                                      otherButtonTitles:nil];
			[alertView show];
		}
		else
		{
			// We are not active, so use a local notification instead
			UILocalNotification *localNotification = [[UILocalNotification alloc] init];
			localNotification.alertAction = @"Ok";
			localNotification.alertBody = [NSString stringWithFormat:@"From: %@\n\n%@",displayName,body];
            
			[[UIApplication sharedApplication] presentLocalNotificationNow:localNotification];
		}
	}
}

@end
