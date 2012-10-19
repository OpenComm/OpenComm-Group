//
//  OCSocketViewController.h
//  iosComm
//
//  Created by Kevin Chen on 10/18/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "OCUDPDelegateHandler.h"

@interface OCSocketViewController : UIViewController {
    OCUDPSocket *UDPServerSocket;
    OCUDPSocket *UDPClientSocket;
    OCUDPDelegateHandler *UDPServerDelegateHandler;
    OCUDPDelegateHandler *UDPClientDelegateHandler;
}

- (IBAction)startServerButtonPressed:(id)sender;

@property (strong, nonatomic) IBOutlet UITextView *serverReceiveMessageText;

@property (strong, nonatomic) IBOutlet UITextField *hostnameField;

@property (strong, nonatomic) IBOutlet UITextField *portField;

- (IBAction)startClientButtonPressed:(id)sender;

@property (strong, nonatomic) IBOutlet UITextField *messageField;

- (IBAction)sendMessageButtonPressed:(id)sender;


@end
