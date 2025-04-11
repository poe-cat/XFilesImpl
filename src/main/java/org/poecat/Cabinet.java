package org.poecat;

import java.util.List;
import java.util.Optional;

interface Cabinet {

    int count();

    Optional<Folder> findFolderByName(String name);

    List<Folder> findFoldersBySize(String size);
}
