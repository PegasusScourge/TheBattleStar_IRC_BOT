@echo off
echo Compiling...
javac -d bin/ src/Bot.java src/Controller.java src/Channel.java src/Commands.java src/LogBot.java src/ChannelBook.java -cp bin/pircbot.jar;.
echo Creating jar...
cd bin
jar cmf META-INF/MANIFEST.MF IRC_Bot_Soniduino.jar *.class
echo Removing class files...
del *.class
pause
java -jar IRC_Bot_Soniduino.jar
cd ..
echo Done.
pause