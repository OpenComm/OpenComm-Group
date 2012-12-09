# build/os-auto.mak.  Generated from os-auto.mak.in by configure.

export OS_CFLAGS   := $(CC_DEF)PJ_AUTOCONF=1 -O2 -Wno-unused-label -DPJ_SDK_NAME="\"iPhoneOS6.0.sdk\"" -arch armv7 -isysroot /Applications/XCode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS6.0.sdk -DPJ_IS_BIG_ENDIAN=0 -DPJ_IS_LITTLE_ENDIAN=1

export OS_CXXFLAGS := $(CC_DEF)PJ_AUTOCONF=1 -O2 -Wno-unused-label -DPJ_SDK_NAME="\"iPhoneOS6.0.sdk\"" -arch armv7 -isysroot /Applications/XCode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS6.0.sdk 

export OS_LDFLAGS  := -O2 -arch armv7 -isysroot /Applications/XCode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS6.0.sdk -framework AudioToolbox -framework Foundation -lpthread  -framework CoreAudio -framework CoreFoundation -framework AudioToolbox -framework CFNetwork -framework UIKit -framework UIKit -framework AVFoundation -framework CoreGraphics -framework QuartzCore -framework CoreVideo -framework CoreMedia

export OS_SOURCES  := 


