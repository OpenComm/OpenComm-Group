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

- (void) playAudio:(NSString *)soundPath {
    
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

-(void)stopAudio
{
    [player stop];
}
-(void)adjustVolume
{
    if (player != nil)
    {
        NSLog (@"Player isnt null");
    }
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
