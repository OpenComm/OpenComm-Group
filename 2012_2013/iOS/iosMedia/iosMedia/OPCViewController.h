//
//  OPCViewController.h
//  iosMedia
//
//  Created by Qiming Fang on 10/25/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <UIKit/UIKit.h>
#include <ifaddrs.h>
#include <netdb.h>
#include <arpa/inet.h>

#include "IPAddress.h"

@interface OPCViewController : UIViewController
@property (strong, nonatomic) IBOutlet UIButton *stopButton;
@property (strong, nonatomic) IBOutlet UIButton *connectButton;
@property (strong, nonatomic) IBOutlet UITextField *ipAddress;
@property (strong, nonatomic) IBOutlet UILabel *myIpAddressFieldBecauseTheOtherOneFailed;

- (NSString*) getIPAddress;
- (IBAction)startConvo:(id)sender;
- (IBAction)stopConvo:(id)sender;


@end
