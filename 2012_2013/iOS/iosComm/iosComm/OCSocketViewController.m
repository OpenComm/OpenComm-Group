//
//  OCSocketViewController.m
//  iosComm
//
//  Created by Kevin Chen on 10/18/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCSocketViewController.h"

@interface OCSocketViewController ()

@end

@implementation OCSocketViewController

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
    
    myAudioBitch = [[OCAudioModule alloc] init];
    
	// Do any additional setup after loading the view.
    NSLog(@"I'm in the SocketViewController");
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // when you touch away from these, then remove the keyboard
    [_hostnameField resignFirstResponder];
    [_portField resignFirstResponder];
    [_messageField resignFirstResponder];
}

- (IBAction)startServerButtonPressed:(id)sender {
    UDPServerSocket = [[OCUDPSocket alloc] init];
    UDPServerDelegateHandler = [[OCUDPDelegateHandler alloc] init];
    [UDPServerSocket setDelegate:UDPServerDelegateHandler];
    [UDPServerDelegateHandler setUDPSocket:UDPServerSocket];
    
    [UDPServerSocket startServerOnPort: 8001];
}

- (void)viewDidUnload {
    [self setServerReceiveMessageText:nil];
    [self setHostnameField:nil];
    [self setPortField:nil];
    [self setMessageField:nil];
    [super viewDidUnload];
}
- (IBAction)startClientButtonPressed:(id)sender {
    /*I'm assuming hostname and port are valid before pressing*/
    UDPClientSocket = [[OCUDPSocket alloc] init];
    UDPClientDelegateHandler = [[OCUDPDelegateHandler alloc] init];
    [UDPClientSocket setDelegate:UDPClientDelegateHandler];
    [UDPClientDelegateHandler setUDPSocket:UDPClientSocket];
    [UDPClientSocket startConnectedToHostName:_hostnameField.text port: _portField.text.intValue];
    
}
- (IBAction)sendMessageButtonPressed:(id)sender {
    NSData *data = [myAudioBitch getLocalAudioAsNSData:@"/Users/qf26/Desktop/echo.mp3"];
    [UDPClientSocket sendData:data];
}
@end
