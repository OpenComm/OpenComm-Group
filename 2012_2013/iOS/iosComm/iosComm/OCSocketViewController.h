//
//  OCSocketViewController.h
//  iosComm
//
//  Created by Kevin Chen on 10/18/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GCDAsyncUdpSocket.h"
#import "Novocaine.h"
#import "AudioFileReader.h"
#import "AudioFileWriter.h"
#import "RingBuffer.h"

#define MAX_DATAGRAM_SIZE 1024

@interface OCSocketViewController : UIViewController <GCDAsyncUdpSocketDelegate> {
    GCDAsyncUdpSocket *UDPServerSocket;
    GCDAsyncUdpSocket *UDPClientSocket;
}

- (IBAction)startServerButtonPressed:(id)sender;

@property (strong, nonatomic) IBOutlet UITextView *serverReceiveMessageText;

@property (strong, nonatomic) IBOutlet UITextField *hostnameField;

@property (strong, nonatomic) IBOutlet UITextField *portField;

- (IBAction)startClientButtonPressed:(id)sender;

@property (strong, nonatomic) IBOutlet UITextField *messageField;

- (IBAction)sendMessageButtonPressed:(id)sender;


@end
