# FileCabinet: recursive folder structure

## Overview

`FileCabinet` is a simple Java utility for working with a recursive folder structure.  
It supports operations like:

- **Finding a folder by name,**
- **filtering folders by size,** (`SMALL`, `MEDIUM`, `LARGE`)
- **counting all folders**, including nested ones.

The implementation uses clean recursive traversal, avoids code duplication by separating traversal from logic, and applies Java  features like lambdas and `Consumer<Folder>` to handle different operations on the folder structure.


## Structure

The folder structure is defined by three interfaces:

```java
interface Folder {
    
    String getName();
    
    String getSize();
}

interface MultiFolder extends Folder {
    
    List<Folder> getFolders();
}

interface Cabinet {

    int count();

    Optional<Folder> findFolderByName(String name);
    
    List<Folder> findFoldersBySize(String size);
}
```

`FileCabinet` implements the `Cabinet` interface and works with a list of `Folder` objects, including recursive structures via `MultiFolder`.

## Features

### `int count()`
- Returns the **total number of folders** (top-level and nested).
- Uses the same traversal logic with a counting lambda.

### `Optional<Folder> findFolderByName(String name)`
- Recursively searches the folder structure.
- Returns the **first folder** matching the given name (if any).
- Efficient: stops searching as soon as it finds a match.

### `List<Folder> findFoldersBySize(String size)`
- Returns **all folders**, including nested ones, that match the given size.
- Uses a generic traversal method with a filtering lambda.



## How it works

The core of the class is a **single recursive method**:

```java
private void traverse(List<Folder> folders, Consumer<Folder> processor)
```

This method walks through the folder tree and applies a `Consumer<Folder>` action to each folder.  This prevents code duplication in `findFoldersBySize` and `count`.

For `findFolderByName`, a separate method is used to allow **early exit** from recursion once a match is found (lambdas don’t let me do that easily).

## Example

You can create folders and subfolders like:

```java
Folder folder1 = new BasicFolder("Docs", "SMALL");
Folder folder2 = new BasicFolder("Images", "MEDIUM");

FolderGroup nested = new FolderGroup("Media", "LARGE");
nested.addSubfolder(folder2);

FolderGroup root = new FolderGroup("Root", "LARGE");
root.addSubfolder(folder1);
root.addSubfolder(nested);

FileCabinet cabinet = new FileCabinet(List.of(root));
```

Then call:

```java
cabinet.findFolderByName("Images");
cabinet.findFoldersBySize("LARGE");
cabinet.count();
```

## Technologies

- Java 8+
- Functional interfaces (`Consumer`)
- `Optional`, `AtomicInteger`
