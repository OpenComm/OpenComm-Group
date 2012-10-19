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
