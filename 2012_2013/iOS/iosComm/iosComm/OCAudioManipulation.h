//
//  OCAudioManipulation.h
//  iosComm
//
//  Created by Qiming Fang on 10/18/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AudioToolbox/AudioToolbox.h>
#import <AVFoundation/AVFoundation.h>

@interface OCAudioManipulation : NSObject

@property (nonatomic, strong) AVAudioPlayer *player;

- (void)audioCapture;

@end