//
//  OPCViewController.m
//  iosMedia
//
//  Created by Qiming Fang on 10/25/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OPCViewController.h"
#import "pjmedia.h"

const char* qimingsArguments[1];

@interface OPCViewController ()

@end

@implementation OPCViewController

- (NSString *)getIPAddress {
    
    NSString *address = @"error";
    struct ifaddrs *interfaces = NULL;
    struct ifaddrs *temp_addr = NULL;
    int success = 0;
    // retrieve the current interfaces - returns 0 on success
    success = getifaddrs(&interfaces);
    if (success == 0) {
        // Loop through linked list of interfaces
        temp_addr = interfaces;
        while(temp_addr != NULL) {
            if(temp_addr->ifa_addr->sa_family == AF_INET) {
                // Check if interface is en0 which is the wifi connection on the iPhone
                if([[NSString stringWithUTF8String:temp_addr->ifa_name] isEqualToString:@"en0"]) {
                    // Get NSString from C String
                    address = [NSString stringWithUTF8String:inet_ntoa(((struct sockaddr_in *)temp_addr->ifa_addr)->sin_addr)];
                    
                }
                
            }
            
            temp_addr = temp_addr->ifa_next;
        }
    }
    // Free memory
    freeifaddrs(interfaces);
    return address;
}

const char* GetLocalIP() {
    char buf[256];
    if(gethostname(buf,sizeof(buf)))
        return NULL;
    struct hostent* he = gethostbyname(buf);
    if(!he)
        return NULL;
    for(int i=0; he->h_addr_list[i]; i++) {
        char* ip = inet_ntoa(*(struct in_addr*)he->h_addr_list[i]);
        if(ip != (char*)-1) return ip;
    }
    return NULL;
}

- (IBAction)startConvo:(id)sender
{
    NSString *ip = _ipAddress.text;
    const char* ipChar = [ip UTF8String];
    qimingsArguments[0] = ipChar;
    
    pjmedia_main(1, (char**)qimingsArguments);
}

- (IBAction)stopConvo:(id)sender
{
    pjmedia_antimain();
}

- (void)viewDidLoad
{
    
    [super viewDidLoad];
	
    NSString* ip = [[[OPCViewController alloc] init ] getIPAddress];
    _myIpAddressFieldBecauseTheOtherOneFailed.text = ip;
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // when you touch away from these, then remove the keyboard
    [_ipAddress resignFirstResponder];
    [_ipAddress resignFirstResponder];
}


@end
