//
//  OCDefaultServerConstantsController.h
//  iosComm
//
//  Created by Kevin Chen on 10/4/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface OCDefaultServerConstantsController : NSObject {
	BOOL *DEBUG;
	NSString *DEFAULT_HOSTNAME;
	NSString *DEFAULT_JID;
	NSString *DEFAULT_PASSWORD;
	int DEFAULT_PORT;
}

@property (copy, nonatomic) BOOL *DEBUG;
@property (copy, nonatomic) NSString *DEAULT_HOSTNAME;
@property (copy, nonatomic) NSString *DEFAULT_JID;
@property (copy, nonatomic) NSString *DEFAULT_PASSWORD;
@property (copy, nonatomic) int DEFAULT_PORT;

@end
