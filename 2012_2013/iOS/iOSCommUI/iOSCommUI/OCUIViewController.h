//
//  OCUIViewController.h
//  iOSCommUI
//
//  Created by Sauhard Bindal on 10/25/12.
//  Copyright (c) 2012 Sauhard Bindal. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OCUIViewController : UIViewController


@property (strong, nonatomic) IBOutlet UIView *loginView;
@property (strong, nonatomic) IBOutlet UITextField *usernameField;
@property (strong, nonatomic) IBOutlet UITextField *passwordField;
- (IBAction)signUpButton:(id)sender;
- (IBAction)signInButton:(id)sender;

- (IBAction)forgotpasswordButton:(id)sender;

@end
