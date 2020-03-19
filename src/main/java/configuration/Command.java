package configuration;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class Command implements Serializable, Comparable<Command> {

    private static final long serialVersionUID = 1L;
    private String commandName;
    private String task;
    private String fileName;
    private long commandOwner;
    private byte[] contentBytes;

    // command and task
    public Command(String commandName, String task, long commandOwner) {
        this.commandName = commandName;
        this.task = task;
        this.fileName = "";
        this.commandOwner = commandOwner;
    }

    // command and attachment
    public Command(String commandName, InputStream commandContent, String fileName, long commandOwner) throws IOException {
        this.commandName = commandName;
        this.task = "";
        contentBytes = IOUtils.toByteArray(commandContent);
        this.fileName = fileName;
        this.commandOwner = commandOwner;
    }

    // command, task, and attachment
    public Command(String commandName, String task, InputStream commandContent, String fileName, long commandOwner) throws IOException {
        this.commandName = commandName;
        this.task = task;
        contentBytes = IOUtils.toByteArray(commandContent);
        this.fileName = fileName;
        this.commandOwner = commandOwner;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTask() {
        return this.task;
    }

    public void setCommandContent(InputStream commandContent) throws IOException{
        contentBytes = IOUtils.toByteArray(commandContent);
    }

    public InputStream getCommandContent() {
        InputStream commandContent = new ByteArrayInputStream(contentBytes);
        return commandContent;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setCommandOwner(long commandOwner) {
        this.commandOwner = commandOwner;
    }

    public long getCommandOwner() {
        return commandOwner;
    }

    @Override
    public int compareTo(@NotNull Command o) {
        int x = this.commandName.length() - o.commandName.length();
        if(x == 0) {
            x = this.task.length() - o.task.length();
        }
        if(x == 0) {
            x = this.fileName.length() - o.fileName.length();
        }
        return x;
    }


}

