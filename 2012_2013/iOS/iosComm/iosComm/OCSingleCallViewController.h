//
//  OCSingleCallViewController.h
//  iosComm
//
//  Created by Sauhard Bindal on 11/28/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "OCJingleImpl.h"
#import "OCViewController.h"


extern BOOL callActive;

@interface OCSingleCallViewController : UIViewController {}

- (IBAction)cancelButtonPressed:(id)sender;

- (IBAction)conferencesButtonPressed:(id)sender;


- (IBAction)infoButtonPressed:(id)sender;
@property (strong, nonatomic) IBOutlet UIImageView *imageJoker;
@property (strong, nonatomic) IBOutlet UIImageView *imageBane;
@property (strong, nonatomic) IBOutlet UIImageView *imageCatwoman;
@property (strong, nonatomic) IBOutlet UIImageView *nwBox;
@property (strong, nonatomic) IBOutlet UIImageView *neBox;
@property (strong, nonatomic) IBOutlet UIImageView *seBox;
@property (strong, nonatomic) IBOutlet UIImageView *swBox;

@property (strong, nonatomic) IBOutlet UIImageView *nBox;
@property (strong, nonatomic) IBOutlet UIImageView *wBox;
@property (strong, nonatomic) IBOutlet UIImageView *eBox;

//@property BOOL callActive;

@end
