//
//  OCContactsViewController.m
//  iosComm
//
//  Created by Sauhard Bindal on 11/4/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCContactsViewController.h"
#import "OCContactCell.h"
#import "OCContactCardViewController.h"
#import "OCXMPPDelegateHandler.h"
#import "OCViewController.h"
#import "OCConferenceViewController.h"
#import "OCJingleImpl.h"
#import "OCSingleCallViewController.h"
@implementation OCContactsViewController /* Sweet Commented out{
    NSMutableArray* contactsArray;
    NSMutableArray* imageArray;
    NSArray* indexArray; // To create horizontal index on the right
    NSMutableDictionary* sections;
    NSInteger currentSection; // The current section of the index the user is at, not to change if no contact exists with touched letter.
    NSArray* searchResults;
    NSMutableArray* contactsList;
    //OCXMPPDelegateHandler *delegateHandler; //to set the appropriate jingle session
} */

//defined externally in OCViewController
//OCXMPPDelegateHandler *delegateHandler;

//@synthesize tableView;
- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

#pragma mark View lifecycle

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



- (void)viewWillAppear:(BOOL)animated

{
    
    [super viewWillAppear:animated];
    //NSLog(@"Contacs");
    //currentViewController = self;
    
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 400, 44)];
    
    titleLabel.backgroundColor = [UIColor clearColor];
    
    titleLabel.textColor = [UIColor whiteColor];
    
    titleLabel.font = [UIFont boldSystemFontOfSize:20.0];
    
    titleLabel.numberOfLines = 1;
    
    titleLabel.adjustsFontSizeToFitWidth = YES;
    
    titleLabel.shadowColor = [UIColor colorWithWhite:0.0 alpha:0.5];
    
    titleLabel.textAlignment = UITextAlignmentCenter;
    
    
        titleLabel.text = @"contacts";
    
    
    [titleLabel sizeToFit];
    
    
    
    self.navigationItem.titleView = titleLabel;
    
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    NSLog(@"Contacs");
    currentViewController = self;
}

- (void)viewWillDisappear:(BOOL)animated

{
    
    [super viewWillDisappear:animated];
    
}

/*
- (void)viewDidLoad
{
    [super viewDidLoad];
    //contactsArray = [NSArray arrayWithObjects:@"Sofia", @"Giulia", @"Andrea", @"Francesco", @"Alessandro", @"Giorgia", @"Matteo", @"Lorenzo", @"Martina", @"Sara", nil];
    contactsArray = [NSMutableArray arrayWithObjects: @"Sam's Iphone", @"Alabama", @"Alaska", @"American Samoa", @"Arizona", @"Arkansas", @"California", @"Colorado", @"Connecticut", @"Delaware", @"District of Columbia", @"Florida", @"Georgia", @"Guam", @"Hawaii", @"Idaho", @"Illinois", @"Indiana", @"Iowa", @"Kansas", @"Kentucky", @"Louisiana", @"Maine", @"Maryland", @"Massachusetts", @"Michigan", @"Minnesota", @"Mississippi", @"Missouri", @"Montana", @"Nebraska", @"Nevada", @"New Hampshire", @"New Jersey", @"New Mexico", @"New York", @"North Carolina", @"North Dakota", @"Northern Marianas Islands", @"Ohio", @"Oklahoma", @"Oregon", @"Pennsylvania", @"Puerto Rico", @"Rhode Island", @"South Carolina", @"South Dakota", @"Tennessee", @"Texas", @"Utah", @"Vermont", @"Virginia", @"Virgin Islands", @"Washington", @"West Virginia", @"Wisconsin", @"Wyoming", nil];
    imageArray = [NSMutableArray arrayWithObjects:@"zoo1.jpeg", @"zoo2.jpeg", @"zoo3.jpeg", @"zoo4.jpeg", @"zoo5.jpeg", @"zoo6.jpeg", @"zoo7.jpeg", @"zoo8.jpeg", @"zoo9.jpeg", @"zoo10.jpeg", nil];
    sections = [[NSMutableDictionary alloc] init];
    contactsList = [[NSMutableArray alloc] init];
    [self makeContactsArray:contactsArray :imageArray];
    //[self initializeIndexArray];
    [self setUpContactsList];
    currentSection = 0;
    indexArray = [[sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)];
    
    
    [self.tableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:0 inSection:0] atScrollPosition:UITableViewScrollPositionTop animated:NO];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)makeContactsArray: (NSMutableArray *)contacts: (NSMutableArray *) images
{
    NSLog(@"In makeContactsArray");
    int contactsSize = [contacts count];
    int imagesSize = [images count];

    if (imagesSize<contactsSize){
        for (int j = 0; j<(contactsSize-imagesSize); j++){
            [images addObject:@"default.png"];
        }
    }
    
    for (int i = 0; i<contactsSize; i++){
        NSMutableDictionary *contactItem = [[NSMutableDictionary alloc]init];
        [contactItem setValue:[contacts objectAtIndex:i] forKey:@"Name"];
        
        [contactItem setValue:[images objectAtIndex:i] forKey:@"Image"];
        
        //NSLog(@"ContactItem: %@", contactItem);
        [contactsList addObject:contactItem];
        //[contactItem removeAllObjects];
    }
    NSLog(@"Array: %@", contactsList);
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    if (tableView == self.searchDisplayController.searchResultsTableView) {
        return 1;
    }
    else {
        return [[sections allKeys] count];
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    if (tableView == self.searchDisplayController.searchResultsTableView) {
        return [searchResults count];
        
    } else {
    
        return [[sections valueForKey:[[[sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)] objectAtIndex:section]] count];
    }
}

- (void)filterContentForSearchText:(NSString*)searchText scope:(NSString*)scope
{
    NSPredicate *resultPredicate = [NSPredicate
                                    predicateWithFormat:@"SELF contains[cd] %@",
                                    searchText];
    
    searchResults = [contactsArray filteredArrayUsingPredicate:resultPredicate];
}

-(BOOL)searchDisplayController:(UISearchDisplayController *)controller
shouldReloadTableForSearchString:(NSString *)searchString
{
    [self filterContentForSearchText:searchString
                               scope:[[self.searchDisplayController.searchBar scopeButtonTitles]
                                      objectAtIndex:[self.searchDisplayController.searchBar
                                                     selectedScopeButtonIndex]]];
    
    return YES;
}
 */

