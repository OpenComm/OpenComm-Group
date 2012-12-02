//
//  OCSignUpViewController.m
//  iOSCommUI
//
//  Created by Neelesh Bagga on 11/15/12.
//  Copyright (c) 2012 Sauhard Bindal. All rights reserved.
//

#import "OCSignUpViewController.h"
#import <QuartzCore/QuartzCore.h>

@implementation OCSignUpViewController

@synthesize signupView;
@synthesize firstnameField;
@synthesize secondnameField;
@synthesize jobtitleField;
@synthesize passwordField;
@synthesize emailaddressField;
@synthesize confirmpasswordField;
@synthesize lowerView;
@synthesize setimageButton;

//extern OCXMPPDelegateHandler *delegateHandler;


- (void)viewDidLoad
{
    float cornerRadius = 10.0f;
    
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    UIImage *backImage = [UIImage imageNamed:@"opencommimage.tiff"];
    [setimageButton setBackgroundImage:backImage forState:UIControlStateNormal];
    [signupView.layer setCornerRadius:cornerRadius];
    [signupView.layer setBorderColor:[[UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:203.0/255.0 alpha:1.0] CGColor]];
    [signupView.layer setBorderWidth:1.0f];
    
    [lowerView.layer setCornerRadius:cornerRadius];
    [lowerView.layer setBorderColor:[[UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:203.0/255.0 alpha:1.0] CGColor]];
    [lowerView.layer setBorderWidth:1.0f];

    
    [firstnameField.layer setCornerRadius:cornerRadius];
    [secondnameField.layer setCornerRadius:cornerRadius];
    
    [jobtitleField.layer setCornerRadius:cornerRadius];
    [passwordField.layer setCornerRadius:cornerRadius];
    [confirmpasswordField.layer setCornerRadius:cornerRadius];
    
    svos = self.view.center;
    
    [firstnameField setDelegate:self];
    [secondnameField setDelegate:self];
    [emailaddressField setDelegate:self];
    [jobtitleField setDelegate:self];
    [passwordField setDelegate:self];
    [confirmpasswordField setDelegate:self];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info{
    
    //lowerView.alpha = 0;
    
    [picker dismissViewControllerAnimated:YES completion:^{
        
        
        UIImage *theImage = [info objectForKey:UIImagePickerControllerOriginalImage];
        [setimageButton setBackgroundImage:theImage forState:UIControlStateNormal];
        
        [lowerView addSubview:setimageButton];
        
       
    }];
}
     

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // when you touch away from these, then remove the keyboard
    [firstnameField resignFirstResponder];
    [secondnameField resignFirstResponder];
    [emailaddressField resignFirstResponder];
    [jobtitleField resignFirstResponder];
    [passwordField resignFirstResponder];
    [confirmpasswordField resignFirstResponder];
    self.view.center = svos;
}

- (IBAction)setimageButton:(id)sender {
    UIImagePickerController *pickerController=[[UIImagePickerController alloc]init];
    pickerController.sourceType=UIImagePickerControllerSourceTypePhotoLibrary;
    [pickerController setDelegate:self];
    [self presentViewController:pickerController animated:YES completion:nil];
}

- (IBAction)textfieldReturned:(id)sender{
    [sender resignFirstResponder];
    self.view.center = svos;
}

- (IBAction)submitPressed:(id)sender{
    /*Error checking form inputs*/
    NSString *username = nil;
    NSString *errorMessage = nil;
    if ([firstnameField.text length] <= 0) {
        errorMessage = @"First Name Required";
    }
    else if ([secondnameField.text length] <= 0) {
        errorMessage = @"Last Name Required";
    }
    else if ([emailaddressField.text length] <= 0) {
        errorMessage = @"Email Address Required";
    }
    else {
        NSRange rangeOfAt = [emailaddressField.text rangeOfString:@"@"];
        if (rangeOfAt.location == NSNotFound) {
            errorMessage = @"Invalid Email Address";
        }
        else {
            username = [emailaddressField.text substringToIndex: rangeOfAt.location];
    
            NSString *rest = [emailaddressField.text substringFromIndex: rangeOfAt.location];
            NSRange rangeOfPeriod = [rest rangeOfString:@"."];
            if (rangeOfPeriod.location == NSNotFound) {
                errorMessage = @"Invalid Email Address";
            }
            else if ([jobtitleField.text length] <= 0) {
                errorMessage = @"Job Title Required";
            }
            else if ([passwordField.text length] <= 0) {
                errorMessage = @"Password Required";
            }
            else if (![passwordField.text isEqualToString: confirmpasswordField.text]) {
                errorMessage = @"Mismatching Passwords";
                passwordField.text = @"";
                confirmpasswordField.text = @"";
            }
        }
    }
    
    if (errorMessage != nil) {
        UIAlertView *errorAlert = [
                             [UIAlertView alloc]
                             initWithTitle:@"Registration Form Error"
                             message: errorMessage
                             delegate:nil
                             cancelButtonTitle: @"OK"
                             otherButtonTitles: nil
                             ];
        [errorAlert show];
        return ;
    }
    
    /*Register the user with the connected XMPP Stream*/
    //username is not nil if errorMessage == nil
    NSString *jid = [[username stringByAppendingString: @"@"] stringByAppendingString: [[delegateHandler getDefaults] DEFAULT_HOSTNAME]];
    [[delegateHandler myXMPPStream] setMyJID:[XMPPJID jidWithString: jid]];
    NSError *error;
    if (![[delegateHandler myXMPPStream] registerWithPassword: passwordField.text email: emailaddressField.text error: &error]) {
        NSLog(@"Oops, I probably forgot something: %@", error);
    }
    
    //Segue change should be moved to DelegateHandler?
    [delegateHandler setViewController: self];
}

- (IBAction)backButtonPressed:(id)sender {
}

- (void) textFieldDidBeginEditing:(UITextField *)textField
{
    current = textField;
    if ([textField isEqual:jobtitleField] || [textField isEqual:passwordField] || [textField isEqual:confirmpasswordField])
    {
        [self setViewMovedUp:YES forTextField:textField];
    }
}

- (void) textFieldDidEndEditing:(UITextField *)textField
{
    self.view.center = svos;
}

- (void) setViewMovedUp:(BOOL)movedUp forTextField:(UITextField *)textField
{
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:0.1];
    
    NSInteger howMuch = 0;
    if ([textField isEqual:jobtitleField])
    {
        howMuch = 60;
    }
    else if ([textField isEqual:passwordField])
    {
        howMuch = 80;
    }
    else
    {
        howMuch = 100;
    }
    
    self.view.center = CGPointMake(self.view.center.x, self.view.center.y - howMuch);
    [UIView commitAnimations];
}


@end
