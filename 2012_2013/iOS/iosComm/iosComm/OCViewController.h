//
//  OCViewController.h
//  iosComm
//
//  Created by Qiming Fang on 9/23/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "XMPP.h"

@interface OCViewController : UIViewController {
    /*From iPhoneXMPP*/
    //XMPPStream *xmppStream;
    BOOL debug;
    NSString *DEFAULT_JID;
    NSString *DEFAULT_HOSTNAME;
    NSString *DEFAULT_PASSWORD;
    int DEFAULT_PORT;
    NSString *myPassword;
    XMPPStream *myXMPPStream;
}
@property (strong, nonatomic) IBOutlet UITextField *loginPasswordField;
@property (strong, nonatomic) IBOutlet UITextField *loginUsernameField;
- (IBAction)loginButtonPressed:(id)sender;



@end
