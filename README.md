# sound-browser
Simple app demonstrating ktor HTTP-client and Android Paging library.

With SoundBrowser you can search, browse and play sounds from [freesound.org](https://freesound.org.)

All sound clips are covered by [Creative Commons 0](https://creativecommons.org/publicdomain/zero/1.0) license, and are shorter than 60 seconds of duration.
To find longer clips or sounds under some other license, go to freesound web site. You can change these limitation in FreeSoundHttpClient.kt.

The clip played is always a mp3 preview, not the original sound. In order to download the original sound, navigate to the sound's homepage from sound
details.

For building, load the project to Android Studio. You need freesound API key which can be applied from https://freesound.org/apiv2/apply.
Once you have the key add API key to `gradle.properties` file and build.

> apikey="\<your api key here\>"
