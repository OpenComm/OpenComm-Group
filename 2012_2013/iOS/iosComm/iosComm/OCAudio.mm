//
//  OCAudio.m
//  iosComm
//
//  Created by Qiming Fang on 10/20/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCAudio.h"

@implementation OCAudio

- init {
    ringBuffer = new RingBuffer(32768, 2);
    audioManager = [Novocaine audioManager];
}

- (void) play{
    [audioManager setOutputBlock:^(float *outData, UInt32 numFrames, UInt32 numChannels) {
        ringBuffer->FetchInterleavedData(outData, numFrames, numChannels);
    }];
}

- (void) record{
    [audioManager setInputBlock:^(float *data, UInt32 numFrames, UInt32 numChannels) {
        float volume = 0.5;
        
        printf("%ld\n", numFrames);
        printf("%ld\n", numChannels);
        
        vDSP_vsmul(data, 1, &volume, data, 1, numFrames*numChannels);
        ringBuffer->AddNewInterleavedFloatData(data, numFrames, numChannels);
    }];
}

@end
