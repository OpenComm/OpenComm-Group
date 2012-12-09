//
//  OCUDPDelegateHandler.h
//  iosComm
//
//  Created by Kevin Chen on 10/18/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "OCUDPSocket.h"
#import "OCAudioModule.h"

/** These delegate methods are defined in OCUDPSocket.h 
 ** These are called whenever things happen to a OCUDPSocket.
 **/
@interface OCUDPDelegateHandler : NSObject <OCUDPDelegate> {
    OCAudioModule *ourRevolution;
}

@property (nonatomic, strong, readwrite) OCUDPSocket *UDPSocket;

- (void)echo:(OCUDPSocket *)echo didReceiveData:(NSData *)data fromAddress:(NSData *)addr;

- (void)echo:(OCUDPSocket *)echo didReceiveError:(NSError *)error;

- (void)echo:(OCUDPSocket *)echo didSendData:(NSData *)data toAddress:(NSData *)addr;

- (void)echo:(OCUDPSocket *)echo didFailToSendData:(NSData *)data toAddress:(NSData *)addr error:(NSError *)error;

- (void)echo:(OCUDPSocket *)echo didStartWithAddress:(NSData *)address;

- (void)echo:(OCUDPSocket *)echo didStopWithError:(NSError *)error;

@end
