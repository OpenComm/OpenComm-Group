//
//  OCAudioManipulation.m
//  iosComm
//
//  Created by Qiming Fang on 10/18/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCAudioManipulation.h"

@implementation OCAudioManipulation
@synthesize player;

- (void)audioCapture {

    NSString *pathToMusicFile1 = [[NSBundle mainBundle] pathForResource:@"songbird" ofType:@"mp3"];
    NSLog(@"%@", pathToMusicFile1);
    
    player = [[AVAudioPlayer alloc]initWithContentsOfURL:[NSURL fileURLWithPath:pathToMusicFile1] error:NULL];
    
    player.numberOfLoops = -1;
    player.volume = 25.0;
    [player play];
    
    NSLog(@"Shoud have started");
}

@end