// ss2249 TODO SAUHARD for you
 - (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
 {
     return [[[self fetchedResultsController] sections] count];
     //return 0;
 }
 
 - (NSString *)tableView:(UITableView *)sender titleForHeaderInSection:(NSInteger)sectionIndex
 {
     NSArray *sections = [[self fetchedResultsController] sections];
 
     if (sectionIndex < [sections count])
     {
         id <NSFetchedResultsSectionInfo> sectionInfo = [sections objectAtIndex:sectionIndex];
 
         int section = [sectionInfo.name intValue];
//         switch (section)
//         {
//             case 0  : return @"Available";
//             case 1  : return @"Away";
//             default : return @"Offline";
//         }
     }
 
     return @"";
 }
 
 - (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)sectionIndex
 {
 NSArray *sections = [[self fetchedResultsController] sections];
 
 if (sectionIndex < [sections count])
 {
 id <NSFetchedResultsSectionInfo> sectionInfo = [sections objectAtIndex:sectionIndex];
 return sectionInfo.numberOfObjects;
 }
 
 return 0;
 }
 
 - (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
 {
 static NSString *CellIdentifier = @"Cell";
 
 UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
 if (cell == nil)
 {
 cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
 reuseIdentifier:CellIdentifier];
 }
 
 XMPPUserCoreDataStorageObject *user = [[self fetchedResultsController] objectAtIndexPath:indexPath];
 
 cell.textLabel.text = user.displayName;
 
 return cell;
 }
 // Sweet finish Sauhard todo

/* Sweet COmmented Out

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"ContactCell";
    OCContactCell *cell = (OCContactCell *) [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    //UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"ContactCell" owner:self options:nil];
        cell = [nib objectAtIndex:0];
    }
    
    if (tableView == self.searchDisplayController.searchResultsTableView) {
        cell.contactNameLabel.text = [searchResults objectAtIndex:indexPath.row];
    }
    else {
        NSString *book = [[sections valueForKey:[[[sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)] objectAtIndex:indexPath.section]] objectAtIndex:indexPath.row];
        cell.contactNameLabel.text = book;
    }
    cell.thumbnailImageView.image = [UIImage imageNamed:@"default.png"];
    // Configure the cell...
    return cell;
}



- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    if (tableView == self.searchDisplayController.searchResultsTableView) {
        return NULL;
    }
    else{
        return [[[sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)] objectAtIndex:section];
    }
}

- (NSArray *)sectionIndexTitlesForTableView:(UITableView *)tableView
{
    if (tableView == self.searchDisplayController.searchResultsTableView) {
        return NULL;
    }
    else {
        NSMutableArray *sectionedArray = [[NSMutableArray alloc]init];
        for(char c ='A' ; c <= 'Z' ;  c++)
        {
            [sectionedArray addObject:[NSString stringWithFormat:@"%c",c]];
        }
        return [[NSArray arrayWithObject:UITableViewIndexSearch] arrayByAddingObjectsFromArray:sectionedArray];
    }
}


- (NSInteger)tableView:(UITableView *)tableView sectionForSectionIndexTitle:(NSString *)title atIndex:(NSInteger)index
{
    if (title == UITableViewIndexSearch) {
        CGRect searchBarFrame = self.searchDisplayController.searchBar.frame;
        [tableView scrollRectToVisible:searchBarFrame animated:NO];
        return -1;
    }
    else{
        NSInteger count = 0;
        for(NSString *character in indexArray)
        {
            if([character isEqualToString:title])
            {
                currentSection = count;
                return count;
            }
            count ++;
        }
        return currentSection;
    }
}

- (void) setUpContactsList
{
    // Need to refactor code
    BOOL found;
    
    // Loop through the books and create our keys
    for (NSString *book in contactsArray)
    {
        NSString *c = [book substringToIndex:1];
        // NSLog(@"c: %@", c);
        found = NO;
        
        for (NSString *str in [sections allKeys])
        {
            if ([str isEqualToString:c])
            {
                found = YES;
            }
        }
        
        if (!found)
        {
            // NSLog(@"!found");
            [sections setValue:[[NSMutableArray alloc] init] forKey:c];
            //NSLog(@"Sections = %@", sections);
        }
    }
    // NSLog(@"Sections: %@", sections);
    
    
    // Loop again and sort the books into their respective keys
    for (NSString *book in contactsArray)
    {
        [[sections objectForKey:[book substringToIndex:1]] addObject:book];
    }
    // NSLog(@"Sections2: %@", sections);
    
    
    // Sort each section array
    for (NSString *key in [sections allKeys])
    {
        NSArray *temp =[sections objectForKey:key];
        temp = [temp sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)];
        [sections setValue:temp forKey:key];
    }
    
    // NSLog(@"Sections3: %@", sections);
}
*/


/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

// SWEET Commented out
#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
 
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     */
    // Sweet Commented out
    //[self performSegueWithIdentifier:@"showContactCard" sender:[self.tableView cellForRowAtIndexPath:indexPath]];
    
    /** ~*- Integration Code -*~ **/
    /*Fetch cell*/
    
    XMPPUserCoreDataStorageObject *user = [[self fetchedResultsController] objectAtIndexPath:indexPath];
    XMPPJID *JID = [XMPPJID jidWithUser: [[user.jidStr componentsSeparatedByString:@"@"] objectAtIndex:0] domain: [[delegateHandler getDefaults] DEFAULT_DOMAIN] resource:[[delegateHandler getDefaults] DEFAULT_RESOURCE]];
    
    //alloc and init a new OCJingleImpl object (destroy the old one if it is not nil)
    OCJingleImpl *jingleObj = [[OCJingleImpl alloc] initWithJID: [[delegateHandler myXMPPStream] myJID] xmppStream: [delegateHandler myXMPPStream]];

    //set the OCXMPPDelegateHandler object's OCJingleImpl to this new object.
    [delegateHandler setJingleImpl: jingleObj];

    //Send a session initiate message.
    [[delegateHandler myXMPPStream] sendElement: [jingleObj jingleSessionInitiateTo: JID recvportnum: 8888 SID: nil]];
    
    // For transitioning to the conferencing page
    UIStoryboard *sb = [UIStoryboard storyboardWithName:@"MainStoryboard_iPhone" bundle:nil];
    UIViewController *vc = [sb instantiateViewControllerWithIdentifier:@"singleCallNavigator"];
    NSLog(@"%@", vc);
    vc.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    //OCSingleCallViewController* scvc = (OCSingleCallViewController*) vc;
    //NSLog(@"%@", vc);
    
    [self presentViewController:vc animated:YES completion:NULL];
    callActive = YES;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 55;
}


