//
//  OCAudioModule.m
//  iosComm
//
//  Created by Qiming Fang on 10/18/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCAudioModule.h"

@implementation OCAudioModule

@synthesize player;

- init {
    char path[256];
    [self getFilename:path maxLenth:sizeof path];
    fileURL = CFURLCreateFromFileSystemRepresentation(NULL, (UInt8*)path, strlen(path), false);
    
    return self;
}

- (void) playAudioFromRemoteURL:(NSString *)soundPath {
    
    // Retrieve the sound remotely from server
    NSURL *soundFileURL = [NSURL URLWithString:soundPath];
    NSData *soundData = [NSData dataWithContentsOfURL:soundFileURL];
    NSError *error;
    
    player = [[AVAudioPlayer alloc] initWithData:soundData error:&error];
    
    if (error) {
        NSLog(@"Error in audioPlayer: %@", [error localizedDescription]);
    } else {
        [player prepareToPlay];
        [player play];
    }
}

- (void) playAudioFromNSData:(NSData *) data{
    
    NSError *error;
    player = [[AVAudioPlayer alloc] initWithData:data error:&error];
    
    if (error) {
        NSLog(@"Error in audioPlayer: %@", [error localizedDescription]);
    } else {
        [player prepareToPlay];
        [player play];
    }
}

- (NSData *) getLocalAudioAsNSData:(NSString *) path{
    return [NSData dataWithContentsOfFile:path];
}

void AudioInputCallback(void * inUserData,
                        AudioQueueRef inAQ,
                        AudioQueueBufferRef inBuffer,
                        const AudioTimeStamp * inStartTime,
                        UInt32 inNumberPacketDescriptions,
                        const AudioStreamPacketDescription * inPacketDescs)
{
	RecordState * recordState = (RecordState*)inUserData;
    if (!recordState->recording)
    {
        printf("Not recording, returning\n");
    }
    
    printf("Writing buffer %lld\n", recordState->currentPacket);
    OSStatus status = AudioFileWritePackets(recordState->audioFile,
                                            false,
                                            inBuffer->mAudioDataByteSize,
                                            inPacketDescs,
                                            recordState->currentPacket,
                                            &inNumberPacketDescriptions,
                                            inBuffer->mAudioData);
    if (status == 0)
    {
        recordState->currentPacket += inNumberPacketDescriptions;
    }
    
    AudioQueueEnqueueBuffer(recordState->queue, inBuffer, 0, NULL);
}


- (void)setupAudioFormat:(AudioStreamBasicDescription*)format
{
	format->mSampleRate = 44100;
	format->mFormatID = kAudioFormatLinearPCM;
	format->mFramesPerPacket = 1;
	format->mChannelsPerFrame = 1;
	format->mBytesPerFrame = 2;
	format->mBytesPerPacket = 2;
	format->mBitsPerChannel = 16;
	format->mReserved = 0;
	format->mFormatFlags = kLinearPCMFormatFlagIsBigEndian     |
    kLinearPCMFormatFlagIsSignedInteger |
    kLinearPCMFormatFlagIsPacked;
}

- (void)startRecording
{
    [self setupAudioFormat:&recordState.dataFormat];
    
    recordState.currentPacket = 0;
	
    OSStatus status;
    status = AudioQueueNewInput(&recordState.dataFormat,
                                AudioInputCallback,
                                &recordState,
                                CFRunLoopGetCurrent(),
                                kCFRunLoopCommonModes,
                                0,
                                &recordState.queue);
    
    if (status == 0)
    {
        // Prime recording buffers with empty data
        for (int i = 0; i < NUM_BUFFERS; i++)
        {
            AudioQueueAllocateBuffer(recordState.queue, 16000, &recordState.buffers[i]);
            AudioQueueEnqueueBuffer (recordState.queue, recordState.buffers[i], 0, NULL);
        }
        
        status = AudioFileCreateWithURL(fileURL,
                                        kAudioFileAIFFType,
                                        &recordState.dataFormat,
                                        kAudioFileFlags_EraseFile,
                                        &recordState.audioFile);
        if (status == 0)
        {
            recordState.recording = true;
            status = AudioQueueStart(recordState.queue, NULL);
            if (status == 0)
            {
                NSLog(@"Recording");
            }
        }
    }
    
    if (status != 0)
    {
        [self stopRecording];
        NSLog(@"Record Failed");
    }
}

- (void)stopRecording
{
    recordState.recording = false;
    
    AudioQueueStop(recordState.queue, true);
    for(int i = 0; i < NUM_BUFFERS; i++)
    {
        AudioQueueFreeBuffer(recordState.queue, recordState.buffers[i]);
    }
    
    AudioQueueDispose(recordState.queue, true);
    AudioFileClose(recordState.audioFile);
    NSLog(@"Idle");
}

- (BOOL)getFilename:(char*)buffer maxLenth:(int)maxBufferLength
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
                                                         NSUserDomainMask, YES);
    NSString* docDir = [paths objectAtIndex:0];
    
    NSString* file = [docDir stringByAppendingString:@"/recording.aif"];
    NSLog(@"Recording File: %@", file);
    
    
    return [file getCString:buffer maxLength:maxBufferLength encoding:NSUTF8StringEncoding];
}

-(void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag
{
}
-(void)audioPlayerDecodeErrorDidOccur:(AVAudioPlayer *)player error:(NSError *)error
{
}
-(void)audioPlayerBeginInterruption:(AVAudioPlayer *)player
{
}
-(void)audioPlayerEndInterruption:(AVAudioPlayer *)player
{
}

@end
