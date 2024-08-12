# STYLiSH
AppWorks practice project.

http://3.212.133.81/

## About
Hi! I am Kelsey. This is my STYLiSH project.

## Language
- HTML, CSS
- JavaScript
- Java

## Github Link
[STYLiSH專案連結](https://github.com/Kelseylin2000/Back-End-Class-Tech-Academy)

## How to start web server on port 80
1. Install authbind
    ```
    sudo apt update
    sudo apt install authbind
    ```

2. Use authbind to allow non-root user to use port 80
    ```
    sudo touch /etc/authbind/byport/80
    sudo chmod 500 /etc/authbind/byport/80
    sudo chown ubuntu /etc/authbind/byport/80
    ```

3. Set EC2 security group
   
   Allow http (port 80) inbound flow

4. start web server on port 80

    When you run the program, use `authbind --deep` 

## Run Web Server in the Background
```
nohup authbind --deep java -jar stylish-0.0.1-SNAPSHOT.jar --server.port=80 > output.log 2>&1 &
```
- **`nohup`**: Ignores the logout signal (SIGHUP), ensuring that the application continues running even after you log out.
- **`authbind --deep`**: Allows the application to use privileged ports (such as port 80). The `--deep` option ensures that the application and all its subprocesses use `authbind`.
- **`java -jar stylish-0.0.1-SNAPSHOT.jar --server.port=80`**: Runs the Spring Boot application and specifies that the application should bind to port 80.
- **`> output.log`**: Redirects standard output to the `output.log` file, allowing you to view the application's output.
- **`2>&1`**: Redirects standard error output to the `output.log` file as well, so all output is in one file.
- **`&`**: Runs the command in the background, so the terminal is not occupied by the command.
  
## Working Flow

1. pacakge the project
    ```
    ./mvnw clean package
    ```

2. create assignment branch, add, commit and push 

    ```
    git checkout -b week_?_part_?

    git add
    git commit

    git push --set-upstream origin week_?_part_?
    ```


2. deploy on instance
    ```
    // copy file from local to EC2
    scp -i stylish_key.pem target/stylish-0.0.1-SNAPSHOT.jar ubuntu@ec2-3-212-133-81.compute-1.amazonaws.com:~

    // connect to EC2
    ssh -i stylish_key.pem ubuntu@ec2-3-212-133-81.compute-1.amazonaws.com

    // run the program
    nohup authbind --deep java -jar stylish-0.0.1-SNAPSHOT.jar --server.port=80 > output.log 2>&1 &
    ```

4. open a pull request from origin week_?_part_? to upstream kelsey_develop


