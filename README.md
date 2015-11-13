# DrawMyThing
This is a simple word guesser for Draw My Thing. It allows a user to
input a hint in the form _l___ where underscores are unknown letters in
a word. It has an auto-typing feature that uses the java class Robot to
type words quickly into Minecraft's chat. When you switch from
Minecraft's window to another program's window, it will automatically
stop auto-typing.

### Dependencies
This program relies on Java Native Access which controls the auto-typing feature. You will need to download them [here](https://github.com/java-native-access/jna#readme). You will need jna.jar and jna-platform.jar.

### Usage
To use, simply compile and export as a jar. You will need to add a words.txt file inside the main directory of the jar to make sure the program doesn't throw any errors. You will also need to add a "lib" directory inside the main directory of the jar and place the jna jars inside of lib.
