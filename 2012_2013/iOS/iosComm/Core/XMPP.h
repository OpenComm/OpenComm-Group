// 
// Core classes
// 

#import "XMPPJID.h"
#import "XMPPStream.h"
#import "XMPPElement.h"
#import "XMPPIQ.h"
#import "XMPPMessage.h"
#import "XMPPPresence.h"
#import "XMPPModule.h"

// 
// Authentication
// 

#import "../Authentication/XMPPSASLAuthentication.h"
#import "../Authentication/Digest-MD5/XMPPDigestMD5Authentication.h"
#import "../Authentication/Plain/XMPPPlainAuthentication.h"
#import "../Authentication/X-Facebook-Platform/XMPPXFacebookPlatformAuthentication.h"
#import "../Authentication/Anonymous/XMPPAnonymousAuthentication.h"
#import "../Authentication/Deprecated-Plain/XMPPDeprecatedPlainAuthentication.h"
#import "../Authentication/Deprecated-Digest/XMPPDeprecatedDigestAuthentication.h"

// 
// Categories
// 

#import "../Categories/NSXMLElement+XMPP.h"
