This is a command-line script that installs a kivy app to iOS.

It first creates an Xcode project from the kivy project. Then it runs a tool called ‘ldid’ which allows the application to be installed on iOS without the Appstore. Finally it installs the application over USB to the phone.