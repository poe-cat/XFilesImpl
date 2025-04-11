package org.poecat;

import java.util.*;
import java.util.function.Consumer;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class FileCabinet implements Cabinet {

    private List<Folder> folders;

    public FileCabinet(List<Folder> folders) {
        this.folders = folders;
    }

    //Searches for the first folder with the given name in the entire structure (recursively)
    @Override
    public Optional<Folder> findFolderByName(String name) {
        return findByNameRecursive(folders, name);
    }

    //Recursively searches through folders and returns the first one matching the given name
    private Optional<Folder> findByNameRecursive(List<Folder> folders, String name) {

        for (Folder folder : folders) {
            //Check the current folder
            if (folder.getName().equals(name)) {
                return Optional.of(folder);
            }
            //If the folder contains subfolders, search them recursively
            if (folder instanceof MultiFolder) {
                Optional<Folder> found = findByNameRecursive(((MultiFolder) folder).getFolders(), name);
                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.empty();
    }

    //Returns all folders of the given size (SMALL/MEDIUM/LARGE)
    @Override
    public List<Folder> findFoldersBySize(String size) {

        List<Folder> result = new ArrayList<>();

        //Traverses all folders and collects those matching the given size
        traverse(folders, folder -> {
            if (folder.getSize().equals(size)) {
                result.add(folder);
            }
        });
        return result;
    }

    //Returns the total number of folders in the structure
    @Override
    public int count() {

        AtomicInteger counter = new AtomicInteger();
        traverse(folders, folder -> counter.incrementAndGet());

        return counter.get();
    }

    //Recursively traverses the entire folder structure,
    //executing the provided action for each folder
    private void traverse(List<Folder> folders, Consumer<Folder> processor) {

        for (Folder folder : folders) {
            processor.accept(folder);
            if (folder instanceof MultiFolder) {
                traverse(((MultiFolder) folder).getFolders(), processor);
            }
        }
    }
}
