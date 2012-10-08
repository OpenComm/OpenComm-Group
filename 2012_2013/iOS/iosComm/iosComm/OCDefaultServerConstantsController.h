//
//  OCDefaultServerConstantsController.h
//  iosComm
//
//  Created by Kevin Chen on 10/4/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface OCDefaultServerConstantsController : NSObject {
	Boolean DEBUG_PARAM;
	NSString *DEFAULT_HOSTNAME;
	NSString *DEFAULT_JID;
	NSString *DEFAULT_PASSWORD;
	int DEFAULT_PORT;
}

@property (nonatomic) Boolean DEBUG_PARAM;
@property (copy, nonatomic) NSString *DEFAULT_HOSTNAME;
@property (copy, nonatomic) NSString *DEFAULT_JID;
@property (copy, nonatomic) NSString *DEFAULT_PASSWORD;
@property (nonatomic) int DEFAULT_PORT;

@end