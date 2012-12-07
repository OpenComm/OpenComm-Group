//
//  OCViewController.m
//  iosComm
//
//  Created by Qiming Fang on 9/23/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCViewController.h"
#import "OCXMPPDelegateHandler.h"
#import <QuartzCore/QuartzCore.h>

@interface OCViewController ()

//@synthesize xmppStream;

@end

@implementation OCViewController
@synthesize loginView;
@synthesize loginPasswordField;
@synthesize loginUsernameField;

//extern variable.
OCXMPPDelegateHandler *delegateHandler;
//OCDefaultServerConstantsController *defaults;


//-------------------------------------------------------------------
// Initial load of this page
//-------------------------------------------------------------------
- (void)viewDidLoad
{
    float cornerRadius = 10.0f;
    [super viewDidLoad];
    
	// Do any additional setup after loading the view, typically from a nib.
    
    /*STUBBED - INITIAL INFORMATION*/
    //Site to do this: https://developer.apple.com/library/mac/#documentation/Cocoa/Conceptual/PropertyLists/QuickStartPlist/QuickStartPlist.html
    
    
    // Do any additional setup after loading the view, typically from a nib.
    
    
    // Round login view edges
    [loginView.layer setCornerRadius:cornerRadius];
    [loginView.layer setBorderColor:[[UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:203.0/255.0 alpha:1.0] CGColor]];
    [loginView.layer setBorderWidth:1.0f];
    
    [loginUsernameField.layer setCornerRadius:cornerRadius];
    [loginPasswordField.layer setCornerRadius:cornerRadius];
    
    
    /*Initialize everything for this application ONLY ONCE*/
    if (delegateHandler == nil) {
        OCDefaultServerConstantsController *defaults = [[OCDefaultServerConstantsController alloc] init];
        delegateHandler = [[OCXMPPDelegateHandler alloc] initWithView:self andDefaults:defaults];
        XMPPStream *myXMPPStream = [[XMPPStream alloc] init];
        
        // ss2249 Storage setup
        // Setup roster
        //
        // The XMPPRoster handles the xmpp protocol stuff related to the roster.
        // The storage for the roster is abstracted.
        // So you can use any storage mechanism you want.
        // You can store it all in memory, or use core data and store it on disk, or use core data with an in-memory store,
        // or setup your own using raw SQLite, or create your own storage mechanism.
        // You can do it however you like! It's your application.
        // But you do need to provide the roster with some storage facility.
        
        XMPPRosterCoreDataStorage *myXMPPRosterStorage = [[XMPPRosterCoreDataStorage alloc] init];
        
        XMPPRoster *myXMPPRoster = [[XMPPRoster alloc] initWithRosterStorage:myXMPPRosterStorage];
        
        myXMPPRoster.autoFetchRoster = YES;
        [myXMPPRoster activate:myXMPPStream];
        [myXMPPRoster addDelegate:delegateHandler delegateQueue:dispatch_get_main_queue()];
        
        /*Connect to the XMPP Stream to make sure the server is connectable to*/
        //the JID must be set to something to connect, even if we will not be using it later.
        //Don't worry -- we're not authenticating with this ID! we're just connecting the stream to the server.
        myXMPPStream.myJID = [XMPPJID jidWithString:[defaults DEFAULT_JID]];
        myXMPPStream.hostName = [defaults DEFAULT_HOSTNAME];
        /*Don't need to set port. The default is always 5222*/
        myXMPPStream.hostPort = [defaults DEFAULT_PORT];
        /*Stream and Roster messages are pushed to the main workload queue*/
        [myXMPPStream addDelegate:delegateHandler delegateQueue:dispatch_get_main_queue()];
        [delegateHandler setXMPPRosterStorage:myXMPPRosterStorage roster:myXMPPRoster stream:myXMPPStream];
        NSError *error = nil;
        
  		NSManagedObjectContext *moc = [delegateHandler managedObjectContext_roster];      
        NSFetchRequest *allUsers = [[NSFetchRequest alloc] init];
        [allUsers setEntity:[NSEntityDescription entityForName:@"XMPPUserCoreDataStorageObject"
                                        inManagedObjectContext:moc]];
        [allUsers setIncludesPropertyValues:NO];
        
        NSArray *users = [moc executeFetchRequest:allUsers error:&error];
        //[allUsers release];
        for (NSManagedObject *user in users) {
            [moc deleteObject:user];
        }
        NSError *saveError = nil;
        [moc save:&saveError];
        
        if (![myXMPPStream connect:&error])
        {
            NSLog(@"Oops, I probably forgot something: %@", error);
        }
    }
    
    /*Test if user is connected online*/
    if ([[[[OCAudioPassingProtocol alloc] init] getIPAddress] isEqualToString: @"error"]) {
        UIAlertView *info = [
                             [UIAlertView alloc]
                             initWithTitle:@"Cannot connect to server"
                             message:@"Please turn your WiFi on before using this application."
                             delegate: nil
                             cancelButtonTitle:@"Dismiss"
                             otherButtonTitles:nil
                             ];
        [info show];
    }
    
    [delegateHandler setViewController: self];
    
    NSLog(@"My IP Address: %@", [[[OCAudioPassingProtocol alloc] init] getIPAddress]);
}


//-------------------------------------------------------------------
// Memory warning
//-------------------------------------------------------------------
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//-------------------------------------------------------------------
// What happens when the login button is pressed
//-------------------------------------------------------------------
- (IBAction)loginButtonPressed:(id)sender {
    
    [delegateHandler setViewController: self]; //so the correct segue is called
    
    NSLog(@"Username: %@, Password: %@", loginUsernameField.text, loginPasswordField.text);
    //Have to get ALL variables from the delegateHandler, because they are not initialized in the viewC
    NSString *myPassword;
    if ([[delegateHandler getDefaults] DEBUG_PARAM]) {
        NSLog(@"DEBUG PARAM SET");
        [delegateHandler myXMPPStream].myJID = [XMPPJID jidWithString:[[delegateHandler getDefaults] DEFAULT_JID]];
        //myXMPPStream.hostName = [defaults DEFAULT_DOMAIN];
        /*Don't need to set port. The default is always 5222*/
        //myXMPPStream.hostPort = [defaults DEFAULT_PORT];
        myPassword = [[delegateHandler getDefaults] DEFAULT_PASSWORD];
    }

    else {
        NSString *myJID =
            [[loginUsernameField.text 
             stringByAppendingString: @"/"]
             stringByAppendingString:[[delegateHandler getDefaults] DEFAULT_RESOURCE]];
        myPassword = loginPasswordField.text;
        NSLog(@"%@", myJID);
        [delegateHandler myXMPPStream].myJID = [XMPPJID jidWithString:myJID];
        //myXMPPStream.hostPort = [defaults DEFAULT_PORT];
    }
    
    NSError *error = nil;
    
    XMPPPlainAuthentication *auth = [[XMPPPlainAuthentication alloc]initWithStream: [delegateHandler myXMPPStream] password: myPassword];
    if (![[delegateHandler myXMPPStream] authenticate:auth error:&error]) {
        NSLog(@"Oops, I probably forgot something: %@", error);
    }}


//-------------------------------------------------------------------
// TODO:Somebody please fill in?
//-------------------------------------------------------------------
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    // when you touch away from these, then remove the keyboard
    [loginUsernameField resignFirstResponder];
    [loginPasswordField resignFirstResponder];
}

- (void)viewDidUnload {
    [self setLoginView:nil];
    [super viewDidUnload];
}

@end
