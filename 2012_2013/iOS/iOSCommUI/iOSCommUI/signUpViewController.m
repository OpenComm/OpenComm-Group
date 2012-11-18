//
//  signUpViewController.m
//  iOSCommUI
//
//  Created by Neelesh Bagga on 11/15/12.
//  Copyright (c) 2012 Sauhard Bindal. All rights reserved.
//

#import "signUpViewController.h"
#import <QuartzCore/QuartzCore.h>

@interface signUpViewController ()

@end

@implementation signUpViewController

@synthesize signupView;
@synthesize firstnameField;
@synthesize secondnameField;
@synthesize jobtitleField;
@synthesize passwordField;
@synthesize emailaddressField;
@synthesize confirmpasswordField;
@synthesize lowerView;
@synthesize  setimageButton;


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
}
- (IBAction)setimageButton:(id)sender {
    UIImagePickerController *pickerController=[[UIImagePickerController alloc]init];
    pickerController.sourceType=UIImagePickerControllerSourceTypePhotoLibrary;
    pickerController.delegate=self;
    [self presentViewController:pickerController animated:YES completion:nil];
    
}

- (IBAction)textfieldReturned:(id)sender{
    [sender resignFirstResponder];
}
- (IBAction)submitPressed:(id)sender{
   //Store the field values or check, process the data given by the user.
}

- (IBAction)backButtonPressed:(id)sender {
}




@end
