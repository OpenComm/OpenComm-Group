//
//  forgotpasswordViewController.h
//  iOSCommUI
//
//  Created by Neelesh Bagga on 11/15/12.
//  Copyright (c) 2012 Sauhard Bindal. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface forgotpasswordViewController : UIViewController
@property (strong, nonatomic) IBOutlet UITextField *enteremailField;
- (IBAction)resetButton:(id)sender;
- (IBAction)backButtonPressed:(id)sender;


@property (strong, nonatomic) IBOutlet UIView *firstView;
- (IBAction)textfieldReturned:(id)sender;

@end
