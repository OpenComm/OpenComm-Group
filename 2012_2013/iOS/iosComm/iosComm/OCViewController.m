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
    debug = YES;
    DEFAULT_JID = @"opencommsec";
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)loginButtonPressed:(id)sender {
    NSLog(@"Username: %@, Password: %@", _loginUsernameField.text,
          _loginPasswordField.text);
    if (debug == YES) {
        XMPPStream *myXMPPStream = [[XMPPStream alloc] init];
        myXMPPStream.myJID = [XMPPJID jidWithString:DEFAULT_JID];
        myXMPPStream.hostName = @"cuopencomm.no-ip.org";
        /*Don't need to set port. The default is always 5222*/
        //myXMPPStream.hostPort = 5222;
        
        NSError *error = nil;
        if (![myXMPPStream connect:&error])
        {
            NSLog(@"Oops, I probably forgot something: %@", error);
        }
        else
        {
            NSLog(@"I supposedly reached it");
        }
    }
    
    
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // when you touch away from these, then remove the keyboard
    [_loginUsernameField resignFirstResponder];
    [_loginPasswordField resignFirstResponder];
}

@end
