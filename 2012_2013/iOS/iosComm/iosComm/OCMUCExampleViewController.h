//
//  OCMUCExampleViewController.h
//  iosComm
//
//  Created by Kevin Chen on 12/1/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "OCViewController.h"

@interface OCMUCExampleViewController : UIViewController

@property (strong, nonatomic) IBOutlet UILabel *participantsLabel;
@property (strong, nonatomic) IBOutlet UILabel *fifthMessageLabel;
@property (strong, nonatomic) IBOutlet UIView *fourthMessageLabel;
@property (strong, nonatomic) IBOutlet UILabel *thirdMessageLabel;
@property (strong, nonatomic) IBOutlet UILabel *secondMessageLabel;
@property (strong, nonatomic) IBOutlet UILabel *firstMessageLabel;
@property (strong, nonatomic) IBOutlet UITextView *textMessage;
- (IBAction)sendMessage:(id)sender;

//TODO make this a delegateHandler for MUC chat receivals... for now


@end
