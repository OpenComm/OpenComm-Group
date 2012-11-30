//
//  OCSingleCallViewController.m
//  iosComm
//
//  Created by Sauhard Bindal on 11/28/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCSingleCallViewController.h"
#import "pjmedia.h"

BOOL callActive = YES;


@implementation OCSingleCallViewController
//@synthesize callActive;


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
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)cancelButtonPressed:(id)sender {
    if (callActive == YES) {
        
        pjmedia_antimain();
        callActive = NO;
        //[self dismissModalViewControllerAnimated:YES];
        [[delegateHandler getJingle] terminate];
    }
    else{
        UIAlertView *info = [
                             [UIAlertView alloc]
                             initWithTitle:@"Call not active"
                             message:@"The call is not active press Back button to go back to conferences page"
                             delegate:self
                             cancelButtonTitle:@"Dismiss"
                             otherButtonTitles:nil
                             ];
        [info show];
        
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

- (IBAction)infoButtonPressed:(id)sender {
    NSString* msg;
    if (callActive == YES) {
        msg = @"The call is currently active";
    }
    else {
        msg = @"The call is not active";
        
    }
    UIAlertView *info = [
                         [UIAlertView alloc]
                         initWithTitle:@"Call Status"
                         message:msg
                         delegate:self
                         cancelButtonTitle:@"Dismiss"
                         otherButtonTitles:nil
                         ];
    [info show];
}

- (void)viewDidUnload {
    [super viewDidUnload];
}
@end
