package org.poecat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileCabinet implements Cabinet {

    private List<Folder> folders;

    public FileCabinet(List<Folder> folders) {
        this.folders = folders;
    }

    @Override
    public Optional<Folder> findFolderByName(String name) {
        return findByNameRecursive(folders, name);
    }

    private Optional<Folder> findByNameRecursive(List<Folder> folders, String name) {

        for (Folder folder : folders) {
            if (folder.getName().equals(name)) {
                return Optional.of(folder);
            }
            if (folder instanceof MultiFolder) {
                Optional<Folder> found = findByNameRecursive(((MultiFolder) folder).getFolders(), name);
                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Folder> findFoldersBySize(String size) {

        List<Folder> result = new ArrayList<>();
        findBySizeRecursive(folders, size, result);

        return result;
    }

    private void findBySizeRecursive(List<Folder> folders, String size, List<Folder> result) {

        for (Folder folder : folders) {
            if (folder.getSize().equals(size)) {
                result.add(folder);
            }
            if (folder instanceof MultiFolder) {
                findBySizeRecursive(((MultiFolder) folder).getFolders(), size, result);
            }
        }
    }

    @Override
    public int count() {
        return countRecursive(folders);
    }

    private int countRecursive(List<Folder> folders) {

        int total = 0;

        for (Folder folder : folders) {
            total++;
            if (folder instanceof MultiFolder) {
                total += countRecursive(((MultiFolder) folder).getFolders());
            }
        }
        return total;
    }
}
