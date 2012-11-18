//
//  OCUIViewController.m
//  iOSCommUI
//
//  Created by Sauhard Bindal on 10/25/12.
//  Copyright (c) 2012 Sauhard Bindal. All rights reserved.
//

#import "OCUIViewController.h"
#import <QuartzCore/QuartzCore.h>

@interface OCUIViewController ()

@end

@implementation OCUIViewController
@synthesize loginView;
@synthesize usernameField;
@synthesize passwordField;

- (void)viewDidLoad
{
    float cornerRadius = 10.0f;
    
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    [loginView.layer setCornerRadius:cornerRadius];
    [loginView.layer setBorderColor:[[UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:203.0/255.0 alpha:1.0] CGColor]];
    [loginView.layer setBorderWidth:1.0f];
    
    [usernameField.layer setCornerRadius:cornerRadius];
    [passwordField.layer setCornerRadius:cornerRadius];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)signUpButton:(id)sender {
    //UIViewController *signupView=[[UIViewController alloc]init ];
    
    
    
}

- (IBAction)signInButton:(id)sender {
}

- (IBAction)forgotpasswordButton:(id)sender {
}


//-------------------------------------------------------------------
// TODO:Somebody please fill in?
//-------------------------------------------------------------------
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // when you touch away from these, then remove the keyboard
    [usernameField resignFirstResponder];
    [passwordField resignFirstResponder];
}

@end
