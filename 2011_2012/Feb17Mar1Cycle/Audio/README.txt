OpenComm == Harold Trevor == Feb.17-Feb.23.2012

TestXMPPClient project -- 
modified so that jabber.org can be run w/o issue

when running on emulators, change the following in MUCBuddy.java
comment out: line 49, 161, 218 (MicrophonePusher)
uncomment: line 48, 160, 217 (AudioPusher)