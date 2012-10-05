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
#import "OCXMPPDelegateHandler.h"

@interface OCViewController ()

//@synthesize xmppStream;

@end

@implementation OCViewController

//-------------------------------------------------------------------
// Initial load of this page
//-------------------------------------------------------------------
- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    /*STUBBED - INITIAL INFORMATION*/
    //Site to do this: https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/PropertyLists/QuickStartPlist/QuickStartPlist.html
	
    //a separate class handler for default XMPP stuff.
	defaults = [[OCDefaultServerConstantsController alloc] init];
    delegateHandler = [[OCXMPPDelegateHandler alloc] init];
    myXMPPStream = [[XMPPStream alloc] init];
}

//-------------------------------------------------------------------
// Memory warning
//-------------------------------------------------------------------
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//-------------------------------------------------------------------
// Kevin please fill in?
//-------------------------------------------------------------------
/*
- (void)xmppStreamDidStartNegotiation:(XMPPStream *)sender
{
    NSLog(@"I'm starting the negotiation");
}*/


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

//-------------------------------------------------------------------
// What happens when the login button is pressed
//-------------------------------------------------------------------
- (IBAction)loginButtonPressed:(id)sender {
    //NSLog(@"Username: %@, Password: %@", _loginUsernameField.text,
          //_loginPasswordField.text);
    if (debug == YES) {
        myXMPPStream.myJID = [XMPPJID jidWithString:DEFAULT_JID];
        myXMPPStream.hostName = DEFAULT_HOSTNAME;
        /*Don't need to set port. The default is always 5222*/
        myXMPPStream.hostPort = DEFAULT_PORT;
        myPassword = DEFAULT_PASSWORD;
    }

    else {
        NSString *myJID = _loginUsernameField.text;
        myPassword = _loginPasswordField.text;
        
        myXMPPStream.myJID = [XMPPJID jidWithString:myJID];
        myXMPPStream.hostName = @"chat.facebook.com";
    }
    
    [myXMPPStream addDelegate:delegateHandler delegateQueue:dispatch_get_main_queue()];
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

//-------------------------------------------------------------------
// TODO:Somebody please fill in?
//-------------------------------------------------------------------
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // when you touch away from these, then remove the keyboard
    [_loginUsernameField resignFirstResponder];
    [_loginPasswordField resignFirstResponder];
}

@end
