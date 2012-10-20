//
//  OCAudio.h
//  iosComm
//
//  Created by Qiming Fang on 10/20/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Novocaine.h"
#import "RingBuffer.h"

@interface OCAudio : NSObject {
    Novocaine *audioManager;
    RingBuffer *ringBuffer;
}

- (void) play;
- (void) record;

@end
