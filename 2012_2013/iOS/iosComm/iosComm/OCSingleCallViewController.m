//
//  OCSingleCallViewController.m
//  iosComm
//
//  Created by Sauhard Bindal on 11/28/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCSingleCallViewController.h"
#import "pjmedia.h"
@interface OCSingleCallViewController ()

@end

BOOL callActive = YES;
UILabel *callTextLabel;

@implementation OCSingleCallViewController
//@synthesize callActive;
@synthesize callTextLabel;

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
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    if (callActive == YES){
        callTextLabel.text = @"The call is active";
    }
    else{
        callTextLabel.text = @"The call has been terminated";
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)cancelButtonPressed:(id)sender {
    if (callActive == YES) {
        callTextLabel.text = @"The call has ended";
        pjmedia_antimain();
        callActive = NO;
        //[self dismissModalViewControllerAnimated:YES];
    }
    else{
        UIAlertView *info = [
                             [UIAlertView alloc]
                             initWithTitle:@"Call not active"
                             message:@"The call is not active press Conferences button to go back"
                             delegate:self
                             cancelButtonTitle:@"Dismiss"
                             otherButtonTitles:nil
                             ];
        [info show];
        callTextLabel.text = @"The call is not active";
    }
}

- (IBAction)conferencesButtonPressed:(id)sender {
    if (callActive == YES) {
        UIAlertView *info = [
                             [UIAlertView alloc]
                             initWithTitle:@"Call active"
                             message:@"The call is active please cancel call before going back"
                             delegate:self
                             cancelButtonTitle:@"Dismiss"
                             otherButtonTitles:nil
                             ];
        [info show];
    }
    else {
        [self performSegueWithIdentifier:@"backToConferences" sender:self];
    }
}

- (void)viewDidUnload {
    [super viewDidUnload];
}
@end
