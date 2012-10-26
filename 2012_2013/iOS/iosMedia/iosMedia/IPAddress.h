//
//  IPAddress.h
//  iosMedia
//
//  Created by Qiming Fang on 10/25/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#ifndef iosMedia_IPAddress_h
#define iosMedia_IPAddress_h

#define MAXADDRS	32

extern char *if_names[MAXADDRS];
extern char *ip_names[MAXADDRS];
extern char *hw_addrs[MAXADDRS];
extern unsigned long ip_addrs[MAXADDRS];

// Function prototypes

void InitAddresses();
void FreeAddresses();
void GetIPAddresses();
void GetHWAddresses();

#endif
