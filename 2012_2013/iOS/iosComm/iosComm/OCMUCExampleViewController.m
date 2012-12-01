//
//  OCMUCExampleViewController.m
//  iosComm
//
//  Created by Kevin Chen on 12/1/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCMUCExampleViewController.h"

@interface OCMUCExampleViewController ()

@end

@implementation OCMUCExampleViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        // Core storage
        //XMPPRoomCoreDataStorage *roomDataStorage = [[XMPPRoomCoreDataStorage alloc] init];
        //roomDataStorage
        
        [delegateHandler myXMPPStream];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidUnload {
    [self setParticipantsLabel:nil];
    [self setFifthMessageLabel:nil];
    [self setFourthMessageLabel:nil];
    [self setThirdMessageLabel:nil];
    [self setSecondMessageLabel:nil];
    [self setFirstMessageLabel:nil];
    [self setTextMessage:nil];
    [super viewDidUnload];
}

- (IBAction)sendMessage:(id)sender {
    
}

- (void)xmppRoomDidCreate:(XMPPRoom *)sender
{
	NSLog(@"did create room");
}

- (void)xmppRoomDidJoin:(XMPPRoom *)sender
{
	NSLog(@"did join room");
}

- (void)xmppRoomDidLeave:(XMPPRoom *)sender
{
    NSLog(@"did leave room");
}

- (void)xmppRoom:(XMPPRoom *)sender occupantDidJoin:(XMPPJID *)occupantJID
{
	NSLog(@"occupant did join: %@", [occupantJID full]);
}

- (void)xmppRoom:(XMPPRoom *)sender occupantDidLeave:(XMPPJID *)occupantJID
{
	NSLog(@"occupant did join: %@", [occupantJID full]);
}

- (void)xmppRoom:(XMPPRoom *)sender didReceiveMessage:(XMPPMessage *)message fromOccupant:(XMPPJID *)occupantJID
{
	NSLog(@"did receive msg from: %@", [occupantJID full]);
}

@end
