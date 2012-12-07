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
    NSFetchedResultsController *fetchedResultsController;
}

@property (strong, nonatomic) IBOutlet UITextField *loginPasswordField;
@property (strong, nonatomic) IBOutlet UITextField *loginUsernameField;
- (IBAction)loginButtonPressed:(id)sender;
@property (strong, nonatomic) IBOutlet UIView *loginView;

//Extern delegate handler for everyone to get.
extern OCXMPPDelegateHandler *delegateHandler;

@end
