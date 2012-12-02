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

#define kOFFSET_FOR_KEYBOARD 80.0

@interface OCMUCExampleViewController () {
    NSMutableArray *participants;
    XMPPRoomCoreDataStorage *xmppRoomStorage;
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
    
    if (xmppRoom == nil) {
        // Core storage -- I don't use this personally, but for official usage, you will have to!
        xmppRoomStorage = [[XMPPRoomCoreDataStorage alloc] init];
        if (xmppRoomStorage == nil) {
            xmppRoomStorage = [[XMPPRoomCoreDataStorage alloc] initWithInMemoryStore];
        }
        
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
    
    svos = self.view.center;
    [_textMessage setDelegate:self];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidUnload {
    //[self setParticipantsLabel:nil];
    [self setFifthMessageLabel:nil];
    [self setFourthMessageLabel:nil];
    [self setThirdMessageLabel:nil];
    [self setSecondMessageLabel:nil];
    [self setFirstMessageLabel:nil];
    [self setTextMessage:nil];
    [self setFourthMessageLabel:nil];
    [super viewDidUnload];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // when you touch away from these, then remove the keyboard
    [_textMessage resignFirstResponder];
    //self.view.center = svos;
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
    /*
    _participantsLabel.text = @"Participants: ";
    for (int i = 0; i < [participants count]; i++) {
        _participantsLabel.text = [_participantsLabel.text stringByAppendingString: [participants objectAtIndex: i]];
        if (i != [participants count] - 1) {
            _participantsLabel.text = [_participantsLabel.text stringByAppendingString: @", "];
        }
    }*/
}

- (void)xmppRoomDidLeave:(XMPPRoom *)sender
{
    NSLog(@"did leave room");
}

- (void)xmppRoom:(XMPPRoom *)sender occupantDidJoin:(XMPPJID *)occupantJID
{
    //TODO: for some reason, this isn't called when someone new joins the room!
    [participants addObject: [occupantJID resource]];
	NSLog(@"occupant did join: %@", [occupantJID resource]);
    //update participants label
    /*
    _participantsLabel.text = @"Participants: ";
    for (int i = 0; i < [participants count]; i++) {
        _participantsLabel.text = [_participantsLabel.text stringByAppendingString: [participants objectAtIndex: i]];
        if (i != [participants count] - 1) {
            _participantsLabel.text = [_participantsLabel.text stringByAppendingString: @", "];
        }
    }*/
}

- (void)xmppRoom:(XMPPRoom *)sender occupantDidLeave:(XMPPJID *)occupantJID
{
    //TODO: for some reason, this isn't called when someone new joins the room!
    [participants removeObject: [occupantJID resource]];
	NSLog(@"occupant did leave: %@", [occupantJID resource]);
    //update participants label
    /*
    _participantsLabel.text = @"Participants: ";
    for (int i = 0; i < [participants count]; i++) {
        _participantsLabel.text = [_participantsLabel.text stringByAppendingString: [participants objectAtIndex: i]];
        if (i != [participants count] - 1) {
            _participantsLabel.text = [_participantsLabel.text stringByAppendingString: @", "];
        }
    }*/
}

- (void)xmppRoom:(XMPPRoom *)sender didReceiveMessage:(XMPPMessage *)message fromOccupant:(XMPPJID *)occupantJID
{
    if ([occupantJID resource] == nil) {
        return; //this is from the room only
    }
    //"nickname: messagebody"
    NSString *messageToPrint = [[[occupantJID resource] stringByAppendingString: @": "] stringByAppendingString: [[message elementForName: @"body"] stringValue]];
    //scroll up all messages
    _fifthMessageLabel.text = _fourthMessageLabel.text;
    _fourthMessageLabel.text = _thirdMessageLabel.text;
    _thirdMessageLabel.text = _secondMessageLabel.text;
    _secondMessageLabel.text = _firstMessageLabel.text;
    _firstMessageLabel.text = messageToPrint;
    NSLog(@"%@", messageToPrint);
    //NSLog(@"%@", message);
}

- (void) textFieldDidBeginEditing:(UITextField *)textField
{
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:0.3];
    
    self.view.center = CGPointMake(self.view.center.x, self.view.center.y - 150);
    [UIView commitAnimations];
}

- (void) textFieldDidEndEditing:(UITextField *)textField
{
    self.view.center = svos;
}

- (void) textViewDidBeginEditing:(UITextField *)textField
{
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:0.3];
    
    self.view.center = CGPointMake(self.view.center.x, self.view.center.y - 150);
    [UIView commitAnimations];
}

- (void) textViewDidEndEditing:(UITextField *)textField
{
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:0.3];
    
    self.view.center = CGPointMake(self.view.center.x, self.view.center.y + 150);
    [UIView commitAnimations];
}

@end
