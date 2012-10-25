//
//  OPCViewController.m
//  iosMedia
//
//  Created by Qiming Fang on 10/25/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OPCViewController.h"
#import "pjmedia.h"

@interface OPCViewController ()

@end

@implementation OPCViewController

- (void)viewDidLoad
{
    
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    pjmedia_main(0, NULL);
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
