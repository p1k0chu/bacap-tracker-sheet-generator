package io.github.p1k0chu.bacaptrackersheetgenerator

import io.github.p1k0chu.bacaptrackersheetgenerator.constants.DocumentationColumnsConstants
import io.github.p1k0chu.bacaptrackersheetgenerator.utils.cloneIntoWorkbook
import io.github.p1k0chu.bacaptrackersheetgenerator.utils.getTabName
import io.github.p1k0chu.bacaptrackersheetgenerator.utils.minecraftText
import io.github.p1k0chu.bacaptrackersheetgenerator.utils.removeSheet
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.nio.file.Path
import kotlin.io.path.*

@OptIn(ExperimentalSerializationApi::class)
fun XSSFWorkbook.generateAdvancementsTab(docsWorkbook: Workbook?, packPath: Path, iconsVersion: String) {
    removeSheet("Advancements")

    val advancementsWorksheet = createSheet("Advancements")

    fillAdvancementsHeader(advancementsWorksheet.createRow(0))

    var rowIndex = 1

    for (namespace in packPath.resolve("data").listDirectoryEntries()) {
        val advs = namespace.resolve("advancement")
        if (advs.notExists()) continue

        for (advTab in advs.listDirectoryEntries()) {
            val tabName = getTabName(advTab.name)
            val docSheet: Sheet? = docsWorkbook?.getSheet(tabName)
            val tabStyle: XSSFCellStyle? =
                docSheet?.getRow(1)
                    ?.getCell(1)
                    ?.cellStyle
                    ?.cloneIntoWorkbook(this)

            for (advFile in advTab.listDirectoryEntries("*.json")) {
                val json = Json.decodeFromStream<JsonElement>(advFile.inputStream())
                if (json !is JsonObject) continue

                val display = json["display"]?.jsonObject ?: continue
                val advName = display.jsonObject["title"]!!.minecraftText
                val description = display.jsonObject["description"]!!.minecraftText
                val icon = display.jsonObject["icon"]!!
                    .jsonObject["id"]!!
                    .jsonPrimitive.content
                    .removePrefix("minecraft:")
                val advId = "${namespace.name}:${advTab.name}/${advFile.nameWithoutExtension}"

                val row = advancementsWorksheet.createRow(rowIndex++)
                val docRow: Row? = docSheet?.find {
                    it.getCell(DocumentationColumnsConstants.NAME)?.stringCellValue == advName
                }
                val advStyle = docRow?.getCell(DocumentationColumnsConstants.NAME)
                    ?.cellStyle
                    ?.cloneIntoWorkbook(this)
                val actualReq = docRow?.getCell(DocumentationColumnsConstants.ACTUAL_REQUIREMENTS)?.stringCellValue
                val rewards = docRow?.getCell(DocumentationColumnsConstants.REWARDS)?.stringCellValue

                populateRow(
                    row,
                    advName,
                    icon,
                    iconsVersion,
                    description,
                    actualReq,
                    rewards,
                    advTab.name,
                    advId,
                    advStyle
                )

                if (tabStyle != null) {
                    for (i in 9..18) {
                        val cell = row.getCell(i) ?: row.createCell(i)
                        cell.cellStyle = tabStyle
                    }
                }
            }
        }
    }

    for (i in 0..8) {
        advancementsWorksheet.autoSizeColumn(i)
    }

    for (i in 9..17) {
        advancementsWorksheet.columnHelper.setColWidth(i.toLong(), 25.0)
    }
}

private fun populateRow(
    row: Row,
    advName: String,
    iconItemId: String,
    iconsVersion: String,
    description: String,
    actualReq: String?,
    rewards: String?,
    advTab: String,
    advId: String,
    advStyle: XSSFCellStyle?,
) {
    row.createCell(6).setCellValue(false)
    row.createCell(7).cellFormula = "image(\"https://mc.nerothe.com/img/$iconsVersion/minecraft_$iconItemId.png\")"

    val name = row.createCell(8)
    name.setCellValue(advName)
    name.cellStyle = advStyle

    row.createCell(9).setCellValue(
            if (actualReq?.isNotEmpty() == true) "$description ($actualReq)" else description
        )

    if (rewards != null) row.createCell(10).setCellValue(rewards)
    row.createCell(14).setCellValue(iconItemId)
    row.createCell(15).setCellValue(getTabName(advTab))
    row.createCell(16).setCellValue(advId)
}

private fun fillAdvancementsHeader(header: Row) {
    header.createCell(0).setCellValue("#")
    header.createCell(1).setCellValue("Class")
    header.createCell(2).setCellValue("Who")
    header.createCell(3).setCellValue("Progress")
    header.createCell(4).setCellValue("Num")
    header.createCell(5).setCellValue("Denom")
    header.createCell(6).setCellValue("✓")
    header.createCell(7).setCellValue("Icon")
    header.createCell(8).setCellValue("Name")
    header.createCell(9).setCellValue("Requirements")
    header.createCell(10).setCellValue("Rewards")
    header.createCell(11).setCellValue("Incomplete")
    header.createCell(14).setCellValue("Icon Name")
    header.createCell(15).setCellValue("Tab")
    header.createCell(16).setCellValue("Actual Name")
}
