//
//  signUpViewController.h
//  iOSCommUI
//
//  Created by Neelesh Bagga on 11/15/12.
//  Copyright (c) 2012 Sauhard Bindal. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface signUpViewController : UIViewController <UIImagePickerControllerDelegate>


@property (strong, nonatomic) IBOutlet UIView *signupView;
@property (strong,nonatomic) IBOutlet UIView *lowerView;

@property (strong, nonatomic) IBOutlet UITextField *firstnameField;
@property (strong, nonatomic) IBOutlet UITextField *secondnameField;
@property (strong, nonatomic) IBOutlet UITextField *emailaddressField;

@property (strong, nonatomic) IBOutlet UITextField *jobtitleField;
@property (strong, nonatomic) IBOutlet UITextField *passwordField;
@property (strong, nonatomic) IBOutlet UITextField *confirmpasswordField;
@property (strong, nonatomic) IBOutlet UIButton *setimageButton;
- (IBAction)setimageButton:(id)sender;

- (IBAction)textfieldReturned:(id)sender;
- (IBAction) submitPressed: (id)sender;
- (IBAction)backButtonPressed:(id)sender;

@end
