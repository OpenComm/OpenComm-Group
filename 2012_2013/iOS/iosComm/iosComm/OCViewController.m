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
    
    
    //a separate class handler for default XMPP stuff.
	defaults = [[OCDefaultServerConstantsController alloc] init];
    delegateHandler = [[OCXMPPDelegateHandler alloc] init];
    myXMPPStream = [[XMPPStream alloc] init];
    
    myXMPPRosterStorage = [[XMPPRosterCoreDataStorage alloc] init];
    myXMPPRoster = [[XMPPRoster alloc] initWithRosterStorage:myXMPPRosterStorage];
    myXMPPRoster.autoFetchRoster = YES;
    [myXMPPRoster activate:myXMPPStream];
    
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
// Kevin please fill in?
//-------------------------------------------------------------------
/*
- (void)xmppStreamDidStartNegotiation:(XMPPStream *)sender
{
    NSLog(@"I'm starting the negotiation");
}*/

//-------------------------------------------------------------------
// What happens when the login button is pressed
//-------------------------------------------------------------------
- (IBAction)loginButtonPressed:(id)sender {
    NSLog(@"Username: %@, Password: %@", loginUsernameField.text, loginPasswordField.text);
    if ([loginUsernameField.text isEqualToString: @"test"] && [loginPasswordField.text isEqualToString: @"test"]){
         [self performSegueWithIdentifier:@"successfulLogin" sender:nil];
    }
    else if ([loginUsernameField.text isEqualToString: @"test2"] && [loginPasswordField.text isEqualToString: @"confidential"]){
        [self performSegueWithIdentifier:@"secretAudio" sender:nil];
    }
    else {
        UIAlertView *info = [
                             [UIAlertView alloc]
                             initWithTitle:@"Incorrect Username/Password"
                             message:@"Username/Password combination not found.\nPlease try again with correct details."
                             delegate:self
                             cancelButtonTitle:@"Dismiss"
                             otherButtonTitles:nil
                             ];
        [info show];
    }
    if ([defaults DEBUG_PARAM]) {
        NSLog(@"DEBUG PARAM SET");
        myXMPPStream.myJID = [XMPPJID jidWithString:[defaults DEFAULT_JID]];
        myXMPPStream.hostName = [defaults DEFAULT_DOMAIN];
        /*Don't need to set port. The default is always 5222*/
        myXMPPStream.hostPort = [defaults DEFAULT_PORT];
        myPassword = [defaults DEFAULT_PASSWORD];
    }

    else {
        NSString *myJID =
            [[[[loginUsernameField.text stringByAppendingString: @"@"]
             stringByAppendingString:[defaults DEFAULT_DOMAIN]]
             stringByAppendingString: @"/"]
             stringByAppendingString:[defaults DEFAULT_RESOURCE]];
        myPassword = loginPasswordField.text;
        NSLog(@"%@", myJID);
        myXMPPStream.myJID = [XMPPJID jidWithString:myJID];
        myXMPPStream.hostPort = [defaults DEFAULT_PORT];
    }
    
    delegateHandler = [[OCXMPPDelegateHandler alloc] initWithPassword: myPassword];
    [delegateHandler setXMPPRosterStorage:myXMPPRosterStorage roster:myXMPPRoster stream:myXMPPStream];
    
    [myXMPPStream addDelegate:delegateHandler delegateQueue:dispatch_get_main_queue()];
    [myXMPPRoster addDelegate:delegateHandler delegateQueue:dispatch_get_main_queue()];
    NSError *error = nil;
    
    NSLog(@"stream's JID: %@", myXMPPStream.myJID);
    
    if (![myXMPPStream isDisconnected]) {
        NSLog(@"I'm already connected");
    }
    
    /* THIS IS SOCKET STUFF*/
    /*
    UDPSocket = [[OCUDPSocket alloc] init];
    UDPDelegateHandler = [[OCUDPDelegateHandler alloc] init];
    [UDPSocket setDelegate:UDPDelegateHandler];
    [UDPDelegateHandler setUDPSocket:UDPSocket];
    */
    
    /* THIS IS THE CLIENT STATEMENT*/
    //[UDPSocket startConnectedToHostName:@"ec2-50-16-95-237.compute-1.amazonaws.com" port: 8001];
    /* THIS IS THE SERVER STATEMENT*/
    //[UDPSocket startServerOnPort: 8001];
    
    
    /*This only returns an error if JID and hostname are not set, which is dumb
     *The method is asynchronous, so it returns even though there is not a full connection*/
    if (![myXMPPStream connect:&error])
    {
        NSLog(@"Oops, I probably forgot something: %@", error);
    }
    
    //NSIndexPath *path = [NSIndexPath indexPathForRow:0 inSection:0];
    //XMPPUserCoreDataStorageObject *user = [[self fetchedResultsController] objectAtIndexPath:path];
    //NSLog(@"%@", [[self fetchedResultsController] sections]);
}

- (NSFetchedResultsController *)fetchedResultsController
{
	if (fetchedResultsController == nil)
	{
		NSManagedObjectContext *moc = [delegateHandler managedObjectContext_roster];
		
		NSEntityDescription *entity = [NSEntityDescription entityForName:@"XMPPUserCoreDataStorageObject"
		                                          inManagedObjectContext:moc];
		
		NSSortDescriptor *sd1 = [[NSSortDescriptor alloc] initWithKey:@"sectionNum" ascending:YES];
		NSSortDescriptor *sd2 = [[NSSortDescriptor alloc] initWithKey:@"displayName" ascending:YES];
		
		NSArray *sortDescriptors = [NSArray arrayWithObjects:sd1, sd2, nil];
		
		NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
		[fetchRequest setEntity:entity];
		[fetchRequest setSortDescriptors:sortDescriptors];
		[fetchRequest setFetchBatchSize:10];
		
		fetchedResultsController = [[NSFetchedResultsController alloc] initWithFetchRequest:fetchRequest
		                                                               managedObjectContext:moc
		                                                                 sectionNameKeyPath:@"sectionNum"
		                                                                          cacheName:nil];
		
		NSError *error = nil;
		if (![fetchedResultsController performFetch:&error])
		{
			NSLog(@"Error performing fetch: %@", error);
		}
        
	}
	
	return fetchedResultsController;
}

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
- (IBAction)fetch:(id)sender {
    id <NSFetchedResultsSectionInfo> sectionInfo = [[[self fetchedResultsController] sections] objectAtIndex:1];
    NSLog(@"%u", sectionInfo.numberOfObjects);
    NSIndexPath *path = [NSIndexPath indexPathForRow:1 inSection:0];
    XMPPUserCoreDataStorageObject *user = [[self fetchedResultsController] objectAtIndexPath:path];
    NSLog(@"%@", user.displayName);
}

@end
