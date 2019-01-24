package se;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test2 {

    public static void main(String[] args) {
        String fileName = "HelloWorld.java";

        int index = fileName.lastIndexOf(".");
        log.info(fileName.substring(index+1));
    }
}
