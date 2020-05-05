# Netty Echo Server

## 1. Description

Minimal implementation of echo server using Netty.
This project is a minor modification of the echo server project provided with the netty source code.
The server was created to be used as a quick tool for testing.


## 2. Build

The gradle [`shadowJar`](https://imperceptiblethoughts.com/shadow/) plugin is used for building an executable "fat-jar" .

Run the following command in the project root directory.
````bash
gradle clean build
````

The "fat-jar" will be created as `./build/libs/nettyEcho-<version>-all.jar`.


## 3. Starting the Server

````bash
java -jar netty-echo-<version>-all.jar -p  [port]
````
e.g.:
````bash
java -jar netty-echo-<version>-all.jar -p 4040
````


## 4. Quick Test

To quickly check if the server is working correctly, run netcat from a terminal:
````bash
nc [server-ip-address] [port]
````
e.g.:
````bash
nc localhost 4040
````

After connecting to the server, type a message and press ENTER.
The server should echo the message to the terminal.

Note: I only tested with [`GNU netcat`](http://netcat.sourceforge.net/)