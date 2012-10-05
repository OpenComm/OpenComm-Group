//
//  OCViewController.h
//  iosComm
//
//  Created by Qiming Fang on 9/23/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "XMPP.h"
#import "OCXMPPDelegateHandler.h"

@interface OCViewController : UIViewController {
    /*From iPhoneXMPP*/
    //XMPPStream *xmppStream;
	OCDefaultServerConstantsController *defaults;
    NSString *myPassword;
    XMPPStream *myXMPPStream;
	id <OCXMPPDelegateHandler> *delegateHandler;
}
@property (strong, nonatomic) IBOutlet UITextField *loginPasswordField;
@property (strong, nonatomic) IBOutlet UITextField *loginUsernameField;
@property (retain, nonatomic) id <OCXMPPDelegateHandler> *delegateHandler
- (IBAction)loginButtonPressed:(id)sender;



@end