/*
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"showContactCard"]) {
        NSIndexPath *indexPath = nil;
        OCContactCardViewController *destViewController = segue.destinationViewController;
        if ([self.searchDisplayController isActive]) {
            indexPath = [self.searchDisplayController.searchResultsTableView indexPathForSelectedRow];
            destViewController.contactName = [searchResults objectAtIndex:indexPath.row];
            
        }else{
            indexPath = [self.tableView indexPathForSelectedRow];
            NSString *temp = [[sections valueForKey:[[[sections allKeys] sortedArrayUsingSelector:@selector(localizedCaseInsensitiveCompare:)] objectAtIndex:indexPath.section]] objectAtIndex:indexPath.row];
            destViewController.contactName = temp;
        }
        // Hide bottom tab bar in the detail view
        destViewController.hidesBottomBarWhenPushed = YES;
    }
}
*/


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark NSFetchedResultsController
// ss2249 for Roster
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
		[fetchedResultsController setDelegate:self];
		
		
		NSError *error = nil;
		if (![fetchedResultsController performFetch:&error])
		{
			NSLog(@"Error performing fetch: %@", error);
		}
        
	}
	
	return fetchedResultsController;
}

- (void)controllerDidChangeContent:(NSFetchedResultsController *)controller
{
	[[self tableView] reloadData];
}

- (IBAction)testButton:(id)sender {
   // OCConferenceViewController* conferenceViewController = [[OCConferenceViewController alloc] init];
    /* Code to transition to in call view */
    UIStoryboard *sb = [UIStoryboard storyboardWithName:@"MainStoryboard_iPhone" bundle:nil];
    UIViewController *vc = [sb instantiateViewControllerWithIdentifier:@"singleCallNavigator"];
   // UIViewController *currentVC = conferenceViewController.getSelfValue;
    vc.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    if(currentViewController == nil){
        NSLog(@"current view null");
    }
    else{
        NSLog(@"current view not null");
    }

    [currentViewController presentViewController:vc animated:YES completion:NULL];

}


@end
