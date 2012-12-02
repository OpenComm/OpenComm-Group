//
//  OCMUCExampleViewController.m
//  iosComm
//
//  Created by Kevin Chen on 12/1/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//
//  THIS CLASS IS INSPIRED BY THE ROBBIE HANSON TUTORIAL XMPPStream.xcodeproj
//  Look there for MUC inspiration!

#import "OCMUCExampleViewController.h"

@interface OCMUCExampleViewController () {
    NSMutableArray *participants;
}

@end

@implementation OCMUCExampleViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    // Core storage -- I don't use this personally, but for official usage, you will have to!
    XMPPRoomCoreDataStorage *xmppRoomStorage = [[XMPPRoomCoreDataStorage alloc] init];
    // roomJID is "room@conference.cuopencomm/yourownjid
    XMPPJID *roomJID = [XMPPJID jidWithString: [@"iostestroom@conference.cuopencomm/" stringByAppendingString: [[[delegateHandler myXMPPStream] myJID] user]]];
    xmppRoom = [[XMPPRoom alloc] initWithRoomStorage:xmppRoomStorage jid:roomJID];
    
    [xmppRoom addDelegate:self delegateQueue:dispatch_get_main_queue()];
    // xmppRoomStorage automatically inherits the delegate(s) of it's parent xmppRoom
    
    if([xmppRoom activate: [delegateHandler myXMPPStream]]) {
        NSLog(@"room activated");
    }
    else {
        NSLog(@"room unactivated");
    }
    //use your own jid as your nickname
    [xmppRoom joinRoomUsingNickname: [[[delegateHandler myXMPPStream] myJID] user] history:nil];
    
    //to keep track of participants... you SHOULD use coredatastorage for this though!
    participants = [[NSMutableArray alloc] init];
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

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // when you touch away from these, then remove the keyboard
    [_textMessage resignFirstResponder];
}

- (IBAction)sendMessage:(id)sender {
    [xmppRoom sendMessage: _textMessage.text];
    _textMessage.text = @"";
}

- (void)xmppRoomDidCreate:(XMPPRoom *)sender
{
	NSLog(@"did create room");
    //use the default configurations upon creating the room
    [xmppRoom configureRoomUsingOptions: nil];
}

- (void)xmppRoomDidJoin:(XMPPRoom *)sender
{
    [participants addObject: [sender myNickname]];
	NSLog(@"did join room");
    _participantsLabel.text = @"Participants: ";
    for (int i = 0; i < [participants count]; i++) {
        _participantsLabel.text = [_participantsLabel.text stringByAppendingString: [participants objectAtIndex: i]];
        if (i != [participants count] - 1) {
            _participantsLabel.text = [_participantsLabel.text stringByAppendingString: @", "];
        }
    }
}

- (void)xmppRoomDidLeave:(XMPPRoom *)sender
{
    NSLog(@"did leave room");
}

- (void)xmppRoom:(XMPPRoom *)sender occupantDidJoin:(XMPPJID *)occupantJID
{
    [participants addObject: [occupantJID resource]];
	NSLog(@"occupant did join: %@", [occupantJID resource]);
    //update participants label
    _participantsLabel.text = @"Participants: ";
    for (int i = 0; i < [participants count]; i++) {
        _participantsLabel.text = [_participantsLabel.text stringByAppendingString: [participants objectAtIndex: i]];
        if (i != [participants count] - 1) {
            _participantsLabel.text = [_participantsLabel.text stringByAppendingString: @", "];
        }
    }
}

- (void)xmppRoom:(XMPPRoom *)sender occupantDidLeave:(XMPPJID *)occupantJID
{
    [participants removeObject: [occupantJID resource]];
	NSLog(@"occupant did leave: %@", [occupantJID resource]);
    //update participants label
    _participantsLabel.text = @"Participants: ";
    for (int i = 0; i < [participants count]; i++) {
        _participantsLabel.text = [_participantsLabel.text stringByAppendingString: [participants objectAtIndex: i]];
        if (i != [participants count] - 1) {
            _participantsLabel.text = [_participantsLabel.text stringByAppendingString: @", "];
        }
    }
}

- (void)xmppRoom:(XMPPRoom *)sender didReceiveMessage:(XMPPMessage *)message fromOccupant:(XMPPJID *)occupantJID
{
    if ([occupantJID resource] == nil) {
        return; //this is from the room only
    }
    //"nickname: messagebody"
    NSString *messageToPrint = [[[occupantJID resource] stringByAppendingString: @": "] stringByAppendingString: [[message elementForName: @"body"] stringValue]];
    //scroll up all messages
    _fifthMessageLabel.text = _thirdMessageLabel.text;
    _thirdMessageLabel.text = _secondMessageLabel.text;
    _secondMessageLabel.text = _firstMessageLabel.text;
    _firstMessageLabel.text = messageToPrint;
    NSLog(@"%@", messageToPrint);
    NSLog(@"%@", message);
}

@end
