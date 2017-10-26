Videos Mover
------------------------------------------------

##### Description
Application used for managing downloaded movies and tv series (moving, displaying, cleaning and so on). It was designed to work well with the naming conventions of Kodi Media Center application.

##### Building and running
- clone this repository `git clone https://github.com/lcserny/videos-mover2`
- checkout the master branch (if not already) `git checkout master`
- run maven `mvn clean compile package -Dskiptests`
- copy the jar created wherever you want `cp ui/target/ui-1.0-SNAPSHOT-jar-with-dependencies.jar /you/path`
- run it using Oracle Java RE `java -jar app.jar`

##### How to use
When starting up, the application sets its default paths (based on the config provided) for the following folders:
1. Downloads: the path where you download your movies and tv series
2. Movies: the path where you want the app to move you downloaded movies (renaming them properly)
3. TvShows: the path where you want the app to move you downloaded tv series (renaming them properly)

To search what videos are available, click the "Search" button, the main table will be filled with the found videos (filters are applied by default, for ex. videos smaller than 50Mb are excluded, videos that do not have the correct mime type and so on).
After main table is populated, you can checkmark which videos are movies and which are tv series. Once you do this, their output path will be displayed.
If the output path is not correct or you want to be more accurate, there is a green TMDb metadata search button available for each video which you can use to query TMDb directly and choose from the results the correct video (which automatically changes the output path)
After you are done checkmarking the videos, click on the "Move" button, this will move the videos in their output folders and refresh the main videos table.
###### Note: buttons are provided to change default folder paths in the current app session
 
 
