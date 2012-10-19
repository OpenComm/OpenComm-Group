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
#import "OCUDPDelegateHandler.h"
#import <QuartzCore/QuartzCore.h>

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
    
    //[_loginView.layer setCornerRadius=1.0f];
    [_loginView.layer setCornerRadius:10.0f];
    [_loginView.layer setBorderColor:[[UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:203.0/255.0 alpha:1.0] CGColor]];
    //[_loginView.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    //a separate class handler for default XMPP stuff.
	defaults = [[OCDefaultServerConstantsController alloc] init];
    delegateHandler = [[OCXMPPDelegateHandler alloc] init];
    myXMPPStream = [[XMPPStream alloc] init];
    myAudio = [[OCAudioModule alloc] init];
    
    //NSString * audio = @"http://ec2-50-16-95-237.compute-1.amazonaws.com/qiming_html/echo.mp3";
    //[myAudio playAudioFromRemoteURL:audio];
    //[myAudio playAudio:audio];
    
    [myAudio startRecording];
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
// What happens when the login button is pressed
//-------------------------------------------------------------------
- (IBAction)loginButtonPressed:(id)sender {
    //NSLog(@"Username: %@, Password: %@", _loginUsernameField.text,
          //_loginPasswordField.text);
    [myAudio stopRecording];
    
    
    /*This only returns an error if JID and hostname are not set, which is dumb
     *The method is asynchronous, so it returns even though there is not a full connection*/
    //if (![myXMPPStream connect:&error])
    //{
        //NSLog(@"Oops, I probably forgot something: %@", error);
    //}
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

- (void)viewDidUnload {
    [self setLoginView:nil];
    [super viewDidUnload];
}
@end
