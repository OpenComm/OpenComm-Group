//
//  OCViewController.h
//  iosComm
//
//  Created by Qiming Fang on 9/23/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "XMPPFramework.h"
#import "OCDefaultServerConstantsController.h"
#import "OCXMPPDelegateHandler.h"
#import "OCAudioPassingProtocol.h"
#import "pjmedia.h"

@class OCXMPPDelegateHandler;

@interface OCViewController : UIViewController {
    /*From iPhoneXMPP*/
    //XMPPStream *xmppStream;
	OCDefaultServerConstantsController *defaults;
    NSString *myPassword;
    XMPPStream *myXMPPStream;
    XMPPRoster *myXMPPRoster;
    XMPPRosterCoreDataStorage *myXMPPRosterStorage;
	OCXMPPDelegateHandler *delegateHandler;
    NSFetchedResultsController *fetchedResultsController;
}

@property (strong, nonatomic) IBOutlet UITextField *loginPasswordField;
@property (strong, nonatomic) IBOutlet UITextField *loginUsernameField;
//@property (retain, nonatomic) OCXMPPDelegateHandler *delegateHandler;
- (IBAction)loginButtonPressed:(id)sender;
@property (strong, nonatomic) IBOutlet UIView *loginView;

- (IBAction)fetch:(id)sender;


@end
