#!/bin/sh
PROJECT_DIR=$1

if [ "X$PROJECT_DIR" == "X" ]; then
        echo "usage: $0 <source directory>"
        exit 1
fi

# Parse command-line arguments
PROJECT=$(basename $PROJECT_DIR)
PROJECT=$(echo $PROJECT | tr '[A-Z]' '[a-z]')

# Create XCode project from kivy project.
sudo rm -R /Users/robby/Desktop/apps/kivy-ios/app-$PROJECT
~/Desktop/apps/kivy-ios/tools/create-xcode-project.sh $PROJECT $PROJECT_DIR
echo "Building application for iPhone into ~/Desktop/apps/kivy-ios/app-$PROJECT/build/Release-iphoneos/"
echo 
xcodebuild -project /Users/robby/Desktop/apps/kivy-ios/app-$PROJECT/$PROJECT.xcodeproj -configuration Release > /dev/null

# Copy .app directory to the build dir.
# Run ldid on the executable.
APP_DIR=$(find /Users/robby/Desktop/apps/kivy-ios/app-$PROJECT/build/Release-iphoneos -name $PROJECT'.app')
BUILD_DIR=~/Desktop/apps
echo "Running ldid on the executable $APP_DIR/$PROJECT"
echo 
$BUILD_DIR/ldid -S $APP_DIR/$PROJECT

# Install the .app to the phone's /Applications via ssh via usb.
echo "Killing any instances of the executable named '$PROJECT' currently running on the phone"
ssh -p 2222 root@localhost "killall $PROJECT; rm -rf /Applications/$PROJECT.app"
echo
echo "Installing $APP_DIR to the phone's /Applications via scp via usb (this takes a while)..."
echo 
scp -r -P 2222 $APP_DIR root@localhost:/Applications/ > /dev/null
echo "Refreshing the springboard..."
echo 
ssh -p 2222 root@localhost "chmod +x /Applications/$PROJECT.app/$PROJECT; su mobile -c uicache"
echo "Done."

