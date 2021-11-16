#import <UIKit/UIKit.h>

@interface ScratchViewTools : NSObject
+ (UIColor *)colorFromHexString:(NSString *)hexString;
+ (UIImage *)createImageFromColor:(UIColor *)color;
@end
