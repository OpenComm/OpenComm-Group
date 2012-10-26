//
//  HappeningNowConferenceCell.m
//  iOSCommUI
//
//  Created by Sauhard Bindal on 10/26/12.
//  Copyright (c) 2012 Sauhard Bindal. All rights reserved.
//

#import "HappeningNowConferenceCell.h"

@implementation HappeningNowConferenceCell
@synthesize conferenceTitleLabel = _conferenceTitleLabel;
@synthesize thumbnailImageView = _thumbnailImageView;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
