//
//  OCAccountSettingsViewController.m
//  iosComm
//
//  Created by Sauhard Bindal on 11/29/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCAccountSettingsViewController.h"
#import "OCJingleImpl.h"

@interface OCAccountSettingsViewController ()

@end

@implementation OCAccountSettingsViewController

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

- (void) viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    NSLog(@"Account Setting");
    currentViewController = self;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
