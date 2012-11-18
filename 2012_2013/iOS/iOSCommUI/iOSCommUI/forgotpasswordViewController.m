//
//  forgotpasswordViewController.m
//  iOSCommUI
//
//  Created by Neelesh Bagga on 11/15/12.
//  Copyright (c) 2012 Sauhard Bindal. All rights reserved.
//

#import "forgotpasswordViewController.h"
#import <QuartzCore/QuartzCore.h>

@interface forgotpasswordViewController ()

@end

@implementation forgotpasswordViewController
@synthesize firstView;
@synthesize enteremailField;





- (void)viewDidLoad
{
    float cornerRadius = 10.0f;
    
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    [firstView.layer setCornerRadius:cornerRadius];
    [firstView.layer setBorderColor:[[UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:203.0/255.0 alpha:1.0] CGColor]];
    [firstView.layer setBorderWidth:1.0f];}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // when you touch away from these, then remove the keyboard
    [enteremailField resignFirstResponder];
    
    
}
- (IBAction)textfieldReturned:(id)sender{
    [sender resignFirstResponder];
}


- (IBAction)resetButton:(id)sender {
}

- (IBAction)backButtonPressed:(id)sender {
}

@end
