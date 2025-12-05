package io.github.p1k0chu.bacaptrackersheetgenerator.utils

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.nio.file.Path
import kotlin.io.path.exists

fun CellStyle.cloneIntoWorkbook(wb: XSSFWorkbook): XSSFCellStyle {
    return wb.createCellStyle().also { it.cloneStyleFrom(this) }
}

fun XSSFWorkbook.removeSheet(name: String) {
    val i = getSheetIndex(name)
    if (i > -1) removeSheetAt(i)
}
