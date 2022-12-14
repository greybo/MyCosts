package com.greybot.mycosts.present.folder.preview

import com.greybot.mycosts.data.dto.FolderDTO
import com.greybot.mycosts.models.AdapterItems
import com.greybot.mycosts.utility.formatPathFolder

fun folderCardMake(name: String, path: String, list: List<FolderDTO>): AdapterItems {
    return AdapterItems.FolderItem(name, formatPathFolder(path, name), list.size)
}
