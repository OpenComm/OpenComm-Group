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

@interface OCContactsViewController ()

@end

@implementation OCContactsViewController{
    NSArray* contactsArray;
    NSArray* indexArray; // To create horizontal index on the right
    NSMutableDictionary* sections;
    NSInteger currentSection; // The current section of the index the user is at, not to change if no contact exists with touched letter.
    NSArray* searchResults;
}
//@synthesize tableView;
- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}



- (void)viewDidLoad
{
    [super viewDidLoad];
    //contactsArray = [NSArray arrayWithObjects:@"Sofia", @"Giulia", @"Andrea", @"Francesco", @"Alessandro", @"Giorgia", @"Matteo", @"Lorenzo", @"Martina", @"Sara", nil];
    contactsArray = [NSArray arrayWithObjects: @"Alabama", @"Alaska", @"American Samoa", @"Arizona", @"Arkansas", @"California", @"Colorado", @"Connecticut", @"Delaware", @"District of Columbia", @"Florida", @"Georgia", @"Guam", @"Hawaii", @"Idaho", @"Illinois", @"Indiana", @"Iowa", @"Kansas", @"Kentucky", @"Louisiana", @"Maine", @"Maryland", @"Massachusetts", @"Michigan", @"Minnesota", @"Mississippi", @"Missouri", @"Montana", @"Nebraska", @"Nevada", @"New Hampshire", @"New Jersey", @"New Mexico", @"New York", @"North Carolina", @"North Dakota", @"Northern Marianas Islands", @"Ohio", @"Oklahoma", @"Oregon", @"Pennsylvania", @"Puerto Rico", @"Rhode Island", @"South Carolina", @"South Dakota", @"Tennessee", @"Texas", @"Utah", @"Vermont", @"Virginia", @"Virgin Islands", @"Washington", @"West Virginia", @"Wisconsin", @"Wyoming", nil];
    sections = [[NSMutableDictionary alloc] init];
    
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
    [self performSegueWithIdentifier:@"showContactCard" sender:[self.tableView cellForRowAtIndexPath:indexPath]];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 55;
}



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

@end
