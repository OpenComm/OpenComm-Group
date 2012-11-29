//
//  OCSingleCallViewController.h
//  iosComm
//
//  Created by Sauhard Bindal on 11/28/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <UIKit/UIKit.h>

extern UILabel *callTextLabel;
extern BOOL callActive;

@interface OCSingleCallViewController : UIViewController

- (IBAction)cancelButtonPressed:(id)sender;

- (IBAction)conferencesButtonPressed:(id)sender;


@property (strong, nonatomic) IBOutlet UILabel *callTextLabel;
@property BOOL callActive;


@end
