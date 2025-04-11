package org.poecat;

import java.util.List;
import java.util.Optional;

interface Cabinet {

    int count();

    //zwraca dowolny element o podanej nazwie
    Optional<Folder> findFolderByName(String name);

    List<Folder> findFoldersBySize(String size);
}
