//
//  OCContactCardViewController.m
//  iosComm
//
//  Created by Sauhard Bindal on 11/7/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCContactCardViewController.h"

@interface OCContactCardViewController ()

@end

@implementation OCContactCardViewController
@synthesize contactLabel;
@synthesize contactName;

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
    contactLabel.text = contactName;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
