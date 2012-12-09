//
//  OCAudioPassingProtocol.h
//  iosComm
//
//  Created by Qiming Fang on 10/20/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <ifaddrs.h>
#import <arpa/inet.h>

@interface OCAudioPassingProtocol : NSObject
- (NSString*)getIPAddress;
@end
