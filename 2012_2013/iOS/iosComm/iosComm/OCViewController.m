//
//  OCViewController.m
//  iosComm
//
//  Created by Qiming Fang on 9/23/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCViewController.h"
#import "XMPP.h"
#import "XMPPStream.h"

@interface OCViewController ()

//@synthesize xmppStream;

@end

@implementation OCViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    /*STUBBED - INITIAL INFORMATION*/
    debug = NO;
    DEFAULT_JID = @"opencommsec";
    DEFAULT_PASSWORD = @"secopencomm";
    DEFAULT_HOSTNAME = @"cuopencomm.no-ip.org";
    
    myXMPPStream = [[XMPPStream alloc] init];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
- (void)xmppStreamDidStartNegotiation:(XMPPStream *)sender
{
    NSLog(@"I'm starting the negotiation");
}*/

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
    XMPPDigestMD5Authentication *auth = [[XMPPDigestMD5Authentication alloc]initWithStream: sender password: myPassword];
    NSError *error = nil;
    
    BOOL result = [sender authenticate:auth error:&error];
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

/* kfc35 Created Method to go online*/
- (void) goOnline:(XMPPStream *) sender
{
    XMPPPresence *presc = [XMPPPresence presence];
    [sender sendElement: presc]; //available is implicit supposedly
    NSLog(@"I'm supposedly online");
}

- (void)xmppStream:(XMPPStream *)sender didSendPresence:(XMPPPresence *)presence {
    NSLog(@"I sent my presence");
    [NSThread sleepForTimeInterval: 30];
    [self sendMessage: sender];
}

/* kfc35 Created Method to send a test message*/
- (void) sendMessage:(XMPPStream *)sender
{
    //http://stackoverflow.com/questions/4460823/send-a-message-via-xmppframework-for-ios
    //MY CODE IS NEARLY LIKE THIS WHY ISNT IT SENDINGGGG
    
    NSXMLElement *message = [NSXMLElement elementWithName:@"body"];
    [message setStringValue: @"hi sweet"];
    XMPPMessage *msg = [XMPPMessage messageWithType:@"chat" to:[XMPPJID jidWithString:@"shihui.song@chat.facebook.com"]];
    [msg addChild: message];
    
    [sender sendElement: msg];
    
    NSLog(@"%@", [[msg elementForName: @"body"] stringValue]);
}

- (void)xmppStream:(XMPPStream *)sender didNotAuthenticate:(NSXMLElement *)error
{
    NSLog(@"I did not authenticate");
}

- (IBAction)loginButtonPressed:(id)sender {
    //NSLog(@"Username: %@, Password: %@", _loginUsernameField.text,
          //_loginPasswordField.text);
    if (debug == YES) {
        myXMPPStream.myJID = [XMPPJID jidWithString:DEFAULT_JID];
        myXMPPStream.hostName = DEFAULT_HOSTNAME;
        /*Don't need to set port. The default is always 5222*/
        //myXMPPStream.hostPort = 5222;
        
        [myXMPPStream addDelegate:self delegateQueue:dispatch_get_main_queue()];
        
        NSError *error = nil;
        
        if (![myXMPPStream isDisconnected]) {
            NSLog(@"I'm already connected");
        }
        /*This only returns an error if JID and hostname are not set, which is dumb
         *The method is asynchronous, so it returns even though there is not a full connection*/
        if (![myXMPPStream connect:&error])
        {
            NSLog(@"Oops, I probably forgot something: %@", error);
        }
    }
    else {
        NSString *myJID = _loginUsernameField.text;
        myPassword = _loginPasswordField.text;
        
        myXMPPStream.myJID = [XMPPJID jidWithString:myJID];
        myXMPPStream.hostName = @"chat.facebook.com";
        
        [myXMPPStream addDelegate:self delegateQueue:dispatch_get_main_queue()];
        
        NSError *error = nil;
        
        if (![myXMPPStream isDisconnected]) {
            NSLog(@"I'm already connected");
        }
        /*This only returns an error if JID and hostname are not set, which is dumb
         *The method is asynchronous, so it returns even though there is not a full connection*/
        if (![myXMPPStream connect:&error])
        {
            NSLog(@"Oops, I probably forgot something: %@", error);
        }
    }
}

- (void)xmppStream:(XMPPStream *)sender didReceiveError:(id)error
{
	NSLog(@"Received an error");
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // when you touch away from these, then remove the keyboard
    [_loginUsernameField resignFirstResponder];
    [_loginPasswordField resignFirstResponder];
}

@end
