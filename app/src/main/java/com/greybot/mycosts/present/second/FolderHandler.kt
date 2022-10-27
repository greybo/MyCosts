package com.greybot.mycosts.present.second

import com.greybot.mycosts.base.Path
import com.greybot.mycosts.data.dto.FolderDTO
import com.greybot.mycosts.models.AdapterItems

class FolderHandler {

    fun makeFolderItems(path: String, map: Map<String?, List<FolderDTO>>?): List<AdapterItems> {
        return map?.mapNotNull { listDto ->
            listDto.key?.let { name ->
                val currentPath = Path(path).addToPath(name)
                val currentFolder = listDto.value.find { it.path == currentPath }
                AdapterItems.FolderItem(name, currentPath, countInner = "count:${listDto.value.size}", total = "total: 0", objectId = currentFolder?.objectId)
            }
        } ?: emptyList()
    }
}
