//
//  OCAudioModule.h
//  iosComm
//
//  Created by Qiming Fang on 10/18/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>
#import <Audiotoolbox/AudioQueue.h>

#define NUM_BUFFERS 3
#define SECONDS_TO_RECORD 10

typedef struct
{
    AudioStreamBasicDescription  dataFormat;
    AudioQueueRef                queue;
    AudioQueueBufferRef          buffers[NUM_BUFFERS];
    AudioFileID                  audioFile;
    SInt64                       currentPacket;
    bool                         recording;
} RecordState;

@interface OCAudioModule : NSObject {
    RecordState recordState;
    CFURLRef fileURL;
}

@property (strong, retain) AVAudioPlayer *player;

- (void) playAudioFromRemoteURL:(NSString *) remotePath;
- (void) playAudioFromNSData:(NSData *) data;
- (NSData *) getLocalAudioAsNSData:(NSString *) localPath;
- (void)startRecording;
- (void)stopRecording;
- (BOOL)getFilename:(char*)buffer maxLenth:(int)maxBufferLength;

@end
