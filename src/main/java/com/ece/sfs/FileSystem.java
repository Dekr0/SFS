package com.ece.sfs;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Stack;

public class FileSystem {

    // Directory Path
    private final Stack<String> path = new Stack<>();

    // Map all files into a single table
    private final HashMap<String, FileSystemComponent> absolutEntries = new HashMap<>();

    public FileSystem() {
        absolutEntries.put("/", new Directory("", "/", Calendar.getInstance().getTime()));

        path.push("/");
    }
}
