//
//  OCUDPDelegateHandler.m
//  iosComm
//
//  Created by Kevin Chen on 10/18/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCUDPDelegateHandler.h"
#include <netdb.h>

/*This code is based off of the UDPEcho Apple tutorial, although modified:
 *http://developer.apple.com/library/mac/#samplecode/UDPEcho/Introduction/Intro.html#//apple_ref/doc/uid/DTS40009660-Intro-DontLinkElementID_2
 *We may modify it as we please... attached disclaimer below.
 *taking out the relevant socket stuff, and removing the echo protocol
 
 Written by: DTS
 
 Copyright:  Copyright (c) 2010-12 Apple Inc. All Rights Reserved.
 
 Disclaimer: IMPORTANT: This Apple software is supplied to you by Apple Inc.
 ("Apple") in consideration of your agreement to the following
 terms, and your use, installation, modification or
 redistribution of this Apple software constitutes acceptance of
 these terms.  If you do not agree with these terms, please do
 not use, install, modify or redistribute this Apple software.
 
 In consideration of your agreement to abide by the following
 terms, and subject to these terms, Apple grants you a personal,
 non-exclusive license, under Apple's copyrights in this
 original Apple software (the "Apple Software"), to use,
 reproduce, modify and redistribute the Apple Software, with or
 without modifications, in source and/or binary forms; provided
 that if you redistribute the Apple Software in its entirety and
 without modifications, you must retain this notice and the
 following text and disclaimers in all such redistributions of
 the Apple Software. Neither the name, trademarks, service marks
 or logos of Apple Inc. may be used to endorse or promote
 products derived from the Apple Software without specific prior
 written permission from Apple.  Except as expressly stated in
 this notice, no other rights or licenses, express or implied,
 are granted by Apple herein, including but not limited to any
 patent rights that may be infringed by your derivative works or
 by other works in which the Apple Software may be incorporated.
 
 The Apple Software is provided by Apple on an "AS IS" basis.
 APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING
 WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT,
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING
 THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN
 COMBINATION WITH YOUR PRODUCTS.
 
 IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT,
 INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY
 OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION
 OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY
 OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR
 OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.*/


@implementation OCUDPDelegateHandler

@synthesize UDPSocket = _UDPSocket;

static NSString * DisplayAddressForAddress(NSData * address)
// Returns a dotted decimal string for the specified address (a (struct sockaddr)
// within the address NSData).
{
    int         err;
    NSString *  result;
    char        hostStr[NI_MAXHOST];
    char        servStr[NI_MAXSERV];
    
    result = nil;
    
    if (address != nil) {
        
        // If it's a IPv4 address embedded in an IPv6 address, just bring it as an IPv4
        // address.  Remember, this is about display, not functionality, and users don't
        // want to see mapped addresses.
        
        if ([address length] >= sizeof(struct sockaddr_in6)) {
            const struct sockaddr_in6 * addr6Ptr;
            
            addr6Ptr = [address bytes];
            if (addr6Ptr->sin6_family == AF_INET6) {
                if ( IN6_IS_ADDR_V4MAPPED(&addr6Ptr->sin6_addr) || IN6_IS_ADDR_V4COMPAT(&addr6Ptr->sin6_addr) ) {
                    struct sockaddr_in  addr4;
                    
                    memset(&addr4, 0, sizeof(addr4));
                    addr4.sin_len         = sizeof(addr4);
                    addr4.sin_family      = AF_INET;
                    addr4.sin_port        = addr6Ptr->sin6_port;
                    addr4.sin_addr.s_addr = addr6Ptr->sin6_addr.__u6_addr.__u6_addr32[3];
                    address = [NSData dataWithBytes:&addr4 length:sizeof(addr4)];
                    assert(address != nil);
                }
            }
        }
        err = getnameinfo([address bytes], (socklen_t) [address length], hostStr, sizeof(hostStr), servStr, sizeof(servStr), NI_NUMERICHOST | NI_NUMERICSERV);
        if (err == 0) {
            result = [NSString stringWithFormat:@"%s:%s", hostStr, servStr];
            assert(result != nil);
        }
    }
    
    return result;
}

static NSString * DisplayStringFromData(NSData *data)
// Returns a human readable string for the given data.
{
    NSMutableString *   result;
    NSUInteger          dataLength;
    NSUInteger          dataIndex;
    const uint8_t *     dataBytes;
    
    assert(data != nil);
    
    dataLength = [data length];
    dataBytes  = [data bytes];
    
    result = [NSMutableString stringWithCapacity:dataLength];
    assert(result != nil);
    
    [result appendString:@"\""];
    for (dataIndex = 0; dataIndex < dataLength; dataIndex++) {
        uint8_t     ch;
        
        ch = dataBytes[dataIndex];
        if (ch == 10) {
            [result appendString:@"\n"];
        } else if (ch == 13) {
            [result appendString:@"\r"];
        } else if (ch == '"') {
            [result appendString:@"\\\""];
        } else if (ch == '\\') {
            [result appendString:@"\\\\"];
        } else if ( (ch >= ' ') && (ch < 127) ) {
            [result appendFormat:@"%c", (int) ch];
        } else {
            [result appendFormat:@"\\x%02x", (unsigned int) ch];
        }
    }
    [result appendString:@"\""];
    
    return result;
}

