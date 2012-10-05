//
//  OCDefaultServerConstantsController.m
//  iosComm
//
//  Created by Kevin Chen on 10/4/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCDefaultServerConstantsController.h"

@implementation OCDefaultServerConstantsController

 - (id) init {
 
    self = [super init];
    if (self) {
        NSString *errorDesc = nil;
        NSPropertyListFormat format;
        NSString *plistPath;
		// this should be the current directory
        NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
           NSUserDomainMask, YES) objectAtIndex:0];
        plistPath = [rootPath stringByAppendingPathComponent:@"Data.plist"];
        if (![[NSFileManager defaultManager] fileExistsAtPath:plistPath]) {
            plistPath = [[NSBundle mainBundle] pathForResource:@"Data" ofType:@"plist"];
        }
        NSData *plistXML = [[NSFileManager defaultManager] contentsAtPath:plistPath];
        NSDictionary *temp = (NSDictionary *)[NSPropertyListSerialization
            propertyListFromData:plistXML
            mutabilityOption:NSPropertyListMutableContainersAndLeaves
            format:&format
            errorDescription:&errorDesc];
        if (!temp) {
            NSLog(@"Error reading plist: %@, format: %d", errorDesc, format);
        }
        self.DEBUG = [temp objectForKey:@"DEBUG"];
		self.DEFAULT_JID = [temp objectForKey:@"DEFAULT_JID"];
		self.DEFAULT_HOSTNAME = [temp objectForKey:@"DEFAULT_HOSTNAME"];
 		self.DEFAULT_PASSWORD = [temp objectForKey:@"DEFAULT_PASSWORD"];
		self.DEFAULT_PORT = [temp objectForKey:@"DEFAULT_PORT"];
    }
    return self;

}

@synthesize DEBUG;
@synthesize DEFAULT_HOSTNAME;
@synthesize DEFAULT_JID;
@synthesize DEFAULT_PASSWORD;
@synthesize DEFAULT_PORT;

@end
