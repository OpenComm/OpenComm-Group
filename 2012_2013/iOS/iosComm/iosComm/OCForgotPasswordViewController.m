//
//  OCForgotPasswordViewController.m
//  iOSCommUI
//
//  Created by Neelesh Bagga on 11/15/12.
//  Copyright (c) 2012 Sauhard Bindal. All rights reserved.
//

#import "OCForgotPasswordViewController.h"
#import <QuartzCore/QuartzCore.h>

@interface OCForgotPasswordViewController ()

@end

@implementation OCForgotPasswordViewController
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
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc]
                                    initWithURL:[NSURL
                                                 URLWithString:@"http://199.167.198.149/userchange.php"]];
    [request setHTTPMethod:@"POST"];
    NSString *email = [enteremailField text];
    [request setValue:email forHTTPHeaderField:@"userEmail"];
    [request setValue:@"forgot" forHTTPHeaderField:@"action"];
    
    NSURLConnection *theConnection=[[NSURLConnection alloc] initWithRequest:request delegate:self];
    if (theConnection) {
        // Create the NSMutableData to hold the received data.
        // receivedData is an instance variable declared elsewhere.
        _receivedData = [NSMutableData data];
    } else {
        // Inform the user that the connection failed.
    }}


- (IBAction)backButtonPressed:(id)sender {
}



- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    // This method is called when the server has determined that it
    // has enough information to create the NSURLResponse.
    
    // It can be called multiple times, for example in the case of a
    // redirect, so each time we reset the data.
    
    // receivedData is an instance variable declared elsewhere.
    [_receivedData setLength:0];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    // Append the new data to receivedData.
    // receivedData is an instance variable declared elsewhere.
    [_receivedData appendData:data];
    NSLog(@"receivedData is %@", _receivedData);
}
@end