static NSString * DisplayErrorFromError(NSError *error)
// Given an NSError, returns a short error string that we can print, handling
// some special cases along the way.
{
    NSString *      result;
    NSNumber *      failureNum;
    int             failure;
    const char *    failureStr;
    
    assert(error != nil);
    
    result = nil;
    
    // Handle DNS errors as a special case.
    
    if ( [[error domain] isEqual:(NSString *)kCFErrorDomainCFNetwork] && ([error code] == kCFHostErrorUnknown) ) {
        failureNum = [[error userInfo] objectForKey:(id)kCFGetAddrInfoFailureKey];
        if ( [failureNum isKindOfClass:[NSNumber class]] ) {
            failure = [failureNum intValue];
            if (failure != 0) {
                failureStr = gai_strerror(failure);
                if (failureStr != NULL) {
                    result = [NSString stringWithUTF8String:failureStr];
                    assert(result != nil);
                }
            }
        }
    }
    
    // Otherwise try various properties of the error object.
    
    if (result == nil) {
        result = [error localizedFailureReason];
    }
    if (result == nil) {
        result = [error localizedDescription];
    }
    if (result == nil) {
        result = [error description];
    }
    assert(result != nil);
    return result;
}


- (void)echo:(OCUDPSocket *)echo didReceiveData:(NSData *)data fromAddress:(NSData *)addr
// This UDPEcho delegate method is called after successfully receiving data.
{
    //assert(echo == self.echo);
#pragma unused(echo)
    assert(data != nil);
    assert(addr != nil);
    NSLog(@"received %@ from %@", DisplayStringFromData(data), DisplayAddressForAddress(addr));
}

- (void)echo:(OCUDPSocket *)echo didReceiveError:(NSError *)error
// This UDPEcho delegate method is called after a failure to receive data.
{
    //assert(echo == self.echo);
#pragma unused(echo)
    assert(error != nil);
    NSLog(@"received error: %@", DisplayErrorFromError(error));
}

- (void)echo:(OCUDPSocket *)echo didSendData:(NSData *)data toAddress:(NSData *)addr
// This UDPEcho delegate method is called after successfully sending data.
{
    //assert(echo == self.echo);
//#pragma unused(echo)
    assert(data != nil);
    assert(addr != nil);
    NSLog(@"    sent %@ to   %@", DisplayStringFromData(data), DisplayAddressForAddress(addr));
}

- (void)echo:(OCUDPSocket *)echo didFailToSendData:(NSData *)data toAddress:(NSData *)addr error:(NSError *)error
// This UDPEcho delegate method is called after a failure to send data.
{
    //assert(echo == self.echo);
#pragma unused(echo)
    assert(data != nil);
    assert(addr != nil);
    assert(error != nil);
    NSLog(@" sending %@ to   %@, error: %@", DisplayStringFromData(data), DisplayAddressForAddress(addr), DisplayErrorFromError(error));
}

- (void)echo:(OCUDPSocket *)echo didStartWithAddress:(NSData *)address
// This UDPEcho delegate method is called after the object has successfully started up.
{
    NSLog(@"I started");
    //assert(echo == self.echo);
#pragma unused(echo)
    assert(address != nil);
    
    if (self.UDPSocket.isServer) {
        NSLog(@"receiving on %@", DisplayAddressForAddress(address));
    } else {
        NSLog(@"sending to %@", DisplayAddressForAddress(address));
    }
    
    if ( ! self.UDPSocket.isServer ) {
        //[self sendPacket];
        
        //assert(self.sendTimer == nil);
        //self.sendTimer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(sendPacket) userInfo:nil repeats:YES];
    } 
}

- (void)echo:(OCUDPSocket *)echo didStopWithError:(NSError *)error
// This UDPEcho delegate method is called  after the object stops spontaneously.
{
    //assert(echo == self.echo);
#pragma unused(echo)
    assert(error != nil);
    NSLog(@"failed with error: %@", DisplayErrorFromError(error));
    //self.echo = nil;
}

- (void)sendPacket
// Called by the client code to send a UDP packet.  This is called immediately
// after the client has 'connected', and periodically after that from the send
// timer.
{
    NSLog(@"In Send Packet");
    NSData *    data;
    assert(self.UDPSocket != nil);
    assert( ! self.UDPSocket.isServer );
    
    data = [[NSString stringWithFormat:@"99 bottles of beer on the wall"] dataUsingEncoding:NSUTF8StringEncoding];
    assert(data != nil);
    NSLog(@"About to send this data: %@", data);
    [self.UDPSocket sendData:data];
}


@end
