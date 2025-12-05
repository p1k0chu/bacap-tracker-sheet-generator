package io.github.p1k0chu.bacaptrackersheetgenerator

import io.github.p1k0chu.bacaptrackersheetgenerator.utils.removeSheet
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.nio.file.Path
import kotlin.io.path.inputStream

private const val ALL_ITEMS_ID = "blazeandcave:challenges/all_the_items"
private const val STACK_ALL_ITEMS_ID = "blazeandcave:challenges/stack_all_the_blocks"
private const val ALL_BLOCKS_ID = "blazeandcave:challenges/all_the_items"
private const val STACK_ALL_BLOCKS_ID = "blazeandcave:challenges/stack_all_the_blocks"

private const val ALL_ITEMS_PATH = "data/blazeandcave/advancement/challenges/all_the_items.json"
private const val STACK_ALL_ITEMS_PATH = "data/blazeandcave/advancement/challenges/stack_all_the_items.json"
private const val ALL_BLOCKS_PATH = "data/blazeandcave/advancement/challenges/all_the_blocks.json"
private const val STACK_ALL_BLOCKS_PATH = "data/blazeandcave/advancement/challenges/stack_all_the_blocks.json"

@OptIn(ExperimentalSerializationApi::class)
fun XSSFWorkbook.generateItemsTab(packPath: Path, iconsVersion: String, transPath: Path) {
    removeSheet("Items-Blocks")
    val sheet = createSheet("Items-Blocks")
    fillHeader(sheet.createRow(0))

    val transJson: JsonObject = Json.decodeFromStream(transPath.inputStream())

    val allItemsStack = packPath.resolve(STACK_ALL_ITEMS_PATH).getAllCriteria()
    val allBlocksStack = packPath.resolve(STACK_ALL_BLOCKS_PATH).getAllCriteria()

    val allItems = packPath.resolve(ALL_ITEMS_PATH).getAllCriteria() - allItemsStack
    val allBlocks = packPath.resolve(ALL_BLOCKS_PATH).getAllCriteria() - allBlocksStack

    var rowIndex = 1

    allItems.forEach { itemId ->
        populateRow(
            sheet.createRow(rowIndex++),
            iconsVersion,
            transJson.getItemName(itemId),
            1,
            itemId,
            ALL_ITEMS_ID
        )
    }
    allItemsStack.forEach { itemId ->
        populateRow(
            sheet.createRow(rowIndex++),
            iconsVersion,
            transJson.getItemName(itemId),
            64,
            itemId,
            STACK_ALL_ITEMS_ID
        )
    }
    allBlocks.forEach { blockId ->
        populateRow(
            sheet.createRow(rowIndex++),
            iconsVersion,
            transJson.getBlockName(blockId),
            1,
            blockId,
            ALL_BLOCKS_ID
        )
    }
    allBlocksStack.forEach { blockId ->
        populateRow(
            sheet.createRow(rowIndex++),
            iconsVersion,
            transJson.getBlockName(blockId),
            64,
            blockId,
            STACK_ALL_BLOCKS_ID
        )
    }

    for (i in 2..6) {
        sheet.autoSizeColumn(i)
    }
}

private fun populateRow(row: Row, iconsVersion: String, itemName: String, quantity: Int, itemId: String, advancementId: String) {
    row.createCell(1).setCellValue(false)
    row.createCell(2).cellFormula = "image(\"https://nerothe.com/img/$iconsVersion/minecraft_$itemId.png\")"
    row.createCell(3).setCellValue(itemName)
    row.createCell(4).setCellValue(quantity.toString())
    row.createCell(5).setCellValue(itemId)
    row.createCell(6).setCellValue(advancementId)
}

private fun fillHeader(row: Row) {
    row.createCell(1).setCellValue("✓")
    row.createCell(3).setCellValue("Item/Block")
    row.createCell(4).setCellValue("Qty")
    row.createCell(5).setCellValue("Item ID")
    row.createCell(6).setCellValue("Advancement ID")
}

private fun JsonObject.getItemName(id: String): String {
    return get("item.minecraft.$id")?.jsonPrimitive?.content ?: id
}

private fun JsonObject.getBlockName(id: String): String {
    return get("block.minecraft.$id")?.jsonPrimitive?.content ?: id
}

@OptIn(ExperimentalSerializationApi::class)
private fun Path.getAllCriteria(): Set<String> {
    return Json.decodeFromStream<JsonObject>(inputStream())["criteria"]!!.jsonObject.keys
}

