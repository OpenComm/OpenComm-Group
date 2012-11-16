//
//  signUpViewController.m
//  iOSCommUI
//
//  Created by Neelesh Bagga on 11/15/12.
//  Copyright (c) 2012 Sauhard Bindal. All rights reserved.
//

#import "signUpViewController.h"

@interface signUpViewController ()

@end

@implementation signUpViewController
@synthesize signupView;
@synthesize firstnameField;
@synthesize secondnameField;
@synthesize jobtitleField;
@synthesize passwordField;
@synthesize confirmpasswordField;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    float cornerRadius = 10.0f;
    
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    [signupView.layer setCornerRadius];
    [signupView.layer setBorderColor:[[UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:203.0/255.0 alpha:1.0] CGColor]];
    [signupView.layer setBorderWidth:1.0f];
    
    [firstnameField.layer setCornerRadius:cornerRadius];
    [secondnameField.layer setCornerRadius:cornerRadius];
    
    [jobtitleField.layer setCornerRadius:cornerRadius];
    [passwordField.layer setCornerRadius:cornerRadius];
    [confirmpasswordField.layer setCornerRadius:cornerRadius];
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
