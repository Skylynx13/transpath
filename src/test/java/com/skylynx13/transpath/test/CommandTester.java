package com.skylynx13.transpath.test;

import com.skylynx13.transpath.log.TransLog;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandTester {
    // To run a command like "cmd /c chcp 437|copy sourceFolder/sourceFile targetFolder/targetFile"
    private static void execCmd(String cmd) {
        try {
            Process cmdProcessor = Runtime.getRuntime().exec(cmd);
            BufferedReader iReader =
                    new BufferedReader(new InputStreamReader(
                            new BufferedInputStream(cmdProcessor.getInputStream())
                    ));
            BufferedReader eReader =
                    new BufferedReader(new InputStreamReader(
                            new BufferedInputStream(cmdProcessor.getErrorStream())
                    ));
            String inLine;
            while ((inLine = iReader.readLine()) != null) {
                TransLog.getLogger().info("Return: {}", inLine);
            }
            while ((inLine = eReader.readLine()) != null) {
                TransLog.getLogger().info("Error: {}", inLine);
            }
            if (cmdProcessor.waitFor() != 0) {
                if (cmdProcessor.exitValue() == 1) {
                    TransLog.getLogger().info("Error executing.");
                }
            }
        } catch (IOException | InterruptedException e) {
            TransLog.getLogger().error("", e);
        }
    }
}
