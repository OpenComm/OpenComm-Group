//
//  OCAudioModule.h
//  iosComm
//
//  Created by Qiming Fang on 10/18/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>

@interface OCAudioModule : NSObject

@property (strong, retain) AVAudioPlayer *player;

- (void) playAudioFromRemoteURL:(NSString *) remotePath;
- (void) playAudioFromNSData:(NSData *) data;
- (NSData *) getLocalAudioAsNSData:(NSString *) localPath;

@end
