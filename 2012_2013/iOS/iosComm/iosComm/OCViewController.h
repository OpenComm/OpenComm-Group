//
//  OCViewController.h
//  iosComm
//
//  Created by Qiming Fang on 9/23/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OCViewController : UIViewController
@property (strong, nonatomic) IBOutlet UITextField *loginPasswordField;
@property (strong, nonatomic) IBOutlet UITextField *loginUsernameField;
- (IBAction)loginButtonPressed:(id)sender;

@end
