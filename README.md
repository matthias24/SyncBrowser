# SyncBrowser



***
## Description in english.

Project SyncBrowser

Syncs browser bookmarks between different browsers.  

Beschreibung auf Deutsch ist weiter unten.


### The Problem

Most browsers have functionality to sync bookmarks between different platforms. Sadly there is no functionality to sync bookmarks between different browsers. This problem is solved by this program.


### The Build

A ready jar with all dependencies is in the folder `SyncBrowser/bin`. If you want to build the jar by your own, do the following:

    cd ../SyncBrowser  
    mvn clean package

The build jar is in the folder `SyncBrowser/target/synvbrowser.jar`.


### The Usage

Actually this program can just convert the bookmarks between different browsers, but not automatically sync them. You have to use some JRE 17. This works this way:

    cd ../SyncBrowser/bin

Show help:

    java -jar syncbrowser_1.0.0.jar -h

Convert bookmark from Safari format to Chrome/Brave format:

    java -jar syncbrowser_1.0.0.jar -in ../Bookmarks.plist -out ../Bookmarks

Bookmarks from Safari are in macOS under:

    /Users/<username>/Library/Safari/Bookmarks.plist

Bookmarks from Chrome/Brave are in macOS under:

    /Users/<username>/Library/Application Support/Google/Chrome/Default/Bookmarks
    /Users/<username>/Library/Application Support/BraveSoftware/Brave-Browser/Default/Bookmarks

Actually this are the only Reader and Writer, that are implemented. Additional implementations can be added as a Java Service to the Classpath.



***
## Beschreibung auf deutsch

Projekt SyncBrowser

Synchronisiert Browser Lesezeichen zwischen verschiendenen Browsern.  

Description in english is on top of page.


### Das Problem

Die Browser bieten Funktionalität, um Lesezeichen zwischen verschiedenen Platformen zu synchronisieren. Leider existiert keine Funktionalität, um Lesezeichen zwischen verschiedenen Browsern zu synchronisieren. Dieses Problem löst dieses Programm.


### Das Bauen

Ein fertiges Jar mit allen Abhängigkeiten befindet sich im Verzeichnis `SyncBrowser/bin`. Möchte man das Jar selbst bauen, geht dies folgendermaßen:

    cd ../SyncBrowser  
    mvn clean package

Das gebaute Jar befindet sich dann im Verzeichnis `SyncBrowser/target/synvbrowser.jar`.


### Die Verwendung

Aktuell kann dieses Programm die Lesezeichen verschiedener Browser nur konvertieren, aber noch nicht automatisch synchronisieren. Dafür wird eine JRE 17 benötigt. Dies geht folgendermaßen:

    cd ../SyncBrowser/bin

Hilfe ausgeben:

    java -jar syncbrowser_1.0.0.jar -h

Lesezeichen von Safari-Format nach Chrome/Brave Format konvertieren:

    java -jar syncbrowser_1.0.0.jar -in ../Bookmarks.plist -out ../Bookmarks

Lesezeichen von Safari stehen in macOS unter:

    /Users/<username>/Library/Safari/Bookmarks.plist

Lesezeichen von Chrome bzw. Brave stehen in macOS unter:

    /Users/<username>/Library/Application Support/Google/Chrome/Default/Bookmarks
    /Users/<username>/Library/Application Support/BraveSoftware/Brave-Browser/Default/Bookmarks

Aktuell sind dies die einzigen Reader bzw. Writer, die implementiert sind. Weitere können auch selbst als Java Service zum Classpath addiert werden.

