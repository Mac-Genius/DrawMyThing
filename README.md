# DrawMyThing
This is a simple word guesser for Draw My Thing. It allows a user to
input a hint in the form _l___ where underscores are unknown letters in
a word. It has an auto-typing feature that uses the java class Robot to
type words quickly into Minecraft's chat. When you switch from
Minecraft's window to another program's window, it will automatically
stop auto-typing.

### Dependencies
This program relies on Java Native Access which controls the auto-typing feature. You will need to download them [here](https://github.com/java-native-access/jna#readme) if you are planning on compiling the project yourself. You will need jna.jar and jna-platform.jar.

### Usage
To use, simply compile and export as a jar. You will need to add a words.txt file inside the main directory of the jar to make sure the program doesn't throw any errors. You will also need to add a "lib" directory inside the main directory of the jar and place the jna jars inside of lib.

### Download
You can download a copy of the program on mediafire [here](http://www.mediafire.com/download/03zbwyxr314cpue/DrawMyThing.jar). Place it in its own folder, and start it up in the terminal. It comes with a dictionary of words pulled from the actual game.

Verify your download with SHA1:

```
ab1daf763cde8a0e2ef46b2455eceb1c3f28785e
```
