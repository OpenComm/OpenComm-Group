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
    
    UDPServerSocket = [[GCDAsyncUdpSocket alloc] init];
    UDPClientSocket = [[GCDAsyncUdpSocket alloc] init];
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
    
    NSLog(@"starting server ... ");
    
    UDPServerSocket = [[GCDAsyncUdpSocket alloc] initWithDelegate:self delegateQueue:dispatch_get_main_queue()];
    
    NSError *error;
    [UDPServerSocket bindToPort:8001 error:&error];
    [UDPServerSocket beginReceiving:nil];
    
    if (error)
        NSLog(@"%@",[error localizedDescription]);
    else
        NSLog(@"Server started");
}

- (void)viewDidUnload {
    [self setServerReceiveMessageText:nil];
    [self setHostnameField:nil];
    [self setPortField:nil];
    [self setMessageField:nil];
    [super viewDidUnload];
}
- (IBAction)startClientButtonPressed:(id)sender {
    UDPClientSocket = [[GCDAsyncUdpSocket alloc] initWithDelegate:self delegateQueue:dispatch_get_main_queue()];
    NSData *data = NULL;
    
    NSUInteger length = [data length];
    NSUInteger chunksize = MAX_DATAGRAM_SIZE;
    NSUInteger offset = 0;
    
    do {
        
        NSUInteger thisChunkSize = length - offset > chunksize ? chunksize : length - offset;
        NSData *chunk = [NSData dataWithBytesNoCopy:(char *)[data bytes] + offset
                                             length:thisChunkSize freeWhenDone:NO];
        
        [UDPClientSocket sendData:chunk toHost:_hostnameField.text port:(uint16_t)[_portField.text integerValue]
                      withTimeout:-1 tag:1];
        
        offset += thisChunkSize;
        
    } while (offset < length);
    
    NSLog(@"Sent data");
}

- (IBAction)sendMessageButtonPressed:(id)sender {
    //NSData *data = [myAudioBitch getLocalAudioAsNSData:@"/Users/qf26/Desktop/boss.mp3"];
}

/*** Socket Delegate Functions ***/
- (void)udpSocket:(GCDAsyncUdpSocket *)sock didConnectToAddress:(NSData *)address{
    NSLog(@"Connected to address %@", address);
}
- (void)udpSocket:(GCDAsyncUdpSocket *)sock didNotConnect:(NSError *)error{
    NSLog(@"Did not connect. Error %@", error);
}
- (void)udpSocket:(GCDAsyncUdpSocket *)sock didSendDataWithTag:(long)tag{
    NSLog(@"Sent data with tag");
}

- (void)udpSocket:(GCDAsyncUdpSocket *)sock didNotSendDataWithTag:(long)tag dueToError:(NSError *)error{
    
}

- (void)udpSocket:(GCDAsyncUdpSocket *)sock didReceiveData:(NSData *)data
      fromAddress:(NSData *)address withFilterContext:(id)filterContext{
    
    NSLog(@"Received Data from %@", address);
    NSLog(@"%@", [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding]);
}

- (void)udpSocketDidClose:(GCDAsyncUdpSocket *)sock withError:(NSError *)error{
    NSLog(@"Closed. Error: %@", error);
}

@end
