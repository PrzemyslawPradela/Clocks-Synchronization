## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
The client-server application using Java RMI and Berkeley’s Algorithm for clock synchronization on Linux machines with Java Swing graphical interface.
	
## Technologies
Project is created with:
* Java version: 8
* Maven version: 3.6.3
	
## Setup
1. Install Java Runtime Environment
2. Install [Maven](https://maven.apache.org/install.html)
3. Disable NTP
4. Clone this repository
6. To run this project, compile it and run it using mvn:

    *Server*
   ```bash
   $ cd ../rmi-clocks-synchronization-swing
   $ sudo mvn install
   $ sudo mvn exec:java -pl server -Dexec.mainClass=rmi.clocksynchronization.server.StartServer
   ```
    *Client*
      ```bash
      $ cd ../rmi-clocks-synchronization-swing
      $ sudo mvn install
      $ sudo mvn exec:java -pl client -Dexec.mainClass=rmi.clocksynchronization.client.StartClient
      ```